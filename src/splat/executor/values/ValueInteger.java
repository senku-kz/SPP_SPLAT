package splat.executor.values;

import splat.executor.Value;
import splat.parser.elements.TokenType;

public class ValueInteger extends Value {
    private Integer value;

    public ValueInteger(Integer value) {
        this.value = value;
        this.tokenType = TokenType.Integer;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
