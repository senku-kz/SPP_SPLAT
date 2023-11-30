package splat.parser.expressions;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.TokenType;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class UnaryExpression extends Expression {
    private ArithmeticOperators operator;
    private ASTElement node;

    public UnaryExpression(ArithmeticOperators operator, ASTElement node) {
        super(new Token("UNARY_EXPRESSION", "", node.getLine(), node.getColumn()));
        this.operator = operator;
        this.node = node;
    }

    @Override
    public TokenType analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, TokenType> varAndParamMap) throws SemanticAnalysisException {
        return null;
    }
}
