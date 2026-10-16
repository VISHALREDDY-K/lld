package com.lld.strategy;

import com.lld.entities.Task;

public class PrintMessageTask implements Task{
    private String name;
    private String message;

    public PrintMessageTask(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void execute() {
        System.out.println(message);
    }
}
