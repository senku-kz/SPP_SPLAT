package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatementIfElse extends Statement {
    ASTElement expression;
    List<Statement> thanStatement;
    List<Statement> elseStatement = new ArrayList<>();
    public StatementIfElse(Token tok) {
        super(tok);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        TokenType expressionType = this.getType(this.expression, funcMap, varAndParamMap);
        if (!TokenType.Boolean.equals(expressionType)){
            throw new SemanticAnalysisException("Unexpected expression in while.", this.expression);
        }

        for (Statement stmt : this.thanStatement){
            stmt.analyze(funcMap, varAndParamMap);
        }

        for (Statement stmt : this.elseStatement){
            stmt.analyze(funcMap, varAndParamMap);
        }
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
