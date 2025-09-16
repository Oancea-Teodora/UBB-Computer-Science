package com.example.lock_syncronization_mechanism.View;

public class ExitCommand extends Command{
    public ExitCommand(String k, String desc) {
        super(k, desc);
    }

    @Override
    public void execute() {
        System.exit(0);
    }
}
