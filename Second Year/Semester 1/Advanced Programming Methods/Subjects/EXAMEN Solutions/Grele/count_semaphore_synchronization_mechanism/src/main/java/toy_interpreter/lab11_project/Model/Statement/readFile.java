package toy_interpreter.lab11_project.Model.Statement;

import toy_interpreter.lab11_project.Model.ADT.IDictionary;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.Expression.IExpression;
import toy_interpreter.lab11_project.Model.ProgramState.ProgramState;
import toy_interpreter.lab11_project.Model.Type.IType;
import toy_interpreter.lab11_project.Model.Type.IntType;
import toy_interpreter.lab11_project.Model.Type.StringType;
import toy_interpreter.lab11_project.Model.Value.IValue;
import toy_interpreter.lab11_project.Model.Value.IntValue;
import toy_interpreter.lab11_project.Model.Value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class readFile implements IStatement{
    private String id;
    private IExpression expression;

    public readFile(IExpression exp, String varName) {
        this.expression = exp;
        this.id = varName;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        if (currentState.getSymbolTable().isDefined(this.id)) {
            IValue variableValue =  currentState.getSymbolTable().lookUp(this.id);
            if (variableValue.getType().equals(new IntType())) {
                IValue fileName = this.expression.eval(currentState.getSymbolTable(), currentState.getHeapTable());
                if (fileName.getType().equals(new StringType())) {
                    StringValue stringFileName = (StringValue) fileName;
                    BufferedReader fileDescriptor = currentState.getFileTable().lookUp(stringFileName);
                    try {
                        String line = fileDescriptor.readLine();
                        if (line != null) {
                            currentState.getSymbolTable().addKeyValuePair(this.id, new IntValue(Integer.parseInt(line)));
                        }
                        else {
                            currentState.getSymbolTable().addKeyValuePair(this.id, new IntType().getDefaultValue());
                        }
                    } catch (IOException e) {
                        throw new MyException("Failed to read line from the given file.");
                    }
                }
                else {
                    throw new MyException("The type of the given value (" + fileName + ") is not a String.");
                }
            }
            else {
                throw new MyException("The type of the given variable (" + this.id + ") is not Int.");
            }
        }
        else {
            throw new MyException("The given variable (" + this.id + ") is not defined in the symbol table.");
        }

        return null;
    }

    @Override
    public String toString() {
        return "readFile " + this.expression.toString() + " into variable " + this.id;
    }

    @Override
    public IStatement deepCopy() {
        return new readFile(this.expression, this.id);
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = this.expression.typeCheck(typeEnv);
        IType typeVar = typeEnv.lookUp(this.id);
        if (typeExp.equals(new StringType())) {
            if (typeVar.equals(new IntType())) {
                return typeEnv;
            } else {
                throw new MyException("TYPE CHECK ERROR: The given variable is not of type Int.");
            }
        } else {
            throw new MyException("TYPE CHECK ERROR: The given file name is not of type String.");
        }
    }
}
