package toy_interpreter.lab11_project.Model.Expression;

import toy_interpreter.lab11_project.Model.ADT.IDictionary;
import toy_interpreter.lab11_project.Model.ADT.IHeapTable;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.Type.BoolType;
import toy_interpreter.lab11_project.Model.Type.IType;
import toy_interpreter.lab11_project.Model.Value.BoolValue;
import toy_interpreter.lab11_project.Model.Value.IValue;

public class LogicExpression implements IExpression{
    private IExpression exp1;
    private IExpression exp2;
    private String operation;

    public LogicExpression(IExpression e1, IExpression e2, String op) {
        this.exp1 = e1;
        this.exp2 = e2;
        this.operation = op;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symbolTable, IHeapTable<IValue> heapTable) throws MyException {
        IValue val1, val2;
        val1 = exp1.eval(symbolTable, heapTable);
        if (val1.getType().equals(new BoolType())) {
            val2 = exp2.eval(symbolTable, heapTable);
            if (val2.getType().equals(new BoolType())) {
                BoolValue boolVal1 = (BoolValue) val1;
                BoolValue boolVal2 = (BoolValue) val2;
                boolean b1, b2;
                b1 = boolVal1.getValue();
                b2 = boolVal2.getValue();
                return switch (this.operation) {
                    case "&&" -> new BoolValue(b1 && b2);
                    case "||" -> new BoolValue(b1 || b2);
                    case "^" -> new BoolValue(b1 ^ b2);
                    default -> throw new MyException("Invalid logical operation given.");
                };
            } else {
                throw new MyException("Second operand is not a bool.");
            }
        } else {
            throw new MyException("First operand is not a bool.");
        }
    }

    @Override
    public IExpression deepCopy() {
        return new LogicExpression(this.exp1.deepCopy(), this.exp2.deepCopy(), this.operation);
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType type1, type2;
        type1 = this.exp1.typeCheck(typeEnv);
        type2 = this.exp2.typeCheck(typeEnv);
        if (type1.equals(new BoolType())) {
            if (type2.equals(new BoolType())) {
                return new BoolType();
            } else {
                throw new MyException("TYPE CHECK ERROR: Second operand is not a boolean.");
            }
        }
        else {
            throw new MyException("TYPE CHECK ERROR: First operand is not a boolean.");
        }
    }

    @Override
    public String toString() {
        return this.exp1.toString() + " " + this.operation + " " + this.exp2.toString();
    }
}
