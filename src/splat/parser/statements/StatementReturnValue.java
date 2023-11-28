package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;

import java.util.Map;

public class StatementReturnValue extends Statement {
    private ASTElement value;
    public StatementReturnValue(Token token, ASTElement value) {
        super(token);
        this.value = value;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) {

    }
}
