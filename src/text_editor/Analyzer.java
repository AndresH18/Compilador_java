package text_editor;

import text_editor.old.ColumnType;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class Analyzer {

    private Analyzer() {
        // No instantiable
    }


    private static void generateTable(File file) throws CompilerExceptions.UnexpectedException, CompilerExceptions.FileNotFoundException {
        String[] strings = getTextFromFile(file);

    }


    private static String[] getTextFromFile(File file) throws CompilerExceptions.FileNotFoundException, CompilerExceptions.UnexpectedException {
        if (file == null || !file.exists()) {
            throw new CompilerExceptions.FileNotFoundException("File not Found");
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            List<String> l = new LinkedList<>();
            String s;
            while ((s = in.readLine()) != null) {
                l.add(s);
            }
            return l.toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
//            throw new CompilerExceptions.UnexpectedException(e);
        }
        return null;
    }

    /**
     * <p>Analyzes the file to create the symbols table.</p>
     *
     * @return {@code List<String[]>} containing the data of the analyzed file
     */
    private List<String[]> analyzeFile(File file) throws CompilerExceptions.FileNotFoundException {
        if (!Objects.requireNonNull(file).exists()) {
            throw new CompilerExceptions.FileNotFoundException("File not found!");
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
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
        } else if (Compiler.SYMBOLS.containsKey(word)) { // check if the word is part of the symbols table
            // loop through the information of the symbol
            for (int i = 3; i < s.length; i++) {
                s[i] = Compiler.SYMBOLS.get(word).get(ColumnType.values()[i]);
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




    /*
     * GRAMÁTICA LIBRE DE CONTEXTO
     */


    private static void expressionPrima(String e) {
//        if (e.isBlank()) {
//            return;
//        }
        String[] s = e.split(" ");
        int pos = 0;
        for (int i = 0; i < s.length; pos += s[i++].length() + 1) {
            if (s[i].length() == 0) {
                // TODO: reportar espacio invalido
            }
        }
        expressionPrima(s, 0);

        // 5 + 6 * 8 + 2
//        String s = e.substring(0, e.indexOf(" "));
//
//        expressionArithmetic(e.substring(s.length() + 1));
    }

    private static void expressionPrima(String[] s, int n) {
        if (n == s.length) {
            return;
        }
        termino(s, n);
        // TERM
        if (s[n].matches("[0-9]+")) {

        } else if (s[n].equals("(")) {

        } else {
            //
        }
    }

    private static void termino(String[] s, int n) {
        factor(s, n);
        terminoPrimo(s, n + 1);
    }

    private static void terminoPrimo(String[] s, int n) {
        // * factor terminoPrimo
        // / factor terminoPrimo
        // ""
        if (s[n].equals("*")) {
            factor(s, n + 1);
        }
    }

    private static void factor(String[] s, int n) {

    }


    public static void helloWorld() {
        System.out.println("Hello World!!!");
    }


}
