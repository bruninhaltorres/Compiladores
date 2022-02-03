//PALAVRAS RESERVADAS
package analisadores.lexico;

import java.util.Hashtable;

import analisadores.lexico.tokens.TokensEnum;

public class HashTablePL {
    Hashtable<String, TokensEnum> reservedWord = new Hashtable<>();

    public HashTablePL() {
		reservedWord.put("main", TokensEnum.PR_MAIN);
		reservedWord.put("function", TokensEnum.PR_FN);
		reservedWord.put("return", TokensEnum.PR_RETURN);
		reservedWord.put("void",TokensEnum.PR_VOID);
		reservedWord.put("int",TokensEnum.PR_INT);
		reservedWord.put("float",TokensEnum.PR_FLOAT);
		reservedWord.put("char",TokensEnum.PR_CHAR);
		reservedWord.put("string",TokensEnum.PR_STRING);
		reservedWord.put("bool",TokensEnum.PR_BOOL);
		reservedWord.put("SysIn",TokensEnum.PR_SYSIN);
		reservedWord.put("SysOut",TokensEnum.PR_SYSOUT);
		reservedWord.put("if",TokensEnum.PR_IF);
		reservedWord.put("elif",TokensEnum.PR_ELIF);
		reservedWord.put("else",TokensEnum.PR_ELSE);
		reservedWord.put("while",TokensEnum.PR_WHILE);
		reservedWord.put("for",TokensEnum.PR_FOR);
		reservedWord.put("true",TokensEnum.PR_TRUE);
		reservedWord.put("false",TokensEnum.PR_FALSE);
		reservedWord.put("array",TokensEnum.PR_ARRAY);
		reservedWord.put("break",TokensEnum.PR_BREAK);
		reservedWord.put("and",TokensEnum.OPR_AND);
		reservedWord.put("or",TokensEnum.OPR_OR);
		reservedWord.put("not",TokensEnum.OPR_NOT);
	}
}