package Store;
import java.util.Objects;
public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected String status;

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

    public String getStatus() {
        return status;
    }

    @Override public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override public boolean equals(Object Name) {
        if (this == Name) return true;
            if (Name == null || getClass() != Name.getClass()) return false;
                Task name = (Task) Name;
                    return Objects.equals(id, name.id) && Objects.equals(name, name.name) &&
                        Objects.equals(description, name.description) && Objects.equals(status, name.status);
    }

    @Override public String toString() {
        String export = "  Ид: " + id +
                        "  Имя: " + name +
                        "  Описание: " + description +
                        "  Статус: " + status + "\n";
        return export;
    }
}
