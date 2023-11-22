package splat.parser.nodes;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;

public class VariableNode extends ASTElement {
    protected String value;

    public VariableNode(Token token) {
        super(token);
        this.value = token.getValue();
    }
}
