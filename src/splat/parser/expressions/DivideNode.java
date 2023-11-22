package splat.parser.expressions;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.Expression;

public class DivideNode extends Expression {
    private ASTElement node_left;
    private ASTElement node_right;

    public DivideNode(ASTElement node_left, ASTElement node_right) {
        super(new Token("DIVIDE_OPERATOR", "", node_right.getLine(), node_right.getColumn()));
        this.node_left = node_left;
        this.node_right = node_right;
    }
}
