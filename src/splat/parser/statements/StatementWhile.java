package splat.parser.statements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.ValueBoolean;
import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;
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
        TokenType expressionType = this.getType(this.expression, funcMap, varAndParamMap);
        if (!TokenType.Boolean.equals(expressionType)){
            throw new SemanticAnalysisException("Unexpected expression in while.", this.expression);
        }

        for (Statement stmt : this.statements){
            stmt.analyze(funcMap, varAndParamMap);
        }
    }

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall, ExecutionException {
        Value value = this.getNodeValue(this.expression, funcMap, varAndParamMap);

        if (((ValueBoolean)value).getValue()){
            for (Statement stmt : this.statements){
                stmt.execute(funcMap, varAndParamMap);
            }
            this.execute(funcMap, varAndParamMap);
        } else {
            return;
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
