package splat.parser.nodes;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;

public class NumberNode extends ASTElement {
    protected Integer value;

    public NumberNode(Token token) {
        super(token);
        this.value = Integer.valueOf(token.getValue());
    }

    public Integer getIntegerValue() {
        return value;
    }
}
