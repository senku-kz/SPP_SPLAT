package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.Statement;

public class StatementReturnValue extends Statement {
    private ASTElement value;
    public StatementReturnValue(Token token, ASTElement value) {
        super(token);
        this.value = value;
    }
}
