package com.nimesa.careers.multithreading_assignment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ParallelProcessor {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final Processor processor = new Processor();
    private final BlockingQueue<TaskRequest> taskQueue = new LinkedBlockingQueue<>(); //Using Blocking Queue for storing Tasks


    /**
     * Executes each task on its own CompletableFuture Asynchronously
     *
     * @param tasks The list of tasks to execute.
     * @return A CompletableFuture<Void> representing the completion of all tasks.
     */
    public CompletableFuture<Void> execute(List<TaskRequest> tasks) {
        CompletableFuture<?>[] futures = tasks.stream()
                .map(task -> CompletableFuture.runAsync(() -> { //Running the tasks Asynchronously
                    try {
                        processor.execute(task);
                    } catch (InterruptedException e) {
                        System.out.println("Processing Failed for ID: " + task.getId());
                        throw new RuntimeException(e);
                    }
                }, executorService))
                .toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(futures);
    }
}
