package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;
import splat.parser.expressions.BinaryExpression;
import splat.parser.nodes.BooleanNode;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public class StatementIf extends Statement {
            ASTElement expression;
            List<Statement> statements;
    public StatementIf(Token tok) {
        super(tok);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        System.out.println("if-then-else");
        if (this.expression instanceof BinaryExpression){
            BinaryExpression expr = (BinaryExpression) this.expression;
            expr.analyzeAndGetType(funcMap, varAndParamMap);
        } else if (this.expression instanceof BooleanNode) {

        } else {
            throw new SemanticAnalysisException("Unexpected expression in while", this.expression);
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
