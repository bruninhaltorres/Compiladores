package analisadores.lexico;

import java.io.BufferedReader;
import java.io.FileReader;

public class lexical {
    private HashTablePL hashTable = new HashTablePL();
    private int linha, coluna, posicao;

    private char[] context;
    private BufferedReader bufReader;

    public lexical(String file) {

    }

    public Tokens symbolTokens(char character){
        if(character == ';') {
            return Tokens.S_PVIRG;
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
        else if(character == '+') {
            return Tokens.OPR_ADD;
        }
        else if(character == '-') {
            return Tokens.OPR_SUB;
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