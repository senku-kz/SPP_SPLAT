package splat.parser.expressions;

import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.nodes.*;
import splat.parser.statements.StatementFunctionCall;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public class UnaryExpression extends Expression {
    private ArithmeticOperators operator;
    private ASTElement node;

    public UnaryExpression(ArithmeticOperators operator, ASTElement node) {
        super(new Token("UNARY_EXPRESSION", "", node.getLine(), node.getColumn()));
        this.operator = operator;
        this.node = node;
    }

    @Override
    public TokenType analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        if (ArithmeticOperators.UnaryMinus.equals(this.operator)){
            if (this.node instanceof BooleanNode){
                throw new SemanticAnalysisException("Unexpected operator in Unary Expression.", this.node);
            } else if (this.node instanceof StringNode) {
                throw new SemanticAnalysisException("Unexpected operator in Unary Expression.", this.node);
            }
        } else if (ArithmeticOperators.LogicalNOT.equals(this.operator)) {
            if (this.node instanceof NumberNode){
                throw new SemanticAnalysisException("Unexpected operator in Unary Expression.", this.node);
            } else if (this.node instanceof StringNode) {
                throw new SemanticAnalysisException("Unexpected operator in Unary Expression.", this.node);
            }
        } else {
            throw new SemanticAnalysisException("Unexpected operator in Unary Expression.", this.node);

        }

        return this.getType(this.node, funcMap, varAndParamMap);
    }


    private TokenType getType(ASTElement node, Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        TokenType nodeType = null;
        if (node instanceof BinaryExpression){
            nodeType = ((BinaryExpression) node).analyzeAndGetType(funcMap, varAndParamMap);
            if (nodeType == null){
                throw new SemanticAnalysisException("Undefined variable", node);
            }
        } else if (node instanceof UnaryExpression) {
            nodeType = ((UnaryExpression) node).analyzeAndGetType(funcMap, varAndParamMap);
        } else if (node instanceof StringNode) {
            nodeType = TokenType.String;
        } else if (node instanceof NumberNode) {
            nodeType = TokenType.Integer;
        } else if (node instanceof BooleanNode) {
            nodeType = TokenType.Boolean;
        } else if (node instanceof LabelNode) {
            nodeType = varAndParamMap.get(((LabelNode) node).getLabel());
            if (nodeType == null){
                throw new SemanticAnalysisException("Undefined variable", this.node);
            }
        } else if (node instanceof StatementFunctionCall) {
            StatementFunctionCall functionCall = (StatementFunctionCall) node;

            if (funcMap.get(((LabelNode) functionCall.getFunctionName()).getLabel()) == null){
                throw new SemanticAnalysisException("Called function not declared.", node);
            }

            nodeType = funcMap.get(((LabelNode) functionCall.getFunctionName()).getLabel()).getType();

            List<Declaration> functionVarDecl = funcMap.get(((LabelNode) functionCall.getFunctionName()).getLabel()).getParameters();
            List<ASTElement> functionCallParameters = functionCall.getArguments();

            if (functionVarDecl.size() != functionCallParameters.size()){
                throw new SemanticAnalysisException("Number of parameters do not match", node);
            }

            for (int i = 0; i < functionCallParameters.size(); i++ ) {
                VariableDecl vd = (VariableDecl)functionVarDecl.get(i);
                TokenType vdType = vd.getType();

                TokenType vnType = null;
                ASTElement functionParameter = functionCallParameters.get(i);
                if (functionParameter instanceof VariableNode) {
                    VariableNode vn = (VariableNode) functionCallParameters.get(i);
                    vnType = varAndParamMap.get(vn.getValue());
                } else if (functionParameter instanceof NumberNode) {
                    vnType = TokenType.Integer;
                } else if (functionParameter instanceof BooleanNode) {
                    vnType = TokenType.Boolean;
                } else if (functionParameter instanceof StringNode) {
                    vnType = TokenType.String;
                }

                if (!vdType.equals(vnType)){
                    throw new SemanticAnalysisException("Parameters type do not match", node);
                }
            }
        }
        return nodeType;
    }

}
