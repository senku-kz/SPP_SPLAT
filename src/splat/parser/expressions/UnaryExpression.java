package splat.parser.expressions;

import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.Expression;
import splat.parser.elements.FunctionDecl;
import splat.parser.elements.TokenType;
import splat.parser.nodes.BooleanNode;
import splat.parser.nodes.NumberNode;
import splat.parser.nodes.StringNode;
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
        if (ArithmeticOperators.UnaryMinus.equals(this.operator)){
            if (this.node instanceof BooleanNode){
                throw new SemanticAnalysisException("Unexpected operator in Unary Expression.", this.node);
            } else if (this.node instanceof StringNode) {
                throw new SemanticAnalysisException("Unexpected operator in Unary Expression.", this.node);
            }
        } else if (ArithmeticOperators.LogicalNOT.equals(this.operator)) {
            if (this.node instanceof NumberNode){
                throw new SemanticAnalysisException("Unexpected operator in Unary Expression.", this.node);
            } else if (this.node instanceof StringNode) {
                throw new SemanticAnalysisException("Unexpected operator in Unary Expression.", this.node);
            }
        } else {
            throw new SemanticAnalysisException("Unexpected operator in Unary Expression.", this.node);

        }

        return this.getType(this.node, funcMap, varAndParamMap);
    }

    @Override
    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, Value> varAndParamMap) {
        return null;
    }

}
