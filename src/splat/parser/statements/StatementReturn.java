package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;

import java.util.Map;

public class StatementReturn extends Statement {
    public StatementReturn(Token token) {
        super(token);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) {

    }
}
