package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.expressions.BinaryExpression;
import splat.parser.nodes.LabelNode;
import splat.parser.nodes.VariableNode;
import splat.semanticanalyzer.SemanticAnalysisException;

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
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        TokenType vnType = null;

        LabelNode functionName = (LabelNode)this.functionName;

        if (funcMap.get(functionName.getLabel()) == null){
            throw new SemanticAnalysisException("Function not declared.", this.functionName);
        }
//        TokenType nodeType = funcMap.get(functionName.getLabel()).getType();

        List<Declaration> functionVarDecl = funcMap.get(functionName.getLabel()).getParameters();
        List<ASTElement> functionCallParameters = this.arguments;

        if (functionVarDecl.size() != functionCallParameters.size()){
            throw new SemanticAnalysisException("Number of parameters do not match", this.functionName);
        }

        for (int i = 0; i < functionCallParameters.size(); i++ ) {
            VariableDecl vd = (VariableDecl)functionVarDecl.get(i);
            TokenType vdType = vd.getType();

            ASTElement vn = functionCallParameters.get(i);
            if (vn instanceof VariableNode) {
                VariableNode vn2 = (VariableNode) vn;
                vnType = varAndParamMap.get(vn2.getValue());
            } else if (vn instanceof BinaryExpression) {
                BinaryExpression vn2 = (BinaryExpression) vn;
                vnType = vn2.analyzeAndGetType(funcMap, varAndParamMap);
            }

            if (!vdType.equals(vnType)){
                throw new SemanticAnalysisException("Parameters type do not match", this.functionName);
            }
        }
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
