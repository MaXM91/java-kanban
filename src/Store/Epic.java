package Store;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(Integer subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void replaceSubtaskIds(ArrayList<Integer> subtaskIds) {
        int j;
        for (int i = 0; i < subtaskIds.size(); i++) {
            j = subtaskIds.get(i);
            this.subtaskIds.add(j);
        }
    }

    public void removeSubtaskId(Integer id){
        subtaskIds.remove(id);
    }

    @Override public int hashCode() {
        return Objects.hash(id, name, description, status, subtaskIds);
    }

    @Override public boolean equals(Object Name) {
        if (this == Name) return true;
            if (Name == null || getClass() != Name.getClass()) return false;
                Epic name = (Epic) Name;
                    return Objects.equals(id, name.id) && Objects.equals(name, name.name) &&
                        Objects.equals(description, name.description) && Objects.equals(status, name.status) &&
                            Objects.equals(subtaskIds, name.subtaskIds);
    }

    private String variable() {
        String string = "";
        for(int i = 0; i < subtaskIds.size(); i++) {
            string += subtaskIds.get(i);
            string += " ";
        }
            return string;
    }


    @Override public String toString() {
        String export = "   Ид: " + id +
                        "   Входящие Ид: " + variable() +
                        "   Имя: " + name +
                        "   Описание: " + description +
                        "   Статус: " + status + "\n";
        return export;
    }

}
