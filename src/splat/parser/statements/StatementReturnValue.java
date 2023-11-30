package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;
import splat.parser.expressions.BinaryExpression;
import splat.parser.nodes.BooleanNode;
import splat.parser.nodes.NumberNode;
import splat.parser.nodes.StringNode;
import splat.parser.nodes.VariableNode;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class StatementReturnValue extends Statement {
    private ASTElement value;
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

        Map.Entry<String, FunctionDecl> firstEntry = funcMap.entrySet().iterator().next();
        String functionName = firstEntry.getKey();
        FunctionDecl functionDecl = firstEntry.getValue();

        if (!nodeType.equals(functionDecl.getType())){
            String msg = String.format("Error in returning the type of the declared function >>>%s<<<. Expected %s, get %s", functionName, functionDecl.getType(), nodeType);
            throw new SemanticAnalysisException(msg, functionDecl);
        }
    }

}
