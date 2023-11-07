package splat.parser;

import java.util.ArrayList;
import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.*;

public class Parser {

	private List<Token> tokens;
	private Token lookahead;

	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Compares the next token to an expected value, and throws
	 * an exception if they don't match.  This removes the front-most
	 * (next) token  
	 * 
	 * @param expected value of the next token
	 * @throws ParseException if the actual token doesn't match what 
	 * 			was expected
	 */
	private void checkNext(String expected) throws ParseException {

		Token tok = tokens.remove(0);
		
		if (!tok.getValue().equals(expected)) {
			throw new ParseException("Expected '"+ expected + "', got '" 
					+ tok.getValue()+ "'.", tok);
		}
	}
	
	/**
	 * Returns a boolean indicating whether or not the next token matches
	 * the expected String value.  This does not remove the token from the
	 * token list.
	 * 
	 * @param expected value of the next token
	 * @return true iff the token value matches the expected string
	 */
	private boolean peekNext(String expected) {
		return tokens.get(0).getValue().equals(expected);
	}
	
	/**
	 * Returns a boolean indicating whether or not the token directly after
	 * the front most token matches the expected String value.  This does 
	 * not remove any tokens from the token list.
	 * 
	 * @param expected value of the token directly after the next token
	 * @return true iff the value matches the expected string
	 */
	private boolean peekTwoAhead(String expected) {
		return this.tokens.get(1).getValue().equals(expected);
	}
	
	
	/*
	 *  <program> ::= program <decls> begin <stmts> end ;
	 */
	public ProgramAST parse() throws ParseException {
		
		try {
			// Needed for 'program' token position info
			Token startTok = tokens.get(0);
			
			this.checkNext("program");
			
			List<Declaration> decls = this.parseDecls();
			
			this.checkNext("begin");
			
			List<Statement> stmts = parseStmts();
			
			this.checkNext("end");
			this.checkNext(";");
	
			return new ProgramAST(decls, stmts, startTok);
			
		// This might happen if we do a tokens.get(), and nothing is there!
		} catch (IndexOutOfBoundsException ex) {
			throw new ParseException("Unexpectedly reached the end of file.", -1, -1);
		}
	}
	
	/*
	 *  <decls> ::= (  <decl>  )*
	 */
	private List<Declaration> parseDecls() throws ParseException {
		
		List<Declaration> decls = new ArrayList<Declaration>();
		
		while (!this.peekNext("begin")) {
			Declaration decl = this.parseDecl();
			decls.add(decl);
		}
		
		return decls;
	}
	
	/*
	 * <decl> ::= <var-decl> | <func-decl>
	 */
	private Declaration parseDecl() throws ParseException {

		if (peekTwoAhead(":")) {
			return parseVarDecl();
		} else if (peekTwoAhead("(")) {
			return parseFuncDecl();
		} else {
			Token tok = tokens.get(0);
			throw new ParseException("Declaration expected", tok);
		}
	}
	
	/*
	 * <func-decl> ::= <label> ( <params> ) : <ret-type> is 
	 * 						<loc-var-decls> begin <stmts> end ;
	 */
	private FunctionDecl parseFuncDecl() throws ParseException {
		// TODO Auto-generated method stub
//		this.popTokenList();
		return null;
	}

	/*
	 * <var-decl> ::= <label> : <type> ;
	 */
	private VariableDecl parseVarDecl() throws ParseException {
		// TODO Auto-generated method stub
//		this.popTokenList();
		return null;
	}
	
	/*
	 * <stmts> ::= (  <stmt>  )*
	 */
	private List<Statement> parseStmts() throws ParseException {
		// TODO Auto-generated method stub
		List<Statement> statementList = new ArrayList<>();
		this.lookahead = this.tokens.get(0);
		String stopLookAhead = "end";
		while (this.lookahead != null && !this.lookahead.getType().equals(stopLookAhead)){
			statementList.add(this.statement());
//			this.peekNext(stopLookAhead);
		}
		return statementList;
	}


	/*Experimental block*/
	private Statement statement(){
//        return switch (this.lookahead.getType()) {
//            case "{" -> BlockStatement();
//            case ";" -> EmptyStatement();
//            case ":" -> VariableStatement();
//            case "if" -> IfStatement();
//            // Add cases for other statement types here
//            default -> ExpressionStatement();
//        };
		switch (this.lookahead.getType()) {
			case "{":
				return BlockStatement();
			case ";":
				return EmptyStatement();
			case "let":
				return VariableStatement();
			case "if":
				return IfStatement();
			// Add cases for other statement types here
			default:
				return ExpressionStatement();
		}
	}

	private Statement BlockStatement(){
		return null;
	}
	private Statement EmptyStatement(){
		return null;
	}
	private Statement VariableStatement(){
		return null;
	}
	private Statement IfStatement(){
		return null;
	}
	private Statement ExpressionStatement(){
		return null;
	}

//	private Token popTokenList(){
//		return this.tokens.remove(0);
//	}

	// Helper methods
//	private void eat(String tokenType) {
//		Token lookahead = this.tokens.get(0);
//		if (lookahead == null) {
//			throw new RuntimeException("Unexpected end of input, expected: " + tokenType);
//		}
//
//		if (!lookahead.getType().equals(tokenType)) {
//			throw new RuntimeException("Unexpected token: " + lookahead.getValue() + ", expected: " + tokenType);
//		}
//		this.popTokenList();
//	}

}
