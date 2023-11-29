package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.expressions.BinaryExpression;
import splat.parser.nodes.*;
import splat.semanticanalyzer.SemanticAnalysisException;

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
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        TokenType leftNodeType = null;
        TokenType rightNodeType = null;

        if (this.node_left instanceof LabelNode) {
            LabelNode leftNode = (LabelNode)this.node_left;
            leftNodeType = varAndParamMap.get(leftNode.getLabel());
            if (leftNodeType == null){
                throw new SemanticAnalysisException("Undefined variable", this.node_left);
            }
        } else if (this.node_left instanceof StringNode) {
            leftNodeType = TokenType.String;
        } else if (this.node_left instanceof NumberNode) {
            leftNodeType = TokenType.Integer;
        } else if (this.node_left instanceof BinaryExpression) {
            leftNodeType = ((BinaryExpression) this.node_left).analyzeAndGetType(funcMap, varAndParamMap);
        } else if (this.node_left instanceof BooleanNode) {
            leftNodeType = TokenType.Boolean;
        }

        if (this.node_right instanceof BinaryExpression){
            BinaryExpression rightNode = (BinaryExpression) this.node_right;
            rightNodeType = rightNode.analyzeAndGetType(funcMap, varAndParamMap);
            if (rightNodeType == null){
                throw new SemanticAnalysisException("Undefined variable", this.node_right);
            }
        } else if (this.node_right instanceof StringNode) {
            rightNodeType = TokenType.String;
        } else if (this.node_right instanceof NumberNode) {
            rightNodeType = TokenType.Integer;
        } else if (this.node_right instanceof BinaryExpression) {
            rightNodeType = ((BinaryExpression) this.node_right).analyzeAndGetType(funcMap, varAndParamMap);
        } else if (this.node_right instanceof BooleanNode) {
            rightNodeType = TokenType.Boolean;
        }

        if (!leftNodeType.equals(rightNodeType)) {
            throw new SemanticAnalysisException("Type mismatch between left and right StatementExpression", this.node_left);
        }
    }
}
