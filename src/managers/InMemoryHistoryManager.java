package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager {

    protected ArrayList<T> historyTaskIds = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (historyTaskIds.size() == 10) {
            for (int i = 0; i < MAX_HISTORY - 1; i++) {
                historyTaskIds.add(i, historyTaskIds.get(i + 1));
            }
            historyTaskIds.add(MAX_HISTORY - 1, (T) task);
        } else {
            historyTaskIds.add((T) task);
        }
    }


    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyTaskIds);
    }
}
