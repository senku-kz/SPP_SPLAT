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
//        System.out.println("StatementExpression");
        if (this.node_left instanceof LabelNode){
            String labelLeft = ((LabelNode) this.node_left).getLabel();
            Value rightValue = null;

            if (this.node_right instanceof NumberNode) {
                rightValue = new ValueInteger(((NumberNode) this.node_right).getIntegerValue());
            } else if (this.node_right instanceof StringNode) {
                rightValue = new ValueString(((StringNode) this.node_right).getStringValue());
            } else if (this.node_right instanceof BooleanNode) {
                rightValue = new ValueBoolean(((BooleanNode)this.node_right).isValue());
            }

            varAndParamMap.put(labelLeft, rightValue);
        }
    }

}
