package splat.semanticanalyzer;

import splat.SplatException;
import splat.parser.elements.ASTElement;

public class SemanticReturnAnalysisException extends SplatException {

	public SemanticReturnAnalysisException(String msg, ASTElement elem) {
		super(msg, elem.getLine(), elem.getColumn());
	}

	public SemanticReturnAnalysisException(String msg, int line, int column) {
		super(msg, line, column);
	}
}
