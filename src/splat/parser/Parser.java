package splat.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.expressions.ArithmeticOperators;
import splat.parser.expressions.BinaryExpression;
import splat.parser.expressions.UnaryExpression;
import splat.parser.nodes.*;
import splat.parser.statements.*;

public class Parser {

	private final List<Token> tokens;

	private Token currentToken;

	private final HashMap<String, TokenType> tokenTypeMapper = new HashMap<>();

	public Parser(List<Token> tokens) {
		this.tokens = tokens;
		this.currentToken = this.tokens.get(0);

		this.tokenTypeMapper.put("Integer", TokenType.Integer);
		this.tokenTypeMapper.put("Boolean", TokenType.Boolean);
		this.tokenTypeMapper.put("String", TokenType.String);
		this.tokenTypeMapper.put("void", TokenType.Void);
	}


	/**
	 * Compares the next token to an expected value, and throws
	 * an exception if they don't match.  This removes the front-most
	 * (next) token
	 *
	 * @param expected value of the next token
	 * @throws ParseException if the actual token doesn't match what
	 *                        was expected
	 */
	private void checkNext(String expected) throws ParseException {

		Token tok = tokens.remove(0);
		if (!this.tokens.isEmpty()) {
			this.currentToken = this.tokens.get(0);
		}

		if (!tok.getValue().equals(expected)) {
			throw new ParseException("Expected '" + expected + "', got '"
					+ tok.getValue() + "'.", tok);
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
		return tokens.get(1).getValue().equals(expected);
	}


	/*
	 *  <program> ::= program <decls> begin <stmts> end ;
	 */
	public ProgramAST parse() throws ParseException {

		try {
			// Needed for 'program' token position info
			Token startTok = tokens.get(0);

			checkNext("program");

			List<Declaration> decls = parseDecls();

			checkNext("begin");

			List<Statement> stmts = parseStmts();

			checkNext("end");
			checkNext(";");

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

		List<Declaration> decls = new ArrayList<>();

		while (!peekNext("begin")) {
			Declaration decl = parseDecl();
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
			Token token = tokens.get(0);
			throw new ParseException("Declaration expected", token);
		}
	}

	/*
	 * <func-decl> ::= <label> ( <params> ) : <ret-type> is
	 * 						<loc-var-decls> begin <stmts> end ;
	 */
	private FunctionDecl parseFuncDecl() throws ParseException {
		// TODO Auto-generated method stub
		if ("IDENTIFIER".equals(this.currentToken.getType()) && this.peekTwoAhead("(")){
			Token functionName = this.currentToken;
			this.eat();
			this.checkNext("(");

			List<Declaration> functionParameters = new ArrayList<>();
			List<Declaration> functionVariables = new ArrayList<>();

			while (!peekNext(")")) {
				Token token = this.currentToken;
				this.eat();
				this.checkNext(":");

				VariableDecl functionParameter = new VariableDecl(
						this.currentToken,
						token.getValue(),
						tokenTypeMapper.get(this.currentToken.getValue())
				);
				this.eat();
				functionParameters.add(functionParameter);
				if (this.peekNext(",")){
					this.checkNext(",");
				}
			}

			this.checkNext(")");
			this.checkNext(":");

			Token functionType = this.currentToken;
			this.eat();
			this.checkNext("is");


			while (!peekNext("begin")) {
				Declaration functionVariable = parseDecl();
				functionVariables.add(functionVariable);
			}


			this.checkNext("begin");

			List<Statement> statements = this.parseStmts();

			checkNext("end");
			checkNext(";");

            return new FunctionDecl(
					functionName,
					functionName.getValue(),
					this.tokenTypeMapper.get(functionType.getValue()),
					functionParameters,
					functionVariables,
					statements
			);
		}
		return null;
	}

	/*
	 * <var-decl> ::= <label> : <type> ;
	 */
	private VariableDecl parseVarDecl() throws ParseException {
		// TODO Auto-generated method stub
		if ("IDENTIFIER".equals(this.currentToken.getType()) && this.peekTwoAhead(":")){
			Token token = this.currentToken;
			this.eat();
			this.checkNext(":");
//			VariableDecl var = new VariableDecl(this.currentToken, token.getValue(), this.currentToken.getValue());
			VariableDecl var = new VariableDecl(this.currentToken, token.getValue(), this.tokenTypeMapper.get(this.currentToken.getValue()));
			this.eat();
			this.checkNext(";");
			return var;
		}
		throw new ParseException("Unexpected variable declaration.", this.currentToken.getLine(), this.currentToken.getColumn());
	}

	/*
	 * <stmts> ::= (  <stmt>  )*
	 */
	private List<Statement> parseStmts() throws ParseException {
		// TODO Auto-generated method stub
		List<Statement> stmts = new ArrayList<>();

		while (!peekNext("end")) {
			Statement statement = this.statement();
			stmts.add(statement);
		}

		return stmts;
	}

	private void eat() {
		this.tokens.remove(0);
		this.currentToken = tokens.get(0);
	}

	private Statement statement() throws ParseException {
		if ("IDENTIFIER".equals(this.currentToken.getType()) && peekTwoAhead(":=")) {
			ASTElement statement_label = new LabelNode(this.currentToken);
			this.eat();
			this.checkNext(":=");
			if ("IDENTIFIER".equals(this.currentToken.getType()) && peekTwoAhead("(")){
				StatementExpression callFunctionWithValue = new StatementExpression(statement_label, this.functionCall());
				this.checkNext(";");
				return callFunctionWithValue;
			} else {
				ASTElement expression = this.expression();
				this.checkNext(";");
				return new StatementExpression(statement_label, expression);
			}
		} else if (this.peekNext("return")) {
			this.eat();
			if (this.peekNext(";")) {
				Statement statementReturn = new StatementReturn(this.currentToken);
				this.checkNext(";");
				return statementReturn;
			} else {
				ASTElement expression = this.expression();
				Statement statementReturnValue = new StatementReturnValue(this.currentToken, expression);
				this.eat();
				return statementReturnValue;
			}
		} else if (this.peekNext("print")) {
			this.checkNext("print");
			StatementPrint statementPrint = new StatementPrint(this.currentToken);
			ASTElement printExpression;

			if ("IDENTIFIER".equals(this.currentToken.getType()) && peekTwoAhead("(")) {
				printExpression = this.functionCall();
			} else {
				printExpression = this.expression();
			}
			this.checkNext(";");
			statementPrint.setPrintValue(printExpression);

			return statementPrint;
		} else if (this.peekNext("print_line")) {
			this.checkNext("print_line");
			this.checkNext(";");
			return new StatementPrint(this.currentToken);
		} else if (this.peekNext("while")) {
			StatementWhile whileDo = new StatementWhile(this.currentToken);
			this.eat();

			ASTElement statementExpression = this.expression();
			whileDo.setExpression(statementExpression);

			this.checkNext("do");

			List<Statement> stmts = new ArrayList<>();
			while (!peekNext("end") && !peekTwoAhead("while")) {
				Statement statement = this.statement();
				stmts.add(statement);
			}
			this.checkNext("end");
			this.checkNext("while");
			this.checkNext(";");
			whileDo.setStatements(stmts);

			return whileDo;
		} else if (this.peekNext("if")) {
			StatementIfElse statementIfElse = new StatementIfElse(this.currentToken);
			this.eat();

			ASTElement statementExpression = this.expression();
			statementIfElse.setExpression(statementExpression);

			this.checkNext("then");

			List<Statement> stmtsThan = new ArrayList<>();
			while (!peekNext("end") && !peekTwoAhead("if") && !peekNext("else")) {
				Statement statementThan = this.statement();
				stmtsThan.add(statementThan);
			}

			if ("else".equals(this.currentToken.getValue())){
				this.checkNext("else");
				List<Statement> stmtsElse = new ArrayList<>();
				while (!peekNext("end") && !peekTwoAhead("if") ) {
					Statement statementElse = this.statement();
					stmtsElse.add(statementElse);
				}
				statementIfElse.setElseStatement(stmtsElse);
			}
			this.checkNext("end");
			this.checkNext("if");
			this.checkNext(";");
			statementIfElse.setThanStatement(stmtsThan);

			return statementIfElse;
		} else if ("IDENTIFIER".equals(this.currentToken.getType()) && peekTwoAhead("(")) {
			StatementFunctionCall calledFunction = this.functionCall();
			this.checkNext(";");
			return calledFunction;
		}
		throw new ParseException("Unexpected statement: >>>" + this.currentToken.getValue() + "<<<",
				this.currentToken.getLine(),
				this.currentToken.getColumn()
		);
	}

	private ASTElement expression() throws ParseException {
//		this.checkNext("(");
		ASTElement result = this.term();
		while (!this.peekNext(";")){
			if ("ADDITIVE_OPERATOR".equals(this.currentToken.getType())){
				if ("+".equals(this.currentToken.getValue())) {
					this.eat();
					result = new BinaryExpression(ArithmeticOperators.Addition, result, this.term());
				} else if ("-".equals(this.currentToken.getValue())) {
					this.eat();
					result = new BinaryExpression(ArithmeticOperators.Subtraction, result, this.term());
				}
			} else if ("MODULUS_OPERATOR".equals(this.currentToken.getType())) {
				if ("%".equals(this.currentToken.getValue())) {
					this.eat();
					result = new BinaryExpression(ArithmeticOperators.Modulus, result, this.term());
				}
			} else if ("RELATIONAL_OPERATOR".equals(this.currentToken.getType())) {
				if ("<".equals(this.currentToken.getValue())){
					this.eat();
					result = new BinaryExpression(ArithmeticOperators.LessThan, result, this.term());
				} else if (">".equals(this.currentToken.getValue())) {
					this.eat();
					result = new BinaryExpression(ArithmeticOperators.GreaterThan, result, this.term());
				} else if ("<=".equals(this.currentToken.getValue())) {
					this.eat();
					result = new BinaryExpression(ArithmeticOperators.LessThanOrEqualTo, result, this.term());
				} else if (">=".equals(this.currentToken.getValue())) {
					this.eat();
					result = new BinaryExpression(ArithmeticOperators.GreaterThanOrEqualTo, result, this.term());
				}

			} else if ("EQUALITY_OPERATOR".equals(this.currentToken.getType())) {
				if ("==".equals(this.currentToken.getValue())){
					this.eat();
					result = new BinaryExpression(ArithmeticOperators.EqualTo, result, this.term());
				} else if ("!=".equals(this.currentToken.getValue())) {
					this.eat();
					result = new BinaryExpression(ArithmeticOperators.NotEqualTo, result, this.term());
				}
			} else if ("LOGICAL_OPERATORS".equals(this.currentToken.getType())) {
				if ("and".equals(this.currentToken.getValue())){
					this.eat();
					result = new BinaryExpression(ArithmeticOperators.LogicalAnd, result, this.term());
				} else if ("or".equals(this.currentToken.getValue())) {
					this.eat();
					result = new BinaryExpression(ArithmeticOperators.LogicalOr, result, this.term());
				}
			} else if ("IDENTIFIER".equals(this.currentToken.getType())) {
				result = new VariableNode(this.currentToken);
				eat();
			} else {
				break;
			}
		}
		return result;
	}

	private ASTElement term() throws ParseException {
		ASTElement result = this.factor();
		while (!this.peekNext(";") && "MULTIPLICATIVE_OPERATOR".equals(this.currentToken.getType())){
			if ("*".equals(this.currentToken.getValue())) {
				this.checkNext("*");
				result = new BinaryExpression(ArithmeticOperators.Multiplication, result, this.factor());
			} else if ("/".equals(this.currentToken.getValue())) {
				this.checkNext("/");
				result = new BinaryExpression(ArithmeticOperators.Division, result, this.factor());
			}
		}
		return result;
	}


	private ASTElement factor() throws ParseException {
		Token token = this.currentToken;
		if ("LeftParen".equals(this.currentToken.getType())) {
			this.checkNext("(");
			ASTElement expression = this.expression();
			this.checkNext(")");
			return expression;
		} else if ("IDENTIFIER".equals(this.currentToken.getType()) && peekTwoAhead("(")) {
			return this.functionCall();

		} else if ("NUMBER".equals(this.currentToken.getType())) {
			this.eat();
			return new NumberNode(token);
		} else if ("IDENTIFIER".equals(this.currentToken.getType())){
			this.eat();
			if ("true".equals(token.getValue()) || "false".equals(token.getValue())){
				return new BooleanNode(token);
			} else if ("not".equals(token.getValue())) {
				return new UnaryExpression(ArithmeticOperators.LogicalNOT, this.factor());
			} else {
				return new VariableNode(token);
			}
		} else if ("+".equals(this.currentToken.getValue())) {
			this.checkNext("+");
			return new PlusNode(this.factor());
		} else if ("-".equals(this.currentToken.getValue())) {
			this.checkNext("-");
			return new UnaryExpression(ArithmeticOperators.UnaryMinus, this.factor());
		} else if ("STRING".equals(this.currentToken.getType())) {
			this.eat();
			return new StringNode(token);
		}
		throw new ParseException("Unexpected factor in expression: >>>" + this.currentToken.getValue() + "<<<",
				this.currentToken.getLine(),
				this.currentToken.getColumn()
		);
	}

	private StatementFunctionCall functionCall() throws ParseException {
		StatementFunctionCall procedureCall = new StatementFunctionCall(this.currentToken);
		if ("IDENTIFIER".equals(this.currentToken.getType()) && peekTwoAhead("(")) {

			LabelNode functionName = new LabelNode(this.currentToken);
			procedureCall.setFunctionName(functionName);
			this.eat();
			this.checkNext("(");

			while (!peekNext(")")) {
				ASTElement argumentNode = this.expression();
				procedureCall.addArgument(argumentNode);
				if (peekNext(",")) {
					this.checkNext(",");
				}
			}
			this.checkNext(")");
		}
		return procedureCall;
	}


}
