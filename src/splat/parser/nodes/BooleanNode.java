package splat.parser.nodes;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;

public class BooleanNode extends ASTElement {
    private boolean value = false;
    public BooleanNode(Token token) {
        super(token);
        this.value = "true".equals(token.getValue());
    }
}
