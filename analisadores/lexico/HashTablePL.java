//PALAVRAS RESERVADAS
package analisadores.lexico;

import java.util.Hashtable;

public class HashTablePL {
    Hashtable<String, Tokens> reservedWord = new Hashtable<>();

    public HashTablePL() {
		reservedWord.put("main", Tokens.PR_MAIN);
		reservedWord.put("function", Tokens.PR_FN);
		reservedWord.put("return", Tokens.PR_RETURN);
		reservedWord.put("void",Tokens.PR_VOID);
		reservedWord.put("int",Tokens.PR_INT);
		reservedWord.put("float",Tokens.PR_FLOAT);
		reservedWord.put("char",Tokens.PR_CHAR);
		reservedWord.put("string",Tokens.PR_STRING);
		reservedWord.put("bool",Tokens.PR_BOOL);
		reservedWord.put("SysIn",Tokens.PR_SYSIN);
		reservedWord.put("SysOut",Tokens.PR_SYSOUT);
		reservedWord.put("if",Tokens.PR_IF);
		reservedWord.put("elif",Tokens.PR_ELIF);
		reservedWord.put("else",Tokens.PR_ELSE);
		reservedWord.put("while",Tokens.PR_WHILE);
		reservedWord.put("for",Tokens.PR_FOR);
		reservedWord.put("TRUE",Tokens.PR_TRUE);
		reservedWord.put("FALSE",Tokens.PR_FALSE);
	}
}