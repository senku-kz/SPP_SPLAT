package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class StatementPrint extends Statement {
    public StatementPrint(Token tok) {
        super(tok);
    }
}
