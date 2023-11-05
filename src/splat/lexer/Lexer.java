package splat.lexer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
	private final File progFile;
	private String string;
	private int lineNum;
	private int cursor;

	private Tokenizer tokenizer = new Tokenizer();
	public Lexer(File progFile) {
		// TODO Auto-generated constructor stub
		this.progFile = progFile;
		this.lineNum = 1;
	}

	public List<Token> tokenize() throws LexException, IOException {
		// TODO Auto-generated method stub

		BufferedReader br = new BufferedReader(new FileReader(this.progFile));
		String readLine = null;

		List<Token> tokens = new ArrayList<Token>();
		while ((readLine = br.readLine()) != null) {
			tokenizer.init(this.lineNum, readLine);
			Token token = tokenizer.getNextToken();
			while (token != null) {
//				System.out.println(token.getType() + ": " + token.getValue());
				tokens.add(token);
				token = tokenizer.getNextToken();
			}
			this.lineNum++;
		}

		return tokens;
	}

}