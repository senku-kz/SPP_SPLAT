package splat.parser.expressions;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.Expression;

public class AddNode extends Expression {
    private ASTElement node_left;
    private ASTElement node_right;

    public AddNode(ASTElement node_left, ASTElement node_right) {
        super(new Token("ADDITIVE_OPERATOR", "", node_right.getLine(), node_right.getColumn()));
        this.node_left = node_left;
        this.node_right = node_right;
    }
}
