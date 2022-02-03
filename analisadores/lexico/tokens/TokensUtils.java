package analisadores.lexico.tokens;

public class TokensUtils {
    private int startColumn;
    private int endColumn;
    private int line;

    public TokensUtils() {
        this.startColumn = 1;
        this.endColumn = 1;
        this.line = 0;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    public int getLine() {
        return line;
    }

    public void setTokenLine(int line) {
        this.line = line;
    }
}
