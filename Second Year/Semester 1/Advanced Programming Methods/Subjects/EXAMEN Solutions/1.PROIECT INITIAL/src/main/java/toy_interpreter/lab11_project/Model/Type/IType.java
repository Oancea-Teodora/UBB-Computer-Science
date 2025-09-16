package toy_interpreter.lab11_project.Model.Type;

import toy_interpreter.lab11_project.Model.Value.IValue;

public interface IType {
    IValue getDefaultValue();
    IType deepCopy();
}
