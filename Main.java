import analisadores.lexico.Lexico;
import analisadores.lexico.tokens.Token;
import analisadores.lexico.tokens.TokensFile;

public class Main {
  public static void main(String[] args) {
    try {
      String BFSfile = args[0];
      String outputFile = args[1];
      TokensFile.createFile(outputFile);
      Lexico lexical = new Lexico(BFSfile);
      
      /**
       * 1 -  Lê arquivo
       * 2 - Pega uma linha, se for null acabou o arquivo (printa o EOF)
       * 3 - Se nao for null, eu printo a linha vou em cada palavra da linha e dela e formo um token (Caso especial: strings)
       * 4 - Column na nextToken?
       * 5 - Chorar muito e surtar ate arrancar os cabelos
       */
      
      Token token;
      do {
        token = lexical.nextToken();
        TokensFile.write(token.toString());
      } while (!token.lexical.equals("EOF"));
      
      TokensFile.closeFile();

    } catch (Exception e) {
      e.printStackTrace();
    }
  } 
}
