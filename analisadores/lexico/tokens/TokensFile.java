package analisadores.lexico.tokens;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TokensFile {
    private static File arq;
    private static FileWriter myArq;
    private String file;

    public TokensFile(String fileName) {
        this.file = "./resultados/" + fileName;

        try {
            TokensFile.arq = new File(file);
            TokensFile.arq.createNewFile();
            
            TokensFile.myArq = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(String str) {
        try {
            TokensFile.myArq.write(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeFile() {
        try {
            TokensFile.myArq.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
