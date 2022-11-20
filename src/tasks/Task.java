package tasks;

import java.util.Objects;

public class Task {
    protected int id;                                                          // Ид задачи
    protected String name;                                                     // Имя задачи
    protected String description;                                              // Описание задачи
    protected String status;                                                   // Статус задачи

    public Task(int id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public boolean equals(Object Task) {
        if (this == Task) {
            return true;
        }

        if (Task == null || getClass() != Task.getClass()) {
            return false;
        }

        Task task = (Task) Task;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    @Override
    public String toString() {
        return "  Ид: " + id +
                "  Имя: " + name +
                "  Описание: " + description +
                "  Статус: " + status + "\n";

    }
}
