package splat.parser.statements;

import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.ValueBoolean;
import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;
import splat.parser.expressions.BinaryExpression;
import splat.parser.expressions.UnaryExpression;
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

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall {
        // System.out.println("Execute if-then-else");
        Value value = null;

        if (this.expression instanceof BinaryExpression){
            value = ((BinaryExpression) this.expression).evaluate(funcMap, varAndParamMap);
        } else if (this.expression instanceof UnaryExpression){
            value = ((UnaryExpression)this.expression).evaluate(funcMap, varAndParamMap);
        }

        if (((ValueBoolean)value).getValue()){
            for (Statement stmt: this.thanStatement) {
                stmt.execute(funcMap, varAndParamMap);
            }
        } else {
            for (Statement stmt: this.elseStatement) {
                stmt.execute(funcMap, varAndParamMap);
            }
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
