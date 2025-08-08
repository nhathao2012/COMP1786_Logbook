package com.example.todolisttask2;

import java.util.Date;

public class Task {
    public String name;
    public Date deadline;
    public int duration;
    public String description;

    public Task(String name, Date deadline, int duration, String description) {
        this.name = name;
        this.deadline = deadline;
        this.duration = duration;
        this.description = description;
    }
}
