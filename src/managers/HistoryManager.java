package managers;

import tasks.*;

import java.util.List;

public interface HistoryManager {

    int MAX_HISTORY = 10;

    void add(Task task);

    List<Task> getHistory();
}
