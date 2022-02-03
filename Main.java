import java.io.BufferedReader;
import java.io.FileReader;

import analisadores.lexico.Lexico;
import analisadores.lexico.tokens.Tokens;
import analisadores.lexico.tokens.TokensClass;
import analisadores.lexico.tokens.TokensFile;
import analisadores.lexico.tokens.TokensUtils;

public class Main {
  public static void main(String[] args) {
    try {
      String BFSfile = args[0];
      String outputFile = args[1];
      BufferedReader docReader = new BufferedReader(new FileReader("./programs/" + BFSfile));
      TokensFile.createFile(outputFile);
      
      /**
       * 1 -  Lê arquivo
       * 2 - Pega uma linha, se for null acabou o arquivo (printa o EOF)
       * 3 - Se nao for null, eu printo a linha vou em cada palavra da linha e dela e formo um token (Caso especial: strings)
       * 4 - Column na nextToken?
       * 5 - Chorar muito e surtar ate arrancar os cabelos
       */
      
      Lexico lexical = new Lexico();
      TokensUtils coord = new TokensUtils();

      while (true) {
        String line = docReader.readLine();
        coord.setTokenLine(coord.getLine() + 1);
        
        if (line == null) { // Se line igual a null acabou o documento
          TokensFile.write((new TokensClass(Tokens.EOF, "EOF", coord.getLine(), coord.getStartColumn())).toString());
          docReader.close();
          TokensFile.closeFile();
          break;
        }

        TokensFile.write(String.format("%04d  ", coord.getLine()) + " " + line + "\n");



        // String word = "";
        char[] lineArray = line.toCharArray();
        for (int i = 0; i < lineArray.length; i++) {
          /* if (lineArray[i] == ' ') {
            continue;
          } */
          coord.setStartColumn(i);
          TokensFile.write(lexical.nextToken(lineArray, coord, i).toString());
          System.out.println("Voltei!");
          i = coord.getEndColumn();
        }

        /* docReader.close();
        TokensFile.closeFile();
        return; */

        /* String[] lineArray = line.split(" ");
        for (int i = 0; i < lineArray.length; i++) {
          TokensFile.write(lexical.nextToken(line).toString());
        } */
        /* System.out.println("Estou na linha: " + line);
        
        char[] lineArray = line.toCharArray();
        String word = "";
        for(int i = 0; i < lineArray.length; i++) {
          System.out.printf("%c", lineArray[i]);
          if (lineArray[i] == '"') {
            while(lineArray[i] != '"') {
              word += lineArray[i++];
            }
            TokensFile.write(lexical.nextToken(word, column, lineCounter).toString());
          } else if (lineArray[i] == '\'') {
            while(lineArray[i] != '\'') {
              word += lineArray[i++];
            }
            TokensFile.write(lexical.nextToken(word, column, lineCounter).toString());
          } else if (lineArray[i] == ' ') {
            System.out.println("\nCriando token pra palavra: " + word);
            TokensFile.write(lexical.nextToken(word, column, lineCounter).toString());
            System.out.println("Token para " + word + " criado!");
            word = "";
            column = i + 1;
          } else {
            word += lineArray[i];
          }
        } */
      } 

      // Lexico lexical = new Lexico(args[0], args[1]);
    } catch (Exception e) {
      e.printStackTrace();
    }
      // lexical.generateTokens();
  } 
}
