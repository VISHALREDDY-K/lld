package com.lld;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import com.lld.entities.ScheduledTask;
import com.lld.entities.Task;
import com.lld.strategy.SchedulingStrategy;

import observer.TaskExecutionObserver;

public class TaskSchedulerService {
    private static volatile TaskSchedulerService instance;
    private final PriorityQueue<ScheduledTask> taskQueue = new PriorityQueue<>();
    private final List<TaskExecutionObserver> observers = new CopyOnWriteArrayList<>();
    private volatile boolean running;
    private final Object queueLock = new Object();
    private final AtomicInteger sequenceGenerator = new AtomicInteger(0);
    private Thread[] threads;


    public static TaskSchedulerService getInstance() {
        if (instance == null) {
            synchronized (TaskSchedulerService.class) {
                if (instance == null) {
                    instance = new TaskSchedulerService();
                }
            }
        }
        return instance;
    }

    public void initialize(int threadCount){
        if(threadCount <= 0){
            throw new IllegalArgumentException("Thread count must be positive");
        }
        if(running){
            throw new IllegalStateException("TaskSchedulerService is already running");
        }
        this.threads = new Thread[threadCount];
        this.running = true;
        for(int i = 0; i<threadCount; i++){
            threads[i] = new Thread(this::runWorker, "TaskRunner-" + i);
            threads[i].setDaemon(true);
            threads[i].start();
        }
    }

    public String schedule(Task task, SchedulingStrategy strategy){
        if(task == null || strategy == null){
            throw new IllegalArgumentException("Task and strategy must not be null");
        }
        if(!running){
            throw new IllegalStateException("TaskSchedulerService is not running");
        }
        ScheduledTask scheduledTask = new ScheduledTask(task, strategy, sequenceGenerator.getAndIncrement());
        synchronized(queueLock){
            taskQueue.offer(scheduledTask);
            queueLock.notify();
        }
        return scheduledTask.getId();
    }

    public boolean cancel(String taskId){
        if(taskId == null){
            throw new IllegalArgumentException("Task ID must not be null");
        }
        synchronized(queueLock){
            Iterator<ScheduledTask> it = taskQueue.iterator();
            while(it.hasNext()){
                ScheduledTask scheduledTask = it.next();
                if(scheduledTask.getId().equals(taskId)){
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }

    public void shutdown(){

    }

    public void runWorker(){
        while(running){
            synchronized(queueLock){
                if(taskQueue.isEmpty()){
                    try{
                        queueLock.wait();
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                ScheduledTask task = taskQueue.peek();
                long delay = Duration.between(LocalDateTime.now(), task.getNextExecutionTime()).toMillis();
                if(delay > 0){
                    try{
                        queueLock.wait(delay);
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                        return;
                    }
                    continue;
                }

                task = taskQueue.poll();
                executeTask(task);
            }
        }
    }

    


}
