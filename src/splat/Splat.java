package splat;

import java.io.File;
import java.io.IOException;
import java.util.List;

import splat.lexer.Lexer;
import splat.lexer.Token;
import splat.parser.Parser;
import splat.parser.elements.ProgramAST;

public class Splat {

	private final File progFile;
	
	public Splat(File progFile) {
		this.progFile = progFile;
	}
	
	public void processFileAndExecute() throws SplatException, IOException {
		
		// Step 1.  Tokenize
		Lexer lexer = new Lexer(progFile);
		List<Token> tokens = lexer.tokenize();
		
		// Step 2.  Parse
		 Parser parser = new Parser(tokens);
//		 ProgramAST progAST = parser.parse();
		
		// Step 3.  Semantic Analysis
		// SemanticAnalyzer analyzer = new SemanticAnalyzer(progAST);
		// analyzer.analyze();
		
		// Step 4.  Executor
		// Executor executor = new Executor(progAST);
		// executor.runProgram();
		
		// THE END!
	}

}
