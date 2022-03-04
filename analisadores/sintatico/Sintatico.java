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
        if (token.category.equals(TokensEnum.EOF)) {
            return;
        }
    }

    private void S() {
        if (token.category.equals(TokensEnum.PR_FN)) {
            result("S", "'Function' DcFunction S");

            System.out.println(token);
            getToken();

            DcFunction(); // PARAMOS AQUI
            S();

        } /*
           * else if(isTypeCategory()){
           * result("S", "DcVar S");
           * 
           * DcVar();
           * S();
           * } else {
           * result("S", epsilon);
           * System.out.println(token);
           * }
           */
    }

    private void DcFunction() {
        if (token.category.equals(TokensEnum.PR_MAIN)) {
            result("DcFunction", " 'main' '(' Param ')' '{' Instructions '}' ");
        } else if (isFuncType()) { // verifica se o token é um tipo de função.
            result("DcFunction", "Type '_''ID' '(' Param ')' '{' Instructions '}' ");
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

                            Instructions(); // PARAMOS AQ

                            if (token.category.equals(TokensEnum.CL_CHAVES)) {
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
                } else {
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
        } else if (isArray()) { // Declaração de Array
            result("Instructions", "DcArr Instructions");
            System.out.println(token);
            getToken();

            DcArr();
            Instructions();
        } else if (isCommand()) {
            result("Instructions", "Command Instructions");
            Command();
            Instructions();
        } else if (isInOut()) { 
            result("Instructions", "InOut Instructions");

            InOut();
            Instructions();
             
        } else if (token.category.equals(TokensEnum.PR_RETURN)) {
            result("Instructions", "Return Instructions");
            
            Return();
            Instructions();
        } else if(token.category.equals(TokensEnum.ID)) {
            result("Instructions", "'ID' AtrDirFunCall Instructions");

            System.out.println(token);
            getToken();

            AtrDirFunCall();
            Instructions();
        } else {
            result("Instructions", epsilon);
        }
    }

    private void DcVar() {
        result("DcVar", "Type DcVarAtr ';'");

        Type();

        DcVarAtr();

        if (token.category.equals(TokensEnum.S_PVIRG)) {
            System.out.println(token);
            getToken();
        } else {
            error("';'");
        }

    }

    private void DcArr() {
        result("DcArr", "'array' Type '_''ID''[' 'int' ']' DcArrAtr';'");

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

        if (token.category.equals(TokensEnum.OPR_IGUAL)) {
            System.out.println(token);
            getToken();

            if (token.category.equals(TokensEnum.OP_CHAVES)) {
                System.out.println(token);
                getToken();

                ArrAtr();

                if (token.category.equals(TokensEnum.CL_CHAVES)) {
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
        result("DcArrAtr", "CT | 'ID' MultArrAtr");

        if (isConstant() || token.category.equals(TokensEnum.ID)) {
            System.out.println(token);
            getToken();

            MultArrAtr();
        } else {
            error("CT, 'ID'");
        }
    }

    private void MultArrAtr() {
        result("MultArrAtr", " ',' ArrAtr");

        if (token.category.equals(TokensEnum.S_VIRG)) {
            System.out.println(token);
            getToken();

            ArrAtr();
        } else {
            result("DcArrAtr", epsilon);
        }
    }

    private void DcVarAtr() {
        if (token.category.equals(TokensEnum.ID)) {
            result("DcVarAtr", "'_''ID' Atr DcVarAtrFat");

            System.out.println(token);
            getToken();

            Atr();
        } else {
            error("'_''ID'");
        }
    }

    private void Atr() {
        if (token.category.equals(TokensEnum.OPR_IGUAL)) {
            result("Atr", "'=' Ec MultAtr");

            System.out.println(token);
            getToken();

            Ec();
            MultAtr();
        } else {
            result("Atr", epsilon);
        }
    }

    private void MultAtr() {
        if (token.category.equals(TokensEnum.S_VIRG)) {
            result("MultAtr", "',' Atr");

            System.out.println(token);
            getToken();

            DcVarAtr();
        } else {
            result("MultAtr", epsilon);
        }
    }

    private void Param() {
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
        result("DcParam", "Type '_''ID' MultDcParam");

        Type();

        if (token.category.equals(TokensEnum.ID)) {
            System.out.println(token);
            getToken();

            MultDcParam();
        } else {
            error("'_''ID'");
        }
    }

    private void DcParamArray() {
        result("DcParamArray", "'array' Type '_''ID' '[' ']' MultDcParamArray");

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
            error("'ID'");
        }
    }

    private void MultDcParam() {
        if (token.category.equals(TokensEnum.S_VIRG)) {
            getToken();
            if (isVarType()) {
                result("MultDcParam", "',' DcParam");
                DcParam();
            }
            if (token.category.equals(TokensEnum.PR_ARRAY)) {
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
        if (token.category.equals(TokensEnum.PR_IF)) {
            result("Command", "if");
            If();
        } else if (token.category.equals(TokensEnum.PR_WHILE)) {
            result("Command", "while");
            While();
        } else if (token.category.equals(TokensEnum.PR_FOR)) {
            result("Command", "for");
            For();
        } else {
            error("'while', 'if', 'for'");
        }
    }

    private void Condicional() {
        if (token.category.equals(TokensEnum.OP_PAR)) {
            System.out.println(token);
            getToken();

            Eb();
            if (token.category.equals(TokensEnum.CL_PAR)) {
                System.out.println(token);
                getToken();

                if (token.category.equals(TokensEnum.OP_CHAVES)) {
                    System.out.println(token);
                    getToken();

                    Instructions();

                    if (token.category.equals(TokensEnum.CL_CHAVES)) {
                        Elif();
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

    private void If() {
        result("If", " '(' Eb ')' '{' Instructions '}' Elif");

        System.out.println(token);
        getToken();

        Condicional();
    }

    private void Elif() {
        if (token.category.equals(TokensEnum.PR_ELIF)) {
            result("Elif", "'elif' '(' Eb ')' '{' Instructions '}' Elif Else");

            System.out.println(token);
            getToken();

            Condicional();
        } else {
            result("Elif", epsilon);
        }
    }

    private void Else() {
        if (token.category.equals(TokensEnum.PR_ELSE)) {
            result("Else", "'else' '{' Instructions '}'");

            System.out.println(token);
            getToken();

            if (token.category.equals(TokensEnum.OP_CHAVES)) {
                System.out.println(token);
                getToken();

                Instructions();

                if (token.category.equals(TokensEnum.CL_CHAVES)) {
                    System.out.println(token);
                    getToken();
                } else {
                    error("'}'");
                }
            } else {
                error("'{'");
            }
        } else {
            result("Else", epsilon);
        }
    }

    private void While() {
        if (token.category.equals(TokensEnum.PR_WHILE)) {
            result("While", "'(' Eb ')' '{' Instructions '}'");

            System.out.println(token);
            getToken();

            if (token.category.equals(TokensEnum.OP_PAR)) {
                System.out.println(token);
                getToken();

                Eb();

                if (token.category.equals(TokensEnum.CL_PAR)) {
                    System.out.println(token);
                    getToken();

                    if (token.category.equals(TokensEnum.OP_CHAVES)) {
                        System.out.println(token);
                        getToken();

                        Instructions();

                        if (token.category.equals(TokensEnum.CL_CHAVES)) {
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
            } else {
                error("'('");
            }
        } else {
            error("'While'");
        }
    }

    private void For() {
        result("For", "'('Start Stop Increment')' '{' Instructions '}'");

        System.out.println(token);
        getToken();

        if (token.category.equals(TokensEnum.OP_PAR)) {
            System.out.println(token);
            getToken();

            Start();

            Stop();

            Increment();

            if (token.category.equals(TokensEnum.CL_PAR)) {
                System.out.println(token);
                getToken();

                if (token.category.equals(TokensEnum.OP_CHAVES)) {
                    System.out.println(token);
                    getToken();

                    Instructions();

                    if (token.category.equals(TokensEnum.CL_CHAVES)) {
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
        } else {
            error("'('");
        }
    }
    
    private void Start() {
        result("Start", "'_''ID' '=' Ec");

        if (token.category.equals(TokensEnum.ID)) {
            System.out.println(token);
            getToken();

            if (token.category.equals(TokensEnum.OPR_IGUAL)){
                System.out.println(token);
                getToken();

                Ec();
            } else {
                error("'='");
            }
        } else {
            error("'_''ID'");
        }
    }

    private void Stop() {
        result("Stop", "'',' Ec");

        if (token.category.equals(TokensEnum.S_VIRG)) {
            System.out.println(token);
            getToken();

            Ec();
        } else {
            error("','");
        }
    }

    private void Increment() {
        if (token.category.equals(TokensEnum.S_VIRG)) {
            result("Increment", "'',' CT_INT");
            System.out.println(token);
            getToken();
            
            if (token.category.equals(TokensEnum.CT_INT)) {
    
                System.out.println(token);
                getToken();
            } else {
                error("CT_INT");
            }
        } else {
            result("Increment", epsilon);
        }
    }

    private void InOut() {
        if(token.category.equals(TokensEnum.PR_SYSIN)){
            result("InOut", "SysIn");
            SysIn();
        } else if(token.category.equals(TokensEnum.PR_SYSOUT)){
            result("InOut", "SysOut");
            SysOut();
        } else{
            error("'SysIn', 'SysOut'");
        }
    }

    private void SysIn(){
        if(token.category.equals(TokensEnum.PR_SYSIN)){
            result("SysIn", "'SysIn' '(' ParamIn ')' ';'");
            System.out.println(token);
            getToken();

            if(token.category.equals(TokensEnum.OP_PAR)){
                System.out.println(token);
                getToken();
                ParamIn();

                if(token.category.equals(TokensEnum.CL_PAR)){
                    System.out.println(token);
                    getToken();
                    
                    if(token.category.equals(TokensEnum.S_PVIRG)){
                        System.out.println(token);
                        getToken();
                    }
                    else {
                        error("';'");
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
            error("'Input'");
        }
    }

    private void ParamIn(){
        if(token.category.equals(TokensEnum.ID)){
            result("ParamIn", "'ID' MultParamIn"); 
            
            System.out.println(token);
            getToken();

            MultParamIn();
        } else {
            error("'ID'");
        }
    }

    private void MultParamIn() {
        if(token.category.equals(TokensEnum.S_PVIRG)) {
            result("MultParamIn", "',' ParamIn");
            System.out.println(token);
            getToken();

            ParamIn();
        } else {
            result("MultParamIn", epsilon);
        }
    }

    private void SysOut() {
        if(token.category.equals(TokensEnum.PR_SYSOUT)){
            result("SysOut", "'SysOut' '(' ParamOut ')' ';'");
            
            System.out.println(token);
            getToken();
    
            if(token.category.equals(TokensEnum.OP_PAR)){
                System.out.println(token);
                getToken();

                ParamOut();
    
                if(token.category.equals(TokensEnum.CL_PAR)){
                    System.out.println(token);
                    getToken();

                    if(token.category.equals(TokensEnum.S_PVIRG)){
                        System.out.println(token);
                        getToken();
                    } else {
                        error("';'");
                    }
                } else {
                    error("')'");
                }
            } else {
                error("'('");
            }
        } else {
            error("'Output'");
        }
    }

    private void ParamOut() {
        if(token.category.equals(TokensEnum.CT_STRING)) { 
            result("ParamOut", " '\"' CT_STRING '\"' MultParamOut ");
            System.out.println(token);
            getToken();

            MultParamOut();
        } else if(token.category.equals(TokensEnum.ID)){
            result("ParamOut", " ID MultParamOut "); 
            System.out.println(token);
            getToken();

            MultParamOut();
        } else {
            error("CT_STRING, ID");
        }
    }

    private void MultParamOut() {
        if(token.category.equals(TokensEnum.OPR_ADD)){
            result("MultParamOut", " '+' ParamOut ");
            System.out.println(token);
            getToken();

            ParamOut();
        } else {
            result("MultParamOut", epsilon);
        }
    }

    private void Return() {
        if(token.category.equals(TokensEnum.PR_RETURN)) {
            result("Return", "'Return' ParamReturn");

            System.out.println(token);
            getToken();

            ParamReturn();
        } else {
            error("'Return'");
        }
    }

    private void ParamReturn(){
        if(token.category.equals(TokensEnum.S_PVIRG)) {
            result("ReturnParam", "';'");

            System.out.println(token);
            getToken();
        } else {
            result("ReturnParam", "Ec ';'");

            Ec();

            if(token.category.equals(TokensEnum.S_PVIRG)) {
                System.out.println(token);
                getToken();
            } else {
                error("';'");
            }
        }
    }

    private void Type() {
        if (token.category.equals(TokensEnum.PR_VOID)) {
            result("Type", "'void'");
            System.out.println(token);
            getToken();
        } else if (token.category.equals(TokensEnum.PR_FLOAT)) {
            result("Type", "'float'");
            System.out.println(token);
            getToken();
        } else if (token.category.equals(TokensEnum.PR_CHAR)) {
            result("Type", "'char'");
            System.out.println(token);
            getToken();
        } else if (token.category.equals(TokensEnum.PR_STRING)) {
            result("Type", "'string'");
            System.out.println(token);
            getToken();
        } else if (token.category.equals(TokensEnum.PR_BOOL)) {
            result("Type", "'bool'");
            System.out.println(token);
            getToken();
        } else {
            error("'Int', 'Float', 'Char', 'String', 'Bool', 'Void'");
        }
    }

    private boolean isConstant() {
        return token.category.equals(TokensEnum.CT_INT) || token.category.equals(TokensEnum.CT_FLOAT)
                || token.category.equals(TokensEnum.CT_CHAR) || token.category.equals(TokensEnum.CT_STRING)
                || token.category.equals(TokensEnum.PR_TRUE) || token.category.equals(TokensEnum.PR_FALSE);
    }

    private boolean isVarType() {
        return token.category.equals(TokensEnum.PR_INT) || token.category.equals(TokensEnum.PR_FLOAT)
                || token.category.equals(TokensEnum.PR_CHAR) || token.category.equals(TokensEnum.PR_STRING)
                || token.category.equals(TokensEnum.PR_BOOL);
    }

    private boolean isFuncType() {
        return token.category.equals(TokensEnum.PR_VOID) || isVarType();
    }

    private boolean isCommand() {
        if (token.category.equals(TokensEnum.PR_WHILE) || token.category.equals(TokensEnum.PR_IF)
                || token.category.equals(TokensEnum.PR_FOR)) {
            return true;
        }
        return false;
    }

    private boolean isInOut(){
        if (token.category.equals(TokensEnum.PR_SYSIN) || token.category.equals(TokensEnum.PR_SYSOUT)) {
            return true;
        }
        return false;
    }

    private boolean isArray() {
        return (token.category.equals(TokensEnum.PR_ARRAY)) ? true : false;
    }

    private void Ec() {
        result("Ec", "Eb EcLL");
        Eb();
        EcLL();
    }

    private void Eb() {
        result("Eb", "Tb EbLL");
        Tb();
        EbLL();
    }

    private void EcLL() {
        if (token.category.equals(TokensEnum.OPR_CONC)) {
            result("EcLL", "'OP_CONCAT' Eb EcLL");
            System.out.println(token);
            getToken();
            Eb();
            EcLL();
        } else {
            result("EcLL", epsilon);
        }
    }

    private void EbLL() {
        if (token.category.equals(TokensEnum.OPR_OR) || token.category.equals(TokensEnum.OPR_AND)) {
            if (token.category.equals(TokensEnum.OPR_OR)) {
                result("EbLL", "'OP_OR' Tb EbLL");
            } else if (token.category.equals(TokensEnum.OPR_AND)) {
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

    private void Tb() {
        result("Tb", "Ra TbLL");
        Ra();
        TbLL();
    }

    private void TbLL() {
        if (token.category.equals(TokensEnum.OPR_NOT)) {
            result("TbLL", "'OPR_NOT' Ra TbLL");
            System.out.println(token);
            getToken();
            Ra();
            TbLL();
        } else {
            result("TbLL", epsilon);
        }
    }

    private void Ra() {
        result("Ra", "Rb RaLL");
        Rb();
        RaLL();
    }

    private void RaLL() {
        if (isRelCategory()) {
            result("RaLL", "Rel Rb RaLL");
            Rel();
            System.out.println(token);
            getToken();
            Rb();
            RaLL();
        } else {
            result("RaLL", epsilon);
        }
    }

    private void Rb() {
        result("Rb", "Ea RbLL");
        Ea();
        RbLL();
    }

    private void RbLL() {
        if (isOpsCategory()) {
            result("RbLL", "Ops Ea RbLL");
            Ops();
            System.out.println(token);
            getToken();
            Ea();
            RbLL();
        } else {
            result("RbLL", epsilon);
        }
    }

    private void Ea() {
        result("Ea", "Ta EaLL");
        Ta();
        EaLL();
    }

    private void EaLL() {
        if (token.category.equals(TokensEnum.OPR_ADD) || token.category.equals(TokensEnum.OPR_SUB)) {
            if (token.category.equals(TokensEnum.OPR_ADD)) {
                result("EaLL", "'OPR_ADD' Ta EaLL");
            } else if (token.category.equals(TokensEnum.OPR_SUB)) {
                result("EaLL", "'OPR_SUB' Ta EaLL");
            }
            System.out.println(token);
            getToken();
            Ta();
            EaLL();
        } else {
            result("EaLL", epsilon);
        }
    }

    private void Ta() {
        result("Ta", "Fa TaLL");
        Fa();
        TaLL();
    }

    private void TaLL() {
        if (token.category.equals(TokensEnum.OPR_MULT) || token.category.equals(TokensEnum.OPR_DIV)
                || token.category.equals(TokensEnum.OPR_MOD)) {
            if (token.category.equals(TokensEnum.OPR_MULT)) {
                result("TaLL", "'OPR_MULT' Fa TaLL");
            } else if (token.category.equals(TokensEnum.OPR_DIV)) {
                result("TaLL", "'OPR_DIV' Fa TaLL");
            } else if (token.category.equals(TokensEnum.OPR_MOD)) {
                result("TaLL", "'OPR_MOD' Fa TaLL");
            }
            System.out.println(token);
            getToken();
            Fa();
            TaLL();
        } else {
            result("TaLL", epsilon);
        }
    }

    private void Fa() {
        if (token.category.equals(TokensEnum.OP_PAR)) {
            result("Fa", "'(' Ec ')'");
            System.out.println(token);
            getToken();
            Ec();
            if (token.category.equals(TokensEnum.CL_PAR)) {
                System.out.println(token);
                getToken();
            } else {
                error("')'");
            }
        } else if (token.category.equals(TokensEnum.ID)) {
            result("Fa", "'ID' IdFunCall");
            System.out.println(token);
            getToken();
            IdFunCall();
        } else if (token.category.equals(TokensEnum.CT_INT)) {
            result("Fa", "'CT_INT'");
            System.out.println(token);
            getToken();
        } else if (token.category.equals(TokensEnum.CT_FLOAT)) {
            result("Fa", "'CT_FLOAT'");
            System.out.println(token);
            getToken();
        } else if (token.category.equals(TokensEnum.PR_TRUE)) {
            result("Fa", "'PR_TRUE'");
            System.out.println(token);
            getToken();
        } else if (token.category.equals(TokensEnum.PR_FALSE)) {
            result("Fa", "'PR_FALSE'");
            System.out.println(token);
            getToken();
        } else if (token.category.equals(TokensEnum.CT_CHAR)) {
            result("Fa", "'CT_CHAR'");
            System.out.println(token);
            getToken();
        } else if (token.category.equals(TokensEnum.CT_STRING)) {
            result("Fa", "'CT_STRING'");
            System.out.println(token);
            getToken();
        } else if (token.category.equals(TokensEnum.OPR_NOT)) {
            result("Fa", "'OP_NOTUNI' 'ID'");
            System.out.println(token);
            getToken();
            if (token.category.equals(TokensEnum.ID)) {
                System.out.println(token);
                getToken();
            } else {
                error("'ID'");
            }
            // } else if(token.category.equals(TokensEnum.OP_SIZE)){
            // result("Fa", "'OP_SIZE' 'ID'");
            // System.out.println(token);
            // getToken();
            // if(token.category.equals(TokensEnum.ID)){
            // System.out.println(token);
            // getToken();
            // } else {
            // error("'ID'");
            // }
            // } else {
            error("'(', 'ID', 'CT_INT', 'CT_FLOAT', 'PR_TRUE', 'PR_FALSE', 'CT_CHAR', 'CT_STRING', 'OP_NOTUNI'"); // removi
                                                                                                                  // o
                                                                                                                  // OP_SIZE
        }
    }

    private void result(String left, String right) {
        String format = "%10s%s = %s";
        System.out.printf((format) + "%n", "", left, right);
    }

    private void error(String expecteds) {
        System.out.println("Error: Expected " + expecteds + " at position " + token.getPosition());
        System.exit(1);
    }
}