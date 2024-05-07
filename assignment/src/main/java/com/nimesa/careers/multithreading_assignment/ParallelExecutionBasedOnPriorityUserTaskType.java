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

public class ParallelExecutionBasedOnPriorityUserTaskType {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final Map<String, Map<TaskType, Map<Integer, Queue<TaskRequest>>>> userTaskTypePriorityQueues = new ConcurrentHashMap<>();
    private final Processor processor = new Processor();

    /**
     * Submits a task to be processed asynchronously.
     *
     * @param task The task to be submitted.
     */
    public void submitTask(TaskRequest task) {
        String userId = task.getId();
        TaskType taskType = task.getType();
        int priority = task.getPriority();

        userTaskTypePriorityQueues
                .computeIfAbsent(userId, u -> new ConcurrentHashMap<>())
                .computeIfAbsent(taskType, t -> new ConcurrentHashMap<>())
                .computeIfAbsent(priority, p -> new LinkedBlockingQueue<>())
                .offer(task);
    }

    /**
     * Starts processing the submitted tasks asynchronously.
     *
     * @return A CompletableFuture representing the completion of all tasks.
     */
    public CompletableFuture<Void> startProcessing() {
        List<CompletableFuture<Void>> taskFutures = new ArrayList<>();
        for (Map<TaskType, Map<Integer, Queue<TaskRequest>>> taskTypePriorityQueues : userTaskTypePriorityQueues.values()) {
            for (Map<Integer, Queue<TaskRequest>> priorityQueues : taskTypePriorityQueues.values()) {
                for (Queue<TaskRequest> queue : priorityQueues.values()) {
                    taskFutures.add(processTasks(queue));
                }
            }
        }
        return CompletableFuture.allOf(taskFutures.toArray(new CompletableFuture[0]));
    }

    /**
     * Process the tasks in the given task queue asynchronously.
     *
     * @param taskQueue The queue containing the tasks to be processed.
     * @return A CompletableFuture representing the completion of task processing.
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

