package com.lld.state;

import com.lld.entities.Task;
import com.lld.enums.TaskStatus;

public class DoneState implements TaskState {
    @Override
    public void startProgress(Task task) {
        throw new IllegalStateException("Task is already done");
    }

    @Override
    public void completeTask(Task task) {
        throw new IllegalStateException("Task is already done");
    }

    @Override
    public void blockTask(Task task) {
        throw new IllegalStateException("Task is already done");
    }

    @Override
    public void reopenTask(Task task) {
        task.setStatus(TaskStatus.TO_DO);
    }

    @Override
    public TaskStatus getStatus() {
        return TaskStatus.COMPLETED;
    }
}
