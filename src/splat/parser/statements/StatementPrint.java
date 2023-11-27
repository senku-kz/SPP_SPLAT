package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class StatementPrint extends Statement {
    private String value;
    public StatementPrint(Token token, String value) {
        super(token);
        this.value = value;
    }
}
