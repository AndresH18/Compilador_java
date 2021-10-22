package text_editor.glc;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/*
    <Expr> ::= <Term> <ExprPrime>
    <ExprPrime> ::= <Operator1> <Term> <ExprPrime>
                    | epsilon
    <Term> ::= <Factor> <TermPrime>
    <TermPrime> ::= <Operator2> <Factor> <TermPrime>
                    | epsilon
    <Factor> ::= ( <Expr> )
                    | <Num>
                    | <Ident>
    <Operator1> ::= +
                    | -
    <Operator2> ::= *
                    | /
    <Num> ::= <dig> <numPrime>
    <NumPrime> ::= <dig> <NumPrime>
                    | epsilon

 */

/**
 * <p>Class for GLC of Arithmetic Expressions</p>
 */
public class ArithmeticExpressions {
    private Character c;
    private int currentPosition;
    private int paren;
    private final String word;
    private final Queue<String> errors = new LinkedList<>();
    private final Queue<Character> content = new LinkedList<>();

    /**
     * <p>Public constructor, that receives the expression to evaluate</p>
     *
     * @param word the expression to evaluate
     */
    public ArithmeticExpressions(String word) {
        this.word = word;
        c = word.charAt(0);
        currentPosition = 0;
    }

    /**
     * <p>Checks if the Expression is valid.</p>
     *
     * @return a message indicating the result of the analysis
     */
    public final String checkExpression() {
        if (checkParen()) {
            expression();
            if (errors.isEmpty() && word.length() == content.size()) {
                // return new String[]{queueToString(content), Arrays.toString(content.toArray()), "The Expression is Valid"};
//                return queueToString(content) + "The Expression is Valid!";
                return ExpressionForms.getExpressionForms(queueToString(content));
            } else if (!errors.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String error : errors) {
                    sb.append(error).append('\n');
                }
                return sb.toString();
//                return sb.toString();
            } else {
                return "The expression is Not Valid.";
//                return "The expression is Not Valid";
            }
        } else {
            return errors.peek();
//            return errors.peek();
        }
    }

    private <T> String queueToString(Queue<T> queue) {
        StringBuilder sb = new StringBuilder();
        for (T t : queue) {
            sb.append(t);
        }
        return sb.toString();
    }

    /* from here */

    /**
     * <p>Check if there is the same amount of opening and closing parenthesis</p>
     * If the amounts are different, add an error message.
     *
     * @return true if there is the same amount, false otherwise.
     */
    private boolean checkParen() {
//        int n = 0;
//        for (char c1 : word.toCharArray()) {
//            if (c1 == '(') {
//                n++;
//            } else if (c1 == ')') {
//                n--;
//            }
//        }

        Deque<Character> stack = new LinkedList<>();

        for (char c1 : word.toCharArray()) {
            if (c1 == '(') {
                stack.push(c1);
            } else if (c1 == ')') {
                if (stack.isEmpty()) {
                    errors.add("Parenthesis error");
                    return false;
                } else {
                    stack.pop();
                }
            }
        }
        return stack.isEmpty();


//        if (n == 0) {
//            return true;
//        } else if (n > 0) {
//            errors.add("Missing Opening ('s");
//        } else {
//            errors.add("Missing Closing )'s");
//        }
//        return false;
    }


    private void expression() {
        term();
        expressionPrime();
    }

    private void expressionPrime() {
        if (operator1()) {
            term();
            expressionPrime();
        } else if (match(false, '(')) {
            error("Expression");
            factor();
        }
        // epsilon
    }

    private void term() {
        factor();
        termPrime();
    }

    private void termPrime() {
        if (operator2()) {
            factor();
            termPrime();
        } else if (match(false, '(')) {
            error("Term");
            factor();
        }
        // epsilon
    }

    private void factor() {
        if (match(true, '(')) {
            expression();
            if (!match(true, ')')) {
                error("Closing )");
            }
        } else if (c != null && Character.isDigit(c)) {
            num();
        } else if (c != null && Character.isLetter(c)) {
            ident();
        } else {
            // error
            error("digit");
//            nextChar();
            if (c != null) {
                if (c == '+' || c == '-') {
//                redoChar();
                    // expressionPrime
                    expressionPrime();
                } else if (c == '*' || c == '/') {
//                redoChar();
                    // termPrime
                    termPrime();
                } else if (match(false, ')')) {
//                    redoChar(); // este caracter estorba
                    error("Expression");
                }
            }
        }
    }

    private void num() {
        dig();
        numPrime();
    }

    private void numPrime() {
        if (dig()) {
            numPrime();
        }
        // epsilon
    }

    private boolean dig() {
        return match(true, '0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
    }

    private void ident() {
        letra();
        identPrime();
    }

    private void identPrime() {
        if (letra()) {
            identPrime();
        }
    }

    private boolean letra() {
        if (c != null && Character.isLetter(c)) {
            content.add(c);
            nextChar();
            return true;
        }
        return false;
    }

    private boolean operator1() {
        return match(true, '+', '-');
    }

    private boolean operator2() {
        return match(true, '*', '/');
    }

    private boolean match(boolean save, char... chars) {
        if (c != null) {
            for (char aChar : chars) {
                if (c == aChar) {
                    if (save) {
                        content.add(c);
                        nextChar();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void nextChar() {
        if (currentPosition + 1 < word.length()) {
            c = word.charAt(++currentPosition);
        } else {
            c = null;
        }
    }

    /**
     * <p>Publish error</p>
     *
     * @param expected the value that was expected
     * @param <T>      generic type to allow for multiple parameter types
     */
    private <T> void error(T expected) {
        errors.add("Received '" + c + "' on column " + (currentPosition + 1) + ", while expecting " + expected.toString());
    }

    private void redoChar() {
        if (currentPosition - 1 >= 0) {
            c = word.charAt(--currentPosition);
        } else {
            c = 0;
        }
    }

    /**
     * <p>Private class to convert infix expression to postfix and prefix.</p>
     */
    private static class ExpressionForms {
        /**
         * <p>{@link String} used  for control.</p>
         */
        private static final String REPLACE = ";";
        /**
         * <p>{@link String} used  for control.</p>
         */
        private static final String CHANGE = "##";

        /**
         * <p>Devuelve la expression de forma infija, postfija, infija.</p>
         * <p>
         * InFix   : < infix > <br>
         * PreFix  : < prefix > <br>
         * PostFix : < postfix > <br>
         * </p>
         *
         * @param exp la cadena a convertir
         * @return una cadena con
         */
        public static String getExpressionForms(String exp) {
            return String.format("%-8s: %s\n%-8s: %s\n%-8s: %s\n",
                    "InFix", exp,
                    "PreFix", infixToPrefix(exp),
                    "PostFix", infixToPostfix(exp));
        }

        /**
         * <p>Invierte la cadena, remplazando ")" con "(" y "(" con ")"</p>
         *
         * @param s la cadena a invertir
         * @return la cadena invertida
         */
        private static String reverse(String s) {
            s = new StringBuilder(s).reverse().toString();
            s = s.replace(" ", "")
                    .replace("(", CHANGE)
                    .replace(")", "(")
                    .replace(CHANGE, ")")
                    .replace("*", " * ")
                    .replace("/", " / ")
                    .replace("+", " + ")
                    .replace("-", " - ")
                    .replace("^", " ^ ")
                    .replace("(", "( ")
                    .replace(")", " )");
            return s;
        }

        /**
         * <p>Converts the {@param s} to prefix expression.</p>
         *
         * @param s the string to convert
         * @return the prefix expression
         */
        private static String infixToPrefix(String s) {
            s = reverse(s);
            s = infixToPostfix(s, true);
            s = reverse(s);
            s = s.replace(" ", "")
                    .replace(REPLACE + REPLACE, " ")
                    .replace(REPLACE, "");
            return s;
        }

        /**
         * <p>Converts the string to postfix expression.</p>
         *
         * @param s the string to convert
         * @return the postfix expression
         */
        private static String infixToPostfix(String s) {
            return infixToPostfix(s, false);
        }

        /**
         * <p>Converts the string to postfix expression.</p>
         *
         * @param exp    the string to convert
         * @param prefix if the transformation is meant for a real postfix or for prefix.
         * @return the postfix expression
         */
        private static String infixToPostfix(String exp, boolean prefix) {
            StringBuilder sb = new StringBuilder();
            Deque<String> stack = new LinkedList<>();

            exp = exp.replace(" ", "")
                    .replace("*", " * ")
                    .replace("/", " / ")
                    .replace("+", " + ")
                    .replace("-", " - ")
                    .replace("^", " ^ ")
                    .replace("(", "( ")
                    .replace(")", " )");

            String[] strings = exp.split(" ");

            for (String string : strings) {

                if (string.matches("[A-Za-z0-9]+")) {
                    sb.append(prefix ? REPLACE : "").append(string).append(prefix ? REPLACE : " ");

                } else if (string.equalsIgnoreCase("(")) {
                    stack.push(string);

                } else if (string.equalsIgnoreCase(")")) {
                    while (!stack.isEmpty() && !stack.peek().equalsIgnoreCase("(")) {
                        sb.append(prefix ? REPLACE : "").append(stack.pop()).append(prefix ? REPLACE : " ");
                    }
                    stack.pop();

                } else {
                    if (!prefix) {
                        while (!stack.isEmpty() && precedence(string) <= precedence(stack.peek())) {
//                            sb.append(prefix ? REPLACE : "").append(stack.pop()).append(prefix ? REPLACE : " ");
                            sb.append(stack.pop()).append(" ");

                        }
                    } else {
                        while (!stack.isEmpty() && precedence(string) < precedence(stack.peek())) {
//                            sb.append(prefix ? REPLACE : "").append(stack.pop()).append(prefix ? REPLACE : " ");
                            sb.append(REPLACE).append(stack.pop()).append(REPLACE);
                        }
                    }
                    stack.push(string);
                }
            }
            while (!stack.isEmpty()) {
                if (stack.peek().equalsIgnoreCase("(")) {
                    return "Invalid";
                }
                sb.append(prefix ? REPLACE : "").append(stack.pop()).append(prefix ? REPLACE : " ");
            }
            return sb.toString();
        }

        /**
         * <p>Devuelve la precedencia de la cadena, si no es un operador entonces devuelve -1.</p>
         *
         * @param s la cadena para revisar la precedencia
         * @return la precedencia de la cadena
         */
        private static int precedence(String s) {
            return switch (s) {
                case "+", "-" -> 1;
                case "*", "/" -> 2;
                // case "^" -> 3;
                default -> -1;
            };
        }
    }
}