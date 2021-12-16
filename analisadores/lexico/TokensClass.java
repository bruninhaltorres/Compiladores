package analisadores.lexico;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TokensClass {

    public Tokens category;
    public String lexical;
    public int line, column;
    private static File arq;
    private static FileWriter myArq;

    public TokensClass(String FileName) {
        try {
            String file = FileName;
            TokensClass.arq = new File(file);
            TokensClass.arq.createNewFile();
            TokensClass.myArq = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TokensClass(Tokens category, String lexical, int line, int column) {
        this.category = category;
        this.lexical = lexical;
        this.line = line;
        this.column = column;
        TokensClass.write(this.toString());
    }

    public static void write(String str) {
        try {
            TokensClass.myArq.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String format = " [%04d,%04d] (%04d,%20s) {%s} ";
        return String.format(format, line - 1, column, category.ordinal(), category, lexical);
    }
}
