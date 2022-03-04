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
            
        } /* else if(isTypeCategory()){
            result("S", "DcVar S");
            
            DcVar();
            S();
        } else {
            result("S", epsilon);
            System.out.println(token);
        } */
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
        if (isVarType()) { // Declaração de variável
            result("Instructions", "DcVar Instructions");

            DcVar();
            Instructions();
        } else if (token.category.equals(TokensEnum.PR_ARRAY)) { // Declaração de Array
            result("Instructions", "DcArr Instructions");
            System.out.println(token);
            getToken();

            DcArr();
            Instructions();
        } else if (token.category.equals(TokensEnum.PR_WHILE) || token.category.equals(TokensEnum.PR_IF) || token.category.equals(TokensEnum.PR_FOR)) {
            result("Instructions", "Command Instructions");

            Command();
            Instructions();
        }
    }

    private void DcArr() {
        result("DcArr", "'array' Type '_''id''[' 'int' ']' DcArrAtr';'");
        
        Type();

        if (token.category.equals(TokensEnum.OP_COLC)) {
            System.out.println();
            getToken();

            if (token.category.equals(TokensEnum.CT_INT)) {
                System.out.println();
                getToken();

                if (token.category.equals(TokensEnum.CL_COLC)) {
                    System.out.println();
                    getToken();

                    DcArrAtr();
                } else {
                    error("']'");
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
        result("DcArrAtr", "'=' '{' MultArrAtr '}'");
        
        if (token.category.equals(TokensEnum.OPR_IGUAL)){
            System.out.println(token);
            getToken();
            
            if (token.category.equals(TokensEnum.OP_CHAVES)) {
                System.out.println(token);
                getToken();
                
                ArrAtr();
                
                if(token.category.equals(TokensEnum.CL_CHAVES)) {
                    System.out.println(token);
                    getToken();
                } else {
                    error("'}'");
                }
            } else {
                error("'{'");
            }
        } else {
            result("DcArrAtr", epsilon);
        }
    }

    private void ArrAtr() {
        result("DcArrAtr", "CT | 'id' MultArrAtr");

        if (isConstant() || token.category.equals(TokensEnum.ID)) {
            System.out.println(token);
            getToken();
                    
            MultArrAtr();
        } else {
            error("CT, 'id'");
        }
    }

    private void MultArrAtr() {
        result("MultArrAtr"," ',' ArrAtr");

        if(token.category.equals(TokensEnum.S_VIRG)) {
            System.out.println(token);
            getToken();

            ArrAtr();
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
            result("Atr", "'=' Ec MultAtr");

            System.out.println(token);
            getToken();
            
            Ec(); // Falta fazer
            MultAtr();
        } else {
            result("Atr", epsilon);
        }
    }

    private void MultAtr() {
        //MultAtr = ID | Ec
        if (token.category.equals(TokensEnum.S_VIRG)) {
            result("MultAtr", "',' Atr");

            System.out.println(token);
            getToken();

            Atr();
        } else {
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

    private void Command() {
        if(token.category.equals(TokensEnum.PR_IF)){
            result("Command", "if");
            If();
        } else if(token.category.equals(TokensEnum.PR_WHILE)){
            result("Command", "while");
            While();
        } else if(token.category.equals(TokensEnum.PR_FOR)){
            result("Command", "for");
            For();
        } else{
            error("'while', 'if', 'for'");
        }
    }

    private void Condicional() {
        if(token.category.equals(TokensEnum.OP_PAR)) {
            System.out.println(token);
            getToken();

            Eb();

            if(token.category.equals(TokensEnum.CL_PAR)) {
                System.out.println(token);
                getToken();

                if(token.category.equals(TokensEnum.OP_CHAVES)) {
                    System.out.println(token);
                    getToken();

                    Instructions();

                    if(token.category.equals(TokensEnum.CL_CHAVES)){
                        Elif(); // PAREI AQUI
                        Else();
                    } else {
                        error("'}'");
                    }
                } else {
                    error("'{'");
                }
            } else {
                error("')'");
            }
        } else {
            error("'('");
        }
    }

    private void If(){
        if(token.category.equals(TokensEnum.PR_IF)) {
            result("If", "'if' '(' Eb ')' '{' Instructions '}' Elif Else");

            System.out.println(token);
            getToken();

            Condicional();
        } else {
            error("'If'");
        }
    }

    private void Elif(){
        int cont = 0;

        if(token.category.equals(TokensEnum.PR_ELIF)) {
            result("Elif", "'elif' '(' Eb ')' '{' Instructions '}' Elif Else");

            System.out.println(token);
            getToken();

            Condicional();
            cont = 1;
        } else {
            cont = 2;
            error("'Elif'");
        }

        if(cont == 0){
            result("Elif", epsilon);
        }
    }

    private void Else(){
        if(token.category.equals(TokensEnum.PR_ELSE)) {
            result("Else", "'Else' '{' Instructions '}'");

            System.out.println(token);
            getToken();

            Instructions();
        }
        else {
            result("Else", epsilon);
        }
    }

    private void While(){
        if (token.category.equals(TokensEnum.PR_WHILE)) {
            result("While", "'while' '(' Eb ')' '{' Instructions '}'");
            
            System.out.println(token);
            getToken();
            
            if(token.category.equals(TokensEnum.OP_PAR)){
                System.out.println(token);
                getToken();

                Eb();

                if(token.category.equals(TokensEnum.CL_PAR)){
                    System.out.println(token);
                    getToken();
                    
                    if(token.category.equals(TokensEnum.OP_CHAVES)){
                        System.out.println(token);
                        getToken();
                        
                        Instructions();

                        if(token.category.equals(TokensEnum.CL_CHAVES)){
                            System.out.println(token);
                            getToken();

                        } else {
                            error("'}'");
                        }
                    } else {
                        error("'{'");
                    }
                }
                else {
                    error("')'");
                }
            }
            else {
                error("'('");
            }
        }
        else {
            error("'While'");
        }
    }

    private void For(){
        if(token.category.equals(TokensEnum.PR_FOR)) {
            result("For", "'for' '(' Init ';' Condit ';' Increment ')' '{' Instructions '}'");

            System.out.println(token);
            getToken();

            if(token.category.equals(TokensEnum.OP_PAR)) {
                System.out.println(token);
                getToken();

                Init(); //PAREI AQUI

                if(token.category.equals(TokensEnum.S_PVIRG)) {
                    System.out.println(token);
                    getToken();

                    Condit();

                    if(token.category.equals(TokensEnum.S_PVIRG)) {
                        System.out.println(token);
                        getToken();
    
                        Increment();
                        
                        if(token.category.equals(TokensEnum.CL_PAR)) {
                            System.out.println(token);
                            getToken();
                            
                            if(token.category.equals(TokensEnum.OP_CHAVES)) {
                                System.out.println(token);
                                getToken();
                                
                                Instructions();

                                if(token.category.equals(TokensEnum.CL_CHAVES)) {
                                    System.out.println(token);
                                    getToken();
                                    
                                } else {
                                    error("'}'");
                                }
                            } else {
                                error("'{'");
                            }
                        } else {
                            error("')'");
                        }    
                    }
                }
            } else {
                error("'('");
            }
        } else {
            error("'for'");
        }
    }

    private boolean isConstant () {
        return token.category.equals(TokensEnum.CT_INT) || token.category.equals(TokensEnum.CT_FLOAT) || token.category.equals(TokensEnum.CT_CHAR) || token.category.equals(TokensEnum.CT_STRING) || token.category.equals(TokensEnum.PR_TRUE) || token.category.equals(TokensEnum.PR_FALSE) ;
    }

    public boolean isVarType () {
        return token.category.equals(TokensEnum.PR_INT) || token.category.equals(TokensEnum.PR_FLOAT) || token.category.equals(TokensEnum.PR_CHAR) || token.category.equals(TokensEnum.PR_STRING) || token.category.equals(TokensEnum.PR_BOOL);
    }

    public boolean isFuncType () {
        return token.category.equals(TokensEnum.PR_VOID) || isVarType();
    }

    private void Ec() {
        result("Ec", "Eb EcLL");
        Eb();
        EcLL();
    }
 
    private void EcLL() {
        if(token.category.equals(TokensEnum.OPR_CONC)) {
            result("EcLL", "'OP_CONCAT' Eb EcLL");
            System.out.println(token);
            getToken();
            Eb();
            EcLL();
        } else {
            result("EcLL", epsilon);
        }
    }

    private void Eb() {
        result("Eb", "Tb EbLL");
        Tb();
        EbLL();
    }

    private void EbLL(){
        if(token.category.equals(TokensEnum.OPR_OR) || token.category.equals( TokensEnum.OPR_AND)) {
            if(token.category.equals(TokensEnum.OPR_OR)){
                result("EbLL", "'OP_OR' Tb EbLL");
            } else if(token.category.equals(TokensEnum.OPR_AND)){
                result("EbLL", "'OP_AND' Tb EbLL");
            }
            System.out.println(token);
            getToken();
            Tb();
            EbLL();
        } else {
            result("EbLL", epsilon);
        }
    }

    private void Tb(){
        result("Tb", "Ra TbLL");
        Ra();
        TbLL();
    }

    private void TbLL(){
        if(token.category.equals(TokensEnum.OPR_NOT)){
            result("TbLL", "'OPR_NOT' Ra TbLL");
            System.out.println(token);
            getToken();
            Ra();
            TbLL();
        } else{
            result("TbLL", epsilon);
        }
    }

    private void Ra(){
        result("Ra", "Rb RaLL");
        Rb();
        RaLL();
    }

    private void RaLL(){
        if(isRelCategory()){
            result("RaLL", "Rel Rb RaLL");
            Rel();
            System.out.println(token);
            getToken();
            Rb();
            RaLL();
        } else{
            result("RaLL", epsilon);
        }
    }

    private void Rb(){
        result("Rb", "Ea RbLL");
        Ea();
        RbLL();
    }

    private void RbLL(){
        if(isOpsCategory()){
            result("RbLL", "Ops Ea RbLL");
            Ops();
            System.out.println(token);
            getToken();
            Ea();
            RbLL();
        } else{
            result("RbLL", epsilon);
        }
    }

    private void Ea(){
        result("Ea", "Ta EaLL");
        Ta();
        EaLL();
    }

    private void EaLL(){
        if(token.category.equals(TokensEnum.OPR_ADD) || token.category.equals(TokensEnum.OPR_SUB)){
            if(token.category.equals(TokensEnum.OPR_ADD)){
                result("EaLL", "'OPR_ADD' Ta EaLL");
            } else if(token.category.equals(TokensEnum.OPR_SUB)){
                result("EaLL", "'OPR_SUB' Ta EaLL");
            }
            System.out.println(token);
            getToken();
            Ta();
            EaLL();
        } else{
            result("EaLL", epsilon);
        }
    }

    private void Ta(){
        result("Ta", "Fa TaLL");
        Fa();
        TaLL();
    }

    private void TaLL(){
        if(token.category.equals(TokensEnum.OPR_MULT)|| token.category.equals( TokensEnum.OPR_DIV) || token.category.equals( TokensEnum.OPR_MOD)){
            if(token.category.equals(TokensEnum.OPR_MULT)){
                result("TaLL", "'OPR_MULT' Fa TaLL");
            } else if(token.category.equals(TokensEnum.OPR_DIV)){
                result("TaLL", "'OPR_DIV' Fa TaLL");
            } else if(token.category.equals(TokensEnum.OPR_MOD)){
                result("TaLL", "'OPR_MOD' Fa TaLL");
            }
            System.out.println(token);
            getToken();
            Fa();
            TaLL();
        }
        else{
            result("TaLL", epsilon);
        }
    }

    private void Fa(){
        if(token.category.equals(TokensEnum.OP_PAR)){
            result("Fa", "'(' Ec ')'");
            System.out.println(token);
            getToken();
            Ec();
            if (token.category.equals(TokensEnum.CL_PAR)){
                System.out.println(token);
                getToken();
            } else {
                error("')'");
            }
        }
        else if(token.category.equals(TokensEnum.ID)){
            result("Fa", "'id' IdFunCall");
            System.out.println(token);
            getToken();
            IdFunCall();
        } else if(token.category.equals(TokensEnum.CT_INT)){
            result("Fa", "'CT_INT'");
            System.out.println(token);
            getToken();
        } else if(token.category.equals(TokensEnum.CT_FLOAT)){
            result("Fa", "'CT_FLOAT'");
            System.out.println(token);
            getToken();
        } else if(token.category.equals(TokensEnum.PR_TRUE)){
            result("Fa", "'PR_TRUE'");
            System.out.println(token);
            getToken();
        } else if(token.category.equals(TokensEnum.PR_FALSE)){
            result("Fa", "'PR_FALSE'");
            System.out.println(token);
            getToken();
        } else if(token.category.equals(TokensEnum.CT_CHAR)){
            result("Fa", "'CT_CHAR'");
            System.out.println(token);
            getToken();
        } else if(token.category.equals(TokensEnum.CT_STRING)){
            result("Fa", "'CT_STRING'");
            System.out.println(token);
            getToken();
        } else if(token.category.equals(TokensEnum.OPR_NOT)){
            result("Fa", "'OP_NOTUNI' 'id'");
            System.out.println(token);
            getToken();
            if(token.category.equals(TokensEnum.ID)){
                System.out.println(token);
                getToken(); 
            } else {
                error("'id'");
            }
        // } else if(token.category.equals(TokensEnum.OP_SIZE)){
        //     result("Fa", "'OP_SIZE' 'id'");
        //     System.out.println(token);
        //     getToken();
        //     if(token.category.equals(TokensEnum.ID)){
        //         System.out.println(token);
        //         getToken();
        //     } else {
        //         error("'id'");
        //     }
        // } else {
            error("'(', 'id', 'CT_INT', 'CT_FLOAT', 'PR_TRUE', 'PR_FALSE', 'CT_CHAR', 'CT_STRING', 'OP_NOTUNI'"); //removi o OP_SIZE
        }
    }

    public void error (String expecteds) {
        System.out.println("Error: Expected " + expecteds + " at position " + lexico.getPositionToken());
        System.exit(1);
    }

    // a gente terminou a parte de atribuição de array e atribuição múltipla e pegamos as funções ec, ea etc 
}