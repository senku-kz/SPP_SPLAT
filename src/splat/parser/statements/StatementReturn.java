package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class StatementReturn extends Statement {
    public StatementReturn(Token tok) {
        super(tok);
    }
}
