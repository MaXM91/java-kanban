package tasks;

import java.util.Objects;

public class Subtask extends Task {

    protected int epicId;                                                      // Принадлежность к Ид составной задачи

    public Subtask(String name, String description, String status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, epicId);
    }

    @Override
    public boolean equals(Object Subtask) {
        if (this == Subtask) {
            return true;
        }

        if (Subtask == null || getClass() != Subtask.getClass()) {
            return false;
        }

        Subtask subtask = (Subtask) Subtask;
        return Objects.equals(id, subtask.id) && Objects.equals(name, subtask.name) &&
                Objects.equals(description, subtask.description) && Objects.equals(status, subtask.status) &&
                Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public String toString() {
        return "   Ид: " + id +
                "   Эпик Ид: " + epicId +
                "   Имя: " + name +
                "   Описание: " + description +
                "   Статус: " + status + "\n";
    }
}
