
import managers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SimpleTask;
import tasks.Subtask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTasksManagerTest extends TasksManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void startManager() {
        tasksManager = new FileBackedTasksManager(Path.of("save.csv"));

        LocalDateTime epic1LocalDateTime = LocalDateTime.of(2010, 1, 1, 1, 22, 0);
        Duration epic1Duration = Duration.ofMinutes(90);
        epic1 = new Epic("Эпик1", "Проверочный", StatusTask.NEW, epic1LocalDateTime, epic1Duration);

        LocalDateTime subtask1LocalDateTime = LocalDateTime.of(2010, 1, 2, 2, 7, 0);
        Duration subtask1Duration = Duration.ofMinutes(90);
        subtask1 = new Subtask("подзадача1", "проверочная", StatusTask.NEW, subtask1LocalDateTime, subtask1Duration, epic1Id);

        LocalDateTime subtask2LocalDateTime = LocalDateTime.of(2010, 1, 3, 1, 9, 0);
        Duration subtask2Duration = Duration.ofMinutes(1000);
        subtask2 = new Subtask("подзадача2", "проверочная", StatusTask.NEW, subtask2LocalDateTime, subtask2Duration, epic1Id);

        LocalDateTime subtask3LocalDateTime = LocalDateTime.of(2010, 1, 9, 5, 5, 0);
        Duration subtask3Duration = Duration.ofMinutes(600);
        subtask3 = new Subtask("подзадача3", "проверочная", StatusTask.NEW, subtask3LocalDateTime, subtask3Duration, epic1Id);

        LocalDateTime task1LocalDateTime = LocalDateTime.of(2010, 1, 11, 8, 7, 0);
        Duration task1Duration = Duration.ofMinutes(450);
        task1 = new SimpleTask("задача1", "проверочная", StatusTask.NEW, task1LocalDateTime, task1Duration);

        LocalDateTime task2LocalDateTime = LocalDateTime.of(2010, 1, 22, 6, 9, 0);
        Duration task2Duration = Duration.ofMinutes(150);
        task2 = new SimpleTask("задача2", "тестовая2", StatusTask.NEW, task2LocalDateTime, task2Duration);

        LocalDateTime epic2LocalDateTime = LocalDateTime.of(2010, 1, 24, 7, 8, 0);
        Duration epic2Duration = Duration.ofMinutes(90);
        epic2 = new Epic("Эпик2", "Проверочный", StatusTask.NEW, epic2LocalDateTime, epic2Duration);

        tasksManager.removeAllTasks();
        tasksManager.removeAllSubtasks();
        tasksManager.removeAllEpics();
    }

    @Test
    void saveLoadFile() throws IOException, TasksValidateException {

        epic1Id = tasksManager.addEpic(epic1);
        subtask1.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask1);

        task1Id = tasksManager.addTask(task1);

        //System.out.println(tasksManager.getTask(task1Id));
        //System.out.println(tasksManager.getEpic(epic1Id));

        final Path pathSaveFile = Path.of("save.csv");
        File saveFile = new File(String.valueOf(pathSaveFile));
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(saveFile);

        Assertions.assertEquals(tasksManager.getAllTasks(), fileBackedTasksManager.getAllTasks(), "Задачи не совпадают!");
        Assertions.assertEquals(tasksManager.getAllSubtasks(), fileBackedTasksManager.getAllSubtasks(), "Подзадачи не совпадают!");
        Assertions.assertEquals(tasksManager.getAllEpics(), fileBackedTasksManager.getAllEpics(), "Эпики не совпадают!");
    }
}
