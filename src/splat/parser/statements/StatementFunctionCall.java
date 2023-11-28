package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.Statement;
import splat.parser.elements.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatementFunctionCall extends Statement {
    ASTElement functionName;
    List<ASTElement> arguments;

    public StatementFunctionCall(Token tok) {
        super(tok);
        this.arguments = new ArrayList<>();
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) {

    }

    public ASTElement getFunctionName() {
        return functionName;
    }

    public void setFunctionName(ASTElement functionName) {
        this.functionName = functionName;
    }

    public List<ASTElement> getArguments() {
        return arguments;
    }

    public void setArguments(List<ASTElement> arguments) {
        this.arguments = arguments;
    }

    public void addArgument(ASTElement argument) {
        this.arguments.add(argument);
    }
}
