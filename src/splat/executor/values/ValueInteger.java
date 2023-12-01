package splat.executor.values;

import splat.executor.Value;
import splat.parser.elements.TokenType;

public class ValueInteger extends Value {
    private Integer value;

    public ValueInteger(int value) {
        this.value = value;
        this.tokenType = TokenType.Integer;
    }
}
