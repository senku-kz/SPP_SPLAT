package splat.semanticanalyzer;

import splat.parser.elements.*;
import splat.parser.statements.StatementReturn;
import splat.parser.statements.StatementReturnValue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SemanticAnalyzer {

	private ProgramAST progAST;
	
	private Map<String, FunctionDecl> funcMap = new HashMap<>();
	private Map<String, TokenType> progVarMap = new HashMap<>();

	public SemanticAnalyzer(ProgramAST progAST) {
		this.progAST = progAST;
	}

	public void analyze() throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// for our program functions and variables 
		checkNoDuplicateProgLabels();
		
		// This sets the maps that will be needed later when we need to
		// typecheck variable references and function calls in the 
		// program body
		setProgVarAndFuncMaps();
		
		// Perform semantic analysis on the functions
		for (FunctionDecl funcDecl : funcMap.values()) {
			analyzeFuncDecl(funcDecl);
		}
		
		// Perform semantic analysis on the program body
		for (Statement stmt : progAST.getStmts()) {
			stmt.analyze(funcMap, progVarMap);
		}
		
	}

	private void analyzeFuncDecl(FunctionDecl funcDecl) throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// among our function parameters, local variables, and function names
		checkNoDuplicateFuncLabels(funcDecl);
		
		// Get the types of the parameters and local variables
		Map<String, TokenType> varAndParamMap = getVarAndParamMap(funcDecl);

		TokenType returnValueType = TokenType.Void;
		// Perform semantic analysis on the function body
		for (Statement stmt : funcDecl.getStmts()) {
			if (stmt instanceof StatementReturn){
				if (!TokenType.Void.equals(funcDecl.getType())){
					throw new SemanticAnalysisException("Error in returning the type of the declared function.", stmt);
				}
			}
			stmt.analyze(funcMap, varAndParamMap);
		}

	}
	
	
	private Map<String, TokenType> getVarAndParamMap(FunctionDecl funcDecl) {
		Map<String, TokenType> labels = new HashMap<>();
		String label;
		// FIXME: Somewhat similar to setProgVarAndFuncMaps()
		for (Declaration functionParameter : funcDecl.getParameters()) {
			label = functionParameter.getLabel().toString();
			if (functionParameter instanceof VariableDecl) {
				VariableDecl variableDecl = (VariableDecl)functionParameter;
				labels.put(label, variableDecl.getType());
			}
		}

		for (Declaration functionVariable : funcDecl.getVariables()) {
			label = functionVariable.getLabel().toString();
			if (functionVariable instanceof VariableDecl) {
				VariableDecl variableDecl = (VariableDecl)functionVariable;
				labels.put(label, variableDecl.getType());
			}
		}

		return labels;
	}

	private void checkNoDuplicateFuncLabels(FunctionDecl funcDecl) 
									throws SemanticAnalysisException {
		
		// FIXME: Similar to checkNoDuplicateProgLabels()
		Set<String> labels = new HashSet<String>();
		String label;

		for (Declaration functionParameterDecl : funcDecl.getParameters()) {
			label = functionParameterDecl.getLabel().toString();

			if (labels.contains(label) || this.funcMap.containsKey(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in function parameters", functionParameterDecl);
			} else {
				labels.add(label);
			}

		}

		for (Declaration functionVariableDecl : funcDecl.getVariables()) {
			label = functionVariableDecl.getLabel().toString();

			if (labels.contains(label) || this.funcMap.containsKey(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in function variables", functionVariableDecl);
			} else {
				labels.add(label);
			}

		}
	}
	
	private void checkNoDuplicateProgLabels() throws SemanticAnalysisException {
		
		Set<String> labels = new HashSet<String>();
		
 		for (Declaration decl : progAST.getDecls()) {
 			String label = decl.getLabel().toString();
 			
			if (labels.contains(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in program", decl);
			} else {
				labels.add(label);
			}
			
		}
	}
	
	private void setProgVarAndFuncMaps() {
		for (Declaration decl : progAST.getDecls()) {

			String label = decl.getLabel().toString();
			
			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl)decl;
				funcMap.put(label, funcDecl);
				
			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				progVarMap.put(label, varDecl.getType());
			}
		}
	}
}
