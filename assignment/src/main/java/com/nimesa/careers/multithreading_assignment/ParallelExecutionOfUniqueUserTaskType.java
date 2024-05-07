package com.nimesa.careers.multithreading_assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ParallelExecutionOfUniqueUserTaskType {
    private final ExecutorService executorService;
    private final Map<String, Map<TaskType, Queue<TaskRequest>>> userTaskTypeQueues;
    private final Processor processor;

    public ParallelExecutionOfUniqueUserTaskType() {
        this.executorService = Executors.newCachedThreadPool();
        this.processor = new Processor();
        this.userTaskTypeQueues = new ConcurrentHashMap<>();
    }

    /**
     * Submits a task to be processed and add it to queue based on user ID and task type.
     *
     * @param task The task to be submitted.
     */
    public void submitTask(TaskRequest task) {
        String userId = task.getId();
        TaskType taskType = task.getType();
        userTaskTypeQueues
                .computeIfAbsent(userId, u -> new ConcurrentHashMap<>())
                .computeIfAbsent(taskType, t -> new LinkedBlockingQueue<>())
                .offer(task);
    }

    /**
     * Starts processing the submitted tasks.
     * Creates CompletableFuture objects for each task queue and combines them into a single CompletableFuture.
     *
     * @return A CompletableFuture representing the completion of all tasks processing.
     */
    public CompletableFuture<Void> startProcessing() {
        List<CompletableFuture<Void>> taskFutures = new ArrayList<>();
        for (Map<TaskType, Queue<TaskRequest>> taskTypeQueue : userTaskTypeQueues.values()) {
            for (Queue<TaskRequest> queue : taskTypeQueue.values()) {
                taskFutures.add(processTasks(queue));
            }
        }
        return CompletableFuture.allOf(taskFutures.toArray(new CompletableFuture[0]));
    }

    /**
     * Processes the tasks in the given task queue asynchronously.
     *
     * @param taskQueue The queue containing the tasks to be processed.
     * @return A CompletableFuture<Void> representing the completion of all tasks.
     */
    private CompletableFuture<Void> processTasks(Queue<TaskRequest> taskQueue) {
        return CompletableFuture.runAsync(() -> {
            while (!taskQueue.isEmpty()) {
                TaskRequest task = taskQueue.poll();
                try {
                    processor.execute(task);
                } catch (InterruptedException e) {
                    System.out.println("Processing Failed for ID: " + task.getId());
                    throw new RuntimeException(e);
                }
            }
        }, executorService);
    }
}
