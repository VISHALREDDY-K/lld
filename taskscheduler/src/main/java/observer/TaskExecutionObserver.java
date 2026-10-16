package observer;

public interface TaskExecutionObserver {
    public void onTaskStarted(String taskId);

    public void onTaskCompleted(String taskId);

    public void onTaskFailed(String taskId, Exception ex);

}
