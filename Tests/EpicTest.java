
import managers.InMemoryTasksManager;
import managers.StatusTask;
import managers.exceptions.TasksValidateException;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import java.time.Duration;
import java.time.LocalDateTime;

class EpicTest {

    InMemoryTasksManager manager;
    LocalDateTime epic1LocalDateTime = LocalDateTime.of(2016, 1, 22, 22, 10, 21);
    Duration epic1Duration = Duration.ofMinutes(30);

    @BeforeEach
    public void startTasksManager() {
        manager = new InMemoryTasksManager();
    }

    @Test
    public void emptySubtaskIds() {

        Epic epic1 = new Epic("Эпик1", "Проверочный", StatusTask.NEW,
                epic1LocalDateTime, epic1Duration);
        int epic1Id = manager.addEpic(epic1);

        Assertions.assertEquals(StatusTask.NEW, manager.getEpic(epic1Id).getStatus());

        manager.removeAllSubtasks();
    }

    @Test
    public void allStatusNewSubtaskIds() throws TasksValidateException {
        Epic epic1 = new Epic("Эпик1", "Проверочный", StatusTask.DONE,
                epic1LocalDateTime, epic1Duration);
        int epic1Id = manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("подзадача1", "проверочная", StatusTask.NEW,
                epic1LocalDateTime, epic1Duration, 1);

        manager.addSubtask(subtask1);

        Assertions.assertEquals(StatusTask.NEW, manager.getEpic(epic1Id).getStatus());

        manager.removeAllSubtasks();
    }

    @Test
    public void allStatusDoneSubtaskIds() throws TasksValidateException {
        Epic epic1 = new Epic("Эпик1", "Проверочный", StatusTask.NEW,
                epic1LocalDateTime, epic1Duration);
        int epic1Id = manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("подзадача1", "проверочная", StatusTask.DONE,
                epic1LocalDateTime, epic1Duration, epic1Id);

        manager.addSubtask(subtask1);

        Assertions.assertEquals(StatusTask.DONE, manager.getEpic(epic1Id).getStatus());

        manager.removeAllSubtasks();
    }

    @Test
    public void allStatusNewAndDoneSubtaskIds() throws TasksValidateException {
        Epic epic1 = new Epic("Эпик1", "Проверочный", StatusTask.NEW,
                epic1LocalDateTime, epic1Duration);
        int epic1Id = manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("подзадача1", "проверочная", StatusTask.IN_PROGRESS,
                epic1LocalDateTime, epic1Duration, epic1Id);
        manager.addSubtask(subtask1);

        Assertions.assertEquals(StatusTask.IN_PROGRESS, manager.getEpic(epic1Id).getStatus());

        manager.removeAllSubtasks();
    }

    @Test
    public void allStatusInProgressSubtaskIds() throws TasksValidateException {
        Epic epic1 = new Epic("Эпик1", "Проверочный", StatusTask.NEW,
                epic1LocalDateTime, epic1Duration);
        int epic1Id = manager.addEpic(epic1);

        Subtask subtask3 = new Subtask("подзадача3", "проверочная", StatusTask.IN_PROGRESS,
                epic1LocalDateTime, epic1Duration, epic1Id);
        manager.addSubtask(subtask3);

        Assertions.assertEquals(StatusTask.IN_PROGRESS, manager.getEpic(epic1Id).getStatus());

        manager.removeAllSubtasks();
    }
}