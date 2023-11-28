package splat.parser.elements;

import splat.lexer.Token;

public abstract class Declaration extends ASTElement {

	public Declaration(Token token) {
		super(token);
	}

	public String  getLabel() {
		return null;
	}
}
