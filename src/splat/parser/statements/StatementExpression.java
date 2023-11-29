package splat.parser.statements;

import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.expressions.BinaryExpression;
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
        TokenType leftNodeType = null;
        TokenType rightNodeType = null;

        if (this.node_left instanceof LabelNode) {
            LabelNode leftNode = (LabelNode)this.node_left;
            leftNodeType = varAndParamMap.get(leftNode.getLabel());
            if (leftNodeType == null){
                throw new SemanticAnalysisException("Undefined variable", this.node_left);
            }
        } else if (this.node_left instanceof StringNode) {
            leftNodeType = TokenType.String;
        } else if (this.node_left instanceof NumberNode) {
            leftNodeType = TokenType.Integer;
        } else if (this.node_left instanceof BinaryExpression) {
            leftNodeType = ((BinaryExpression) this.node_left).analyzeAndGetType(funcMap, varAndParamMap);
        } else if (this.node_left instanceof BooleanNode) {
            leftNodeType = TokenType.Boolean;
        }

        if (this.node_right instanceof BinaryExpression){
            BinaryExpression rightNode = (BinaryExpression) this.node_right;
            rightNodeType = rightNode.analyzeAndGetType(funcMap, varAndParamMap);
            if (rightNodeType == null){
                throw new SemanticAnalysisException("Undefined variable", this.node_right);
            }
        } else if (this.node_right instanceof StringNode) {
            rightNodeType = TokenType.String;
        } else if (this.node_right instanceof NumberNode) {
            rightNodeType = TokenType.Integer;
        } else if (this.node_right instanceof BinaryExpression) {
            rightNodeType = ((BinaryExpression) this.node_right).analyzeAndGetType(funcMap, varAndParamMap);
        } else if (this.node_right instanceof BooleanNode) {
            rightNodeType = TokenType.Boolean;
        } else if (this.node_right instanceof StatementFunctionCall) {
            StatementFunctionCall functionCall = (StatementFunctionCall) this.node_right;

            if (funcMap.get(((LabelNode) functionCall.getFunctionName()).getLabel()) == null){
                throw new SemanticAnalysisException("Called function not declared.", this.node_right);
            }

            rightNodeType = funcMap.get(((LabelNode) functionCall.getFunctionName()).getLabel()).getType();

            List<Declaration> functionVarDecl = funcMap.get(((LabelNode) functionCall.getFunctionName()).getLabel()).getParameters();
            List<ASTElement> functionCallParameters = functionCall.getArguments();

            if (functionVarDecl.size() != functionCallParameters.size()){
                throw new SemanticAnalysisException("Number of parameters do not match", this.node_right);
            }

            for (Integer i = 0; i < functionCallParameters.size(); i++ ) {
                VariableDecl vd = (VariableDecl)functionVarDecl.get(i);
                TokenType vdType = vd.getType();

                VariableNode vn = (VariableNode) functionCallParameters.get(i);
                TokenType vnType = varAndParamMap.get(vn.getValue());
                if (!vdType.equals(vnType)){
                    throw new SemanticAnalysisException("Parameters type do not match", this.node_right);
                }
            }
        }

        if (!leftNodeType.equals(rightNodeType)) {
            throw new SemanticAnalysisException("Type mismatch between left and right StatementExpression", this.node_left);
        }
    }
}
