package splat.parser.nodes;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;

public class StringNode extends ASTElement {
    protected String stringValue;

    public StringNode(Token token) {
        super(token);
        this.stringValue = token.getValue();
    }

    public String getStringValue() {
        return stringValue;
    }
}
