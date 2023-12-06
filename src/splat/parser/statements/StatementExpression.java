package splat.parser.statements;

import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.ValueBoolean;
import splat.executor.values.ValueInteger;
import splat.executor.values.ValueString;
import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;
import splat.parser.expressions.BinaryExpression;
import splat.parser.expressions.UnaryExpression;
import splat.parser.nodes.BooleanNode;
import splat.parser.nodes.LabelNode;
import splat.parser.nodes.NumberNode;
import splat.parser.nodes.StringNode;
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

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall {
        String labelLeft = ((LabelNode) this.node_left).getLabel();
        Value rightValue = this.getValue(this.node_right, funcMap, varAndParamMap);

        varAndParamMap.put(labelLeft, rightValue);
    }

}
