package toy_interpreter.lab11_project.Model.Type;

import toy_interpreter.lab11_project.Model.Value.IValue;
import toy_interpreter.lab11_project.Model.Value.StringValue;

public class StringType implements IType{
    public StringType() {}

    @Override
    public IValue getDefaultValue() {
        return new StringValue("");
    }

    @Override
    public IType deepCopy() {
        return new StringType();
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof StringType;
    }
}
