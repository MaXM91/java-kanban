package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager {

    private final int MAX_HISTORY = 10;

    protected ArrayList<T> historyTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        historyTasks.add((T) task);
        if (historyTasks.size() > MAX_HISTORY) {
            historyTasks.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyTasks);
    }
}
