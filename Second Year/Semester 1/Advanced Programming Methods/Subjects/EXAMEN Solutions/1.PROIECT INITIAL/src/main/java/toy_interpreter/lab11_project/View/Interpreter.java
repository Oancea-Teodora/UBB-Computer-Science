package toy_interpreter.lab11_project.View;

import toy_interpreter.lab11_project.Controller.Controller;
import toy_interpreter.lab11_project.Model.ADT.*;
import toy_interpreter.lab11_project.Model.Expression.*;
import toy_interpreter.lab11_project.Model.ProgramState.ProgramState;
import toy_interpreter.lab11_project.Model.Statement.*;
import toy_interpreter.lab11_project.Model.Type.BoolType;
import toy_interpreter.lab11_project.Model.Type.IntType;
import toy_interpreter.lab11_project.Model.Type.RefType;
import toy_interpreter.lab11_project.Model.Type.StringType;
import toy_interpreter.lab11_project.Model.Value.BoolValue;
import toy_interpreter.lab11_project.Model.Value.IntValue;
import toy_interpreter.lab11_project.Model.Value.StringValue;
import toy_interpreter.lab11_project.Repository.IRepository;
import toy_interpreter.lab11_project.Repository.Repository;

public class Interpreter {
    private static IStatement getEx1() {
        return new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
               new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
               new PrintStatement(new VariableExpression("v"))));
    }

    private static IStatement getEx2() {
        return new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
               new CompoundStatement(new VariableDeclarationStatement("b", new IntType()),
               new CompoundStatement(new AssignStatement("a", new ArithmeticExpression(new ValueExpression(new IntValue(2)),
                    new ArithmeticExpression(new ValueExpression(new IntValue(3)),
                    new ValueExpression(new IntValue(5)), "*"), "+")),
                    new CompoundStatement(new AssignStatement("b",
                        new ArithmeticExpression(new VariableExpression("a"),
                        new ValueExpression(new IntValue(1)), "+")),
                        new PrintStatement(new VariableExpression("b"))))));
    }

    private static IStatement getEx3() {
        return new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                new CompoundStatement(new IfStatement(new VariableExpression("a"), new AssignStatement("v", new ValueExpression(new IntValue(2))),
                    new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                    new PrintStatement(new VariableExpression("v"))))));
    }

    private static IStatement getEx4() {
        return new CompoundStatement(new VariableDeclarationStatement("varf", new StringType()),
               new CompoundStatement(new AssignStatement("varf", new ValueExpression(new StringValue("test.in"))),
               new CompoundStatement(new openRFile(new VariableExpression("varf")),
               new CompoundStatement(new VariableDeclarationStatement("varc", new IntType()),
               new CompoundStatement(new readFile(new VariableExpression("varf"), "varc"),
               new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
               new CompoundStatement(new readFile(new VariableExpression("varf"), "varc"),
               new CompoundStatement(new PrintStatement(new VariableExpression("varc")), new closeRFile(new VariableExpression("varf"))))))))));
    }

    private static IStatement getEx5() {
        return new CompoundStatement(new VariableDeclarationStatement("varf", new StringType()),
               new CompoundStatement(new AssignStatement("varf", new ValueExpression(new StringValue("test_empty.in"))),
               new CompoundStatement(new openRFile(new VariableExpression("varf")),
               new CompoundStatement(new VariableDeclarationStatement("varc", new IntType()),
               new CompoundStatement(new readFile(new VariableExpression("varf"), "varc"),
               new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
               new CompoundStatement(new readFile(new VariableExpression("varf"), "varc"),
               new CompoundStatement(new PrintStatement(new VariableExpression("varc")), new closeRFile(new VariableExpression("varf"))))))))));
    }

    private static IStatement getEx6() {
        return new PrintStatement(new RelationalExpression(
               new ArithmeticExpression(new ValueExpression(new IntValue(2)), new ValueExpression(new IntValue(3)), "+"),
               new ValueExpression(new IntValue(1)), "<"));
    }

    private static IStatement getEx7() {
        return new PrintStatement(new RelationalExpression(new ArithmeticExpression(
               new ValueExpression(new IntValue(51)), new ValueExpression(new IntValue(3)), "/"),
               new ArithmeticExpression(new ValueExpression(new IntValue(15)), new ValueExpression(new IntValue(2)), "+"), "=="));
    }

    private static IStatement getEx8() {
        return new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
               new CompoundStatement(new NewStmt("v", new ValueExpression(new IntValue(20))),
               new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
               new CompoundStatement(new NewStmt("a", new VariableExpression("v")),
               new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new VariableExpression("a")))))));
    }

    private static IStatement getEx9() {
        return new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
               new CompoundStatement(new NewStmt("v", new ValueExpression(new IntValue(20))),
               new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
               new CompoundStatement(new NewStmt("a", new VariableExpression("v")),
               new CompoundStatement(new PrintStatement(new readFromHeap(new VariableExpression("v"))),
                   new PrintStatement(new ArithmeticExpression(new readFromHeap(new readFromHeap(new VariableExpression("a"))), new ValueExpression(new IntValue(5)), "+")))))));
    }

    private static IStatement getEx10() {
        return new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
               new CompoundStatement(new NewStmt("v", new ValueExpression(new IntValue(20))),
               new CompoundStatement(new PrintStatement(new readFromHeap(new VariableExpression("v"))),
               new CompoundStatement(new writeToHeap("v", new ValueExpression(new IntValue(30))),
                   new PrintStatement(new ArithmeticExpression(new readFromHeap(new VariableExpression("v")), new ValueExpression(new IntValue(5)), "+"))))));
    }

    private static IStatement getEx11() {
        return new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
               new CompoundStatement(new NewStmt("v", new ValueExpression(new IntValue(20))),
               new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
               new CompoundStatement(new NewStmt("a", new VariableExpression("v")),
               new CompoundStatement(new NewStmt("v", new ValueExpression(new IntValue(30))),
                   new PrintStatement(new readFromHeap(new readFromHeap(new VariableExpression("a")))))))));
    }

    private static IStatement getEx12() {
        return new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
               new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(4))),
               new CompoundStatement(new WhileStatement(new RelationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)), ">"),
               new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                   new AssignStatement("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), "-")))),
               new PrintStatement(new VariableExpression("v")))));
    }

    private static IStatement getEx13() {
        return new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
               new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new IntType())),
               new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(10))),
               new CompoundStatement(new NewStmt("a", new ValueExpression(new IntValue(22))),
               new CompoundStatement(new forkStatement(new CompoundStatement(new writeToHeap("a", new ValueExpression(new IntValue(30))),
               new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(32))),
               new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new readFromHeap(new VariableExpression("a"))))))),
               new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new readFromHeap(new VariableExpression("a")))))))));
    }

    public static void main(String[] args) {
        // example 1: int v; v=2; Print(v)
        IStatement ex1 = getEx1();
        ProgramState prg1 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex1);
        IRepository repo1 = new Repository(prg1, "ex1.txt");
        Controller controller1 = new Controller(repo1);

        // example 2: int a;int b; a=2+3*5;b=a+1;Print(b)
        IStatement ex2 = getEx2();
        ProgramState prg2 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex2);
        IRepository repo2 = new Repository(prg2, "ex2.txt");
        Controller controller2 = new Controller(repo2);

        // example 3: bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v)
        IStatement ex3 = getEx3();
        ProgramState prg3 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex3);
        IRepository repo3 = new Repository(prg3, "ex3.txt");
        Controller controller3 = new Controller(repo3);

        // example 4: string varf; varf="test.in"; openRFile(varf); int varc; readFile(varf, varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf)
        IStatement ex4 = getEx4();
        ProgramState prg4 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex4);
        IRepository repo4 = new Repository(prg4, "ex4.txt");
        Controller controller4 = new Controller(repo4);

        // example 5: string varf; varf="test_empty.in"; openRFile(varf); int varc; readFile(varf, varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf)
        IStatement ex5 = getEx5();
        ProgramState prg5 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex5);
        IRepository repo5 = new Repository(prg5, "ex5.txt");
        Controller controller5 = new Controller(repo5);

        // example 6: print(2 + 3 < 1); (false)
        IStatement ex6 = getEx6();
        ProgramState prg6 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex6);
        IRepository repo6 = new Repository(prg6, "ex6.txt");
        Controller controller6 = new Controller(repo6);

        // example 7: print(51 / 3 == 15 + 2); (true)
        IStatement ex7 = getEx7();
        ProgramState prg7 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex7);
        IRepository repo7 = new Repository(prg7, "ex7.txt");
        Controller controller7 = new Controller(repo7);

        // example 8: Ref int v;new(v, 20); Ref Ref int a; new(a, v); print(v); print(a)
        IStatement ex8 = getEx8();
        ProgramState prg8 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex8);
        IRepository repo8 = new Repository(prg8, "ex8.txt");
        Controller controller8 = new Controller(repo8);

        // example 9: Ref int v; new(v, 20); Ref Ref int a; new(a, v); print(rH(v)); print(rH(rH(a)) + 5)
        IStatement ex9 = getEx9();
        ProgramState prg9 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex9);
        IRepository repo9 = new Repository(prg9, "ex9.txt");
        Controller controller9 = new Controller(repo9);

        // example 10: Ref int v; new(v, 20); print(rH(v)); wH(v, 30); print(rH(v) + 5);
        IStatement ex10 = getEx10();
        ProgramState prg10 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex10);
        IRepository repo10 = new Repository(prg10, "ex10.txt");
        Controller controller10 = new Controller(repo10);

        // example 11: Ref int v; new(v, 20); Ref Ref int a; new(a, v); new(v, 30); print(rH(rH(a)))
        IStatement ex11 = getEx11();
        ProgramState prg11 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex11);
        IRepository repo11 = new Repository(prg11, "ex11.txt");
        Controller controller11 = new Controller(repo11);

        // example 12: int v; v=4; (while (v>0) print(v); v=v-1); print(v)
        IStatement ex12 = getEx12();
        ProgramState prg12 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex12);
        IRepository repo12 = new Repository(prg12, "ex12.txt");
        Controller controller12 = new Controller(repo12);

        // example 13: int v; Ref int a; v = 10; new(a,22);
        //             fork(wH(a,30); v = 32; print(v); print(rH(a)));
        //             print(v); print(rH(a))
        IStatement ex13 = getEx13();
        ProgramState prg13 = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyFileTable<>(), new MyHeapTable<>(), ex13);
        IRepository repo13 = new Repository(prg13, "ex13.txt");
        Controller controller13 = new Controller(repo13);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "Exit"));
        menu.addCommand(new RunExample("1", "int v; v = 2; print(v)", controller1));
        menu.addCommand(new RunExample("2", "int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)", controller2));
        menu.addCommand(new RunExample("3", "bool a; int v; a = true; IF (a) THEN (v = 2) ELSE (v = 3); print(v)", controller3));
        menu.addCommand(new RunExample("4", "string varf; varf = \"test.in\"; openRFile(varf); int varc; readFile(varf, varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf)", controller4));
        menu.addCommand(new RunExample("5", "string varf; varf = \"test_empty.in\"; openRFile(varf); int varc; readFile(varf, varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf)", controller5));
        menu.addCommand(new RunExample("6", "print(2 + 3 < 1);", controller6));
        menu.addCommand(new RunExample("7", "print(51 / 3 == 15 + 2);", controller7));
        menu.addCommand(new RunExample("8", "Ref int v;new(v, 20); Ref Ref int a; new(a, v); print(v); print(a)", controller8));
        menu.addCommand(new RunExample("9", "Ref int v; new(v, 20); Ref Ref int a; new(a, v); print(rH(v)); print(rH(rH(a)) + 5)", controller9));
        menu.addCommand(new RunExample("10", "Ref int v; new(v, 20); print(rH(v)); wH(v, 30); print(rH(v) + 5);", controller10));
        menu.addCommand(new RunExample("11", "Ref int v; new(v, 20); Ref Ref int a; new(a, v); new(v, 30); print(rH(rH(a)))", controller11));
        menu.addCommand(new RunExample("12", "int v; v=4; (while (v>0) print(v); v=v-1); print(v)", controller12));
        menu.addCommand(new RunExample("13", "int v; Ref int a; v = 10; new(a,22); fork(wH(a,30); v = 32; print(v); print(rH(a))); print(v); print(rH(a))", controller13));

        menu.show();
    }
}
