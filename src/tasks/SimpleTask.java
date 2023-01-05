package tasks;

import managers.StatusTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SimpleTask extends Task {
    public SimpleTask(String name, String description, StatusTask status, LocalDateTime startDateTime, Duration duration) {
        super(name, description, status, startDateTime, duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public boolean equals(Object SimpleTask) {
        if (this == SimpleTask) {
            return true;
        }

        if (SimpleTask == null || getClass() != SimpleTask.getClass()) {
            return false;
        }

        SimpleTask simpleTask = (SimpleTask) SimpleTask;
        return Objects.equals(id, simpleTask.id) && Objects.equals(name, simpleTask.name) &&
                Objects.equals(description, simpleTask.description) && Objects.equals(status, simpleTask.status);
    }

    @Override
    public String toString() {
        return "   Ид: " + id +
                "   Имя: " + name +
                "   Описание: " + description +
                "   Статус: " + status +
                "   Время старта: " + startDateTime +
                "   Продолжительность: " + duration + "\n";
    }
}
