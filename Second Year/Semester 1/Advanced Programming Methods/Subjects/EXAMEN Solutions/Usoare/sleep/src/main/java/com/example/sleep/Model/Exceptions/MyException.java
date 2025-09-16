package com.example.sleep.Model.Exceptions;

public class MyException extends Exception{
    private final String msg;

    public MyException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public MyException() {
        super("ERROR: Operation failed");
        this.msg = "ERROR: Operation failed";
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
