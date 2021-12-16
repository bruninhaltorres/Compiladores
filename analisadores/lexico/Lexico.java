package analisadores.lexico;
import java.io.BufferedReader;
import java.io.FileReader;

import analisadores.lexico.tokens.Tokens;
import analisadores.lexico.tokens.TokensClass;
import analisadores.lexico.tokens.TokensFile;

public class Lexico {
    private final HashTablePL hashTable = new HashTablePL();
    private int line, column, position, state;
    private char[] content;
    private BufferedReader doc;
    private String currentLine = " "; 
    // private TokensFile myTokens = new TokensFile("token-outputs");

    // TokensClass token;

    public Lexico(String file) {
        try{
            line = 1;
            column = 1; // vai se referir onde começa os tokens
            position = 0;
            doc = new BufferedReader(new FileReader(file)); // new BufferedReader(new FileReader(new File(archive))); ???
            nextLine();
            content = currentLine.toCharArray(); // converter a string em um array char.
            new TokensFile();
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

    public TokensClass generateTokens() {
        char currentChar = ' ';
		String lex = "";
		state = 0;
        int teste = 0;
        while(true) {
            // System.out.printf("!!!%d!!!\t\t\t\n", teste);
            teste += 1;
            System.out.printf("---------------------------\n");
            System.out.printf("Entrei no while:\n");
            System.out.printf("((%d)) Linha: %d | Coluna: %d | Linha: %s | Caracter: %c\n\n", state, line, column, currentLine, currentChar);
            System.out.printf("Position::: %d\n\n", position);
            if(isEOF()){ // se não é EOF, ou seja, não chegou ao final do arquivo
                // System.out.printf("cheguei ao eof");
                if(nextLine()){ // verifica se a proxima linha não é null. Se não for,
                    content = currentLine.toCharArray(); // salva esse linha
                } else {
                    new TokensClass(Tokens.EOF, "EOF", line, column);
                    TokensFile.closeFile();
                    return null;
                }
            }
            currentChar = nextChar();
            System.out.printf("\n");
            for(int i = 0; i < content.length; i++) {
                System.out.printf("%c(%d) ",content[i], i);
            }
            System.out.printf("tamanho: %d\n\n", content.length);
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
                        state = 8;
                    } else if(isLetter(currentChar)) {
                        lex += currentChar;
                        state = 10;
                    } else if(isWhitespace(currentChar) ) {
                        state = 0;
                    } else if(currentChar == '/') {
                        lex += currentChar;
                        state = 9;
                        // column++;
                        // return new TokensClass(Tokens.OPR_DIV,lex,line,column);
                    } else if(currentChar == '+') {
                        lex += currentChar;
                        column++;
                        new TokensClass(Tokens.OPR_ADD,lex,line,column);
                    } else if(currentChar == '-') {
                        lex += currentChar;
                        column++;
                        new TokensClass(Tokens.OPR_SUB,lex,line,column);
                    } else if(currentChar == '*') {
                        lex += currentChar;
                        column++;
                        new TokensClass(Tokens.OPR_MULT,lex,line,column);
                    } else if(currentChar == '%') {
                        lex += currentChar;
                        column++;
                        new TokensClass(Tokens.OPR_MOD,lex,line,column);
                    } else if(currentChar == '(') {
                        lex += currentChar;
                        column++;
                        new TokensClass(Tokens.OP_PAR,lex,line,column);
                    } else if(currentChar == ')') {
                        lex += currentChar;
                        column++;
                        new TokensClass(Tokens.CL_PAR,lex,line,column);
                    } else if(currentChar == '[') {
                        lex += currentChar;
                        column++;
                        new TokensClass(Tokens.OP_COLC,lex,line,column);
                    } else if(currentChar == ']') {
                        lex += currentChar;
                        column++;
                        new TokensClass(Tokens.CL_COLC,lex,line,column);
                    } else if(currentChar == ';') {
                        lex += currentChar;
                        column++;
                        new TokensClass(Tokens.S_PVIRG,lex,line,column);
                    } else if(currentChar == ',') {
                        lex += currentChar;
                        column++;
                        new TokensClass(Tokens.S_VIRG,lex,line,column); 
                    } else if(currentChar == '&') {
                        lex += currentChar;
                        column++;
                        new TokensClass(Tokens.OPR_CONC,lex,line,column);
                    } else {
                        new TokensClass(Tokens.ERR_DESC,lex,line,column);
                    }
                    break;
                
                // IDENTIFICADORES
                case 1:
                    if(isLetter(currentChar)){
                        lex += currentChar;
                        state = 2;
                    } else {
                        new TokensClass(Tokens.ERR_ID, lex, line, column);
                        System.out.println("erro");
                        // throw new Exception("error message");  
                    }
                    break;
                case 2:
                    if(isLetter(currentChar) || isDigit(currentChar)){
                        lex += currentChar;
                    } else if (isWhitespace(currentChar) || isOperator(currentChar)){
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
                    state = 0;
                    new TokensClass(Tokens.ID,lex,line,column);
                    break;
                
                // DIGITOS
                case 4:
                    if(isDigit(currentChar)) { // PODE LER QUANTOS NUMEROS QUISER
                        lex += currentChar;
                    } else if(currentChar == '.') {
                        lex += currentChar;
                        state = 5;
                        column++;
                    } else if(!isLetter(currentChar) || !isDigit(currentChar) || isWhitespace(currentChar) || isOperator(currentChar)) { // pode ter uma letra depois do digito?
                        back();
                        state = 6;
                    } else { // PRA QUE Ta SERVINDO ESSE ELSE???
                        column++;
                        new TokensClass(Tokens.ERR_NUM,lex,line,column);
                    }
                    break;
                case 5:
                    if(isDigit(currentChar)) {
                        lex += currentChar;
                    } else if(currentChar == '.') {
                        column++;
                        new TokensClass(Tokens.ERR_NUM, lex,line,column);
                    } else if(!isLetter(currentChar) || !isDigit(currentChar) || isWhitespace(currentChar) || isOperator(currentChar)) {
                        back();
                        state = 7;
                    } else {
                        column++;
                        new TokensClass(Tokens.ERR_NUM, lex,line,column);
                    }
                    break;
                case 6:
                    // back();
                    column++;
                    new TokensClass(Tokens.PR_INT,lex,line,column);
                    state = 0;
                    break;
                case 7:
                    // back();
                    column++;
                    new TokensClass(Tokens.PR_FLOAT,lex,line,column);
                    break;

                // OPERADORES
                case 8:
                    back(); //Volta duas vezes para verficar as tuplas dos operadores relacionais
                    back();
                    currentChar = nextChar();
                    if(currentChar == '>') {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex += currentChar;
                            column++;
                             new TokensClass(Tokens.OPR_MAIORIG,lex,line,column);
                        }else {
                            back();
                            column++;
                             new TokensClass(Tokens.OPR_MAIOR,lex,line,column);
                        }
                    }else if(currentChar == '<') {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex += currentChar;
                            column++;
                             new TokensClass(Tokens.OPR_MENORIG,lex,line,column);
                        }else {
                            back();
                            column++;
                             new TokensClass(Tokens.OPR_MENOR,lex,line,column);
                        }
                    }else if(currentChar == '=') {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex+=currentChar;
                            column++;
                             new TokensClass(Tokens.OPR_DIGUAL,lex,line,column);
                        }else {
                            back();
                            column++;
                             new TokensClass(Tokens.OPR_IGUAL,lex,line,column);
                        }
                    }else if(currentChar == '!') {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex+=currentChar;
                            column++;
                             new TokensClass(Tokens.OPR_DIF,lex,line,column);
                        }else {
                            back();
                            column++;
                             new TokensClass(Tokens.OPR_NOT,lex,line,column);
                        }
                    }else {
                        column++;
                        new TokensClass(Tokens.ERR_DESC,lex,line,column);
                    }
                    break;

                // COMENTaRIOS:
                case 9:
                    if(currentChar == '/'){
                        state = 0;
                    } else {
                        lex += currentChar;
                        column++;   
                        new TokensClass(Tokens.OPR_DIV,lex,line,column);
                    }
                    break;
                // PALAVRAS RESERVAS:
                case 10:
                    if(isLetter(currentChar)) {
                        lex += currentChar;
                    } else if(!isLetter(currentChar) || isWhitespace(currentChar) || isOperator(currentChar)) {
                        back();
                        state = 11;
                    }
                    break;

                case 11:
                    back();
                    if(hashTable.reservedWord.get(lex) != null) {
                        column++;
                        new TokensClass(hashTable.reservedWord.get(lex),lex,line,column);
                    }else {
                        column++;
                        new TokensClass(Tokens.ERR_PR,lex,line,column); 
                    }
                    break;
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

    private boolean isWhitespace(char ch) {
        return Character.isWhitespace(ch) || ch == '\t' || ch == '\n' || ch == '\r' || ch == '\f' || ch == '\0' || ch == '\b';
    }
}