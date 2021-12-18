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

    public Lexico(String BFSfile, String outputFile) {
        try{
            this.line = 1;
            this.column = 1;
            this.position = 0;
            this.doc = new BufferedReader(new FileReader("./programs/" + BFSfile));
            new TokensFile(outputFile);
            nextLine();
            this.content = this.currentLine.toCharArray();
        }catch(Exception exception) {
            System.out.println("Arquivo não encontrado!");
            exception.printStackTrace();
        }   
    }

    public boolean nextLine(){
        String currentLineAux = " ";
        try {
            currentLineAux = this.doc.readLine();
        }catch(Exception exception) {
            exception.printStackTrace();
        }

        if(currentLineAux != null) {
            this.currentLine = currentLineAux;
            TokensFile.write(this.line + " " + this.currentLine + "\n");
            this.currentLine += " ";
            this.line++;
            this.position = 0;
            this.column = 0;

            return true;
        }
        return false;
    }

    public void generateTokens() {
        char currentChar = ' ';
		String lex = "";
		this.state = 0;
        while(true) {

            if(isEOF()){ 
                if(nextLine()){
                    this.content = this.currentLine.toCharArray();
                } else {
                    TokensFile.write((new TokensClass(Tokens.EOF, "EOF", this.line, this.column)).toString());
                    TokensFile.closeFile();
                    return;
                }
            }

            currentChar = nextChar();

            switch(this.state){
                case 0:
                    lex = "";
                    if(currentChar == '_'){
                        lex += currentChar;
                        this.state = 1;
                    } else if(Symbols.isDigit(currentChar)) {
                        lex += currentChar;
                        this.state = 4;
                    } else if(Symbols.isOperator(currentChar)) {
                        lex += currentChar;
                        this.state = 8;
                    } else if(Symbols.isLetter(currentChar)) {
                        lex += currentChar;
                        this.state = 10;
                    } else if(Symbols.isOther(currentChar) ) {
                        this.state = 0;
                    } else if (currentChar == '#') {
                        lex += currentChar;
                        this.column++;
                        this.state = 9;
                    } else if(currentChar == '/') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.OPR_DIV,lex,this.line,this.column).toString()));
                    } else if(currentChar == '+') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.OPR_ADD,lex,this.line,this.column).toString()));
                    } else if(currentChar == '-') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.OPR_SUB,lex,this.line,this.column)).toString());
                    } else if(currentChar == '*') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.OPR_MULT,lex,this.line,this.column).toString()));
                    } else if(currentChar == '%') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.OPR_MOD,lex,this.line,this.column).toString()));
                    } else if(currentChar == '(') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.OP_PAR,lex,this.line,this.column).toString()));
                    } else if(currentChar == ')') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.CL_PAR,lex,this.line,this.column).toString()));
                    } else if(currentChar == '{') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.OP_CHAVES,lex,this.line,this.column).toString()));
                    } else if(currentChar == '}') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.CL_CHAVES,lex,this.line,this.column).toString()));
                    } else if(currentChar == '[') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.OP_COLC,lex,this.line,this.column).toString()));
                    } else if(currentChar == ']') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.CL_COLC,lex,this.line,this.column).toString()));
                    } else if(currentChar == ';') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.S_PVIRG,lex,this.line,this.column).toString()));
                    } else if(currentChar == ',') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.S_VIRG,lex,this.line,this.column).toString()));
                    } else if(currentChar == '&') {
                        lex += currentChar;
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.OPR_CONC,lex,this.line,this.column).toString()));
                    } else if(currentChar == '"') {
                        lex += currentChar;
                        this.column++;
                        this.state = 12;
                    } else if (currentChar == '\'') {
                        lex += currentChar;
                        this.column++;
                        this.state = 13;
                    } else {
                        TokensFile.write((new TokensClass(Tokens.ERR_DESC,lex,this.line,this.column).toString()));
                    }
                    break;
                case 1:
                    if(Symbols.isLetter(currentChar)){
                        lex += currentChar;
                        this.state = 2;
                    } else {
                        TokensFile.write((new TokensClass(Tokens.ERR_ID,lex,this.line,this.column).toString()));
                    }
                    break;
                case 2:
                    if(Symbols.isLetter(currentChar) || Symbols.isDigit(currentChar)){
                        lex += currentChar;
                    } else if (Symbols.isOther(currentChar) || Symbols.isOperator(currentChar) || Symbols.isSymbol(currentChar) ){
                        back();
					    this.state = 3;
                    } else {
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.ERR_ID,lex,this.line,this.column).toString()));
                    }
                    break;
                case 3:
                    back();
                    this.column++;
                    this.state = 0;
                    TokensFile.write((new TokensClass(Tokens.ID,lex,this.line,this.column).toString()));
                    break;
                case 4:
                    if(Symbols.isDigit(currentChar)) {
                        lex += currentChar;
                    } else if(currentChar == '.') {
                        lex += currentChar;
                        this.state = 5;
                        this.column++;
                    } else if(!Symbols.isLetter(currentChar) || !Symbols.isDigit(currentChar) || Symbols.isOther(currentChar) || Symbols.isOperator(currentChar)) { // pode ter uma letra depois do digito?
                        back();
                        this.state = 6;
                    } else {
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.ERR_NUM,lex,this.line,this.column).toString()));
                    }
                    break;
                case 5:
                    if(Symbols.isDigit(currentChar)) {
                        lex += currentChar;
                    } else if(currentChar == '.') {
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.ERR_NUM,lex,this.line,this.column).toString()));
                    } else if(!Symbols.isLetter(currentChar) || !Symbols.isDigit(currentChar) || Symbols.isOther(currentChar) || Symbols.isOperator(currentChar)) {
                        back();
                        this.state = 7;
                    } else {
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.ERR_NUM,lex,this.line,this.column).toString()));
                    }
                    break;
                case 6:
                    back();
                    this.column++;
                    this.state = 0;
                    TokensFile.write((new TokensClass(Tokens.CT_INT,lex,this.line,this.column).toString()));
                    break;
                case 7:
                    back();
                    this.column++;
                    this.state = 0;
                    TokensFile.write((new TokensClass(Tokens.CT_FLOAT,lex,this.line,this.column).toString()));
                    break;

                case 8:
                    back();

                    if(lex.equals(">")) {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex += currentChar;
                            this.column++;
                            this.state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_MAIORIG,lex,this.line,this.column).toString()));
                        }else {
                            back();
                            this.column++;
                            this.state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_MAIOR,lex,this.line,this.column).toString()));
                        }
                    }else if(lex.equals("<")) {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex += currentChar;
                            this.column++;
                            this.state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_MENORIG,lex,this.line,this.column).toString()));
                        } else {
                            back();
                            this.column++;
                            this.state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_MENOR,lex,this.line,this.column).toString()));
                        }
                    }else if(lex.equals("=")) {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex+=currentChar;
                            this.column++;
                            this.state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_DIGUAL,lex,this.line,this.column).toString()));
                        } else {
                            back();
                            this.column++;
                            this.state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_IGUAL,lex,this.line,this.column).toString()));
                        }
                    }else if(lex.equals("!")) {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex+=currentChar;
                            this.column++;
                            this.state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_DIF,lex,this.line,this.column).toString()));
                        }else {
                            back();
                            this.column++;
                            this.state = 0;
                            TokensFile.write((new TokensClass(Tokens.OPR_NOT,lex,this.line,this.column).toString()));
                        }
                    } else if (lex.equals("|")){ // REMOVER ISSO PQ AGR É OR
                        back();
                        this.column++;
                        this.state = 0;
                        TokensFile.write((new TokensClass(Tokens.OPR_OR,lex,this.line,this.column).toString()));
                    } else {
                        this.column++;
                        this.state = 0;
                        TokensFile.write((new TokensClass(Tokens.ERR_DESC,lex,this.line,this.column).toString()));
                    }
                    break;
                case 9:
                    back();
                    this.position = this.currentLine.length();
                    this.state = 0;
                    break;
                case 10:
                    if(Symbols.isLetter(currentChar)) {
                        lex += currentChar;
                    } else if(!Symbols.isLetter(currentChar) || Symbols.isOther(currentChar) || Symbols.isOperator(currentChar) || Symbols.isSymbol(currentChar)) {
                        back();
                        this.state = 11;
                    }
                    break;

                case 11:
                    back();
                    if(this.hashTable.reservedWord.get(lex) != null) {
                        this.column++;
                        this.state = 0;
                        TokensFile.write((new TokensClass(this.hashTable.reservedWord.get(lex),lex,this.line,this.column).toString()));
                    }else {
                        this.column++;
                        TokensFile.write((new TokensClass(Tokens.ERR_PR,lex,this.line,this.column).toString()));
                    }
                    break;
                case 12:
                    back();
                    while(currentChar != '"') {
                        currentChar = nextChar();
                        lex += currentChar;
                    } 
                    this.column++;
                    this.state = 0;
                    TokensFile.write((new TokensClass(Tokens.CT_STRING,lex,this.line,this.column).toString()));
                    break;
                case 13:
                    back();
                    while(currentChar != '\'') {
                        currentChar = nextChar();
                        lex += currentChar;
                    } 
                    this.column++;
                    this.state = 0;
                    TokensFile.write((new TokensClass(Tokens.CT_CHAR,lex,this.line,this.column).toString()));
                    break;
            }
        }
    }

    private boolean isEOF() {
        return this.position == this.content.length;
    }

    private void back() {
        this.position--;
    }

    private char nextChar() {
        return this.content[this.position++];
    }
}