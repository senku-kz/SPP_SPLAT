package splat.parser.expressions;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.Expression;

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
    public String toString() {
        return "{" + node_left + " " + operator + " " + node_right + '}';
    }
}
