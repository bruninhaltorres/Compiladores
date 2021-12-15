package analisadores.lexico;

import java.io.BufferedReader;
import java.io.FileReader;

public class Lexico {
    private final HashTablePL hashTable = new HashTablePL(); // final: É um método que não deixa que a variável seja sobrescrita nas subclasses.
    private int line, column, position, state;

    private char[] content;// guardar a linha caracter por caracter
    private BufferedReader doc; //nosso programa a ser executado

    private String currentLine = " "; // guarda a linha inteira.

    // TokensClass token;

    public Lexico(String file) {
        try{
            line = 1;
            column = 1; // vai se referir onde começa os tokens
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
            currentLine += " "; // apenas por uso visual
            line++;
            position = 0;
            column = 0; // se refere a coluna do content

            return true;
        }
        return false;
    }

    public TokensClass nextToken() {
        char currentChar;
		String lex = "";
		state = 0;
        while(true) {
            if(isEOF()){ // se não é EOF, ou seja, não chegou ao final do arquivo
                if(nextLine()){ // verifica se a proxima linha não é null. Se não for,
                    content = currentLine.toCharArray(); // salva esse linha
                } else {
                    return new TokensClass(Tokens.EOF, "EOF", line, column);
                }
            }
            currentChar = nextChar();
            switch(state){
                case 0:
                    if(currentChar == '_'){
                        lex += currentChar;
                        state = 1;
                    } else if(isDigit(currentChar)) {
                        lex += currentChar;
                        state = 4;
                    } else if(isOperator(currentChar)) {
                        lex += currentChar;
                        state = 7;
                    } else if(isOther(currentChar) ) {
                        state = 0;
                    } else if(currentChar == '+') {
                        lex += currentChar;
                        column++;
                        return new TokensClass(Tokens.OPR_ADD,lex,line,column);
                    } else if(currentChar == '-') {
                        lex += currentChar;
                        column++;
                        return new TokensClass(Tokens.OPR_SUB,lex,line,column);
                    } else if(currentChar == '*') {
                        lex += currentChar;
                        column++;
                        return new TokensClass(Tokens.OPR_MULT,lex,line,column);
                    } else if(currentChar == '/') {
                        lex += currentChar;
                        column++;
                        return new TokensClass(Tokens.OPR_DIV,lex,line,column);
                    } else if(currentChar == '%') {
                        lex += currentChar;
                        column++;
                        return new TokensClass(Tokens.OPR_MOD,lex,line,column);
                    } else if(currentChar == '(') {
                        lex += currentChar;
                        column++;
                        return new TokensClass(Tokens.OP_PAR,lex,line,column);
                    } else if(currentChar == ')') {
                        lex += currentChar;
                        column++;
                        return new TokensClass(Tokens.CL_PAR,lex,line,column);
                    } else if(currentChar == '[') {
                        lex += currentChar;
                        column++;
                        return new TokensClass(Tokens.OP_COLC,lex,line,column);
                    } else if(currentChar == ']') {
                        lex += currentChar;
                        column++;
                        return new TokensClass(Tokens.CL_COLC,lex,line,column);
                    } else if(currentChar == ';') {
                        lex += currentChar;
                        column++;
                        return new TokensClass(Tokens.S_PVIRG,lex,line,column);
                    } else if(currentChar == ',') {
                        lex += currentChar;
                        column++;
                        return new TokensClass(Tokens.S_VIRG,lex,line,column); 
                    } else if(currentChar == '&') {
                        lex += currentChar;
                        column++;
                        return new TokensClass(Tokens.OPR_CONC,lex,line,column);
                    } else {
                        new TokensClass(Tokens.ERR_DESC,lex,line,column);
                    }
                    break;

                case 1:
                    if(isLetter(currentChar)){
                        lex += currentChar;
                        state = 2;
                    } else {
                        new TokensClass(Tokens.ERR_ID, lex, line, column);
                    }
                case 2:
                    if(isLetter(currentChar) || isDigit(currentChar)){
                        lex += currentChar;
                    } else if (isOther(currentChar) || isOperator(currentChar)){
                        back();
					    state = 3;
                    } else {
                        column++;
                        new TokensClass(Tokens.ERR_ID,lex,line,column);
                    }
                    break;
                case 3:
                    back();
                    column++;
                    return new TokensClass(Tokens.ID,lex,line,column);
                    
            }
        }
        
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

    private boolean isEOF() { // retorna true se a posição for do tamanho do array content. 
        return position == content.length;
    }

    private void back() {
        position--;
    }

    private char nextChar() {
        return content[position++];
    }

    private boolean isDigit(char ch) {
		return Character.isDigit(ch);
	}

    private boolean isLetter(char ch) {
		return Character.isLetter(ch);
	}

    private boolean isOperator(char ch) {
        return ch == '>' || ch == '<' || ch == '=' || ch == '!';
    }

    private boolean isOther(char ch) {
        return Character.isWhitespace(ch) || ch == '\t' || ch == '\n' || ch == '\r' || ch == '\f' || ch == '\0' || ch == '\b';
    }
}