package splat.parser.nodes;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;

public class PlusNode extends ASTElement {
    ASTElement number;
    public PlusNode(ASTElement number) {
        super(new Token("Positive Number", "", number.getLine(), number.getColumn()));
        this.number = number;
    }

}
