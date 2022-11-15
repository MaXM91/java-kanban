package Store;
import java.util.Objects;
public class Subtask extends Task {

    protected int epicId;

    public Subtask(int id, String name, String description, String status) {
        super(id, name, description, status);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override public int hashCode() {
        return Objects.hash(id, name, description, status, epicId);
    }

    @Override public boolean equals(Object Name) {
        if (this == Name) return true;
            if(Name == null || getClass() != Name.getClass()) return false;
                Subtask name = (Subtask) Name;
                    return Objects.equals(id, name.id) && Objects.equals(name, name.name) &&
                        Objects.equals(description, name.description) && Objects.equals(status, name.status) &&
                            Objects.equals(epicId, name.epicId);
    }

    @Override public String toString() {
        String export = "   Ид: " + id +
                        "   Эпик Ид: " + epicId +
                        "   Имя: " + name +
                        "   Описание: " + description +
                        "   Статус: " + status + "\n";
        return export;
    }
}
