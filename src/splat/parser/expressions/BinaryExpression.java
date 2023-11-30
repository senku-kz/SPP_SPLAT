package splat.parser.expressions;

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
