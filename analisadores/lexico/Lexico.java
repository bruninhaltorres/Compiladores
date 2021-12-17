package analisadores.lexico;
import java.io.BufferedReader;
import java.io.FileReader;

import analisadores.lexico.tokens.Tokens;
import analisadores.lexico.tokens.TokensClass;
import analisadores.lexico.tokens.TokensFile;
import analisadores.lexico.Symbols;

public class Lexico {
    private final HashTablePL hashTable = new HashTablePL();
    private int line, column, position, state;
    private char[] content;
    private BufferedReader doc;
    private String currentLine = " "; 
    // private Symbols symbols = new Symbols();

    public Lexico(String BFSfile) {
        try{
            this.line = 1;
            this.column = 1; // vai se referir onde começa os tokens
            this.position = 0;
            this.doc = new BufferedReader(new FileReader(BFSfile));//Abre o nosso arquivo BFS
            new TokensFile();
            nextLine();
            content = currentLine.toCharArray(); // converter a string em um array char.
        }catch(Exception exception) {
            System.out.println("Arquivo não encontrado!");
            exception.printStackTrace(); // para auxiliar no encontro do erro.
        }   
    }

    public boolean nextLine(){ // Pega a proxima linha
        String currentLineAux = " ";
        try {
            currentLineAux = doc.readLine();
        }catch(Exception exception) {
            exception.printStackTrace();
        }

        if(currentLineAux != null) {
            this.currentLine = currentLineAux;
            // System.out.printf("%d %s \n", this.line, this.currentLine);
            TokensFile.write(this.line + " " + this.currentLine + "\n");
            this.currentLine += " "; // apenas por uso visual
            this.line++;
            this.position = 0;
            this.column = 0; // se refere a coluna do content

            return true;
        }
        return false;
    }

    public TokensClass generateTokens() {
        char currentChar = ' ';
		String lex = "";
		this.state = 0;
        while(true) {

            if(isEOF()){ // se não é EOF, ou seja, não chegou ao final do arquivo
                if(nextLine()){ // verifica se a proxima linha não é null. Se não for,
                    content = currentLine.toCharArray(); // salva esse linha
                } else {
                    TokensFile.write((new TokensClass(Tokens.EOF, "EOF", line, column)).toString());
                    TokensFile.closeFile();
                    return null;
                }
            }

            currentChar = nextChar();

            switch(state){
                case 0:
                    lex = "";
                    if(currentChar == '_'){
                        lex += currentChar;
                        state = 1;
                    } else if(Symbols.isDigit(currentChar)) {
                        lex += currentChar;
                        state = 4;
                    } else if(Symbols.isOperator(currentChar)) {
                        lex += currentChar;
                        state = 8;
                    } else if(Symbols.isLetter(currentChar)) {
                        lex += currentChar;
                        state = 10;
                    } else if(Symbols.isOther(currentChar) ) {
                        state = 0;
                    } else if (currentChar == '#') {
                        lex += currentChar;
                        column++;
                        state = 9;
                    } else if(currentChar == '/') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.OPR_DIV,lex,line,column).toString()));
                    } else if(currentChar == '+') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.OPR_ADD,lex,line,column).toString()));
                    } else if(currentChar == '-') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.OPR_SUB,lex,line,column)).toString());
                    } else if(currentChar == '*') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.OPR_MULT,lex,line,column).toString()));
                    } else if(currentChar == '%') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.OPR_MOD,lex,line,column).toString()));
                    } else if(currentChar == '(') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.OP_PAR,lex,line,column).toString()));
                    } else if(currentChar == ')') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.CL_PAR,lex,line,column).toString()));
                    } else if(currentChar == '{') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.OP_CHAVES,lex,line,column).toString()));
                    } else if(currentChar == '}') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.CL_CHAVES,lex,line,column).toString()));
                    } else if(currentChar == '[') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.OP_COLC,lex,line,column).toString()));
                    } else if(currentChar == ']') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.CL_COLC,lex,line,column).toString()));
                    } else if(currentChar == ';') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.S_PVIRG,lex,line,column).toString()));
                    } else if(currentChar == ',') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.S_VIRG,lex,line,column).toString()));
                    } else if(currentChar == '&') {
                        lex += currentChar;
                        column++;
                        TokensFile.write((new TokensClass(Tokens.OPR_CONC,lex,line,column).toString()));
                    } else if(currentChar == '"') {
                        lex += currentChar;
                        column++;
                        state = 12;
                    } else if (currentChar == '\'') {
                        lex += currentChar;
                        column++;
                        state = 13;
                    } else {
                        TokensFile.write((new TokensClass(Tokens.ERR_DESC,lex,line,column).toString()));
                    }
                    break;
                case 1:
                    if(Symbols.isLetter(currentChar)){
                        lex += currentChar;
                        state = 2;
                    } else {
                        TokensFile.write((new TokensClass(Tokens.ERR_ID,lex,line,column).toString()));
                    }
                    break;
                case 2:
                    if(Symbols.isLetter(currentChar) || Symbols.isDigit(currentChar)){
                        lex += currentChar;
                    } else if (Symbols.isOther(currentChar) || Symbols.isOperator(currentChar) || Symbols.isSymbol(currentChar) ){
                        back();
					    state = 3;
                    } else {
                        column++;
                        TokensFile.write((new TokensClass(Tokens.ERR_ID,lex,line,column).toString()));
                    }
                    break;
                case 3:
                    back();
                    column++;
                    state = 0;
                    TokensFile.write((new TokensClass(Tokens.ID,lex,line,column).toString()));
                    break;
                
                // DIGITOS
                case 4:
                    if(Symbols.isDigit(currentChar)) { // PODE LER QUANTOS NUMEROS QUISER
                        lex += currentChar;
                    } else if(currentChar == '.') {
                        lex += currentChar;
                        state = 5;
                        column++;
                    } else if(!Symbols.isLetter(currentChar) || !Symbols.isDigit(currentChar) || Symbols.isOther(currentChar) || Symbols.isOperator(currentChar)) { // pode ter uma letra depois do digito?
                        back();
                        state = 6;
                    } else { // PRA QUE Ta SERVINDO ESSE ELSE???
                        column++;
                        TokensFile.write((new TokensClass(Tokens.ERR_NUM,lex,line,column).toString()));
                    }
                    break;
                case 5:
                    if(Symbols.isDigit(currentChar)) {
                        lex += currentChar;
                    } else if(currentChar == '.') {
                        column++;
                        TokensFile.write((new TokensClass(Tokens.ERR_NUM,lex,line,column).toString()));
                    } else if(!Symbols.isLetter(currentChar) || !Symbols.isDigit(currentChar) || Symbols.isOther(currentChar) || Symbols.isOperator(currentChar)) {
                        back();
                        state = 7;
                    } else {
                        column++;
                        TokensFile.write((new TokensClass(Tokens.ERR_NUM,lex,line,column).toString()));
                    }
                    break;
                    
                // INTEIRO:
                case 6:
                    back();
                    column++;
                    state = 0;
                    TokensFile.write((new TokensClass(Tokens.CT_INT,lex,line,column).toString()));
                    break;

                // FLOAT:
                case 7:
                    back();
                    column++;
                    state = 0;
                    TokensFile.write((new TokensClass(Tokens.CT_FLOAT,lex,line,column).toString()));
                    break;

                // OPERADORES
                case 8:
                    back(); //Volta duas vezes para verficar as tuplas dos operadores relacionais

                    if(lex.equals(">")) {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex += currentChar;
                            column++;
                            state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_MAIORIG,lex,line,column).toString()));
                        }else {
                            back();
                            column++;
                            state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_MAIOR,lex,line,column).toString()));
                        }
                    }else if(lex.equals("<")) {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex += currentChar;
                            column++;
                            state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_MENORIG,lex,line,column).toString()));
                        } else {
                            back();
                            column++;
                            state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_MENOR,lex,line,column).toString()));
                        }
                    }else if(lex.equals("=")) {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex+=currentChar;
                            column++;
                            state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_DIGUAL,lex,line,column).toString()));
                        } else {
                            back();
                            column++;
                            state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_IGUAL,lex,line,column).toString()));
                        }
                    }else if(lex.equals("!")) {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex+=currentChar;
                            column++;
                            state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_DIF,lex,line,column).toString()));
                        }else {
                            back();
                            column++;
                            state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_NOT,lex,line,column).toString()));
                        }
                    } else if (lex.equals("|")){ // REMOVER ISSO PQ AGR É OR
                        back();
                        column++;
                        state = 0;
                        TokensFile.write((new TokensClass(Tokens.OPR_OR,lex,line,column).toString()));
                    } else {
                        column++;
                        state = 0;
                        TokensFile.write((new TokensClass(Tokens.ERR_DESC,lex,line,column).toString()));
                    }
                    break;

                // COMENTaRIOS:
                case 9:
                    back();
                    position = currentLine.length();
                    state = 0;
                    break;

                // PALAVRAS RESERVADAS:
                case 10:
                    if(Symbols.isLetter(currentChar)) {
                        lex += currentChar;
                    } else if(!Symbols.isLetter(currentChar) || Symbols.isOther(currentChar) || Symbols.isOperator(currentChar) || Symbols.isSymbol(currentChar)) {
                        back();
                        state = 11;
                    }
                    break;

                case 11:
                    back();
                    if(hashTable.reservedWord.get(lex) != null) {
                        column++;
                        state = 0;
                        TokensFile.write((new TokensClass(hashTable.reservedWord.get(lex),lex,line,column).toString()));
                    }else {
                        column++;
                        TokensFile.write((new TokensClass(Tokens.ERR_PR,lex,line,column).toString()));
                    }
                    break;
                case 12:
                    back();
                    while(currentChar != '"') {
                        currentChar = nextChar();
                        lex += currentChar;
                    } 
                    column++;
                    state = 0;
                    TokensFile.write((new TokensClass(Tokens.CT_STRING,lex,line,column).toString()));
                    break;
                case 13:
                    back();
                    while(currentChar != '\'') {
                        currentChar = nextChar();
                        lex += currentChar;
                    } 
                    column++;
                    state = 0;
                    TokensFile.write((new TokensClass(Tokens.CT_CHAR,lex,line,column).toString()));
                    break;
            }
        }
    }

    private boolean isEOF() {
        return position == content.length;
    }

    private void back() {
        position--;
    }

    private char nextChar() {
        return content[position++];
    }
}