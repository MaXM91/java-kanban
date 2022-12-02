package tasks;

import managers.StatusTask;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();               // ИДы входящих составных задач

    public Epic(String name, String description, StatusTask status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(Integer subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(Integer id) {
        subtaskIds.remove(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, subtaskIds);
    }

    @Override
    public boolean equals(Object Epic) {
        if (this == Epic) {
            return true;
        }

        if (Epic == null || getClass() != Epic.getClass()) {
            return false;
        }

        Epic epic = (Epic) Epic;
        return Objects.equals(id, epic.id) && Objects.equals(name, epic.name) &&
                Objects.equals(description, epic.description) && Objects.equals(status, epic.status) &&
                Objects.equals(subtaskIds, epic.subtaskIds);
    }

    private String variable() {
        String string = "";
        for (Integer subtaskId : subtaskIds) {
            string += subtaskId;
            string += " ";
        }
        return string;
    }

    @Override
    public String toString() {
        return  "   Ид: " + id +
                "   Входящие Ид: " + variable() +
                "   Имя: " + name +
                "   Описание: " + description +
                "   Статус: " + status + "\n";

    }

}
