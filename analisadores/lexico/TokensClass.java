package analisadores.lexico;

public class TokensClass {

    public Tokens category;
    public String lexical;
    public int line, column;

    public TokensClass() { }

    public TokensClass(Tokens category, String lexical, int line, int column) {
        this.category = category;
        this.lexical = lexical;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        String format = " [%04d,%04d] (%04d,%20s) {%s} ";
        return String.format(format, line - 1, column, category.ordinal(), category, lexical);
    }
}
