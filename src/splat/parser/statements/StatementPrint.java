package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;

import java.util.Map;

public class StatementPrint extends Statement {
    private ASTElement printValue;
    public StatementPrint(Token token) {
        super(token);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) {

    }

    public void setPrintValue(ASTElement printValue) {
        this.printValue = printValue;
    }
}
