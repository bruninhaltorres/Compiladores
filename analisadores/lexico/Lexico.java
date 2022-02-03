package analisadores.lexico;
import java.io.BufferedReader;
import java.io.FileReader;

import analisadores.lexico.tokens.TokensEnum;
import analisadores.lexico.tokens.Token;
import analisadores.lexico.tokens.TokensFile;

public class Lexico {
    private final HashTablePL hashTable = new HashTablePL();
    private int line, column, position, state, currentColumn;
    private char[] content;
    private BufferedReader doc;
    private String currentLine = " "; 

    public Lexico(String BFSfile) {
        try{
            this.line = 1;
            this.column = 1;
            this.currentColumn = 0;
            this.position = 0;
            this.doc = new BufferedReader(new FileReader("./programs/" + BFSfile));
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
            TokensFile.write(String.format("%04d  ", this.line) + this.currentLine + "\n");
            this.currentLine += " ";
            this.line++;
            this.position = 0;
            this.column = 0;

            return true;
        }
        return false;
    }

    public Token nextToken() {
        char currentChar = ' ';
		String lex = "";
		this.state = 0;
        while(true) {

            if(isEOF()){ 
                if(nextLine()){
                    this.content = this.currentLine.toCharArray();
                    this.currentColumn = 0;
                } else {
                    return((new Token(TokensEnum.EOF, "EOF", this.line, this.column)));
                }
            }

            currentChar = nextChar();
            this.currentColumn++;    

            switch(this.state){
                case 0:
                    this.column = this.currentColumn;
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
                        
                        this.state = 9;
                    } else if(currentChar == '/') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.OPR_DIV,lex,this.line,this.column)));
                    } else if(currentChar == '+') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.OPR_ADD,lex,this.line,this.column)));
                    } else if(currentChar == '-') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.OPR_SUB,lex,this.line,this.column)));
                    } else if(currentChar == '*') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.OPR_MULT,lex,this.line,this.column)));
                    } else if(currentChar == '%') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.OPR_MOD,lex,this.line,this.column)));
                    } else if(currentChar == '(') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.OP_PAR,lex,this.line,this.column)));
                    } else if(currentChar == ')') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.CL_PAR,lex,this.line,this.column)));
                    } else if(currentChar == '{') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.OP_CHAVES,lex,this.line,this.column)));
                    } else if(currentChar == '}') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.CL_CHAVES,lex,this.line,this.column)));
                    } else if(currentChar == '[') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.OP_COLC,lex,this.line,this.column)));
                    } else if(currentChar == ']') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.CL_COLC,lex,this.line,this.column)));
                    } else if(currentChar == ';') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.S_PVIRG,lex,this.line,this.column)));
                    } else if(currentChar == ',') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.S_VIRG,lex,this.line,this.column)));
                    } else if(currentChar == '&') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.OPR_CONC,lex,this.line,this.column)));
                    } else if (currentChar == '~') {
                        lex += currentChar;
                        
                        return((new Token(TokensEnum.OPR_INVERS,lex,this.line,this.column)));
                    } else if(currentChar == '"') {
                        lex += currentChar;
                        
                        this.state = 12;
                    } else if (currentChar == '\'') {
                        lex += currentChar;
                        
                        this.state = 13;
                    } else {
                        return((new Token(TokensEnum.ERR_DESC,lex,this.line,this.column)));
                    }
                    break;
                case 1:
                    if(Symbols.isLetter(currentChar)){
                        lex += currentChar;
                        this.state = 2;
                    } else {
                        return((new Token(TokensEnum.ERR_ID,lex,this.line,this.column)));
                    }
                    break;
                case 2:
                    if(Symbols.isLetter(currentChar) || Symbols.isDigit(currentChar)){
                        lex += currentChar;
                    } else if (Symbols.isOther(currentChar) || Symbols.isOperator(currentChar) || Symbols.isSymbol(currentChar) ){
                        back();
					    this.state = 3;
                    } else {
                        
                        return((new Token(TokensEnum.ERR_ID,lex,this.line,this.column)));
                    }
                    break;
                case 3:
                    back();
                    
                    return((new Token(TokensEnum.ID,lex,this.line,this.column)));
                case 4:
                    if(Symbols.isDigit(currentChar)) {
                        lex += currentChar;
                    } else if(currentChar == '.') {
                        lex += currentChar;
                        this.state = 5;
                        
                    } else if(!Symbols.isLetter(currentChar) || !Symbols.isDigit(currentChar) || Symbols.isOther(currentChar) || Symbols.isOperator(currentChar)) { // pode ter uma letra depois do digito?
                        back();
                        this.state = 6;
                    } else {
                        
                        return((new Token(TokensEnum.ERR_NUM,lex,this.line,this.column)));
                    }
                    break;
                case 5:
                    if(Symbols.isDigit(currentChar)) {
                        lex += currentChar;
                    } else if(currentChar == '.') {
                        
                        return((new Token(TokensEnum.ERR_NUM,lex,this.line,this.column)));
                    } else if(!Symbols.isLetter(currentChar) || !Symbols.isDigit(currentChar) || Symbols.isOther(currentChar) || Symbols.isOperator(currentChar)) {
                        back();
                        this.state = 7;
                    } else {
                        
                        return((new Token(TokensEnum.ERR_NUM,lex,this.line,this.column)));
                    }
                    break;
                case 6:
                    back();
                    
                    return((new Token(TokensEnum.CT_INT,lex,this.line,this.column)));
                    
                case 7:
                    back();
                    
                    return((new Token(TokensEnum.CT_FLOAT,lex,this.line,this.column)));

                case 8:
                    back();

                    if(lex.equals(">")) {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex += currentChar;
                            
                            return((new Token(TokensEnum.OPR_MAIORIG,lex,this.line,this.column)));
                        }else {
                            back();
                            
                            return((new Token(TokensEnum.OPR_MAIOR,lex,this.line,this.column)));
                        }
                    } else if(lex.equals("<")) {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex += currentChar;
                            
                            return((new Token(TokensEnum.OPR_MENORIG,lex,this.line,this.column)));
                        } else {
                            back();
                            
                            return((new Token(TokensEnum.OPR_MENOR,lex,this.line,this.column)));
                        }
                    } else if(lex.equals("=")) {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex+=currentChar;
                            
                            return((new Token(TokensEnum.OPR_DIGUAL,lex,this.line,this.column)));
                        } else {
                            back();
                            
                            return((new Token(TokensEnum.OPR_IGUAL,lex,this.line,this.column)));
                        }
                    } else if(lex.equals("!")) {
                        currentChar = nextChar();
                        if(currentChar == '=') {
                            lex+=currentChar;
                            
                            return((new Token(TokensEnum.OPR_DIF,lex,this.line,this.column)));
                        } else {
                            back();
                            
                            return((new Token(TokensEnum.ERR_DESC,lex,this.line,this.column)));
                        }
                    } else {
                        
                        return((new Token(TokensEnum.ERR_DESC,lex,this.line,this.column)));
                    }
                    
                case 9:
                    back();
                    this.position = this.currentLine.length();


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
                        
                        return((new Token(this.hashTable.reservedWord.get(lex),lex,this.line,this.column)));
                    } else {
                        
                        return((new Token(TokensEnum.ERR_PR,lex,this.line,this.column)));
                    }

                case 12:
                    back();
                    while(currentChar != '"') {
                        currentChar = nextChar();
                        lex += currentChar;
                        this.currentColumn++;
                    } 
                    
                    return((new Token(TokensEnum.CT_STRING,lex,this.line,this.column)));

                case 13:
                    back();
                    while(currentChar != '\'') {
                        currentChar = nextChar();
                        lex += currentChar;
                    } 
                    
                    return((new Token(TokensEnum.CT_CHAR,lex,this.line,this.column)));

            }
        }
    }

    private boolean isEOF() {
        return this.position == this.content.length;
    }

    private void back() {
        this.position--;
        this.currentColumn--;
    }

    private char nextChar() {
        return this.content[this.position++];
    }
}