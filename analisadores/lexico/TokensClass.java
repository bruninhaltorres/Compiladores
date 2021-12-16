package analisadores.lexico;
import java.io.PrintWriter;

import java.io.File;
import java.io.FileWriter;

public class TokensClass {

    public Tokens category;
    public String lexical;
    public int line, column;
    private static File arq = new File("./out/output.txt");
    // private static PrintWriter gravarArq = new PrintWriter(arq);
    private static FileWriter myArq = new FileWriter(arq);

    public TokensClass() { }

    public TokensClass(Tokens category, String lexical, int line, int column) {
        this.category = category;
        this.lexical = lexical;
        this.line = line;
        this.column = column;
    }

    public static void createFile() {
        try {
            TokensClass.arq.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFile() {
        try {

        } catch {

        }
    }

    @Override
    public String toString() {
        String format = " [%04d,%04d] (%04d,%20s) {%s} ";
        return String.format(format, line - 1, column, category.ordinal(), category, lexical);
    }
}
