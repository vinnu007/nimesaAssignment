package com.nimesa.careers.multithreading_assignment;

public class Task {
    private final TaskRequest taskRequest;
    Task(TaskRequest taskRequest){
        this.taskRequest = taskRequest;
    }

    public TaskResponse run() throws InterruptedException {

        int size = taskRequest.getSize();
//        System.out.println("Before Sleep : "+taskRequest.getId() +" with Size: "+size);
        Thread.sleep(size*1000);
//        System.out.println("After Sleep : "+taskRequest.getId());
        return new TaskResponse(taskRequest.getId(),taskRequest.getSubmittedBy(),Status.SUCCESS, taskRequest.getType());
    }

}
