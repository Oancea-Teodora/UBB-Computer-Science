package com.example.cyclic_barrier_synchronization_mechanism.View;

import com.example.cyclic_barrier_synchronization_mechanism.Controller.Controller;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Exceptions.MyException;

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
