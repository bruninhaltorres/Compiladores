package analisadores.lexico.tokens;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TokensFile {
    private static File arq;
    private static FileWriter myArq;
    private String file = "tokens-output";

    public TokensFile() {
        try {
            TokensFile.arq = new File(file);
            TokensFile.arq.createNewFile();

            System.out.println("Arquivo Criado!!");
            
            TokensFile.myArq = new FileWriter(file);
            System.out.println("Arquivo aberto!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(String str) {
        try {
            // System.out.println("\n\n\n\n\n("+str+")\n\n\n\n\n");
            TokensFile.myArq.write(str);
        } catch (Exception e) {
            System.out.println("Não foi possível escrever  a string " + str + " no arquivo");
            e.printStackTrace();
        }
    }

    public static void closeFile() {
        try {
            TokensFile.myArq.close();
            System.out.println("Arquivo fechado!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
