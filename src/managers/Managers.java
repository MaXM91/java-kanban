package managers;

import java.nio.file.Path;

public class Managers {

    private static Path pathSaveFile = Path.of("save.csv");

    public static TasksManager getDefault() {
        return new FileBackedTasksManager(pathSaveFile);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
