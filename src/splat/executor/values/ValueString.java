package splat.executor.values;

import splat.executor.Value;
import splat.parser.elements.TokenType;

public class ValueString extends Value {
    private String value;

    public ValueString(String value) {
        this.value = value;
        this.tokenType = TokenType.String;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
