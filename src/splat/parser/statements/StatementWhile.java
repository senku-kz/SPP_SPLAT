package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;

import java.util.List;
import java.util.Map;

public class StatementWhile extends Statement {
    ASTElement expression;
    List<Statement> statement;

    public StatementWhile(Token tok) {
        super(tok);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) {

    }

    public ASTElement getExpression() {
        return expression;
    }

    public void setExpression(ASTElement expression) {
        this.expression = expression;
    }

    public List<Statement> getStatement() {
        return statement;
    }

    public void setStatement(List<Statement> statement) {
        this.statement = statement;
    }
}
