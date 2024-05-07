package com.nimesa.careers.multithreading_assignment;

import java.util.ArrayList;
import java.util.List;

public class Processor {


    public List<TaskResponse> execute(List<TaskRequest> taskRequest) throws InterruptedException {
        List<TaskResponse> taskResponses = new ArrayList<>();
        for (TaskRequest request : taskRequest) {
            taskResponses.add(execute(request));
        }
        return taskResponses;
    }

    public TaskResponse execute(TaskRequest taskRequest) throws InterruptedException {
        System.out.println("Starting Task by " + taskRequest.getSubmittedBy()+ " with type as: "+taskRequest.getType());
        Task task = new Task(taskRequest);
        TaskResponse response = task.run();
        System.out.println("Completed Task by " + response.getSubmittedBy() + " With Status: " + response.getStatus()+ " with type as: "+response.getType());
        return response;
    }
}
