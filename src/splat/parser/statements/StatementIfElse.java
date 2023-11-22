package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class StatementIfElse extends Statement {
    public StatementIfElse(Token tok) {
        super(tok);
    }
}
