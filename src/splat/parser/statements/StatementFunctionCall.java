package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.Statement;

import java.util.ArrayList;
import java.util.List;

public class StatementFunctionCall extends Statement {
    ASTElement functionName;
    List<ASTElement> arguments;

    public StatementFunctionCall(Token tok) {
        super(tok);
        this.arguments = new ArrayList<>();
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
