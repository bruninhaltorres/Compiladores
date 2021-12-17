package analisadores.lexico;

import java.util.ArrayList;

public class Symbols {
    private static int i;
    private static char[] symbols = {
        ';', 
        '(', 
        ')', 
        '\'', 
        '\"', 
        '.', 
        '|', 
        '&',  
        '+', 
        '-', 
        '%', 
        '/', 
        '\\', 
        '{', 
        '}', 
        '[', 
        ']', 
        '^', 
        '`', 
        '~', 
        ','
    };
    
    /* private static ArrayList<Character> symbols = new ArrayList<Character>({
        ';', 
        '(', 
        ')', 
        '\'', 
        '\"', 
        '.', 
        '|', 
        '&',  
        '+', 
        '-', 
        '%', 
        '/', 
        '\\', 
        '{', 
        '}', 
        '[', 
        ']', 
        '^', 
        '`', 
        '~', 
        ','
    }); */
    
    private static char[] others = {
        '\t', 
        '\n', 
        '\r', 
        '\f', 
        '\0', 
        '\b'
    };

    private static char[] operators = {
        '>', 
        '<', 
        '!', 
        '='
    };

    public Symbols() {}

    public static boolean isDigit(char ch) {
		return Character.isDigit(ch);
	}
    
    public static boolean isLetter(char ch) {
		return Character.isLetter(ch);
	}

    public static boolean isOperator(char ch) {
        for( Symbols.i = 0; Symbols.i < Symbols.operators.length; Symbols.i++) {
            if(ch == Symbols.operators[i]) {
                return true;
            }
        }
        return false;   
    }

    public static boolean isOther(char ch) {
        for(Symbols.i = 0; Symbols.i < Symbols.others.length; Symbols.i++) {
            if(ch == Symbols.others[i] || Character.isWhitespace(ch)) {
                return true;
            }
        }
        return false;   
    }

    public static boolean isSymbol(char ch) {
        for(Symbols.i = 0; Symbols.i < Symbols.symbols.length; Symbols.i++) {
            if(ch == Symbols.symbols[i]) {
                return true;
            }
        }
        return false;   
    }
    
    
}