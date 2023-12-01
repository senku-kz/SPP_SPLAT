package splat.executor.values;

import splat.executor.Value;
import splat.parser.elements.TokenType;

public class ValueBoolean extends Value {
    private Boolean value;

    public ValueBoolean(Boolean value) {
        this.value = value;
        this.tokenType = TokenType.Boolean;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
