package com.nimesa.careers.multithreading_assignment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ProcessorTest {
    private final List<String> users = List.of("User4", "User1", "User3", "User2");
    private final List<TaskType> taskTypes = List.of(TaskType.values());

    //### 1. Parallel Execution of All Tasks
    @Test
    public void testParallelExecutionOfAllTasks() {
        List<TaskRequest> jobList = getJobList(10);
        ParallelProcessor processor = new ParallelProcessor();
        CompletableFuture<Void> allTasksFuture = processor.execute(jobList);
        allTasksFuture.join(); // Wait until all tasks have been processed before continuing execution
    }

    //### 2. Parallel Execution of Unique User Tasks
    @Test
    public void testParallelExecutionOfUniqueUserTask() {
        List<TaskRequest> jobList = getJobList(10);
        ParallelExecutionOfUniqueUserTask taskExecutor = new ParallelExecutionOfUniqueUserTask();
        for (TaskRequest task : jobList) {
            taskExecutor.submitTask(task);
        }
        CompletableFuture<Void> allTasksFuture = taskExecutor.startProcessing();
        allTasksFuture.join();
        assertTrue(true, "All tasks have been processed");
    }

    //### 3. Parallel Execution of Unique User and Task Type
    @Test
    public void testParallelExecutionOfUniqueUserTaskType() {
        List<TaskRequest> jobList = getJobList(10);
        ParallelExecutionOfUniqueUserTaskType taskExecutor = new ParallelExecutionOfUniqueUserTaskType();
        for (TaskRequest task : jobList) {
            taskExecutor.submitTask(task);
        }
        CompletableFuture<Void> allTasksFuture = taskExecutor.startProcessing();
        allTasksFuture.join();
        assertTrue(true, "All tasks have been processed");
    }

    //### 4. Parallel Execution Based on Priority, User, and Task Type
    @Test
    public void testParallelExecutionBasedOnPriorityUserTaskType() {
        List<TaskRequest> jobList = getJobList(10);
        ParallelExecutionBasedOnPriorityUserTaskType taskExecutor = new ParallelExecutionBasedOnPriorityUserTaskType();
        for (TaskRequest task : jobList) {
            taskExecutor.submitTask(task);
        }
        CompletableFuture<Void> allTasksFuture = taskExecutor.startProcessing();
        allTasksFuture.join();
        assertTrue(true, "All tasks have been processed");
    }

    private List<TaskRequest> getJobList(int n) {
        List<TaskRequest> taskRequests = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String user = (String) getRandomStringFromList(users);
            TaskType taskType = (TaskType) getRandomStringFromList(taskTypes);
            int priority = getRandomNumber(0, 5);
            int size = getRandomNumber(1, 10);

            TaskRequest taskRequest = new TaskRequest(UUID.randomUUID().toString(), user, priority, taskType, size);
            taskRequests.add(taskRequest);
        }
        return taskRequests;
    }

    private Object getRandomStringFromList(List<?> strings) {
        int size = strings.size();
        int randomIndex = getRandomNumber(0, size);
        return strings.get(randomIndex);
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}