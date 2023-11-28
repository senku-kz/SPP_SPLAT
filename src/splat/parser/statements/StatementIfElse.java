package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;

import java.util.List;
import java.util.Map;

public class StatementIfElse extends Statement {
    ASTElement expression;
    List<Statement> thanStatement;
    List<Statement> elseStatement = null;
    public StatementIfElse(Token tok) {
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

    public List<Statement> getThanStatement() {
        return thanStatement;
    }

    public void setThanStatement(List<Statement> thanStatement) {
        this.thanStatement = thanStatement;
    }

    public List<Statement> getElseStatement() {
        return elseStatement;
    }

    public void setElseStatement(List<Statement> elseStatement) {
        this.elseStatement = elseStatement;
    }
}
