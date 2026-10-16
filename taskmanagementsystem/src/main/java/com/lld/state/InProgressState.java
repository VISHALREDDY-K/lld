package com.lld.state;

import com.lld.entities.Task;
import com.lld.enums.TaskStatus;

public class InProgressState implements TaskState {

    @Override
    public void startProgress(Task task) {
        throw new IllegalStateException("Task is already in progress");
    }

    @Override
    public void completeTask(Task task) {
        task.setStatus(TaskStatus.COMPLETED);
    }

    @Override
    public void blockTask(Task task) {
        task.setStatus(TaskStatus.BLOCKED);
    }

    @Override
    public void reopenTask(Task task) {
        task.setStatus(TaskStatus.TO_DO);
    }

    @Override
    public TaskStatus getStatus() {
        return TaskStatus.IN_PROGRESS;
    }

}
