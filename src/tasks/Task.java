package tasks;

import managers.StatusTask;

public abstract class Task {
    protected int id;
    protected String name;
    protected String description;
    protected StatusTask status;

    public Task(String name, String description, StatusTask status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public StatusTask getStatus() {
        return status;
    }
}
