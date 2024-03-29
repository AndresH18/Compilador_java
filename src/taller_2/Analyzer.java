package taller_2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Andres Hoyos
 * @author Alejandro Garcia
 * <p>
 */
public final class Analyzer {
    /**
     * <p>Language name.</p>
     */
    public static final String LANGUAGE_NAME = "AlejiNdres";

    /**
     * <p>Language extension.</p>
     * <p><i>extension = aa</i></p>
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
     * <p>Subset of {@link ColumnType} containing the allowed keys. </p>
     */
    private static final EnumSet<ColumnType> SYMBOLS_KEYS = EnumSet.range(ColumnType.TOKEN, ColumnType.TYPE2);

    /**
     * <p>{@code Map<String, Map<ColumnType, String>> } to containing the symbols.</p>
     */
    public static final Map<String, Map<ColumnType, String>> SYMBOLS;

    // static initializer to load symbols
    static {
        SYMBOLS = loadSymbols();
    }

    /**
     * <p>Abstract representation of the code file</p>
     */
    private File codeFile;


    public Analyzer() {
    }

    /**
     * <p>Map the header with a map of characteristic.<br>
     * It's similar to a .json data map</p>
     * <p><b>The Map is inmodificable.</b></p>
     *
     * @return a map containing the symbols
     */
    private static Map<String, Map<ColumnType, String>> loadSymbols() {
        File file = new File(DATA_DIRECTORY + SYMBOLS_TXT_FILE);
        // long to count the lines in the file to address syntax errors
        long lineCounter = 0x01;
        // strings to store the header, key and value
        String header, key, value;
        // column type to get the "key" in the enum
        ColumnType typeKey;
        // Map to contain the headers and characteristic maps
        Map<String, Map<ColumnType, String>> head = new HashMap<>();
        // Map to contain the characteristics of the header
        Map<ColumnType, String> body;

        // read the file
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {

            // string to store the read lines, starting from the first line
            String line = in.readLine();

            // loop to navigate the file lines
            while (line != null) {
                // loop to skip blank lines
                while (line != null && line.isBlank()) {
                    // read next line
                    line = in.readLine();
                    lineCounter++;
                }
                // check if it is not the end of the file
                if (line != null) {
                    // if line start with the comment symbol, skip
                    if (!line.startsWith(SYMBOL_COMMENT)) {
                        // stores the header
                        header = line.replaceAll(" ", "");
                        // read next line
                        line = in.readLine();
                        lineCounter++;
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
                                // checks if the "key" belongs to the set of valid keys,
                                // while also checking if that "key" is also an element of ColumnType
                                if (!SYMBOLS_KEYS.contains(ColumnType.valueOf(key))) {
                                    // the key is element of ColumnType but is not part of allowed keys
                                    throw new IllegalArgumentException();
                                }
                                // maps the key value pair
                                body.put(ColumnType.valueOf(key), value);
                                key = value = "";
                            }
                            // read next line
                            line = in.readLine();
                            lineCounter++;
                        }
                        // maps the header as the key and the characteristics map as the value
                        head.put(header, Collections.unmodifiableMap(body));
                    } else {
                        // read next line, to skip comment
                        line = in.readLine();
                        lineCounter++;
                    }
                }
            }
            // return the map, unmodifiable
            return Collections.unmodifiableMap(head);

        } catch (IOException e) {
            // prints the Exception to the standard error stream
            e.printStackTrace(System.err);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ILLEGAL KEY ON LINE " + lineCounter);
        }
        throw new RuntimeException("Symbols file not found");
    }

    /**
     * <p>Analyzes the file to create the symbols table.</p>
     *
     * @return {@code List<String[]>} containing the data of the analyzed file
     */
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
                // boolean to control if elements are considered part of a string
                boolean isString = false;
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
                                wordInfo(list, sb.toString(), line, col - sb.length(), isString);
                                sb.setLength(0);
                            }

                            if (c == '\n') {
                                line++;
                                col = 1;
                            } else {
                                col++;
                            }
                        }

                        case ';' -> {
                            // add the word information
                            wordInfo(list, sb.toString(), line, col - sb.length(), isString);
                            // empty builder
                            sb.setLength(0);
                            // add information of ;
                            wordInfo(list, String.valueOf(';'), line, col, isString);
                            col++;
                        }

                        case '+', '-', '*', '/', '=', '!' -> {
                            // add character to builder
                            sb.append((char) c);

                            col++;
                        }

                        case '"' -> {
                            // toggles the isString flag
                            isString = !isString;
                            // add the word information
                            wordInfo(list, sb.toString(), line, col, isString);
                            // empty builder
                            sb.setLength(0);
                            // add " information
                            wordInfo(list, "\"", line, col, isString);

                            col++;

                        }

                        case '(', ')' -> {
                            //
                            wordInfo(list, sb.toString(), line, col, isString);
                            // vaciar el String
                            sb.setLength(0);
                            // add information of ( or )
                            wordInfo(list, String.valueOf((char) c), line, col, isString);

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

    /**
     * <p>Adds the word info to the provided {@code List<String[]>}.</p>
     *
     * @param l          list onto which to add the word
     * @param word       to analyze
     * @param line       of the word
     * @param col        of the word
     * @param partString if the word is inside " marks, therefore part of a string
     */
    private void wordInfo(List<String[]> l, String word, int line, int col, boolean partString) {
        // require that the list and the word are not null
        Objects.requireNonNull(l);
        Objects.requireNonNull(word);

        // if word length is 0 or is filled with spaces
        if (word.isBlank()) {
            return;
        }
        /* TEMPLATE FOR THE COLUMNS ORDER
         * "SYMBOL", "LINE", "COLUMN", "TOKEN", "ID_TOKEN", "TYPE", "TYPE1", "TYPE2"
         */
        // create new String array with the size of the number of columns
        String[] s = new String[ColumnType.values().length];
        // due to direct access to array positions, verify that they can be accessed
        if (s.length < 7) // s.length - 1 < 6 ==> s.length < 6 + 1
            throw new RuntimeException("Invalid length");

        s[0] = word;
        s[1] = String.valueOf(line);
        s[2] = String.valueOf(col);

        if (partString) { // check if the word is inside the quote marks signifying a String
            s[3] = s[6] = s[7] = "";
            s[5] = "string";
        } else if (SYMBOLS.containsKey(word)) { // check if the word is part of the symbols table
            // loop through the information of the symbol
            for (int i = 3; i < s.length; i++) {
                s[i] = SYMBOLS.get(word).get(ColumnType.values()[i]);
            }
        } else {
            s[3] = "Identificador";
            s[4] = "";
            for (int i = 4; i < s.length; i++) {
                s[i] = "";
            }
        }
        // add the array to the list
        l.add(s);
    }

    /**
     * <p>Finds the possible <b><i>Arithmetic Expressions inside the code.</i></b>.
     * <i>Due to some issues, it can consider what is inside a String as an expression.</i></p>
     *
     * @return {@code String[]} containing the expressions
     */
    public String[] findMathExpressions() {
        if (codeFile != null) {
            // read the file
            try (BufferedReader in = new BufferedReader(new FileReader(codeFile))) {

                // list to store the expressions
                List<String> list = new LinkedList<>();
                // stack to store the chars
                Deque<Integer> stack = new LinkedList<>();

                // store the char
                int c;
                /*
                 * flags
                 */
                // space after letter -> true, expression is over -> false
                boolean banderaEspacio = false;
                // symbol -> true, finish reading expression -> false
                boolean banderaSimbolo = false;
                // number -> true, no number -> false
                boolean bandNumero = false;
                // read the characters of the file
                while ((c = in.read()) != -1) {
                    // if char is an operator or space
                    if (!(c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == ' ')) {
                        // check flags
                        if (banderaEspacio && bandNumero) {
                            // check flag
                            if (banderaSimbolo) {
                                if (bandNumero) {
                                    // valid expression
                                    printM(list, stack);
                                }
                            }
                            // remove elements from stack
                            stack.clear();
                            // set flag to false
                            banderaSimbolo = false;
                        }
                        // set flag to false
                        banderaEspacio = false;
                        // set flag to on
                        bandNumero = true;
                        // add char to stack
                        stack.addFirst(c);

                        // check if character is space
                    } else if (c == ' ') {
                        // character is space
                        banderaEspacio = true;
                        // add char to stack
                        stack.addFirst(c);

                    } else {
                        // else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                        banderaSimbolo = true;
                        banderaEspacio = false;
                        bandNumero = false;
                        // add character to stack
                        stack.addFirst(c);
                    }
                }

                if (banderaSimbolo) {
                    // valid expression
                    printM(list, stack);
                }
                // return expressions
                return list.toArray(new String[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new String[0];
    }


    /**
     * <p>Adds the characters in the {@code Deque<Integer>} to the {@code List<String>} as characters.
     * It is asume that the {@code Deque<Integer>} contains only characters and that it was used as a <b>Stack.</b></p>
     *
     * @param list  a list in which to store the word
     * @param stack containing the characters
     */
    private void printM(List<String> list, Deque<Integer> stack) {
        // get iterator in descending order
        var iter = stack.descendingIterator();
        StringBuilder s = new StringBuilder();
        // while iterator has elements
        while (iter.hasNext()) {
            s.append(Character.toString(iter.next()));

        }
        // add string to the list
        list.add(s.toString());
    }

    /**
     * <p>Set the File for the analyzer, Checking that is a valid file type</p>
     */
    public void setCodeFile(File codeFile) {
        if (codeFile.getName().endsWith("." + LANGUAGE_EXTENSION)) {
            this.codeFile = codeFile;
        } else {
            throw new UnsupportedOperationException("Invalid File");
        }
    }

    /*
     * THIS SECTION IS NOT IMPLEMENTED YET OR USED
     */

    private static void arithmeticExpression(String f) {
        String[][] strings = codeTo2DArray(f);
        Objects.requireNonNull(strings);
        String regex = ".*[ +*/%-].*";
        List<String> math = new LinkedList<>();

        for (int line = 0; line < strings.length; line++) {
            for (int j = 0; j < strings[line].length; j++) {
//                System.out.print(strings[i][j] + "\t");
//                if (strings[line][j].matches(regex))

            }
//            System.out.println();
        }

    }

    private static String[][] codeTo2DArray(String f) {
        try (BufferedReader in = new BufferedReader(new FileReader(f))) {
            List<String[]> list = new LinkedList<>();
            String s;
            while ((s = in.readLine()) != null) {
                list.add(s.split(" "));
            }
            return list.toArray(String[][]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
