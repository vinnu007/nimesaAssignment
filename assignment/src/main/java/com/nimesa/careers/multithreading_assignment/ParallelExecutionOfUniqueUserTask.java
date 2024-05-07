package com.nimesa.careers.multithreading_assignment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ParallelExecutionOfUniqueUserTask {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final Processor processor = new Processor();
    private final Map<String, BlockingQueue<TaskRequest>> userTaskQueues = new ConcurrentHashMap<>();

    /**
     * Submits the task to respective task queue based on the user ID.
     *
     * @param task The task to be submitted.
     */
    public void submitTask(TaskRequest task) {
        System.out.println("Submitting tasks: " + task.getSubmittedBy() + " and type is " + task.getType());
        String userId = task.getId();
        userTaskQueues.computeIfAbsent(userId, key -> new LinkedBlockingQueue<>()).offer(task);
    }

    /**
     * Starts processing all queued tasks concurrently.
     *
     * @return A CompletableFuture<Void> representing the completion of all tasks.
     */
    public CompletableFuture<Void> startProcessing() {
        List<CompletableFuture<Void>> taskFutures = userTaskQueues.values().stream()
                .map(this::processTasks)
                .collect(Collectors.toList());

        return CompletableFuture.allOf(taskFutures.toArray(new CompletableFuture[0]));
    }

    /**
     * Process tasks from the given task queue asynchronously.
     *
     * @param taskQueue The task queue from which tasks will be processed.
     * @return A CompletableFuture<Void> representing the completion of all tasks.
     */
    private CompletableFuture<Void> processTasks(BlockingQueue<TaskRequest> taskQueue) {
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
