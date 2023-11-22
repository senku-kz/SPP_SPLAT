package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class StatementProcedureCall extends Statement {
    public StatementProcedureCall(Token tok) {
        super(tok);
    }
}
