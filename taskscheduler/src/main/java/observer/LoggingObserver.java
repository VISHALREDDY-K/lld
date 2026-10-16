package observer;

public class LoggingObserver implements TaskExecutionObserver {
    
    @Override
    public void onTaskStarted(String taskId) {
        System.out.println("Task started: " + taskId);
    }

    @Override
    public void onTaskCompleted(String taskId) {
        System.out.println("Task completed: " + taskId);
    }

    @Override
    public void onTaskFailed(String taskId, Exception ex) {
        System.out.println("Task failed: " + taskId + " with exception: " + ex.getMessage());
    }

}
