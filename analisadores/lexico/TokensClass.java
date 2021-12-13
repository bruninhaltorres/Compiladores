package analisadores.lexico;

public class TokensClass {

    public Tokens category;
    public String lexeme;
    public int line, column;

    public TokensClass(Tokens category, String lexeme, int line, int column) {
        this.category = category;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        String format = " [%04d,%04d] (%04d,%20s) {%s} ";
        return String.format(format, line - 1, column, category.ordinal(), category, lexeme);
    }

}
