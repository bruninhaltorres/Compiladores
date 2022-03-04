package analisadores.lexico.tokens;

public class Token {

    public TokensEnum category;
    public String lexical;
    public int line, column;
    
    public Token() {}

    public Token(TokensEnum category, String lexical, int line, int column) {
        this.category = category;
        this.lexical = lexical;
        this.line = line;
        this.column = column;
    }

    public String getPosition() {
        return this.line + ":" + this.column;
    }

    @Override
    public String toString() {
        String format = "              [%04d,%04d] (%04d,%20s) {%s}\n";
        return String.format(format, line - 1, column, category.ordinal(), category, lexical);
    }
}
