package com.lld.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.lld.enums.TaskPriority;
import com.lld.enums.TaskStatus;
import com.lld.state.TaskState;

public class Task {
    private final String id;
    private final String title;
    private final String description;
    private final User createdBy;
    private final User assignee;
    private final TaskPriority priority;
    private TaskStatus status;
    private TaskState taskState;
    private final long createdAt;
    private final long dueDate;
    private final Set<Tag> tags;
    private final List<Comment> comments;
    private final List<ActivityLog> activityLogs;
    private final List<Task> subTasks;

    private Task(String title, String description, User createdBy, User assignee, TaskPriority priority,
            TaskStatus status, long createdAt, long dueDate) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.assignee = assignee;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.tags = new HashSet<>();
        this.comments = new ArrayList<>();
        this.activityLogs = new ArrayList<>();
        this.subTasks = new ArrayList<>();
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
        activityLogs.add(new ActivityLog("Status changed to " + status, LocalDateTime.now()));
    }

    public synchronized void addComment(Comment comment) {
        this.comments.add(comment);
        activityLogs.add(new ActivityLog("Comment added: " + comment.getText(), LocalDateTime.now()));
    }

    public synchronized void addSubTask(Task subTask) {
        this.subTasks.add(subTask);
        activityLogs.add(new ActivityLog("SubTask added: " + subTask.getId(), LocalDateTime.now()));
    }

    public synchronized void startProgress() {
        taskState.startProgress(this);
    }

    public synchronized void completeTask() {
        taskState.completeTask(this);
    }

    public synchronized void blockTask() {
        taskState.blockTask(this);
    }

    public synchronized void reopenTask() {
        taskState.reopenTask(this);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public User getAssignee() {
        return assignee;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getDueDate() {
        return dueDate;
    }

    public static class Builder {
        private String title;
        private String description;
        private User createdBy;
        private User assignee;
        private TaskPriority priority;
        private TaskStatus status;
        private long createdAt;
        private long dueDate;

        public Builder(String title, User createdBy) {
            this.title = title;
            this.createdBy = createdBy;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder createdBy(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder assignee(User assignee) {
            this.assignee = assignee;
            return this;
        }

        public Builder priority(TaskPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder dueDate(long dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Task build() {
            return new Task(title, description, createdBy, assignee, priority, status, createdAt, dueDate);
        }
    }

}
