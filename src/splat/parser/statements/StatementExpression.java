package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class StatementExpression extends Statement {
    private final ASTElement node_left;
    private final ASTElement node_right;
    public StatementExpression(ASTElement node_left, ASTElement node_right) {
        super(new Token("STATEMENT EXPRESSION", "", node_right.getLine(), node_right.getColumn()));
        this.node_left = node_left;
        this.node_right = node_right;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        TokenType leftNodeType = this.getType(this.node_left, funcMap, varAndParamMap);
        TokenType rightNodeType = this.getType(this.node_right, funcMap, varAndParamMap);

        if (!leftNodeType.equals(rightNodeType)) {
            throw new SemanticAnalysisException("Type mismatch between left and right StatementExpression", this.node_left);
        }
    }

}
