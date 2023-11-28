package splat.parser.elements;

import splat.lexer.Token;

public class VariableDecl extends Declaration {

	// Need to add some fields
	private String name;
	private TokenType type;

	// Need to add extra arguments for setting fields in the constructor 
	public VariableDecl(Token token, String name, TokenType type) {
		super(token);
		this.name = name;
		this.type = type;
	}

	// Getters?
	
	// Fix this as well
	public String toString() {
		return null;
	}

	public String getName() {
		return name;
	}

	public TokenType getType() {
		return type;
	}
}
