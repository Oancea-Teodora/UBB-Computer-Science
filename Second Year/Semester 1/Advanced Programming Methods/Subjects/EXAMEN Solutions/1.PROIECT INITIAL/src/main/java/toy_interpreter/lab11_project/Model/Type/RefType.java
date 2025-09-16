package toy_interpreter.lab11_project.Model.Type;

import toy_interpreter.lab11_project.Model.Value.IValue;
import toy_interpreter.lab11_project.Model.Value.RefValue;

public class RefType implements IType{
    IType inner;

    public RefType(IType inner) {
        this.inner = inner;
    }

    public IType getInner() {
        return this.inner;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof RefType otherRefType) {
            if (otherRefType.getInner() == null) {
                return true;
            }
            else {
                return inner.equals(otherRefType.getInner());
            }
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Ref " + this.inner.toString();
    }

    @Override
    public IValue getDefaultValue() {
        return new RefValue(0, this.inner);
    }

    @Override
    public IType deepCopy() {
        return new RefType(this.inner);
    }
}
