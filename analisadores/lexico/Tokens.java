//TODOS OS TOKENS DA LINGUAGEM

package analisadores.lexico;

public enum Tokens {

    EOF,
    ID,

    PR_MAIN,
    PR_FN,
    PR_RETURN,
    PR_VOID,
    PR_INT,
    PR_FLOAT,
    PR_CHAR,
    PR_STRING,
    PR_BOOL,
    PR_SYSIN,
    PR_SYSOUT,
    PR_IF,
    PR_ELIF,
    PR_ELSE,
    PR_WHILE,
    PR_FOR,
    PR_TRUE,
    PR_FALSE,
    BOOL_VALUE,
    
    UNDER,
    OPR_ADD,
    OPR_SUB,
    OPR_MULT,
    OPR_DIV,
    OPR_MOD,
    OPR_IGUAL,
    OPR_DIF,
    OPR_DIGUAL,
    OPR_MAIOR,
    OPR_MENOR,
    OPR_MAIORIG,
    OPR_CONC,

    OPR_MENORIG,
    OPR_AND,
    OPR_OR,
    OPR_NOT,

    OP_CHAVES,
    CL_CHAVES,
    OP_PAR,
    CL_PAR,
    OP_COLC,
    CL_COLC,

    S_VIRG,
    S_PVIRG,

    ERR_ID,
    ERR_CHAR,
    ERR_NUM,
    ERR_PR,
    ERR_DESC,
}