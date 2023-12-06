package splat.executor.values;

import splat.executor.Value;
import splat.parser.elements.TokenType;

public class ValueVoid extends Value {
    public ValueVoid() {
        this.tokenType = TokenType.Void;
    }
}
