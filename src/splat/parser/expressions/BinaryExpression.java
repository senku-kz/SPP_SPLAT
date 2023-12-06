package splat.parser.expressions;

import splat.executor.ExecutionException;
import splat.executor.Value;
import splat.executor.values.ValueBoolean;
import splat.executor.values.ValueInteger;
import splat.executor.values.ValueString;
import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.TokenType;
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

        TokenType leftNodeType = this.getType(this.node_left, funcMap, varAndParamMap);
        TokenType rightNodeType = this.getType(this.node_right, funcMap, varAndParamMap);

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
                if (TokenType.Integer.equals(leftNodeType) || TokenType.Boolean.equals(leftNodeType)) {
                    return leftNodeType;
                } else {
                    throw new SemanticAnalysisException("Using not Integer type for BinaryExpression.", this.node_left);
                }

            }
        } else {
            throw new SemanticAnalysisException("Type mismatch between left and right BinaryExpression", this.node_left);
        }

    }

    @Override
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ExecutionException {
        Value valueLeft = this.getNodeValue(this.node_left, funcMap, varAndParamMap);
        Value valueRight = this.getNodeValue(this.node_right, funcMap, varAndParamMap);
        Value valueResult = null;

        if (ArithmeticOperators.Addition.equals(this.operator)){
            Integer t = ((ValueInteger)valueLeft).getValue() + ((ValueInteger)valueRight).getValue();
            valueResult = new ValueInteger(t);
        } else if (ArithmeticOperators.Subtraction.equals(this.operator)) {
            Integer t = ((ValueInteger)valueLeft).getValue() - ((ValueInteger)valueRight).getValue();
            valueResult = new ValueInteger(t);
        } else if (ArithmeticOperators.Multiplication.equals(this.operator)) {
            Integer t = ((ValueInteger)valueLeft).getValue() * ((ValueInteger)valueRight).getValue();
            valueResult = new ValueInteger(t);
        } else if (ArithmeticOperators.Division.equals(this.operator)) {
            try {
                Integer t = ((ValueInteger)valueLeft).getValue() / ((ValueInteger)valueRight).getValue();
                valueResult = new ValueInteger(t);
            } catch (ArithmeticException e){
                throw new ExecutionException("ArithmeticException - Can't be divided by Zero", this.node_left);
            }

        } else if (ArithmeticOperators.Modulus.equals(this.operator)) {
            Integer t = ((ValueInteger)valueLeft).getValue() % ((ValueInteger)valueRight).getValue();
            valueResult = new ValueInteger(t);

        } else if (ArithmeticOperators.LessThan.equals(this.operator)) {
            if (((ValueInteger) valueLeft).getValue() < ((ValueInteger) valueRight).getValue()) {
                valueResult = new ValueBoolean(true);
            } else {
                valueResult = new ValueBoolean(false);
            }
        } else if (ArithmeticOperators.GreaterThan.equals(this.operator)) {
            if (((ValueInteger) valueLeft).getValue() > ((ValueInteger) valueRight).getValue()) {
                valueResult = new ValueBoolean(true);
            } else {
                valueResult = new ValueBoolean(false);
            }
        } else if (ArithmeticOperators.LessThanOrEqualTo.equals(this.operator)) {
            if (((ValueInteger) valueLeft).getValue() <= ((ValueInteger) valueRight).getValue()) {
                valueResult = new ValueBoolean(true);
            } else {
                valueResult = new ValueBoolean(false);
            }
        } else if (ArithmeticOperators.GreaterThanOrEqualTo.equals(this.operator)) {
            if (((ValueInteger) valueLeft).getValue() >= ((ValueInteger) valueRight).getValue()) {
                valueResult = new ValueBoolean(true);
            } else {
                valueResult = new ValueBoolean(false);
            }
        } else if (ArithmeticOperators.EqualTo.equals(this.operator)) {
            if (valueLeft instanceof ValueBoolean){
                if (((ValueBoolean) valueLeft).getValue() && ((ValueBoolean)valueRight).getValue()) {
                    valueResult = new ValueBoolean(true);
                } else {
                    valueResult = new ValueBoolean(false);
                }

            } else if (valueLeft instanceof ValueInteger) {
                if (((ValueInteger) valueLeft).getValue() == ((ValueInteger)valueRight).getValue()) {
                    valueResult = new ValueBoolean(true);
                } else {
                    valueResult = new ValueBoolean(false);
                }

            } else if (valueLeft instanceof ValueString) {
                if (((ValueString) valueLeft).getValue().equals(((ValueString)valueRight).getValue())) {
                    valueResult = new ValueBoolean(true);
                } else {
                    valueResult = new ValueBoolean(false);
                }

            }
        } else if (ArithmeticOperators.NotEqualTo.equals(this.operator)) {
            if (valueLeft != valueRight) {
                valueResult = new ValueBoolean(true);
            } else {
                valueResult = new ValueBoolean(false);
            }

        } else if (ArithmeticOperators.LogicalAnd.equals(this.operator)) {
            if (((ValueBoolean) valueLeft).getValue() && ((ValueBoolean) valueRight).getValue()) {
                valueResult = new ValueBoolean(true);
            } else {
                valueResult = new ValueBoolean(false);
            }
        } else if (ArithmeticOperators.LogicalOr.equals(this.operator)) {
            if (((ValueBoolean) valueLeft).getValue() || ((ValueBoolean) valueRight).getValue()) {
                valueResult = new ValueBoolean(true);
            } else {
                valueResult = new ValueBoolean(false);
            }

        }
        return valueResult;
    }

    @Override
    public String toString() {
        return "{" + node_left + " " + operator + " " + node_right + '}';
    }
}
