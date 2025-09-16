package toy_interpreter.lab11_project.Model.Expression;

import toy_interpreter.lab11_project.Model.ADT.IDictionary;
import toy_interpreter.lab11_project.Model.ADT.IHeapTable;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.Type.IType;
import toy_interpreter.lab11_project.Model.Value.IValue;

public interface IExpression {
    IValue eval(IDictionary<String, IValue> symbolTable, IHeapTable<IValue> heapTable) throws MyException;
    IExpression deepCopy();
    IType typeCheck(IDictionary<String, IType> typeEnv) throws MyException;
}
