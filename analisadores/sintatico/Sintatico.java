package analisadores.sintatico;

import analisadores.lexico.*;
import analisadores.lexico.tokens.Token;
import analisadores.lexico.tokens.TokensEnum;

public class Sintatico {

    private Token token;
    private Lexico lexico;

    private String epsilon = "Epsilon";

    public Sintatico(String BFSfile) {
        lexico = new Lexico(BFSfile);
        getToken();
        S();
    }

    private void getToken() {
        token = lexico.nextToken();
        if(token.category.equals(TokensEnum.EOF)) {
            return;
        }
    }
    
    private void result(String left, String right) {
        String format = "%10s%s = %s";
        System.out.printf((format) + "%n", "", left, right);
    }

    private void Type() {
        if(token.category.equals(TokensEnum.PR_VOID)) {
            result("Type", "'void'");
            System.out.println(token);
            getToken();
        }
        else if(token.category.equals(TokensEnum.PR_FLOAT)) {
            result("Type", "'float'");
            System.out.println(token);
            getToken();
        }
        else if(token.category.equals(TokensEnum.PR_CHAR)) {
            result("Type", "'char'");
            System.out.println(token);
            getToken();
        }
        else if(token.category.equals(TokensEnum.PR_STRING)) {
            result("Type", "'string'");
            System.out.println(token);
            getToken();
        }
        else if(token.category.equals(TokensEnum.PR_BOOL)) {
            result("Type", "'bool'");
            System.out.println(token);
            getToken();
        }
        else {
            error("'Int', 'Float', 'Char', 'String', 'Bool', 'Void'");
        }
    }

    private void S(){
        if(token.category.equals(TokensEnum.PR_FN)){
            result("S", "'Function' DcFunction S");

            System.out.println(token);
            getToken();

            DcFunction(); // PARAMOS AQUI
            S();
            
        } else if(isTypeCategory()){
            result("S", "DcVar S");
            
            DcVar();
            S();
        } else {
            result("S", epsilon);
            System.out.println(token);
        }
    }

    private void DcFunction () {
        if (token.category.equals(TokensEnum.PR_MAIN)) {
            result("DcFunction", " 'main' '(' Param ')' '{' Instructions '}' ");
        } else if (isFuncType()) { // verifica se o token é um tipo de função.
            result("DcFunction", "Type '_''id' '(' Param ')' '{' Instructions '}' ");
            Type();
            
            if (token.category.equals(TokensEnum.ID)) {
                System.out.println(token);
                getToken();

                if (token.category.equals(TokensEnum.OP_PAR)) {
                    System.out.println(token);
                    getToken();

                    Param();

                    if (token.category.equals(TokensEnum.CL_PAR)) {
                        System.out.println(token);
                        getToken();
                        
                        if (token.category.equals(TokensEnum.OP_CHAVES)) {
                            System.out.println(token);
                            getToken();
                            
                            Instructions(); //PARAMOS AQ
                            
                            if (token.category.equals(TokensEnum.CL_CHAVES)) {
                                System.out.println(token);
                                getToken();
                            } else {
                                error("'}'");
                            }
                        } else {
                            error("'{'");
                        }
                    } else{
                        error("')'");
                    }
                } else{
                    error("'('");
                }
            }
        } else {
            error("'int', 'float', 'char', 'string', 'bool', 'void'");
        }
    }

    private void Instructions() {
        if (isVarType()) { //Variaveis normais
            result("Instructions", "DcVar Instructions");

            DcVar();
            Instructions();
        } else if (token.category.equals(TokensEnum.PR_ARRAY)) {
            result("Instructions", "DcArr Instructions");
            System.out.println(token);
            getToken();

            DcArr();
            Instructions();
        }
    }

    private void DcArr() {
        result("DcArr", "'array' Type '_''id''[' 'int' ']' DcArrAtr';'");

        Type();

        if (token.category.equals(TokensEnum.OP_CHAVES)) {
            System.out.println();
            getToken();

            if (token.category.equals(TokensEnum.CT_INT)) {
                System.out.println();
                getToken();

                if (token.category.equals(TokensEnum.CL_CHAVES)) {
                    System.out.println();
                    getToken();

                    DcArrAtr();
                } else {
                    error("']'")
                }
            } else {
                error("'int'");
            }
        } else {
            error("'['");
        }

        if (token.category.equals(TokensEnum.S_PVIRG)) {
            System.out.println(token);
            getToken();
        } else {
            error("';'");
        }
    }

    private void DcArrAtr() {
        result("DcArrAtr", "'array' Type '_''id''[' 'int' ']' '=' '{' Ea '}';'");

        if (token.category.equals(TokensEnum.OPR_IGUAL)){
            if(isConstant()) {
                System.out.println(token);
                getToken();
            } 
        } else {
            result("DcArrAtr", epsilon);
        }
    }

    private void DcVar() {
        result("DcVar", "Type DcVarAtr ';'");
        
        Type();

        DcVarAtr(); // PARAMOS AQUI

        if (token.category.equals(TokensEnum.S_PVIRG)) {
            System.out.println(token);
            getToken();
        } else {
            error("';'");
        }

    }

    

    private void DcVarAtr() {
        if (token.category.equals(TokensEnum.ID)) {
            result("DcVarAtr", "'_''id' Atr DcVarAtrFat");

            System.out.println(token);
            getToken();
            
            Atr(); //PARAMOS AQUI
            DcIdAtrFat();
        } else {
            error("'id'");
        }
    }

    private void Atr() {
        if(token.category.equals(TokensEnum.OPR_IGUAL)) {
            result("Atr", "'=' MultAtr");

            System.out.println(token);
            getToken();
            
            MultAtr();
        } else {
            result("Atr", epsilon);
        }
    }

    private void MultAtr() {
        if (token.category.equals(TokensEnum.ID)) {
            result("MultAtr", "'_''id' Atr");

            System.out.println(token);
            getToken();

            Atr();
        }
        else {
            result("MultAtr", epsilon);
        }
    }

    public void Param() {
        if (isVarType()) {
            result("Param", "DcParam");
            DcParam(); 
        } else if (token.category.equals(TokensEnum.PR_ARRAY)) {
            result("Param", "DcParamArray");
            getToken();
            DcParamArray();
        } else {
            result("Param", epsilon); // não tem parâmetro;
        }
    }

    private void DcParam() {
        result("DcParam", "Type 'id' MultDcParam");
        
        Type(); 
        
        if (token.category.equals(TokensEnum.ID)) {
            System.out.println(token);
            getToken();

            MultDcParam();
        } else {
            error("'id'");
        }
    }
    
    private void DcParamArray() {
        result("DcParamArray", "'array' Type 'id' '[' ']' MultDcParamArray");

        Type();
        
        if (token.category.equals(TokensEnum.ID)) {
            System.out.println(token);
            getToken();

            if (token.category.equals(TokensEnum.OP_COLC)) {
                System.out.println(token);
                getToken();

                if (token.category.equals(TokensEnum.CL_COLC)) {
                    System.out.println(token);
                    getToken();
        
                    MultDcParam();
                    
                } else {
                    error("']'");
                }
            } else {
                error("'['");
            }
        } else {
            error("'id'");
        }        
    }

    private void MultDcParam() {
        if (token.category.equals(TokensEnum.S_VIRG)) {
            getToken();
            if (isVarType()) {
                result("MultDcParam", "',' DcParam");
                DcParam(); 
            }
            if (token.category.equals(TokensEnum.PR_ARRAY)){
                result("MultDcParam", "',' DcParamArray");
                getToken();
                DcParamArray();
            }
        
            System.out.println(token);
            getToken();

        } else {
            result("MultDcParam", epsilon);
        }
    }

    private boolean isConstant () {
        return return token.category.equals(TokensEnum.CT_INT) || token.category.equals(TokensEnum.CT_FLOAT) || token.category.equals(TokensEnum.CT_CHAR) || token.category.equals(TokensEnum.CT_STRING) || token.category.equals(TokensEnum.PR_TRUE) || token.category.equals(TokensEnum.PR_FALSE) ;
    }

    public boolean isVarType () {
        return token.category.equals(TokensEnum.PR_INT) || token.category.equals(TokensEnum.PR_FLOAT) || token.category.equals(TokensEnum.PR_CHAR) || token.category.equals(TokensEnum.PR_STRING) || token.category.equals(TokensEnum.PR_BOOL);
    }

    public boolean isFuncType () {
        return token.category.equals(TokensEnum.PR_VOID) || isVarType();
    }

    public void error (String expecteds) {
        System.out.println("Error: Expected " + expecteds + " at position " + lexico.getPositionToken());
        System.exit(1);
    }
}