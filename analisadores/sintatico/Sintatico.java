package analisadores.sintatico;

import analisadores.lexico.*;
import analisadores.lexico.tokens.Token;
import analisadores.lexico.tokens.TokensEnum;
import analisadores.lexico.tokens.TokensFile;

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
            result("S", "'function' DcFunction S");

            TokensFile.write(token.toString());
            getToken();

            DcFunction();

        } else {
            error("'function'");
        }
    }

    private void DcFunction() {
        if (token.category.equals(TokensEnum.PR_MAIN)) {
            result("DcFunction", " 'main' FunctionHeader");
            TokensFile.write(token.toString());
            getToken();

            FunctionHeader();
        } else if (isFuncType()) {
            result("DcFunction", "Type ID FunctionHeader");
            Type();

            if (token.category.equals(TokensEnum.ID)) {
                TokensFile.write(token.toString());
                getToken();

                FunctionHeader();
            }

            S();
        } else {
            TokensFile.write("DCfunction");
            error("'int', 'float', 'char', 'string', 'bool', 'void'");
        }
    }

    private void FunctionHeader() {
        result("FunctionHeader", "'(' Param ')' '{' Instructions '}'");
        
        if (token.category.equals(TokensEnum.OP_PAR)) {
            TokensFile.write(token.toString());
            getToken();

            Param();

            if (token.category.equals(TokensEnum.CL_PAR)) {
                TokensFile.write(token.toString());
                getToken();

                if (token.category.equals(TokensEnum.OP_CHAVES)) {
                    TokensFile.write(token.toString());
                    getToken();

                    Instructions();

                    if (token.category.equals(TokensEnum.CL_CHAVES)) {
                        TokensFile.write(token.toString());
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

    private void Instructions() {
        if(token.category.equals(TokensEnum.COMMENT)){
            getToken();

            Instructions();
        } else if (isVarType()) { // Declaração de variável
            result("Instructions", "DcVar Instructions");

            DcVar();
            Instructions();
        } else if (isArray()) { // Declaração de Array
            result("Instructions", "DcArr Instructions");
            TokensFile.write(token.toString());
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
        } else if (token.category.equals(TokensEnum.ID)) {
            result("Instructions", "ID AtrId Instructions");

            TokensFile.write(token.toString());
            getToken();

            AtrId();
            Instructions();
        } else {
            result("Instructions", epsilon);
        }
    }

    private void DcArr() {
        result("DcArr", "'array' Type ID '[' CT_INT | ID ']' DcArrAtr';'");

        Type();

        if (token.category.equals(TokensEnum.ID)) {
            TokensFile.write(token.toString());
            getToken();
        

            if (token.category.equals(TokensEnum.OP_COLC)) {
                TokensFile.write(token.toString());
                getToken();

                if (token.category.equals(TokensEnum.CT_INT) || token.category.equals(TokensEnum.ID)) {
                    TokensFile.write(token.toString());
                    getToken();

                    if (token.category.equals(TokensEnum.CL_COLC)) {
                        TokensFile.write(token.toString());
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
        } else {
            error("ID");
        }

        if (token.category.equals(TokensEnum.S_PVIRG)) {
            TokensFile.write(token.toString());
            getToken();
        } else {
            error("';'");
        }
    }

    private void DcArrAtr() {
        result("DcArrAtr", "'=' '{' MultArrAtr '}'");

        if (token.category.equals(TokensEnum.OPR_IGUAL)) {
            TokensFile.write(token.toString());
            getToken();

            if (token.category.equals(TokensEnum.OP_CHAVES)) {
                TokensFile.write(token.toString());
                getToken();

                ArrAtr();

                if (token.category.equals(TokensEnum.CL_CHAVES)) {
                    TokensFile.write(token.toString());
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
        result("ArrAtr", "CT | ID MultArrAtr");

        if (isConstant() || token.category.equals(TokensEnum.ID)) {
            TokensFile.write(token.toString());
            getToken();

            MultArrAtr();
        } else {
            error("CT, ID");
        }
    }

    private void MultArrAtr() {
        if (token.category.equals(TokensEnum.S_VIRG)) {
            result("MultArrAtr", " ',' ArrAtr");
            TokensFile.write(token.toString());
            getToken();

            ArrAtr();
        } else {
            result("MultArrAtr", epsilon);
        }
    }

    private void DcVar() {
        result("DcVar", "Type DcVarAtr ';'");

        Type();

        DcVarAtr();

        if (token.category.equals(TokensEnum.S_PVIRG)) {
            TokensFile.write(token.toString());
            getToken();
        } else {
            error("';'");
        }

    }

    private void DcVarAtr() {
        if (token.category.equals(TokensEnum.ID)) {
            result("DcVarAtr", "ID Atr MultAtr");

            TokensFile.write(token.toString());
            getToken();

            Atr();
            MultAtr();
        } else {
            error("ID");
        }
    }

    private void AtrId() {
        if (token.category.equals(TokensEnum.OP_PAR)) {
            result("AtrId", "FunctionCall");
            
            FunctionCall();
        } else {
            result("AtrId", "Atr");

            // TokensFile.write(token.toString());
            // getToken();

            Atr();
            if(token.category.equals(TokensEnum.S_PVIRG)){
                TokensFile.write(token.toString());
                getToken();
            } else {
                error("';'");
            }
        }
    }

    private void Atr() {
        if (token.category.equals(TokensEnum.OPR_IGUAL)) {
            result("Atr", "'=' Ec MultAtr");

            TokensFile.write(token.toString());
            getToken();

            Ec();
            
            MultAtr();
        } else if(token.category.equals(TokensEnum.OP_COLC)) {
            result("MultAtr", "'[' Ea ']' Atr");
            TokensFile.write(token.toString());
            getToken();

            Ea();

            if(token.category.equals(TokensEnum.CL_COLC)){
                TokensFile.write(token.toString());
                getToken();

                Atr();
            } else {
                error("']'");
            }
        }else {
            result("Atr", epsilon);
        }
    }

    private void MultAtr() {
        if (token.category.equals(TokensEnum.S_VIRG)) {
            result("MultAtr", "',' DcVarAtr");

            TokensFile.write(token.toString());
            getToken();

            DcVarAtr();
        } else if(token.category.equals(TokensEnum.OP_COLC)) {
            result("MultAtr", "'[' Ea ']' MultAtr");
            TokensFile.write(token.toString());
            getToken();

            Ea();
            MultAtr();

            if(token.category.equals(TokensEnum.CL_COLC)){
                TokensFile.write(token.toString());
                getToken();
            } else {
                error("']'");
            }

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
            TokensFile.write(token.toString());
            getToken();
            DcParamArray();
        } else {
            result("Param", epsilon); // não tem parâmetro;
        }
    }

    private void DcParam() {
        result("DcParam", "Type ID MultDcParam");

        Type();

        if (token.category.equals(TokensEnum.ID)) {
            TokensFile.write(token.toString());
            // getToken();

            MultDcParam();
        } else {
            error("ID ");
        }
    }

    private void MultDcParam() {
        if (token.category.equals(TokensEnum.S_VIRG)) {
            TokensFile.write(token.toString());
            getToken();
            if (isVarType()) { // verifica se é outra variável
                result("MultDcParam", "',' DcParam");

                DcParam();
            } else if (token.category.equals(TokensEnum.PR_ARRAY)) { // ou se é outro array
                result("MultDcParam", "',' DcParamArray");
                getToken();
                DcParamArray();
            } else {
                error("'int', 'float', 'bool', 'string', 'char', 'array'");
            }

            TokensFile.write(token.toString());
            getToken();

        } else {
            TokensFile.write("MultDcParam");

            result("MultDcParam", epsilon);
        }
    }

    private void DcParamArray() {
        result("DcParamArray", "'array' Type ID  '[' ']' MultDcParam");

        Type();

        if (token.category.equals(TokensEnum.ID)) {
            TokensFile.write(token.toString());
            getToken();

            if (token.category.equals(TokensEnum.OP_COLC)) {
                TokensFile.write(token.toString());
                getToken();

                if (token.category.equals(TokensEnum.CL_COLC)) {
                    TokensFile.write(token.toString());
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
        result("Condicional", "'(' Eb ')' '{' Instructions '}' Elif Else");
        if (token.category.equals(TokensEnum.OP_PAR)) {
            TokensFile.write(token.toString());
            getToken();

            Eb();
            
            if (token.category.equals(TokensEnum.CL_PAR)) {
                TokensFile.write(token.toString());
                getToken();

                if (token.category.equals(TokensEnum.OP_CHAVES)) {
                    TokensFile.write(token.toString());
                    getToken();

                    Instructions();
                    if (token.category.equals(TokensEnum.CL_CHAVES)) {
                        TokensFile.write(token.toString());
                        getToken();
                        
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
        result("If", " 'if' Condicional");

        TokensFile.write(token.toString());
        getToken();

        Condicional();
    }

    private void Elif() {
        if (token.category.equals(TokensEnum.PR_ELIF)) {
            result("Elif", "'elif' Condicional");

            TokensFile.write(token.toString());
            getToken();

            Condicional();
        } else {
            result("Elif", epsilon);
        }
    }

    private void Else() {
        if (token.category.equals(TokensEnum.PR_ELSE)) {
            result("Else", "'else' '{' Instructions '}'");

            TokensFile.write(token.toString());
            getToken();

            if (token.category.equals(TokensEnum.OP_CHAVES)) {
                TokensFile.write(token.toString());
                getToken();

                Instructions();

                if (token.category.equals(TokensEnum.CL_CHAVES)) {
                    TokensFile.write(token.toString());
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

    // private void CondWhile(){

    // }

    private void While() {
        if (token.category.equals(TokensEnum.PR_WHILE)) {
            result("While", "'while' '(' Ec ')' '{' Instructions '}'");

            TokensFile.write(token.toString());
            getToken();

            if (token.category.equals(TokensEnum.OP_PAR)) {
                TokensFile.write(token.toString());
                getToken();

                Ec();
                // MultAtr();

                if (token.category.equals(TokensEnum.CL_PAR)) {
                    TokensFile.write(token.toString());
                    getToken();

                    if (token.category.equals(TokensEnum.OP_CHAVES)) {
                        TokensFile.write(token.toString());
                        getToken();

                        Instructions();

                        if (token.category.equals(TokensEnum.CL_CHAVES)) {
                            TokensFile.write(token.toString());
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
            error("'while'");
        }
    }

    private void For() {
        result("For", "'for' '('Start Stop Increment')' '{' Instructions '}'");

        TokensFile.write(token.toString());
        getToken();

        if (token.category.equals(TokensEnum.OP_PAR)) {
            TokensFile.write(token.toString());
            getToken();

            Start();

            Stop();

            Increment();

            if (token.category.equals(TokensEnum.CL_PAR)) {
                TokensFile.write(token.toString());
                getToken();

                if (token.category.equals(TokensEnum.OP_CHAVES)) {
                    TokensFile.write(token.toString());
                    getToken();

                    Instructions();

                    if (token.category.equals(TokensEnum.CL_CHAVES)) {
                        TokensFile.write(token.toString());
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
        result("Start", " ID  '=' Ec");

        if (token.category.equals(TokensEnum.ID)) {
            TokensFile.write(token.toString());
            getToken();

            if (token.category.equals(TokensEnum.OPR_IGUAL)) {
                TokensFile.write(token.toString());
                getToken();

                Ec();
            } else {
                error("'='");
            }
        } else {
            error("ID ");
        }
    }

    private void Stop() {
        result("Stop", "',' Ec");

        if (token.category.equals(TokensEnum.S_VIRG)) {
            TokensFile.write(token.toString());
            getToken();

            Ec();
        } else {
            error("','");
        }
    }

    private void Increment() {
        if (token.category.equals(TokensEnum.S_VIRG)) {
            result("Increment", "',' CT_INT");
            TokensFile.write(token.toString());
            getToken();

            if (token.category.equals(TokensEnum.CT_INT)) {

                TokensFile.write(token.toString());
                getToken();
            } else {
                error("CT_INT");
            }
        } else {
            result("Increment", epsilon);
        }
    }

    private void InOut() {
        if (token.category.equals(TokensEnum.PR_SYSIN)) {
            result("InOut", "SysIn");
            SysIn();
        } else if (token.category.equals(TokensEnum.PR_SYSOUT)) {
            result("InOut", "SysOut");
            SysOut();
        } else {
            error("'SysIn', 'SysOut'");
        }
    }

    private void SysIn() {
        if (token.category.equals(TokensEnum.PR_SYSIN)) {
            result("SysIn", "'SysIn' '(' ParamIn ')' ';'");
            TokensFile.write(token.toString());
            getToken();

            if (token.category.equals(TokensEnum.OP_PAR)) {
                TokensFile.write(token.toString());
                getToken();
                ParamIn();

                if (token.category.equals(TokensEnum.CL_PAR)) {
                    TokensFile.write(token.toString());
                    getToken();

                    if (token.category.equals(TokensEnum.S_PVIRG)) {
                        TokensFile.write(token.toString());
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
            error("'SysIn'");
        }
    }

    private void ParamIn() {
        if (token.category.equals(TokensEnum.ID)) {
            result("ParamIn", " ID MultParamIn");

            TokensFile.write(token.toString());
            getToken();

            if (token.category.equals(TokensEnum.OP_COLC)) {
                result("MultAtr", "'[' Ea ']'");
                TokensFile.write(token.toString());
                getToken();

                Ea();

                if (token.category.equals(TokensEnum.CL_COLC)) {
                    TokensFile.write(token.toString());
                    getToken();
                } else {
                    error("']'");
                }

                MultParamIn();
            }
        } else {
            error("'ID'");
        }
    }

    private void MultParamIn() {
        if (token.category.equals(TokensEnum.S_PVIRG)) {
            result("MultParamIn", "',' ParamIn");
            TokensFile.write(token.toString());
            getToken();

            ParamIn();
        } else {
            result("MultParamIn", epsilon);
        }
    }

    private void SysOut() {
        if (token.category.equals(TokensEnum.PR_SYSOUT)) {
            result("SysOut", "'SysOut' '(' ParamOut ')' ';'");

            TokensFile.write(token.toString());
            getToken();

            if (token.category.equals(TokensEnum.OP_PAR)) {
                TokensFile.write(token.toString());
                getToken();

                ParamOut();

                if (token.category.equals(TokensEnum.CL_PAR)) {
                    TokensFile.write(token.toString());
                    getToken();

                    if (token.category.equals(TokensEnum.S_PVIRG)) {
                        TokensFile.write(token.toString());
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
            error("'SysOut'");
        }
    }

    private void ParamOut() {
        if (token.category.equals(TokensEnum.CT_STRING)) {
            result("ParamOut", " '\"' CT_STRING '\"' MultParamOut ");
            TokensFile.write(token.toString());
            getToken();

            MultParamOut();
        } else if (token.category.equals(TokensEnum.ID)) {
            result("ParamOut", " ID MultParamOut ");
            TokensFile.write(token.toString());
            getToken();

            if(token.category.equals(TokensEnum.OP_COLC)) {
                result("MultParamOut", "'[' Ea ']'");
                TokensFile.write(token.toString());
                getToken();
    
                Ea();
    
                if(token.category.equals(TokensEnum.CL_COLC)){
                    TokensFile.write(token.toString());
                    getToken();
                } else {
                    error("']'");
                }
    
            }

            MultParamOut();
        } else {
            error("CT_STRING, ID");
        }
    }

    private void MultParamOut() {
        if (token.category.equals(TokensEnum.OPR_ADD)) {
            result("MultParamOut", "'+' ParamOut ");
            TokensFile.write(token.toString());
            getToken();

            ParamOut();
        } else {
            result("MultParamOut", epsilon);
        }
    }

    private void Return() {
        if (token.category.equals(TokensEnum.PR_RETURN)) {
            result("Return", "'return' ParamReturn");

            TokensFile.write(token.toString());
            getToken();

            ParamReturn();
        } else {
            error("'return'");
        }
    }

    private void ParamReturn() {
        if (token.category.equals(TokensEnum.S_PVIRG)) {
            result("ParamReturn", "';'");

            TokensFile.write(token.toString());
            getToken();
        } else {
            result("ParamReturn", "Ec ';'");

            Ec();

            if (token.category.equals(TokensEnum.S_PVIRG)) {
                TokensFile.write(token.toString());
                getToken();
            } else {
                error("';'");
            }
        }
    }

    private void IdFunCall() {
        if (token.category.equals(TokensEnum.OP_PAR)) {
            result("IdFunCall", "FunctionCall");

            FunctionCall();
        } else if(token.category.equals(TokensEnum.OP_COLC)) {
            result("IdFunCall", "'[' Ea ']' IdFunCall");
            TokensFile.write(token.toString());
            getToken();

            Ea();

            if(token.category.equals(TokensEnum.CL_COLC)){
                TokensFile.write(token.toString());
                getToken();
            } else {
                error("']'");
            }

        }
    }

    private void FunctionCall() {
        if (token.category.equals(TokensEnum.OP_PAR)) {
            result("FunctionCall", "'(' ParamFunctionCall");

            TokensFile.write(token.toString());
            getToken();

            ParamFunctionCall();
        } else {
            error("'('");
        }
    }

    private void ParamFunctionCall() {
        if (token.category.equals(TokensEnum.CL_PAR)) {
            result("ParamFunctionCall", "')' ';'");

            TokensFile.write(token.toString());
            getToken();

            if (token.category.equals(TokensEnum.S_PVIRG)) {
                TokensFile.write(token.toString());
                getToken();
            } else {
                error("';'");
            }
        } else {
            result("ParamFunctionCall", "ParamFunction ')' ';'");

            ParamFunction();

            if (token.category.equals(TokensEnum.CL_PAR)) {
                TokensFile.write(token.toString());
                getToken();

                if (token.category.equals(TokensEnum.S_PVIRG)) {
                    TokensFile.write(token.toString());
                    getToken();
                } else {
                    error("';'");
                }
            } else {
                error("')'");
            }
        }
    }

    private void ParamFunction() {
        result("ParamFunction", "Ec MultParamFunction");

        Ec();
        MultParamFunction();
    }

    private void MultParamFunction() {
        if (token.category.equals(TokensEnum.S_VIRG)) {
            result("MultParamFunction", "',' ParamFunction");

            TokensFile.write(token.toString());
            getToken();

            ParamFunction();
        } else {
            result("MultParamFunction", epsilon);
        }
    }

    private void Equality() {
        if (token.category.equals(TokensEnum.OPR_DIGUAL)) {
            result("Equality", "'OPR_DIGUAL'");
        } else if (token.category.equals(TokensEnum.OPR_DIF)) {
            result("Equality", "'OPR_DIF'");
        } else {
            error("'OPR_DIGUAL', 'OPR_DIF'");
        }
    }

    private void Comparation() {
        if (token.category.equals(TokensEnum.OPR_MAIOR)) {
            result("Comparation", "'OPR_MAIOR'");
        } else if (token.category.equals(TokensEnum.OPR_MAIORIG)) {
            result("Comparation", "'OPR_MAIORIG'");
        } else if (token.category.equals(TokensEnum.OPR_MENOR)) {
            result("Comparation", "'OPR_MENOR'");
        } else if (token.category.equals(TokensEnum.OPR_MENORIG)) {
            result("Comparation", "'OPR_MENORIG'");
        } else {
            error("'OPR_MAIOR', 'OPR_MAIORIG', 'OPR_MENOR', 'OPR_MENORIG'");
        }
    }

    private void Type() {
        if (token.category.equals(TokensEnum.PR_VOID)) {
            result("Type", "'void'");
            TokensFile.write(token.toString());
            getToken();
        }else if (token.category.equals(TokensEnum.PR_INT)) {
            result("Type", "'int'");
            TokensFile.write(token.toString());
            getToken();
        } else if (token.category.equals(TokensEnum.PR_FLOAT)) {
            result("Type", "'float'");
            TokensFile.write(token.toString());
            getToken();
        } else if (token.category.equals(TokensEnum.PR_CHAR)) {
            result("Type", "'char'");
            TokensFile.write(token.toString());
            getToken();
        } else if (token.category.equals(TokensEnum.PR_STRING)) {
            result("Type", "'string'");
            TokensFile.write(token.toString());
            getToken();
        } else if (token.category.equals(TokensEnum.PR_BOOL)) {
            result("Type", "'bool'");
            TokensFile.write(token.toString());
            getToken();
        } else {
            TokensFile.write("Type");
            error("'int', 'float', 'char', 'string', 'bool', 'void'");
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

    private boolean isInOut() {
        if (token.category.equals(TokensEnum.PR_SYSIN) || token.category.equals(TokensEnum.PR_SYSOUT)) {
            return true;
        }
        return false;
    }

    private boolean isArray() {
        return (token.category.equals(TokensEnum.PR_ARRAY)) ? true : false;
    }

    private boolean isEqualityOPR() {
        return token.category.equals(TokensEnum.OPR_DIGUAL) || token.category.equals(TokensEnum.OPR_DIF);
    }

    private boolean isComparationOPR() {
        return token.category.equals(TokensEnum.OPR_MAIOR) || token.category.equals(TokensEnum.OPR_MAIORIG)
                || token.category.equals(TokensEnum.OPR_MENOR) || token.category.equals(TokensEnum.OPR_MENORIG);
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
            result("EcLL", "'OPR_CONC' Eb EcLL");
            TokensFile.write(token.toString());
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
                result("EbLL", "'OPR_OR' Tb EbLL");
            } else if (token.category.equals(TokensEnum.OPR_AND)) {
                result("EbLL", "'OPR_AND' Tb EbLL");
            }
            TokensFile.write(token.toString());
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
            TokensFile.write(token.toString());
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
        if (isEqualityOPR()) {
            result("RaLL", "Rel Rb RaLL");
            Equality();
            TokensFile.write(token.toString());
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
        if (isComparationOPR()) {
            result("RbLL", "Comparation Ea RbLL");
            Comparation();
            TokensFile.write(token.toString());
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
            TokensFile.write(token.toString());
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
            TokensFile.write(token.toString());
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
            TokensFile.write(token.toString());
            getToken();
            Ec();
            if (token.category.equals(TokensEnum.CL_PAR)) {
                TokensFile.write(token.toString());
                getToken();
            } else {
                error("')'");
            }
        } else if (token.category.equals(TokensEnum.ID)) {
            result("Fa", "'ID' IdFunCall");
            TokensFile.write(token.toString());
            getToken();
            IdFunCall();
        } else if (token.category.equals(TokensEnum.CT_INT)) {
            result("Fa", "'CT_INT'");
            TokensFile.write(token.toString());
            getToken();
        } else if (token.category.equals(TokensEnum.CT_FLOAT)) {
            result("Fa", "'CT_FLOAT'");
            TokensFile.write(token.toString());
            getToken();
        } else if (token.category.equals(TokensEnum.PR_TRUE)) {
            result("Fa", "'PR_TRUE'");
            TokensFile.write(token.toString());
            getToken();
        } else if (token.category.equals(TokensEnum.PR_FALSE)) {
            result("Fa", "'PR_FALSE'");
            TokensFile.write(token.toString());
            getToken();
        } else if (token.category.equals(TokensEnum.PR_BOOL)) {
            result("Fa", "'PR_BOOL'");
            TokensFile.write(token.toString());
            getToken();
        }else if (token.category.equals(TokensEnum.CT_CHAR)) {
            result("Fa", "'CT_CHAR'");
            TokensFile.write(token.toString());
            getToken();
        } else if (token.category.equals(TokensEnum.CT_STRING)) {
            result("Fa", "'CT_STRING'");
            TokensFile.write(token.toString());
            getToken();
        } else if (token.category.equals(TokensEnum.OPR_INVERS)) {
            result("Fa", "'OPR_INVERS' 'ID'");
            TokensFile.write(token.toString());
            getToken();
            if (token.category.equals(TokensEnum.ID)) {
                TokensFile.write(token.toString());
                getToken();
            } else {
                error("'ID'");
            }
        } else {
            error("'(', 'ID', 'CT_INT', 'CT_FLOAT', 'PR_TRUE', 'PR_FALSE', 'PR_BOOL', 'CT_CHAR', 'CT_STRING', 'OPR_INVERS'");
        }
    }

    private void result(String left, String right) {
        String format = "%10s%s = %s";
        TokensFile.write(String.format((format) + "%n", "", left, right));
    }

    private void error(String expectation) {
        TokensFile.write("Error "+ lexico.getPosition() +": Expected " + expectation + " but received '" + token.lexical + "'");
        System.exit(1);
    }
}