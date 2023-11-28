package splat.parser.elements;

import splat.lexer.Token;

public class VariableDecl extends Declaration {

	// Need to add some fields
	private String label;
	private TokenType type;

	// Need to add extra arguments for setting fields in the constructor 
	public VariableDecl(Token token, String label, TokenType type) {
		super(token);
		this.type = type;
		this.label = label;
	}

	// Getters?
	
	// Fix this as well
	public String toString() {
		return this.label;
	}

	public TokenType getType() {
		return type;
	}

	@Override
	public String getLabel() {
		return this.label;
	}
}
