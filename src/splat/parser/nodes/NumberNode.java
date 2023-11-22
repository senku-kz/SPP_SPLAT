package splat.parser.nodes;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;

public class NumberNode extends ASTElement {
    protected float value;

    public NumberNode(Token token) {
        super(token);
        this.value = Float.parseFloat(token.getValue());
    }
}
