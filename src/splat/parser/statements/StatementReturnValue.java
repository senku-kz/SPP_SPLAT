package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class StatementReturnValue extends Statement {
    public StatementReturnValue(Token tok) {
        super(tok);
    }
}
