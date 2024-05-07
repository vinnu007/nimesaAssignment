package com.nimesa.careers.multithreading_assignment;

public class TaskResponse {
    private String id;
    private Status status;
    private String submittedBy;
    private TaskType type;

    public TaskResponse(String id, String submittedBy, Status status, TaskType type) {
        this.id = id;
        this.status = status;
        this.submittedBy = submittedBy;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
