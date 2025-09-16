package com.example.cyclic_barrier_synchronization_mechanism.View;

public abstract class Command {
    private String key;
    private String description;

    public Command(String k, String desc) {
        this.key = k;
        this.description = desc;
    }

    public String getKey() {
        return this.key;
    }

    public String getDescription() {
        return this.description;
    }

    public abstract void execute();
}
