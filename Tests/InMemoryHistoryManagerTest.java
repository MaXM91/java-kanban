
import managers.InMemoryTasksManager;
import managers.StatusTask;
import managers.TasksValidateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SimpleTask;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class InMemoryHistoryManagerTest {
    InMemoryTasksManager manager;

    Epic epic1;
    int epic1Id;
    Subtask subtask1;
    int subtask1Id;
    Subtask subtask2;
    int subtask2Id;
    Subtask subtask3;
    int subtask3Id;
    SimpleTask task1;
    int task1Id;
    SimpleTask task2;
    int task2Id;

    @BeforeEach
    public void startTasksManager() throws TasksValidateException {
        manager = new InMemoryTasksManager();

        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();

        LocalDateTime epic1LocalDateTime = LocalDateTime.of(2010, 1, 1, 0, 0, 0);
        Duration epic1Duration = Duration.ofMinutes(90);
        epic1 = new Epic("Эпик1", "Проверочный", StatusTask.NEW, epic1LocalDateTime, epic1Duration);
        epic1Id = manager.addEpic(epic1);

        LocalDateTime subtask1LocalDateTime = LocalDateTime.of(2010, 1, 2, 0, 0, 0);
        Duration subtask1Duration = Duration.ofMinutes(90);
        subtask1 = new Subtask("подзадача1", "проверочная", StatusTask.NEW, subtask1LocalDateTime, subtask1Duration, epic1Id);
        subtask1Id = manager.addSubtask(subtask1);

        LocalDateTime subtask2LocalDateTime = LocalDateTime.of(2010, 1, 3, 0, 0, 0);
        Duration subtask2Duration = Duration.ofMinutes(1000);
        subtask2 = new Subtask("подзадача2", "проверочная", StatusTask.NEW, subtask2LocalDateTime, subtask2Duration, epic1Id);
        subtask2Id = manager.addSubtask(subtask2);

        LocalDateTime subtask3LocalDateTime = LocalDateTime.of(2010, 1, 9, 0, 0, 0);
        Duration subtask3Duration = Duration.ofMinutes(600);
        subtask3 = new Subtask("подзадача3", "проверочная", StatusTask.NEW, subtask3LocalDateTime, subtask3Duration, epic1Id);
        subtask3Id = manager.addSubtask(subtask3);

        LocalDateTime task1LocalDateTime = LocalDateTime.of(2010, 1, 11, 0, 0, 0);
        Duration task1Duration = Duration.ofMinutes(450);
        task1 = new SimpleTask("задача1", "проверочная", StatusTask.NEW, task1LocalDateTime, task1Duration);
        task1Id = manager.addTask(task1);

        LocalDateTime task2LocalDateTime = LocalDateTime.of(2010, 1, 22, 0, 0, 0);
        Duration task2Duration = Duration.ofMinutes(150);
        task2 = new SimpleTask("задача2", "тестовая2", StatusTask.NEW, task2LocalDateTime, task2Duration);
        task2Id = manager.addTask(task2);
    }

    @Test
    void getRemoveHistory() {
        //Заполнение истории
        List<Task> savedTask = new ArrayList<>();

        savedTask.add(epic1);
        savedTask.add(subtask1);
        savedTask.add(subtask2);
        savedTask.add(subtask3);
        savedTask.add(task1);
        savedTask.add(task2);

        System.out.println(manager.getEpic(epic1Id));
        System.out.println(manager.getSubtask(subtask1Id));
        System.out.println(manager.getSubtask(subtask2Id));
        System.out.println(manager.getSubtask(subtask3Id));
        System.out.println(manager.getTask(task1Id));
        System.out.println(manager.getTask(task2Id));

        System.out.println(savedTask);
        System.out.println(manager.getHistory());

        Assertions.assertArrayEquals(savedTask.toArray(), manager.getHistory().toArray(), "История не совпадает!");

        //Удаление из середины истории
        savedTask.remove(subtask2);
        manager.removeSubtaskById(subtask2.getId());

        Assertions.assertArrayEquals(savedTask.toArray(), manager.getHistory().toArray(), "История не совпадает!");

        //Удаление с конца истории
        savedTask.remove(task2);
        manager.removeTaskById(task2Id);

        Assertions.assertArrayEquals(savedTask.toArray(), manager.getHistory().toArray(), "История не совпадает!");

        //Удаление начала истории
        savedTask.remove(epic1);
        savedTask.remove(subtask1);
        savedTask.remove(subtask3);
        manager.removeEpicById(epic1Id);

        Assertions.assertArrayEquals(savedTask.toArray(), manager.getHistory().toArray(), "История не совпадает!");

        //Работа с повторами в истории
        manager.getTask(task1Id);
        manager.getTask(task1Id);

        Assertions.assertArrayEquals(savedTask.toArray(), manager.getHistory().toArray(), "История повторяется!");
    }
}