import analisadores.lexico.Lexico;

public class Main {
    public static void main(String[] args) {
      Lexico lexical = new Lexico(args[0], args[1]);
      lexical.generateTokens();
	  }
}
