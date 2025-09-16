package com.example.switch_statement.View;

public class ExitCommand extends Command{
    public ExitCommand(String k, String desc) {
        super(k, desc);
    }

    @Override
    public void execute() {
        System.exit(0);
    }
}
