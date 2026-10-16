package com.lld.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.lld.enums.TaskStatus;
import com.lld.strategy.SchedulingStrategy;

public class ScheduledTask implements Comparable<ScheduledTask> {
    private final String id;
    private final Task task;
    private final SchedulingStrategy strategy;
    private LocalDateTime nextExecutionTime;
    private LocalDateTime lastExecutionTime;
    private TaskStatus taskStatus;
    private final int sequenceNumber;

    public ScheduledTask(Task task, SchedulingStrategy strategy, int sequenceNumber) {
        this.id = UUID.randomUUID().toString();
        this.task = task;
        this.strategy = strategy;
        this.sequenceNumber = sequenceNumber;
        this.taskStatus = TaskStatus.SCHEDULED;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setNextExecutionTime(LocalDateTime nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }

    public LocalDateTime getLastExecutionTime() {
        return lastExecutionTime;
    }

    public void setLastExecutionTime(LocalDateTime lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public int compareTo(ScheduledTask other) {
        int compare = this.nextExecutionTime.compareTo(other.nextExecutionTime);
        if (compare == 0) {
            return this.sequenceNumber - other.sequenceNumber;
        }
        return compare; 
    }
}
