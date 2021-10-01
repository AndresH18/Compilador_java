package taller_3;
import java.util.*;


public class analizadorExpr {

    char Token_Entrada;
    int posicion;
    char cadenaAnalizada[];

    void match(char t){
        if (t == Token_Entrada){
            Token_Entrada= SigToken();
        }else{
            System.out.println("errormatch");
        }
    }
    char primerToken(){
        posicion++;
        return (cadenaAnalizada[0]);
    }
    char SigToken(){
        posicion++;
        return(cadenaAnalizada[posicion-1]);
    }
    void expresion(){
        term();
        expresion_prima();
    }

    void expresion_prima(){
        if(){

        }else{
            ;
        }
    }

    void term(){
        factor();
        term_prima();

    }

    void term_prima(){
        if (){

        }else{
            ;
        }
    }

    void factor(){

    }

    void num(){
        dig();
        num_prima();
    }

    void num_prima(){
        if (Character.isDigit(Token_Entrada)){
            dig();
            num_prima();
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
        }else{
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

    public void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String expr = sc.nextLine();
        System.out.println("Iniciando analisis");
        posicion=0;
        Token_Entrada=primerToken();
        expresion();
    }

}
