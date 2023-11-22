package splat.parser.nodes;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;

public class MinusNode extends ASTElement{
    ASTElement number;
    public MinusNode(ASTElement number) {
        super(new Token("Negative Number", "", number.getLine(), number.getColumn()));
        this.number = number;
    }
}
