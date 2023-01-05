package tasks;

import managers.StatusTask;
import managers.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();               // ИДы входящих составных задач
    LocalDateTime endTime;

    public Epic(String name, String description, StatusTask status, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
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

    public TypeTask getTypeTask() {
        return TypeTask.EPIC;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startDateTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration() {
        this.duration = Duration.between(startDateTime, endTime);
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
        return "   Ид: " + id +
                "   Входящие Ид: " + variable() +
                "   Имя: " + name +
                "   Описание: " + description +
                "   Статус: " + status +
                "   Время начала: " + startDateTime +
                "   Продолжительность: " + duration + "\n";

    }

}
