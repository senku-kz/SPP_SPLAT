package splat.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private int lineNumber;
    private String string;
    private int cursor;
    private final List<String[]> spec = new ArrayList<>();

    public void init(int lineNumber, String input) {
        this.lineNumber = lineNumber;
        this.string = input;
        this.cursor = 0;

        // --------------------------------------
        this.spec.add(new String[]{"^\\s+", null}); // Whitespace

        this.spec.add(new String[]{"^\\/\\/.*", null}); // Single-line comments
        this.spec.add(new String[]{"^\\/\\*[\\s\\S]*?\\*\\/", null}); // Multi-line comments
        // --------------------------------------
        // Symbols and delimiters:
        this.spec.add(new String[]{"^;", ";"}); // Semicolon
        this.spec.add(new String[]{"^\\{", "{"}); // LeftBrace
        this.spec.add(new String[]{"^\\}", "}"}); // RightBrace
        this.spec.add(new String[]{"^\\(", "LeftParen"}); // LeftParen
        this.spec.add(new String[]{"^\\)", "RightParen"}); // RightParen
        this.spec.add(new String[]{"^\\[", "["}); // LeftBracket
        this.spec.add(new String[]{"^\\]", "]"}); // RightBracket
        this.spec.add(new String[]{"^,", ","}); // Comma
        this.spec.add(new String[]{"^\\.", "."}); // Dot

        // Relational Operators
        // <, >, <=, >=
        this.spec.add(new String[]{"^[<>]=?", "RELATIONAL_OPERATOR"}); // Relational Operators
        this.spec.add(new String[]{"^[=!]=", "EQUALITY_OPERATOR"}); // Equality Operators

        // Logical Operators
        // ||, &&, !
        this.spec.add(new String[]{"^&&", "LOGICAL_AND"}); // Logical AND
        this.spec.add(new String[]{"^\\|\\|", "LOGICAL_OR"}); // Logical OR
//        this.spec.add(new String[]{"^!", "LOGICAL_NOT"}); // Logical NOT

        // --------------------------------------
        // Keywords
        this.spec.add(new String[]{"^\\blet\\b", "let"});
        this.spec.add(new String[]{"^\\bif\\b", "if"});
        this.spec.add(new String[]{"^\\belse\\b", "else"});
//        this.spec.add(new String[]{"^\\btrue\\b", "true"});
//        this.spec.add(new String[]{"^\\bfalse\\b", "false"});
        this.spec.add(new String[]{"^\\bnull\\b", "null"});

        // --------------------------------------
        // OOP keywords
        this.spec.add(new String[]{"^\\bclass\\b", "class"});
        this.spec.add(new String[]{"^\\bthis\\b", "this"});
        this.spec.add(new String[]{"^\\bextends\\b", "extends"});
        this.spec.add(new String[]{"^\\bsuper\\b", "super"});
        this.spec.add(new String[]{"^\\bnew\\b", "new"});

        // --------------------------------------
        // Iterators
        this.spec.add(new String[]{"^\\bwhile\\b", "while"});
        this.spec.add(new String[]{"^\\bdo\\b", "do"});
        this.spec.add(new String[]{"^\\bfor\\b", "for"});
        this.spec.add(new String[]{"^\\bdef\\b", "def"});
        this.spec.add(new String[]{"^\\breturn\\b", "return"});

        // --------------------------------------
        // Math operators: +, -, *, /
        this.spec.add(new String[]{"^[+\\-]", "ADDITIVE_OPERATOR"});
        this.spec.add(new String[]{"^[*\\/]", "MULTIPLICATIVE_OPERATOR"});
        this.spec.add(new String[]{"^[*\\%]", "MODULUS_OPERATOR"});

        // --------------------------------------
        // Assignment operators: :=, *=, /=, +=, -=
        this.spec.add(new String[]{"^:=", "SIMPLE_ASSIGN"});
        this.spec.add(new String[]{"^[*\\/+\\-]=?", "COMPLEX_ASSIGN"});

        this.spec.add(new String[]{"^\\d+", "NUMBER"}); // Numbers
        this.spec.add(new String[]{"^\"[^\"]*\"", "STRING"}); // Double-quoted String
        this.spec.add(new String[]{"^'[^']*'", "STRING"}); // Single-quoted String
        this.spec.add(new String[]{"^\\w+", "IDENTIFIER"}); // Identifier

        this.spec.add(new String[]{"^:", "COLON"}); // Identifier

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

        for (String[] entry : this.spec) {
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

        throw new LexException("Invalid character: " + "\"" + remainingString.charAt(0) + "\"", this.lineNumber, this.cursor);
    }
}
