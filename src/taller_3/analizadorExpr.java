package taller_3;
import java.util.*;


public class analizadorExpr {

    char Token_Entrada;
    int posicion;
    char cadenaAnalizada[];

    void iniciarAnalisis(char x[]) {
        System.out.println("Iniciando analisis");
        cadenaAnalizada = x;
        posicion = 0;
        Token_Entrada=primerToken();
        expresion();
    }

    void match(char t){
        if (t == Token_Entrada) {
            Token_Entrada = SigToken();
        } else {
                System.out.println("errormatch");
        }
    }
    char primerToken(){
        posicion++;
        return (cadenaAnalizada[0]);
    }
    char SigToken() {
        if (posicion==cadenaAnalizada.length) {
            System.out.println("\nAnalisis finalizado");
            System.exit(0);
            return 'x';
        }else {
            posicion++;
            return (cadenaAnalizada[posicion - 1]);
        }
    }
    void expresion(){
        term();
        expresion_prima();
    }

    void expresion_prima(){
        if(Token_Entrada=='+' || Token_Entrada=='-'){
            System.out.print(Token_Entrada);
            match(Token_Entrada);
            term();
            expresion_prima();
        }else{
            ;
        }
    }

    void term(){
        factor();
        term_prima();
    }

    void term_prima(){
        if (Token_Entrada=='*' || Token_Entrada=='/'){
            System.out.print(Token_Entrada);
            match(Token_Entrada);
            factor();
            term_prima();
        }
    }

    void factor(){
        if (Token_Entrada=='('){
            System.out.print(Token_Entrada);
            match(Token_Entrada);
            expresion();
            if (Token_Entrada==')') {
                System.out.print(Token_Entrada);
                match(Token_Entrada);
            }else{
                System.out.println("errorFactor");
            }
        }else if(Token_Entrada==')'){
            System.out.println("ErrorFactor");
        } else if (Character.isDigit(Token_Entrada)){
            num();
        }else if (Character.isLetter(Token_Entrada)){
            ident();
        }else{
            System.out.println("ErrorFactor");
        }

    }

    void num(){
        dig();
        num_prima();
    }

    void num_prima(){
        if (Character.isDigit(Token_Entrada)){
            dig();
            num_prima();
        }else if(Character.isLetter(Token_Entrada)) {
            System.out.println("Error numprima");
        }else{
            ;
        }

    }

    void dig(){
        if (Character.isDigit(Token_Entrada)){
            System.out.print(Token_Entrada);
            match(Token_Entrada);
        }else{
            System.out.print("errordig");
        }
    }

    void ident(){
        letra();
        ident_prima();
    }

    void ident_prima(){
        if (Character.isLetter(Token_Entrada)){
            letra();
            ident_prima();
        }else if(Character.isDigit(Token_Entrada)){
            System.out.println("errorIdentprima");
        }
        else{
            ;
        }
    }

    void letra(){
        if (Character.isLetter(Token_Entrada)){
            System.out.print(Token_Entrada);
            match(Token_Entrada);
        }else{
            System.out.println("ErrorLetra");
        }
    }

    public static void main(String[] args) {
        analizadorExpr a=new analizadorExpr();
        System.out.println("Inserte la cadena a analizar");
        Scanner sc = new Scanner(System.in);
        String cadena = sc.nextLine();
        char[] cadenaAnalizada = cadena.toCharArray();
        a.iniciarAnalisis(cadenaAnalizada);
    }

}
