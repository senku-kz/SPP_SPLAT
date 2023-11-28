package splat.parser.nodes;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;

public class VariableNode extends ASTElement {
    private String value;

    public VariableNode(Token token) {
        super(token);
        this.value = token.getValue();
    }

    public String getValue() {
        return value;
    }
}
