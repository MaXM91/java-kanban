package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager {

    int MAX_HISTORY = 10;

    protected ArrayList<T> historyTasks = new ArrayList<>();

    @Override
    public void add() {
        if (historyTasks.size() == MAX_HISTORY) {
            historyTasks.remove(0);
            historyTasks.add((T) InMemoryTaskManager.historyTask);
        } else {
            historyTasks.add((T) InMemoryTaskManager.historyTask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyTasks);
    }
}
