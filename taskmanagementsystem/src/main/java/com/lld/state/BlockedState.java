package com.lld.state;

import com.lld.entities.Task;
import com.lld.enums.TaskStatus;

public class BlockedState implements TaskState {
    @Override
    public void startProgress(Task task) {
        task.setStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public void completeTask(Task task) {
        throw new IllegalStateException("Task is blocked");
    }

    @Override
    public void blockTask(Task task) {
        throw new IllegalStateException("Task is already blocked");
    }

    @Override
    public void reopenTask(Task task) {
        task.setStatus(TaskStatus.TO_DO);
    }

    @Override
    public TaskStatus getStatus() {
        return TaskStatus.BLOCKED;
    }
}
