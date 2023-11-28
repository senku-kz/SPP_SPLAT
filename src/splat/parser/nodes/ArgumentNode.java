package splat.parser.nodes;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;

import java.util.List;

public class ArgumentNode extends ASTElement {
    protected ASTElement argument;
    public ArgumentNode(Token token) {
        super(token);
        if ("NUMBER".equals(token.getType())){
            this.argument = new NumberNode(token);
        } else if ("IDENTIFIER".equals(token.getType())) {
            this.argument = new VariableNode(token);
        }
    }
}
