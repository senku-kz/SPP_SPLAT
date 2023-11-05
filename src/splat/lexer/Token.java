package splat.lexer;

public class Token {
    private final String type;
    private final String value;
    private final int line;
    private final int column;

    public Token(String type, String val, int line, int column) {
        this.type = type;
        this.value = val;
        this.line = line;
        this.column = column;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

//    @Override
//    public String toString() {
//        return this.val + " is on line " + this.line + " and column " + this.column;
//    }

    @Override
    public String toString() {
        return "Token{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
