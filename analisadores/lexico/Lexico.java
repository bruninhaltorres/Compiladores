package analisadores.lexico;
import analisadores.lexico.tokens.Tokens;
import analisadores.lexico.tokens.TokensClass;
import analisadores.lexico.tokens.TokensFile;
import analisadores.lexico.tokens.TokensUtils;

public class Lexico {
    private final HashTablePL hashTable = new HashTablePL();

    public Lexico() {
    }

    public TokensClass nextToken(char[] line, TokensUtils coordinates, int column) {
        char currentChar;
		String lex = "";
		int state = 0;

        while(true) {
            currentChar = line[column];
            System.out.printf("%c", currentChar);
            System.out.println(" column = " + column);

            switch(state){
                case 0:
                    lex = "";
                    if(currentChar == '_'){
                        lex += currentChar;
                    column++;
                        state = 1;
                    } else if(Symbols.isDigit(currentChar)) {
                        lex += currentChar;
                    column++;
                        state = 4;
                    } else if(Symbols.isOperator(currentChar)) {
                        lex += currentChar;
                    column++;
                        state = 8;
                    } else if(Symbols.isLetter(currentChar)) {
                        lex += currentChar;
                    column++;
                        state = 10;
                        // System.out.println("aq, state = " + state);
                    } else if(Symbols.isOther(currentChar) ) {
                        state = 0;
                    } else if (currentChar == '#') {
                        lex += currentChar;
                    column++;
                        state = 9;
                    } else if(currentChar == '/') {
                        lex += currentChar;
                    column++;
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return new TokensClass(Tokens.OPR_DIV,lex,coordinates.getLine(),coordinates.getStartColumn());
                    } else if(currentChar == '+') {
                        lex += currentChar;
                    column++;
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_ADD,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == '-') {
                        lex += currentChar;
                    column++;
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_SUB,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == '*') {
                        lex += currentChar;
                    column++;
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_MULT,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == '%') {
                        lex += currentChar;
                    column++;
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_MOD,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == '(') {
                        lex += currentChar;
                    column++;
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OP_PAR,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == ')') {
                        lex += currentChar;
                    column++;
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.CL_PAR,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == '{') {
                        lex += currentChar;
                    column++;
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OP_CHAVES,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == '}') {
                        lex += currentChar;
                    column++;
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.CL_CHAVES,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == '[') {
                        lex += currentChar;
                    column++;

                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OP_COLC,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == ']') {
                        lex += currentChar;
                    column++;

                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.CL_COLC,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == ';') {
                        lex += currentChar;
                    column++;

                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.S_PVIRG,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == ',') {
                        lex += currentChar;
                    column++;

                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.S_VIRG,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == '&') {
                        lex += currentChar;
                    column++;

                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_CONC,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if (currentChar == '~') {
                        lex += currentChar;
                    column++;

                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_INVERS,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(currentChar == '"') {
                        lex += currentChar;
                    column++;

                        state = 12;
                    } else if (currentChar == '\'') {
                        lex += currentChar;
                    column++;

                        state = 13;
                    } else {
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.ERR_DESC,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    }
                    break;
                case 1:
                    if(Symbols.isLetter(currentChar)){
                        lex += currentChar;
                    column++;
                        state = 2;
                    } else {
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.ERR_ID,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    }
                    break;
                case 2:
                    if(Symbols.isLetter(currentChar) || Symbols.isDigit(currentChar)){
                        lex += currentChar;
                    column++;
                    } else if (Symbols.isOther(currentChar) || Symbols.isOperator(currentChar) || Symbols.isSymbol(currentChar) ){
                        // back();
					    state = 3;
                    } else {

                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.ERR_ID,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    }
                    break;
                case 3:
                    // back();
                    state = 0;
                        coordinates.setEndColumn(column);
                    System.out.println("TERMINEI O TOKEN");
                    System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.ID,lex,coordinates.getLine(),coordinates.getStartColumn())));
                case 4:
                    if(Symbols.isDigit(currentChar)) {
                        lex += currentChar;
                    column++;
                    } else if(currentChar == '.') {
                        lex += currentChar;
                    column++;
                        state = 5;

                    } else if(!Symbols.isLetter(currentChar) || !Symbols.isDigit(currentChar) || Symbols.isOther(currentChar) || Symbols.isOperator(currentChar)) { // pode ter uma letra depois do digito?
                        // back();
                        state = 6;
                    } else {
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.ERR_NUM,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    }
                    
                case 5:
                    if(Symbols.isDigit(currentChar)) {
                        lex += currentChar;
                    column++;
                    } else if(currentChar == '.') {

                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.ERR_NUM,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    } else if(!Symbols.isLetter(currentChar) || !Symbols.isDigit(currentChar) || Symbols.isOther(currentChar) || Symbols.isOperator(currentChar)) {
                        // back();
                        state = 7;
                    } else {

                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.ERR_NUM,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    }
                    
                case 6:
                    // back();
                    state = 0;
                        coordinates.setEndColumn(column);
                    System.out.println("TERMINEI O TOKEN");
                    System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.CT_INT,lex,coordinates.getLine(),coordinates.getStartColumn())));
                case 7:
                    // back();
                    state = 0;
                        coordinates.setEndColumn(column);
                    System.out.println("TERMINEI O TOKEN");
                    System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.CT_FLOAT,lex,coordinates.getLine(),coordinates.getStartColumn())));

                case 8:
                    // back();

                    if(lex.equals(">")) {
                        // currentChar = nextChar();
                        if(currentChar == '=') {
                            lex += currentChar;
                        column++;
    
                            state = 0;
                        coordinates.setEndColumn(column);

                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_MAIORIG,lex,coordinates.getLine(),coordinates.getStartColumn())));
                        }else {
                            // back();
    
                            state = 0;
                        coordinates.setEndColumn(column);

                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_MAIOR,lex,coordinates.getLine(),coordinates.getStartColumn())));
                        }
                    }else if(lex.equals("<")) {
                        // currentChar = nextChar();
                        if(currentChar == '=') {
                            lex += currentChar;
                        column++;
    
                            state = 0;
                        coordinates.setEndColumn(column);

                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_MENORIG,lex,coordinates.getLine(),coordinates.getStartColumn())));
                        } else {
                            // back();
    
                            state = 0;
                        coordinates.setEndColumn(column);

                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_MENOR,lex,coordinates.getLine(),coordinates.getStartColumn())));
                        }
                    }else if(lex.equals("=")) {
                        if(currentChar == '=') {
                        lex += currentChar;
                    column++;
                        state = 0;
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_DIGUAL,lex,coordinates.getLine(),coordinates.getStartColumn())));
                        } else {
                            state = 0;
                        coordinates.setEndColumn(column);

                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_IGUAL,lex,coordinates.getLine(),coordinates.getStartColumn())));
                        }
                    }else if(lex.equals("!")) {
                        // currentChar = nextChar();
                        if(currentChar == '=') {
                            lex += currentChar;
                        column++;
    
                            state = 0;
                        coordinates.setEndColumn(column);

                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.OPR_DIF,lex,coordinates.getLine(),coordinates.getStartColumn())));
                        } else {
                            // back();
    
                            state = 0;
                        coordinates.setEndColumn(column);

                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.ERR_DESC,lex,coordinates.getLine(),coordinates.getStartColumn())));
                        }
                    } else {

                        state = 0;
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return((new TokensClass(Tokens.ERR_DESC,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    }
                    
                case 9:
                    // back();
                    // this.position = this.currentLine.length();
                    state = 0;
                    break;

                case 10:
                    if(Symbols.isLetter(currentChar)) {
                        lex += currentChar;
                    column++;
                    } else if(!Symbols.isLetter(currentChar) || Symbols.isOther(currentChar) || Symbols.isOperator(currentChar) || Symbols.isSymbol(currentChar)) {
                        // back();
                        state = 11;
                    }
                    break;
                case 11:
                    // back();
                    if(this.hashTable.reservedWord.get(lex) != null) {

                        state = 0;
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return new TokensClass(this.hashTable.reservedWord.get(lex),lex,coordinates.getLine(),coordinates.getStartColumn());
                    } else {
                        coordinates.setEndColumn(column);
                        System.out.println("TERMINEI O TOKEN");
                        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                        return new TokensClass(Tokens.ERR_PR,lex,coordinates.getLine(),coordinates.getStartColumn());
                    }
                    // 
                case 12:
                    // back();
                    while(currentChar != '"') {
                        // currentChar = nextChar();
                        lex += currentChar;
                    column++;
                        // this.currentColumn++;
                    } 
                    state = 0;
                    coordinates.setEndColumn(column);
                    System.out.println("TERMINEI O TOKEN");
                    System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                    return((new TokensClass(Tokens.CT_STRING,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    
                case 13:
                    // back();
                    while(currentChar != '\'') {
                        // currentChar = nextChar();
                        lex += currentChar;
                    column++;
                    } 
                    state = 0;
                    coordinates.setEndColumn(column);
                    System.out.println("TERMINEI O TOKEN");
                    System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
                    return((new TokensClass(Tokens.CT_CHAR,lex,coordinates.getLine(),coordinates.getStartColumn())));
                    
            }
        }
    }

/*     private boolean isEOF() {
    System.out.println("TERMINEI O TOKEN");
    System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
    return this.position == this.content.length;
    }

    // private void back() {
        this.position--;
        // this.currentColumn--;
    }

    // private char nextChar() {
        System.out.println("TERMINEI O TOKEN");
        System.out.println("lex = " + lex);
                        System.out.println("Terminei na coluna " + column);
        return this.content[this.position++];
    } */
}
