package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.Statement;

public class StatementPrint extends Statement {
    private ASTElement printValue;
    public StatementPrint(Token token) {
        super(token);
    }

    public void setPrintValue(ASTElement printValue) {
        this.printValue = printValue;
    }
}
