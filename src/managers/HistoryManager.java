package managers;

import tasks.*;

import java.util.List;

public interface HistoryManager {

    void add();

    List<Task> getHistory();
}
