package splat.parser.statements;

import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.ValueVoid;
import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;
import splat.parser.expressions.BinaryExpression;
import splat.parser.expressions.UnaryExpression;
import splat.parser.nodes.VariableNode;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class StatementReturnValue extends Statement {
    private final ASTElement value;

    protected TokenType returnedNodeType = null;

    public StatementReturnValue(Token token, ASTElement value) {
        super(token);
        this.value = value;
    }

    public ASTElement getValue() {
        return value;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        TokenType nodeType = this.getType(this.value, funcMap, varAndParamMap);
        this.returnedNodeType = nodeType;

        Map.Entry<String, FunctionDecl> firstEntry = funcMap.entrySet().iterator().next();
        String functionName = firstEntry.getKey();

        TokenType functionType = firstEntry.getValue().getType();
        if (!nodeType.equals(functionType)){
            String msg = String.format("Error in returning the type of the declared function >>>%s<<<. Expected %s, get %s", functionName, functionType, nodeType);
            throw new SemanticAnalysisException(msg, firstEntry.getValue());
        }
    }

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall {
        Value returnValue = this.getValue(this.value, funcMap, varAndParamMap);
        throw new ReturnFromCall(returnValue);
    }

    public TokenType getReturnedNodeType() {
        return returnedNodeType;
    }
}
