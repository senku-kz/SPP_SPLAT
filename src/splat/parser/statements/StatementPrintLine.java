package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class StatementPrintLine extends Statement {
    public StatementPrintLine(Token tok) {
        super(tok);
    }
}
