package tasks;

import java.util.Objects;

public class Subtask extends Task {

    protected int epicId;                                                      // Принадлежность к Ид составной задачи

    public Subtask(int id, String name, String description, String status) {
        super(id, name, description, status);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
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
