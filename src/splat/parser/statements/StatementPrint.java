package splat.parser.statements;

import splat.executor.ReturnFromCall;
import splat.executor.Value;
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

public class StatementPrint extends Statement {
    private ASTElement printValue;
    public StatementPrint(Token token) {
        super(token);
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        TokenType nodeType = null;
        if (this.printValue instanceof NumberNode) {
            nodeType = TokenType.Integer;
        } else if (this.printValue instanceof BooleanNode) {
            nodeType = TokenType.Boolean;
        } else if (this.printValue instanceof StringNode){
            nodeType = TokenType.String;
        } else if (this.printValue instanceof BinaryExpression) {
            nodeType = ((BinaryExpression) this.printValue).analyzeAndGetType(funcMap, varAndParamMap);
        } else if (this.printValue instanceof VariableNode) {
            nodeType = varAndParamMap.get(((VariableNode) this.printValue).getValue());
        }

//        if (!TokenType.String.equals(nodeType)){
//            throw new SemanticAnalysisException("Print value not a String type", this.printValue);
//        }
    }

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) throws ReturnFromCall {
//        System.out.println("execute print");
        if (this.printValue instanceof StringNode) {
            String string = ((StringNode) this.printValue).getStringValue();
            System.out.print(string.substring(1, string.length() - 1));
        } else if (this.printValue instanceof NumberNode) {
            System.out.print(((NumberNode) this.printValue).getIntegerValue());
        } else if (this.printValue instanceof BooleanNode) {
            System.out.print(((BooleanNode) this.printValue).isValue());
        } else if (this.printValue instanceof BinaryExpression) {
            Value value = ((BinaryExpression)this.printValue).evaluate(funcMap, varAndParamMap);
            System.out.print(value);
        }
    }

    public void setPrintValue(ASTElement printValue) {
        this.printValue = printValue;
    }
}
