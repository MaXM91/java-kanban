package managers;

public class Managers {

    static TaskManager taskManager = new InMemoryTaskTaskManager();
    static HistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }
}
