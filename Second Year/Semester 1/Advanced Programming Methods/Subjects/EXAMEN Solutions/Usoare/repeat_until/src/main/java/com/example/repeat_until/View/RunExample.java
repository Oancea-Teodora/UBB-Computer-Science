package com.example.repeat_until.View;

import com.example.repeat_until.Controller.Controller;
import com.example.repeat_until.Model.Exceptions.MyException;

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
