package text_editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class Analyzer {

    private Analyzer() {
        // No instantiable
    }

    /**
     * <p>Analyzes the file to create the symbols table.</p>
     *
     * @param file the file to analyze.
     * @return {@code List<String[]>} with the elements of the table
     * @throws CompilerExceptions.UnexpectedException   if there was an error analyzing the file.
     * @throws CompilerExceptions.FileNotFoundException if the file could not be found.
     */
    public static List<String[]> generateTable(File file) throws CompilerExceptions.UnexpectedException, CompilerExceptions.FileNotFoundException {
        // read the lines form file
        String[] lines = Objects.requireNonNull(getTextFromFile(file));
        /* control flags */
        boolean isString = false;
        boolean space = false;
        // String Builder
        StringBuilder sb = new StringBuilder();
        // List of type String[] containing the data of the table
        List<String[]> list = new LinkedList<>();
        // move between lines
        for (int i = 0; i < lines.length; i++) {
            // move through line
            for (int j = 0; j < lines[i].length(); j++) {

                if (Character.isLetterOrDigit(lines[i].charAt(j)) || lines[i].charAt(j) == '_') {
                    sb.append(lines[i].charAt(j));
                    space = false;

                } else if (lines[i].charAt(j) == '(' || lines[i].charAt(j) == ')') {
                    addWordInfo(list, sb.toString(), i, j - sb.length(), isString);
                    sb.setLength(0);
                    addWordInfo(list, String.valueOf(lines[i].charAt(j)), i, j, isString);
                    space = false;

                } else if (lines[i].charAt(j) == '"') {
                    // print word info, take into account the isString
                    addWordInfo(list, sb.toString(), i, j - sb.length(), isString);

                    sb.setLength(0);
                    // print char
                    addWordInfo(list, "\"", i, j, isString);

                    isString = !isString;
                    space = false;

                } else if (lines[i].charAt(j) == ' ' || lines[i].charAt(j) == '\n') {
                    if (space && !isString) {
                        // report invalid space
                    } else {
                        if (!sb.isEmpty()) {
                            // print word info
                            addWordInfo(list, sb.toString(), i, j - sb.length(), isString);
                            sb.setLength(0);
                        }
                        space = true;
                    }

                } else if (lines[i].charAt(j) == ';') {
                    // print word info
                    addWordInfo(list, sb.toString(), i, j - sb.length(), isString);
                    sb.setLength(0);

                    addWordInfo(list, ";", i, j - sb.length(), isString);

                } else if (isOperator(lines[i].charAt(j))) {
                    sb.append(lines[i].charAt(j));
                    space = false;
                }
            }

        }

        return list;
    }

    /**
     * <p>Returns the lines of the file.</p>
     *
     * @param file the file to read
     * @return {@code String[]} containing the file lines.
     * @throws CompilerExceptions.UnexpectedException   if there was an error analyzing the file.
     * @throws CompilerExceptions.FileNotFoundException if the file could not be found.
     */
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
     * <p>Check if the char is a valid operator</p>
     *
     * @param c the character to check.
     * @return true if the character is a valid operator, false otherwise.
     */
    private static boolean isOperator(char c) {
        return switch (c) {
            case '+', '-', '*', '/', '=', '!' -> true;
            default -> false;
        };
    }

    /**
     * <p>Check if the {@link String} contains a valid operator.</p>
     *
     * @param s the {@link String} to check.
     * @return true if the {@link String} has a valid operator
     */
    private static boolean containsOperator(String s) {
        return s.contains("+")
                || s.contains("-")
                || s.contains("*")
                || s.contains("/")
                || s.contains("=")
                || s.contains("!");

    }

    /**
     * <p>Adds the word info to the provided {@code List<String[]>}.</p>
     *
     * @param l        list onto which to add the word
     * @param word     to analyze
     * @param line     of the word
     * @param col      of the word
     * @param isString if the word is inside " marks, therefore part of a string
     */
    private static void addWordInfo(List<String[]> l, String word, int line, int col, boolean isString) {
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

        if (isString) { // check if the word is inside the quote marks signifying a String
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

    /**
     * <p>Finds the possible <b><i>Arithmetic Expressions inside the code.</i></b>.
     * <i>Due to some issues, it can consider what is inside a String as an expression.</i></p>
     *
     * @return {@code String[]} containing the expressions
     */
    public static String[] findMathExpressions(File codeFile) {
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
            return list.toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
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
    private static void printM(List<String> list, Deque<Integer> stack) {
        // get iterator in descending order
        var iter = stack.descendingIterator();
        StringBuilder s = new StringBuilder();
        String c;
        // while iterator has elements
        while (iter.hasNext()) {
            c = Character.toString(iter.next());
            if (c.contains(";")) {
                break;
            }
            s.append(c);

        }
        // add string to the list
        list.add(s.toString());
    }


    /**
     * <p>Analyzes the file to create the symbols table.</p>
     *
     * @return {@code List<String[]>} containing the data of the analyzed file
     */
    @Deprecated(forRemoval = true)
    public List<String[]> analyzeFile(File codeFile) {
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
    @Deprecated(forRemoval = true)
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
     * GRAMATICA LIBRE DE CONTEXTO
     */

    // TODO: this is what alejo is in charge
    public static String checkExpression(String s) {
        return "Hello World";
    }


    private static void expressionPrima(String e) {
//        if (e.isBlank()) {
//            return;
//        }
        String[] s = e.split(" ");
        int pos = 0;
        for (int i = 0; i < s.length; pos += s[i++].length() + 1) {
            if (s[i].length() == 0) {
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
}
