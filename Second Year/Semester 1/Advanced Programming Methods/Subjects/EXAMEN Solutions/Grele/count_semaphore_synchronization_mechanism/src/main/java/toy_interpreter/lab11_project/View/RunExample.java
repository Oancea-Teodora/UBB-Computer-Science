package toy_interpreter.lab11_project.View;

import toy_interpreter.lab11_project.Controller.Controller;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;

public class RunExample extends Command{
    private Controller controller;

    public RunExample(String k, String desc, Controller c) {
        super(k, desc);
        this.controller = c;
    }

    @Override
    public void execute() {
        try {
            this.controller.typeCheck();
            this.controller.allSteps();
        } catch (InterruptedException | MyException e) {
            System.out.println(e.getMessage());
        }
    }
}
