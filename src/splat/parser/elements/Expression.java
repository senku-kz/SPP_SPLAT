package splat.parser.elements;

import splat.lexer.Token;
import splat.parser.expressions.BinaryExpression;
import splat.parser.expressions.UnaryExpression;
import splat.parser.nodes.*;
import splat.parser.statements.StatementFunctionCall;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public abstract class Expression extends ASTElement {

	public Expression(Token token) {
		super(token);
	}

	/**
	 * This will be needed for Phase 3 - this abstract method will need to be
	 * implemented by every Expression subclass.  This method does two things:
	 * 
	 * 1) Performs typechecking and semantic analysis on this expression, and
	 * recursively calls the same method on any sub-expressions.  Note that we
	 * will usually need the types of the immediate sub-expressions to make 
	 * sure all the parts of this expression are of proper types.
	 * 
	 * 2) Determines the type of this expression. 
	 * 
	 * funcMap is needed in case this expression or a sub-expression contains
	 * a function call -- we would need to make sure the argument number and 
	 * types match, and also get the return type.
	 * 
	 * varAndParamMap is needed in case this expression or a sub-expression
	 * contains variables or parameters -- we use this map to keep track of
	 * what items are currently in scope, and what their types are
	 */
	 public abstract TokenType analyzeAndGetType(Map<String, FunctionDecl> funcMap,
												 Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException;
	
	/**
	 * This will be needed for Phase 4 - this abstract method will need to be
	 * implemented by every Expression subclass.  This method is used to 
	 * "calculate" the value of this expression, which will usually require we
	 * recursively call the same method on all sub-expressions. 
	 * 
	 * funcMap is needed in case this expression or a sub-expression contains
	 * a function call -- we will have to evaluate the individual arguments and 
	 * create a new varAndParamMap to bind the function params to the new values
	 * and then execute the function body.  More on this later...
	 *  
	 * varAndParamMap is needed in case this expression or a sub-expression
	 * contains variables or parameters -- we use this map to keep track of the
	 * values of the items that are currently in scope
	 */
//	public abstract Value evaluate(Map<String, FunctionDecl> funcMap,
//                                 Map<String, Value> varAndParamMap);

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
