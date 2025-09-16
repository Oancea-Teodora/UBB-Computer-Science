package com.example.sleep.View;

import com.example.sleep.Controller.Controller;
import com.example.sleep.Model.Exceptions.MyException;

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
