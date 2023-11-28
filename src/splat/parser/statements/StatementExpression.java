package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;

import java.util.Map;

public class StatementExpression extends Statement {
    private ASTElement node_left;
    private ASTElement node_right;
    public StatementExpression(ASTElement node_left, ASTElement node_right) {
        super(new Token("STATEMENT EXPRESSION", "", node_right.getLine(), node_right.getColumn()));
        this.node_left = node_left;
        this.node_right = node_right;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) {

    }
}
