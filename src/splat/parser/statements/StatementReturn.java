package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;
import splat.parser.expressions.BinaryExpression;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class StatementReturn extends Statement {
    public StatementReturn(Token token) {
        super(token);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        Map.Entry<String, FunctionDecl> firstEntry = funcMap.entrySet().iterator().next();
        String functionName = firstEntry.getKey();
        FunctionDecl functionDecl = firstEntry.getValue();
        if (!"void".equals(functionDecl.getType().toString())){
            throw new SemanticAnalysisException("Error in returning the type of the declared function >>>" + functionName + "<<<",
                    functionDecl
            );
        }
    }
}
