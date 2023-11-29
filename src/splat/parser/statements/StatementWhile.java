package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;
import splat.parser.expressions.BinaryExpression;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public class StatementWhile extends Statement {
    ASTElement expression;
    List<Statement> statements;

    public StatementWhile(Token tok) {
        super(tok);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        if (this.expression instanceof BinaryExpression){
            BinaryExpression expr = (BinaryExpression) this.expression;
            expr.analyzeAndGetType(funcMap, varAndParamMap);
        }

        for (Statement stmt : this.statements){
            stmt.analyze(funcMap, varAndParamMap);
        }
    }

    public ASTElement getExpression() {
        return expression;
    }

    public void setExpression(ASTElement expression) {
        this.expression = expression;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }
}
