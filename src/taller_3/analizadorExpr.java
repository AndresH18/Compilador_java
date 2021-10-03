package taller_3;

import java.util.*;


public class analizadorExpr {

    char Token_Entrada;
    int posicion;
    char[] cadenaAnalizada;

    void iniciarAnalisis(char x[]) {
        System.out.println("Iniciando analisis");
        cadenaAnalizada = x;
        posicion = 0;
        Token_Entrada = primerToken();
        expresion();
    }

    void match(char t) {
        if (t == Token_Entrada) {
            Token_Entrada = SigToken();
        } else {
            System.out.println("errormatch");
        }
    }

    char primerToken() {
        posicion++;
        return (cadenaAnalizada[0]);
    }

    char SigToken() {
        if (posicion == cadenaAnalizada.length) {
            System.out.println("\nAnalisis finalizado");
            System.exit(1);
            return 'x';
        } else {
            posicion++;
            return (cadenaAnalizada[posicion - 1]);
        }
    }

    void expresion() {
        term();
        expresion_prima();
    }

    void expresion_prima() {
        if (Token_Entrada == '+' || Token_Entrada == '-') {
            System.out.print(Token_Entrada);
            match(Token_Entrada);
            term();
            expresion_prima();
        } else {
            ;
        }
    }

    void term() {
        factor();
        term_prima();

    }

    void term_prima() {
        if (Token_Entrada == '*' || Token_Entrada == '/') {
            System.out.print(Token_Entrada);
            match(Token_Entrada);
            factor();
            term_prima();
        }
    }

    void factor() {
        if (Token_Entrada == '(') {
            System.out.print(Token_Entrada);
            match(Token_Entrada);
            expresion();
            if (Token_Entrada == ')') {
                System.out.print(Token_Entrada);
                match(Token_Entrada);
            } else {
                System.out.println("errorFactor");
            }
        } else if (Token_Entrada == ')') {
            System.out.println("ErrorFactor");
        } else if (Character.isDigit(Token_Entrada)) {
            num();
        } else if (Character.isLetter(Token_Entrada)) {
            ident();
        } else {
            System.out.println("ErrorFactor");
        }

    }

    void num() {
        dig();
        num_prima();
    }

    void num_prima() {
        if (Character.isDigit(Token_Entrada)) {
            dig();
            num_prima();
        } else if (Character.isLetter(Token_Entrada)) {
            System.out.println("Error numprima");
        } else {
            ;
        }

    }

    void dig() {
        if (Character.isDigit(Token_Entrada)) {
            System.out.print(Token_Entrada);
            match(Token_Entrada);
        } else {
            System.out.print("errordig");
        }
    }

    void ident() {
        letra();
        ident_prima();
    }

    void ident_prima() {
        if (Character.isLetter(Token_Entrada)) {
            letra();
            ident_prima();
        } else if (Character.isDigit(Token_Entrada)) {
            System.out.println("errorIdentprima");
        } else {
            ;
        }
    }

    void letra() {
        if (Character.isLetter(Token_Entrada)) {
            System.out.print(Token_Entrada);
            match(Token_Entrada);
        } else {
            System.out.println("ErrorLetra");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String cadena = "";
        while (!cadena.equalsIgnoreCase("exit")) {
            analizadorExpr a = new analizadorExpr();
            System.out.println("Inserte la cadena a analizar");
            cadena = sc.nextLine();
            char[] cadenaAnalizada = cadena.toCharArray();
            a.iniciarAnalisis(cadenaAnalizada);
        }
    }

}

class AnalizadorExpresiones2 {

    private Queue<String> error;
    private Queue<Character> content = new LinkedList<>();

    private String word;
    private char c;
    private int position;

    public AnalizadorExpresiones2(String word) {
        error = new LinkedList<>();
        this.word = word;
        c = word.charAt(0);
        position = 0;
    }

    private boolean match(char c1) {
//        System.out.println(word.charAt(position) + " === " + c1);
        if (word.charAt(position) == c1) {
            return true;
        }
        return false;

//        c = word.charAt(++position);
    }

    private void nextChar() {
        content.add(c);
        if (position + 1 == word.length()) {
            return;
        }
        c = word.charAt(++position);
    }

    public void start() {
        expression();
    }

    private void expression() {
        term();
        expressionPrime();
    }

    private void expressionPrime() {
        if (match('+') || match('-')) {
            nextChar();
            term();
            expressionPrime();
        } else {
            // epsilon
        }
    }

    private void term() {
        factor();
        termPrime();
    }

    private void termPrime() {
        if (match('*') || match('/')) {
            nextChar();
            factor();
            termPrime();
        } else {
            // epsilon
        }
    }

    private void factor() {
        if (match('(')) {
            nextChar();
            expression();
            if (match(')')) {
                nextChar();
            }
        } else if (Character.isDigit(c)) {
            nextChar();
        }
    }

    public Queue<Character> getContent() {
        return content;
    }

    public static void main(String[] args) {

//        TestGround t = new TestGround(new Scanner(System.in).nextLine());
        var t = new AnalizadorExpresiones2(args[0]);
        t.start();
    }
}
