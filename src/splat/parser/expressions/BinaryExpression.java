package splat.parser.expressions;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.TokenType;
import splat.parser.nodes.*;
import splat.parser.statements.StatementFunctionCall;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class BinaryExpression extends Expression {
    private ArithmeticOperators operator;
    private ASTElement node_left;
    private ASTElement node_right;
    public BinaryExpression(ArithmeticOperators operator, ASTElement node_left, ASTElement node_right) {
        super(new Token("BINARY_EXPRESSION", "", node_right.getLine(), node_right.getColumn()));
        this.operator = operator;
        this.node_left = node_left;
        this.node_right = node_right;
    }

    @Override
    public TokenType analyzeAndGetType(Map<String, FunctionDecl> funcMap,
                                       Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        TokenType leftNodeType = null;
        TokenType rightNodeType = null;

        if (this.node_left instanceof VariableNode) {
            VariableNode leftNode = (VariableNode)this.node_left;
            leftNodeType = varAndParamMap.get(leftNode.getValue());
            if (leftNodeType == null){
                String msg = String.format("Undefined variable %s", leftNode.getValue());
                throw new SemanticAnalysisException(msg, this.node_left);
            }
        } else if (this.node_left instanceof StringNode) {
            leftNodeType = TokenType.String;
        } else if (this.node_left instanceof NumberNode) {
            leftNodeType = TokenType.Integer;
        } else if (this.node_left instanceof BinaryExpression) {
            leftNodeType = ((BinaryExpression) this.node_left).analyzeAndGetType(funcMap, varAndParamMap);
        } else if (this.node_left instanceof BooleanNode) {
            leftNodeType = TokenType.Boolean;
        } else if (this.node_left instanceof StatementFunctionCall) {
            StatementFunctionCall node = (StatementFunctionCall) this.node_left;
            String functionName = ((LabelNode)node.getFunctionName()).getLabel();
            leftNodeType = funcMap.get(functionName).getType();
        }

        if (this.node_right instanceof VariableNode) {
            VariableNode rightNode = (VariableNode)this.node_right;
            rightNodeType = varAndParamMap.get(rightNode.getValue());
            if (rightNodeType == null){
                String msg = String.format("Undefined variable %s", rightNode.getValue());
                throw new SemanticAnalysisException(msg, this.node_right);
            }
        } else if (this.node_right instanceof StringNode) {
            rightNodeType = TokenType.String;
        } else if (this.node_right instanceof NumberNode) {
            rightNodeType = TokenType.Integer;
        } else if (this.node_right instanceof BinaryExpression) {
            rightNodeType = ((BinaryExpression) this.node_right).analyzeAndGetType(funcMap, varAndParamMap);
        } else if (this.node_right instanceof BooleanNode) {
            rightNodeType = TokenType.Boolean;
        } else if (this.node_right instanceof StatementFunctionCall) {
            StatementFunctionCall node = (StatementFunctionCall) this.node_right;
            String functionName = ((LabelNode)node.getFunctionName()).getLabel();
            rightNodeType = funcMap.get(functionName).getType();
        }

        if (leftNodeType.equals(rightNodeType)){
            if (
                    ArithmeticOperators.GreaterThan.equals(this.operator)
                            || ArithmeticOperators.LessThan.equals(this.operator)
                            || ArithmeticOperators.GreaterThanOrEqualTo.equals(this.operator)
                            || ArithmeticOperators.LessThanOrEqualTo.equals(this.operator)
                            || ArithmeticOperators.EqualTo.equals(this.operator)
                            || ArithmeticOperators.NotEqualTo.equals(this.operator)
            ){
                return TokenType.Boolean;
            } else {
                return leftNodeType;
            }
        } else {
            throw new SemanticAnalysisException("Type mismatch between left and right BinaryExpression", this.node_left);
        }

    }

    @Override
    public String toString() {
        return "{" + node_left + " " + operator + " " + node_right + '}';
    }
}
