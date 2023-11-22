package splat.parser.elements;

import splat.lexer.Token;

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
}
