import analisadores.lexico.Lexico;
import analisadores.lexico.tokens.Token;
import analisadores.lexico.tokens.TokensEnum;
import analisadores.lexico.tokens.TokensFile;

import analisadores.sintatico.*;

public class Main {
  public static void main(String[] args) {
    try {
      String BFSfile = args[0];
      String outputFile = args[1];
      TokensFile.createFile(outputFile);
      // Lexico lexical = new Lexico(BFSfile);
      Sintatico sintatico = new Sintatico(BFSfile);
      
      /* Token token;
      do {
        token = lexical.nextToken();
        if(token.category.equals(TokensEnum.COMMENT)) { // Comentário
          continue;
        }
        TokensFile.write(token.toString());
      } while (!token.lexical.equals("EOF"));
       */
      
      TokensFile.closeFile();

    } catch (Exception e) {
      e.printStackTrace();
    }
  } 
}
