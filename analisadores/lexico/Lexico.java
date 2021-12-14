package analisadores.lexico;

import java.io.BufferedReader;
import java.io.FileReader;

public class Lexico {
    private final HashTablePL hashTable = new HashTablePL(); // final: É um método que não deixa que a variável seja sobrescrita nas subclasses.
    private int line, columm, position;

    private char[] content;// guardar a linha caracter por caracter
    private BufferedReader doc; //nosso programa a ser executado

    private String currentLine = " "; // guarda a linha inteira.

    Tokens token;

    public Lexico(String file) {
        try{
            line = 1;
            columm = 1;
            position = 0;
            doc = new BufferedReader(new FileReader(file)); // new BufferedReader(new FileReader(new File(archive))); ???
            nextLine();
            content = currentLine.toCharArray(); // converter a string em um array char.
        }catch(Exception exception) {
            exception.printStackTrace(); // para auxiliar no encontro do erro.
        }   
    }

    public boolean nextLine(){ // pq a função é booleana?
        String currentLineAux = " ";
        try {
            currentLineAux = doc.readLine();
        }catch(Exception exception) {
            exception.printStackTrace();
        }

        if(currentLineAux != null) {
            currentLine = currentLineAux;
            System.out.printf("%4d %s %n", line, currentLine);
            currentLine += " "; // pq concatena um espaço?
            line++;
            position = 0;
            columm = 0; // pq volta pra posição 0 e não pra posição 1? o espaço tem haver?

            return true;
        }
        return false;
    }

    public Tokens nextToken() {
        
    }

    public Tokens symbolTokens(char character){
        if(character == ';') {
            return Tokens.S_PVIRG;
        }
        else if(character == '_'){
            return Tokens.UNDER;
        }
        else if(character == ',') {
            return Tokens.S_PVIRG;
        }
        else if(character == '(') {
            return Tokens.OP_PAR;
        }
        else if(character == ')') {
            return Tokens.CL_PAR;
        }
        else if(character == '[') {
            return Tokens.OP_COLC;
        }
        else if(character == ']') {
            return Tokens.CL_COLC;
        }
        else if(character == '{') {
            return Tokens.OP_CHAVES;
        }
        else if(character == '}') {
            return Tokens.CL_CHAVES;
        }
        else if(character == '+') {
            return Tokens.OPR_ADD;
        }
        else if(character == '-') {
            return Tokens.OPR_SUB;
        }
        else if(character == '=') {
            return Tokens.OPR_IGUAL;
        }
        else if(character == '!') {
            return Tokens.OPR_NOT;
        }
        else if(character == '*') {
            return Tokens.OPR_MULT;
        }
        else if(character == '/') {
            return Tokens.OPR_DIV;
        }
        else if(character == '%') {
            return Tokens.OPR_MOD;
        }
        else {
            return Tokens.ERR_DESC;
        }
    }
}