package splat.parser.nodes;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;

public class LabelNode extends ASTElement {
    private String label;
    public LabelNode(Token token) {
        super(token);
        this.label = token.getValue();
    }
}
