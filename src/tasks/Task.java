package tasks;

import managers.StatusTask;
import managers.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Task {
    protected int id;
    protected String name;
    protected String description;
    protected StatusTask status;
    protected LocalDateTime startDateTime;
    protected Duration duration;


    public Task(String name, String description, StatusTask status, LocalDateTime startDateTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startDateTime = startDateTime;
        this.duration = duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StatusTask getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public TypeTask getTypeTask() {
        return TypeTask.SIMPLE_TASK;
    }

    public Integer getEpicId() {
        return null;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public Duration getDuration() {
        return duration;
    }

}
