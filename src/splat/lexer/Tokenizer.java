package splat.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private int lineNumber;
    private String string;
    private int cursor;

    public void init(int lineNumber, String input) {
        this.lineNumber = lineNumber;
        this.string = input;
        this.cursor = 0;
    }

    public boolean isEOF() {
        return cursor == string.length();
    }

    public boolean hasMoreTokens() {
        return cursor < string.length();
    }

    public Token getNextToken() throws LexException {
        if (!hasMoreTokens()) {
            return null;
        }

        String remainingString = string.substring(cursor);

        List<String[]> spec = new ArrayList<>();
        spec.add(new String[]{"^\\s+", null}); // Whitespace
        spec.add(new String[]{"^\\/\\/.*", null}); // Single-line comments
        spec.add(new String[]{"^\\/\\*[\\s\\S]*?\\*\\/", null}); // Multi-line comments
        spec.add(new String[]{"^;", ";"}); // Semicolon
        spec.add(new String[]{"^\\{", "{"}); // LeftBrace
        spec.add(new String[]{"^\\}", "}"}); // RightBrace
        spec.add(new String[]{"^\\(", "("}); // LeftParen
        spec.add(new String[]{"^\\)", ")"}); // RightParen
        spec.add(new String[]{"^\\[", "["}); // LeftBracket
        spec.add(new String[]{"^\\]", "]"}); // RightBracket
        spec.add(new String[]{"^,", ","}); // Comma
        spec.add(new String[]{"^\\.", "."}); // Dot
        spec.add(new String[]{"^[<>]=?", "RELATIONAL_OPERATOR"}); // Relational Operators
        spec.add(new String[]{"^[=!]=", "EQUALITY_OPERATOR"}); // Equality Operators
        spec.add(new String[]{"^&&", "LOGICAL_AND"}); // Logical AND
        spec.add(new String[]{"^\\|\\|", "LOGICAL_OR"}); // Logical OR
        spec.add(new String[]{"^!", "LOGICAL_NOT"}); // Logical NOT
        spec.add(new String[]{"^\\blet\\b", "let"}); // Keywords
        spec.add(new String[]{"^\\bif\\b", "if"});
        spec.add(new String[]{"^\\belse\\b", "else"});
        spec.add(new String[]{"^\\btrue\\b", "true"});
        spec.add(new String[]{"^\\bfalse\\b", "false"});
        spec.add(new String[]{"^\\bnull\\b", "null"});
        spec.add(new String[]{"^\\bclass\\b", "class"}); // OOP Keywords
        spec.add(new String[]{"^\\bthis\\b", "this"});
        spec.add(new String[]{"^\\bextends\\b", "extends"});
        spec.add(new String[]{"^\\bsuper\\b", "super"});
        spec.add(new String[]{"^\\bnew\\b", "new"});
        spec.add(new String[]{"^\\bwhile\\b", "while"}); // Iterators
        spec.add(new String[]{"^\\bdo\\b", "do"});
        spec.add(new String[]{"^\\bfor\\b", "for"});
        spec.add(new String[]{"^\\bdef\\b", "def"});
        spec.add(new String[]{"^\\breturn\\b", "return"});
        spec.add(new String[]{"^:=", "SIMPLE_ASSIGN"}); // Assignment operators
        spec.add(new String[]{"^[*\\/+\\-]=?", "COMPLEX_ASSIGN"});
        spec.add(new String[]{"^[+\\-]", "ADDITIVE_OPERATOR"}); // Math operators
        spec.add(new String[]{"^[*\\/]","MULTIPLICATIVE_OPERATOR"});
        spec.add(new String[]{"^[*\\%]","MODULUS_OPERATOR"});
        spec.add(new String[]{"^\\d+", "NUMBER"}); // Numbers
        spec.add(new String[]{"^\"[^\"]*\"", "STRING"}); // Double-quoted String
        spec.add(new String[]{"^'[^']*'", "STRING"}); // Single-quoted String
        spec.add(new String[]{"^\\w+", "IDENTIFIER"}); // Identifier
        spec.add(new String[]{"^:", "COLON"}); // Identifier

        for (String[] entry : spec) {
            String regex = entry[0];
            String tokenType = entry[1];
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(remainingString);
            if (matcher.find()) {
                if (tokenType == null) {
                    this.cursor += matcher.group().length();
                    return getNextToken();
                }
                String tokenValue = matcher.group();
                this.cursor += tokenValue.length();
                return new Token(tokenType, tokenValue, this.lineNumber, this.cursor);
            }
        }

//        throw new SyntaxError("Unexpected token: \"" + remainingString.charAt(0) + "\"");
        throw new LexException("Invalid character: " + "\"" + remainingString.charAt(0) + "\"", this.lineNumber, this.cursor);
    }
}
