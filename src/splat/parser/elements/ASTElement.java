package splat.parser.elements;

import splat.lexer.Token;
import splat.parser.expressions.BinaryExpression;
import splat.parser.expressions.UnaryExpression;
import splat.parser.nodes.*;
import splat.parser.statements.StatementFunctionCall;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public abstract class ASTElement {
	private int line;
	private int column;

	public ASTElement(Token token) {
		this.line = token.getLine();
		this.column = token.getColumn();
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	protected TokenType getType(ASTElement node, Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
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
				throw new SemanticAnalysisException("Undefined variable", node);
			}
		} else if (node instanceof VariableNode) {
			nodeType = varAndParamMap.get(((VariableNode) node).getValue());
			if (nodeType == null){
				throw new SemanticAnalysisException("Undefined variable", node);
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

				ASTElement functionParameter = functionCallParameters.get(i);
				TokenType vnType = this.getType(functionParameter, funcMap, varAndParamMap);

				if (!vdType.equals(vnType)){
					throw new SemanticAnalysisException("Parameters type do not match", node);
				}
			}
		}
		return nodeType;
	}

}
