import managers.InMemoryTasksManager;
import managers.StatusTask;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.SimpleTask;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTasksManagerTest extends TasksManagerTest<InMemoryTasksManager> {

    @BeforeEach
    public void startTasksManager() {

        tasksManager = new InMemoryTasksManager();

        LocalDateTime epic1LocalDateTime = LocalDateTime.of(2010, 1, 1, 1, 0, 0);
        Duration epic1Duration = Duration.ofMinutes(90);
        epic1 = new Epic("Эпик1", "Проверочный", StatusTask.NEW, epic1LocalDateTime, epic1Duration);

        LocalDateTime subtask1LocalDateTime = LocalDateTime.of(2010, 1, 2, 2, 0, 0);
        Duration subtask1Duration = Duration.ofMinutes(90);
        subtask1 = new Subtask("подзадача1", "проверочная", StatusTask.NEW, subtask1LocalDateTime, subtask1Duration, epic1Id);

        LocalDateTime subtask2LocalDateTime = LocalDateTime.of(2010, 1, 3, 6, 0, 0);
        Duration subtask2Duration = Duration.ofMinutes(1000);
        subtask2 = new Subtask("подзадача2", "проверочная", StatusTask.NEW, subtask2LocalDateTime, subtask2Duration, epic1Id);

        LocalDateTime subtask3LocalDateTime = LocalDateTime.of(2010, 1, 9, 5, 0, 0);
        Duration subtask3Duration = Duration.ofMinutes(600);
        subtask3 = new Subtask("подзадача3", "проверочная", StatusTask.NEW, subtask3LocalDateTime, subtask3Duration, epic1Id);

        LocalDateTime task1LocalDateTime = LocalDateTime.of(2010, 1, 11, 4, 0, 0);
        Duration task1Duration = Duration.ofMinutes(450);
        task1 = new SimpleTask("задача1", "проверочная", StatusTask.NEW, task1LocalDateTime, task1Duration);

        LocalDateTime task2LocalDateTime = LocalDateTime.of(2010, 1, 22, 8, 0, 0);
        Duration task2Duration = Duration.ofMinutes(150);
        task2 = new SimpleTask("задача2", "тестовая2", StatusTask.NEW, task2LocalDateTime, task2Duration);

        LocalDateTime epic2LocalDateTime = LocalDateTime.of(2010, 1, 24, 9, 0, 0);
        Duration epic2Duration = Duration.ofMinutes(90);
        epic2 = new Epic("Эпик2", "Проверочный", StatusTask.NEW, epic2LocalDateTime, epic2Duration);

        tasksManager.removeAllTasks();
        tasksManager.removeAllSubtasks();
        tasksManager.removeAllEpics();
    }
}