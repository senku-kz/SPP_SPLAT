package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class StatementWhile extends Statement {
    public StatementWhile(Token tok) {
        super(tok);
    }
}
