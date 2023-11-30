package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.expressions.BinaryExpression;
import splat.parser.expressions.UnaryExpression;
import splat.parser.nodes.*;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public class StatementExpression extends Statement {
    private ASTElement node_left;
    private ASTElement node_right;
    public StatementExpression(ASTElement node_left, ASTElement node_right) {
        super(new Token("STATEMENT EXPRESSION", "", node_right.getLine(), node_right.getColumn()));
        this.node_left = node_left;
        this.node_right = node_right;
    }

    @Override
    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        TokenType leftNodeType = this.getType(this.node_left, funcMap, varAndParamMap);
        TokenType rightNodeType = this.getType(this.node_right, funcMap, varAndParamMap);

        if (!leftNodeType.equals(rightNodeType)) {
            throw new SemanticAnalysisException("Type mismatch between left and right StatementExpression", this.node_left);
        }
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
                throw new SemanticAnalysisException("Undefined variable", this.node_left);
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

            for (Integer i = 0; i < functionCallParameters.size(); i++ ) {
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
