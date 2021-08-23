package taller_2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Andres Hoyos
 * <p>
 * TODO: Modificar {@link #analyzeFile()} para considerar lo que este en comillas sea parte de la string
 */
public class Analyzer {
    /**
     * <p>Language name.</p>
     * TODO: definir un nombre para el lenguaje
     */
    public static final String LANGUAGE_NAME = "My Language";

    /**
     * <p>Language extension.</p>
     * TODO: definir una extension para el lenguaje
     */
    public static final String LANGUAGE_EXTENSION = "txt";

    /**
     * <p>Project path.</p>
     */
    public static final String PROJECT_DIRECTORY = System.getProperty("user.dir") + File.separatorChar;

    /**
     * <p>Data directory path.</p>
     */
    public static final String DATA_DIRECTORY = PROJECT_DIRECTORY + "data" + File.separatorChar;

    /**
     * <p>Output directory path.</p>
     */
    public static final String OUTPUT_DIRECTORY = PROJECT_DIRECTORY + "output" + File.separatorChar;

    /**
     * <p>Input directory path.</p>
     */
    public static final String INPUT_DIRECTORY = PROJECT_DIRECTORY + "input" + File.separatorChar;

    /**
     * <p>Log file path.</p>
     */
    public static final String LOG_FILE = OUTPUT_DIRECTORY + "log.txt";

    /**
     * <p>Symbols text file name.</p>
     */
    public static final String SYMBOLS_TXT_FILE = "symbols.txt";

    /**
     * <p>Symbols' comment identifier.</p>
     */
    public static final String SYMBOL_COMMENT = "##";

    /**
     * <p>{@code Map<String, Map<String, String>> } to containing the symbols.</p>
     */
    public static final Map<String, Map<String, String>> SYMBOLS;

    // static initializer to load symbols
    static {
        SYMBOLS = loadSymbols();
    }

    /**
     * <p>Abstract representation of the code file</p>
     */
    private File codeFile;

    private final String[] elementsTypes;

    {
        // TODO: revisar elementos
        elementsTypes = new String[]{"SYMBOL", "LINE", "COLUMN", "NAME", "TYPE", "TYPE1", "TYPE2"};
    }

    public Analyzer() {
    }

    /**
     * <p>Map the header with a map of characteristic.<br>
     * It's similar to a .json data map</p>
     * <p><b>The Map is inmodificable.</b></p>
     *
     * @return a map containing the symbols
     */
    private static Map<String, Map<String, String>> loadSymbols() {
        File file = new File(DATA_DIRECTORY + SYMBOLS_TXT_FILE);
        // read the file
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            // string to store the read lines, starting from the first line
            String line = in.readLine();
            // strings to store the header, key and value
            String header, key, value;
            // Map to contain the headers and characteristic maps
            Map<String, Map<String, String>> head = new HashMap<>();
            // Map to contain the characteristics of the header
            Map<String, String> body;
            // loop to navigate the file lines
            while (line != null) {
                // loop to skip blank lines
                while (line != null && line.isBlank()) {
                    // read next line
                    line = in.readLine();
                }
                // check if it is not the end of the file
                if (line != null) {
                    // if line start with the comment symbol, skip
                    if (!line.startsWith(SYMBOL_COMMENT)) {
                        // stores the header
                        header = line.replaceAll(" ", "");
                        // read next line
                        line = in.readLine();
                        // initialize the characteristics map
                        body = new HashMap<>();
                        // loop to map the key value pairs
                        // checks if is not the end of the file and that the line is not blank
                        while (line != null && !line.isBlank()) {
                            // !(line == null || line.isBlank())
                            // remove empty spaces form line
                            line = line.replaceAll(" ", "");
                            // check that line contains ":" in order to separate
                            if (line.contains(":")) {
                                // stores the part before the ":"
                                key = line.split(":")[0];
                                // stores the part after the ":"
                                value = line.split(":")[1];
                                // maps the key value pair
                                body.put(key, value);
                            }
                            // read next line
                            line = in.readLine();
                        }
                        // maps the header as the key and the characteristics map as the value
                        head.put(header, Collections.unmodifiableMap(body));
                    } else {
                        // read next line, to skip comment
                        line = in.readLine();
                    }
                }
            }
            // return the map, unmodifiable
            return Collections.unmodifiableMap(head);

        } catch (IOException e) {
            // prints the Exception to the standard error stream
            e.printStackTrace(System.err);
        }
        throw new RuntimeException("Symbols file not found");
    }

    public List<String[]> analyzeFile() {
        // check file is not null
        if (codeFile != null) {
            // read file
            try (BufferedReader in = new BufferedReader(new FileReader(codeFile))) {
                // list to store the data
                List<String[]> list = new LinkedList<>();
                // store the character
                int c;
                // counters for line and column
                int line = 1, col = 1;
                // store the characters
                StringBuilder sb = new StringBuilder();
                // loop through the file
                while ((c = in.read()) != -1) {
                    // switch to identify the character
                    switch (c) {
                        // space or line jump
                        case '\n', ' ' -> {
                            // builder is not empty
                            if (!sb.isEmpty()) {
                                // print word info
                                wordInfo(list, sb.toString(), line, col - sb.length());
                                sb.setLength(0);
                            }

                            if (c == '\n') {
                                line++;
                                col = 1;
                            } else {
                                col++;
                            }
                        }

                        // ;
                        case ';' -> {
                            // print the word information
                            wordInfo(list, sb.toString(), line, col - sb.length());
                            // empty builder
                            sb.setLength(0);
                            // print information of ;
                            wordInfo(list, String.valueOf(';'), line, col);
                            col++;
                        }
                        case '+', '-', '*', '/', '=', '!' -> {
                            // agregar el caracter al String
                            sb.append((char) c);
                            // aumentar una columna
                            col++;
                        }
                        // "
                        case '"' -> {
                            // print the word information
                            wordInfo(list, sb.toString(), line, col);
                            // empty builder
                            sb.setLength(0);
                            // print " information
                            wordInfo(list, "\"", line, col);
                            col++;
                        }

                        case '(', ')' -> {
                            // imprimir la informacion de la palabra, con la linea, columna y args
                            wordInfo(list, sb.toString(), line, col);
                            // vaciar el String
                            sb.setLength(0);
                            // imprimir la informacion de ( o ), con la linea, columna y args
                            wordInfo(list, String.valueOf((char) c), line, col);
                            // aumentar una columna
                            col++;
                        }

                        default -> {
                            // if character is letter or digit
                            if (Character.isLetterOrDigit(c) || c == '_') {
                                sb.append((char) c);
                                col++;
                            }
                        }
                    } // end switch
                } // end while

                return list;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void wordInfo(List<String[]> l, String word, int line, int col) {
        Objects.requireNonNull(l);
        Objects.requireNonNull(word);

        if (word.isBlank()) {
            return;
        }
        // "SYMBOL", "LINE", "COLUMN", "NAME", "TYPE", "TYPE1", "TYPE2"
        String[] s = new String[elementsTypes.length];

        if (s.length < 4)
            throw new RuntimeException("Invalid length");

        s[0] = word;
        s[1] = String.valueOf(line);
        s[2] = String.valueOf(col);
        if (SYMBOLS.containsKey(word)) {
            for (int i = 3; i < s.length; i++) {
                s[i] = SYMBOLS.get(word).get(elementsTypes[i]);
            }
        } else {
            s[3] = "Identificador";
            for (int i = 4; i < s.length; i++) {
                s[i] = "";
            }
        }

        l.add(s);
    }

    public File getCodeFile() {
        return codeFile;
    }

    public void setCodeFile(File codeFile) {
        if (codeFile.getName().endsWith("." + LANGUAGE_EXTENSION)) {
            this.codeFile = codeFile;
        } else {
            throw new UnsupportedOperationException("Invalid File");
        }
    }

    public String[] getElementsTypes() {
        return elementsTypes;
    }
}