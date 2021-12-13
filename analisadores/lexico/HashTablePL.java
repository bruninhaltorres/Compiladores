package analisadores.lexico;

import java.util.Hashtable;

public class HashTablePL {
    Hashtable<String, Tokens> palavraReservada = new Hashtable<>();

    public HashTablePL() {
		palavraReservada.put("main", Tokens.PR_MAIN);
		palavraReservada.put("function", Tokens.PR_FN);
		palavraReservada.put("return", Tokens.PR_RETURN);
		palavraReservada.put("void",Tokens.PR_VOID);
		palavraReservada.put("int",Tokens.PR_INT);
		palavraReservada.put("float",Tokens.PR_FLOAT);
		palavraReservada.put("char",Tokens.PR_CHAR);
		palavraReservada.put("string",Tokens.PR_STRING);
		palavraReservada.put("bool",Tokens.PR_BOOL);
		palavraReservada.put("SysIn",Tokens.PR_SYSIN);
		palavraReservada.put("SysOut",Tokens.PR_SYSOUT);
		palavraReservada.put("if",Tokens.PR_IF);
		palavraReservada.put("elif",Tokens.PR_ELIF);
		palavraReservada.put("else",Tokens.PR_ELSE);
		palavraReservada.put("while",Tokens.PR_WHILE);
		palavraReservada.put("for",Tokens.PR_FOR);
		palavraReservada.put("TRUE",Tokens.PR_TRUE);
		palavraReservada.put("FALSE",Tokens.PR_FALSE);
	}

}