package splat.parser.statements;

import org.w3c.dom.Node;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.ValueBoolean;
import splat.executor.values.ValueInteger;
import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.expressions.BinaryExpression;
import splat.parser.nodes.LabelNode;
import splat.parser.nodes.NumberNode;
import splat.parser.nodes.VariableNode;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatementFunctionCall extends Statement {
    ASTElement functionName;

    List<ASTElement> arguments;

    Value returnValue;

    public StatementFunctionCall(Token tok) {
        super(tok);
        this.arguments = new ArrayList<>();
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        LabelNode functionName = (LabelNode)this.functionName;

        if (funcMap.get(functionName.getLabel()) == null){
            throw new SemanticAnalysisException("Function not declared.", this.functionName);
        }

        List<Declaration> functionVarDecl = funcMap.get(functionName.getLabel()).getParameters();
        List<ASTElement> functionCallParameters = this.arguments;

        if (functionVarDecl.size() != functionCallParameters.size()){
            throw new SemanticAnalysisException("Number of parameters do not match", this.functionName);
        }

        for (int i = 0; i < functionCallParameters.size(); i++ ) {
            VariableDecl vd = (VariableDecl)functionVarDecl.get(i);
            TokenType vdType = vd.getType();

            ASTElement vn = functionCallParameters.get(i);
            TokenType vnType = this.getType(vn, funcMap, varAndParamMap);

            if (!vdType.equals(vnType)){
                throw new SemanticAnalysisException("Parameters type do not match.", this.functionName);
            }
        }
    }

    @Override
    public void execute(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap){
        String functionName = ((LabelNode)this.functionName).getLabel();
        List<Declaration> functionParameters = funcMap.get(functionName).getParameters();
        Map<String, Value> functionVarAndParamMap = new HashMap<>();

        for (int i=0; i<this.arguments.size(); i++){
            String prmName = functionParameters.get(i).getLabel();
            ASTElement n = this.arguments.get(i);
            Value argValue = this.getNodeValue(n, funcMap, varAndParamMap);

            functionVarAndParamMap.put(prmName, argValue);
        }

//        Add function Variables declaration
//        for ()
        try {
            for (Statement stmt : funcMap.get(((LabelNode)this.functionName).getLabel()).getStmts()){
                stmt.execute(funcMap, functionVarAndParamMap);
            }

        } catch (ReturnFromCall ex) {
            // System.out.println(" Function Reterned");
            this.returnValue = ex.getReturnVal();
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

    public Value getReturnValue() {
        return returnValue;
    }
}
