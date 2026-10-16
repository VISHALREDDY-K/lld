package com.lld.state;

import com.lld.entities.Task;
import com.lld.enums.TaskStatus;

public class ToDoState implements TaskState {
    @Override
    public void startProgress(Task task) {
        task.setStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public void completeTask(Task task) {
        throw new IllegalStateException("Task cannot be completed directly from ToDo state");
    }

    @Override
    public void blockTask(Task task) {
        task.setStatus(TaskStatus.BLOCKED);
    }

    @Override
    public void reopenTask(Task task) {
        throw new IllegalStateException("Task cannot be reopened from ToDo state");
    }

    @Override
    public TaskStatus getStatus() {
        return TaskStatus.TO_DO;
    }
}
