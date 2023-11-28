package splat.parser.elements;

import splat.lexer.Token;

import java.util.List;

public class FunctionDecl extends Declaration {
	// Need to add some fields
	private final String name;

	private final String type;

	private final List<Declaration> parameters;

	private final List<Declaration> variables;

	private final List<Statement> statements;

	// Need to add extra arguments for setting fields in the constructor
	public FunctionDecl(Token token,
						String name,
						String type,
						List<Declaration> parameters,
						List<Declaration> variables,
						List<Statement> statements
	) {
		super(token);
		this.name = name;
		this.type = type;
		this.parameters = parameters;
		this.variables = variables;
		this.statements = statements;
	}

	// Getters?
	
	// Fix this as well
	public String toString() {
		return this.name;
	}

	public List<Statement> getStmts() {
		return this.statements;
	}

	public String  getLabel(){
		return name;
	}

	public List<Declaration> getParameters(){
		return this.parameters;
	}

	public List<Declaration> getVariables() {
		return variables;
	}
}
