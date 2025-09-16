package toy_interpreter.lab11_project.Model.Expression;

import toy_interpreter.lab11_project.Model.ADT.IDictionary;
import toy_interpreter.lab11_project.Model.ADT.IHeapTable;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.Type.IType;
import toy_interpreter.lab11_project.Model.Type.RefType;
import toy_interpreter.lab11_project.Model.Value.IValue;
import toy_interpreter.lab11_project.Model.Value.RefValue;

public class readFromHeap implements IExpression{
    private IExpression expression;

    public readFromHeap(IExpression exp) {
        this.expression = exp;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symbolTable, IHeapTable<IValue> heapTable) throws MyException {
        IValue expressionValue = this.expression.eval(symbolTable, heapTable);
        if (!(expressionValue.getType().equals(new RefType(null)))) {
            throw new MyException("ERROR: The given expression(" + this.expression.toString() + ") does not evaluate to a RefValue.");
        }
        RefValue refValue = (RefValue) expressionValue;
        int address = refValue.getAddress();
        if (!heapTable.isDefined(address)) {
            throw new MyException("ERROR: The address of the given RefValue(" + this.expression.toString() + ") is not available in the heap.");
        }
        return heapTable.getHeapValue(address);
    }

    @Override
    public IExpression deepCopy() {
        return new readFromHeap(this.expression.deepCopy());
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType type = this.expression.typeCheck(typeEnv);
        if (type instanceof RefType) {
            RefType refType = (RefType) type;
            return refType.getInner();
        } else {
            throw new MyException("TYPE CHECK ERROR: The readHeap argument is not a Ref Type.");
        }
    }

    @Override
    public String toString() {
        return "readFromHeap(" + this.expression.toString() + ")";
    }
}
