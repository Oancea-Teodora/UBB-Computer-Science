package toy_interpreter.lab11_project.Model.Value;

import toy_interpreter.lab11_project.Model.Type.IType;

public interface IValue {
    IType getType();
    IValue deepCopy();
}
