package text_editor.glc;

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
    private String word;
    private Queue<String> errors = new LinkedList<>();
    private Queue<Character> content = new LinkedList<>();

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
                return "The Expression is Valid!";
            } else if (!errors.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String error : errors) {
                    sb.append(error).append('\n');
                }
                return sb.toString();
            } else {
                return "The expression is Not Valid";
            }
        } else {
            return errors.peek();
        }
    }

    /* from here */

    /**
     * <p>Check if there is the same amount of opening and closing parenthesis</p>
     * If the amounts are different, add an error message.
     *
     * @return true if there is the same amount, false otherwise.
     */
    private boolean checkParen() {
        int n = 0;
        for (char c1 : word.toCharArray()) {
            if (c1 == '(') {
                n++;
            } else if (c1 == ')') {
                n--;
            }
        }

        if (n == 0) {
            return true;
        } else if (n > 0) {
            errors.add("Missing Opening ('s");
        } else {
            errors.add("Missing Closing )'s");
        }
        return false;
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

    private void redoChar() {
        if (currentPosition - 1 >= 0) {
            c = word.charAt(--currentPosition);
        } else {
            c = 0;
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
}