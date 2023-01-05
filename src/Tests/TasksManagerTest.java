package Tests;

import managers.InMemoryTasksManager;
import managers.StatusTask;
import managers.TasksManager;
import managers.TasksValidateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tasks.Epic;
import tasks.SimpleTask;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class TasksManagerTest<T extends TasksManager> {

    T tasksManager;

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
    Epic epic2;

    @Test
    void addTask() throws TasksValidateException {
        task1Id = tasksManager.addTask(task1);
        SimpleTask savedTask = tasksManager.getTask(task1Id);

        assertEquals(1, task1Id, "Не возвращён ИД задачи!");

        assertNotNull(savedTask, "Задача не найдена!");
        assertEquals(task1, savedTask, "Задачи не совпадают!");

        List<SimpleTask> tasks = tasksManager.getAllTasks();

        assertNotNull(tasks, "Задача не возвращена!");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают!!");
        assertEquals(1, tasks.size(), "Неверное количество задач!");
    }

    @Test
    void addSubtask() throws TasksValidateException {
        epic1Id = tasksManager.addEpic(epic1);
        subtask1.setEpicId(epic1Id);
        subtask1Id = tasksManager.addSubtask(subtask1);

        final Subtask savedSubtask = tasksManager.getSubtask(subtask1Id);

        assertEquals(2, subtask1Id, "Не возвращён ИД подзадачи!");

        assertNotNull(savedSubtask, "Подзадача не найдена!");
        assertEquals(subtask1, savedSubtask, "Подзадачи не совпадают!");

        final List<Subtask> subtasks = tasksManager.getAllSubtasks();

        assertNotNull(subtasks, "Подзадача не возвращена!");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают!!");
        assertEquals(epic1Id, subtasks.size(), "Неверное количество подзадач!");
        assertEquals(epic1Id, subtask1.getEpicId(), "Неверный ИД эпика подзадачи!");
    }

    @Test
    void addEpic() throws TasksValidateException {
        epic1Id = tasksManager.addEpic(epic1);
        final Epic savedEpic = tasksManager.getEpic(epic1Id);
        subtask1.setEpicId(epic1Id);
        subtask1Id = tasksManager.addSubtask(subtask1);

        assertEquals(1, epic1Id, "Не возвращён ИД эпика!");

        assertNotNull(savedEpic, "Епик не возвращён!");
        assertEquals(epic1, savedEpic, "Епики не совпадают!");

        final List<Epic> epics = tasksManager.getAllEpics();

        assertNotNull(epics, "Епик не возвращён!");
        assertEquals(epic1, epics.get(0), "Епики не совпадают!");
        assertEquals(1, epics.size(), "Неверное количество эпиков!");

        assertEquals(2, subtask1Id, "Неверный ИД входящей в эпик подзадачи!");
    }

    @Test
    void updateTask() throws TasksValidateException {
        task1Id = tasksManager.addTask(task1);

        LocalDateTime task1LocalDateTime = LocalDateTime.of(2010, 1, 17, 0, 0, 0);
        Duration task1Duration = Duration.ofMinutes(450);
        SimpleTask simple1Task = new SimpleTask("1задача", "1тестовая", StatusTask.DONE, task1LocalDateTime, task1Duration);
        simple1Task.setId(task1Id);
        tasksManager.updateTask(simple1Task);

        final Task updateTask = tasksManager.getTask(task1Id);

        assertEquals(1, task1Id, "Не возвращён ИД задачи!");

        assertNotNull(updateTask, "Задача не найдена!");
        assertEquals(simple1Task, updateTask, "Задачи не совпадают!");

        final List<SimpleTask> tasks = tasksManager.getAllTasks();

        assertNotNull(tasks, "Задача не возвращена!");
        assertEquals(simple1Task, tasks.get(0), "Задачи не совпадают!!");
        assertEquals(1, tasks.size(), "Неверное количество задач!");
    }

    @Test
    void updateSubtask() throws TasksValidateException {
        epic1Id = tasksManager.addEpic(epic1);
        subtask1.setEpicId(epic1Id);
        subtask1Id = tasksManager.addSubtask(subtask1);

        LocalDateTime subtask1LocalDateTime = LocalDateTime.of(2010, 1, 6, 0, 0, 0);
        Duration subtask1Duration = Duration.ofMinutes(90);
        Subtask sub2task = new Subtask("2подзадача", "2тестовая", StatusTask.DONE, subtask1LocalDateTime, subtask1Duration, epic1Id);
        sub2task.setId(subtask1Id);
        tasksManager.updateSubtask(sub2task);

        final Subtask updateSubtask = tasksManager.getSubtask(subtask1Id);

        assertEquals(2, subtask1Id, "Не возвращён ИД подзадачи!");

        assertNotNull(updateSubtask, "Подзадача не найдена!");
        assertEquals(sub2task, updateSubtask, "Подзадачи не совпадают!");

        final List<Subtask> subtasks = tasksManager.getAllSubtasks();

        assertNotNull(subtasks, "Подзадача не возвращена!");
        assertEquals(sub2task, subtasks.get(0), "Подзадачи не совпадают!!");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач!");
        assertEquals(1, sub2task.getEpicId(), "Неверный ИД эпика подзадачи!");

    }

    @Test
    void updateEpicTask() throws TasksValidateException {
        epic1Id = tasksManager.addEpic(epic1);

        LocalDateTime epic1LocalDateTime = LocalDateTime.of(2010, 2, 7, 0, 0, 0);
        Duration epic1Duration = Duration.ofMinutes(30);
        Epic e1pic = new Epic("1эпик", "1эпик", StatusTask.NEW, epic1LocalDateTime, epic1Duration);
        e1pic.setId(epic1Id);
        tasksManager.updateEpicTask(e1pic);
        subtask1.setEpicId(epic1Id);
        subtask1Id = tasksManager.addSubtask(subtask1);

        Epic updateEpic = tasksManager.getEpic(epic1Id);

        assertEquals(1, epic1Id, "Не возвращён ИД эпика!");

        assertNotNull(updateEpic, "Епик не возвращён!");
        assertEquals(e1pic, updateEpic, "Епики не совпадают!");

        final List<Epic> epics = tasksManager.getAllEpics();

        assertNotNull(epics, "Епик не возвращён!");
        assertEquals(e1pic, epics.get(0), "Епики не совпадают!");
        assertEquals(1, epics.size(), "Неверное количество эпиков!");

        assertEquals(2, subtask1Id, "Неверный ИД входящей в эпик подзадачи!");
    }


    @Test
    void removeTaskById() throws TasksValidateException {
        task1Id = tasksManager.addTask(task1);

        tasksManager.removeTaskById(task1Id);

        Assertions.assertTrue(tasksManager.getAllTasks().isEmpty(), "Задача не удалена!");
    }

    @Test
    void removeSubtaskById() throws TasksValidateException {
        epic1Id = tasksManager.addEpic(epic1);
        subtask1.setEpicId(epic1Id);
        subtask1Id = tasksManager.addSubtask(subtask1);

        tasksManager.removeSubtaskById(subtask1Id);

        Assertions.assertTrue(tasksManager.getAllSubtasks().isEmpty(), "Подзадача не удалена!");
        Assertions.assertTrue(epic1.getSubtaskIds().isEmpty(), "Подзадача не удалена из списка подзадачь эпика!");
    }

    @Test
    void removeEpicById() throws TasksValidateException {
        epic1Id = tasksManager.addEpic(epic1);
        subtask1.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask1);

        tasksManager.removeEpicById(epic1Id);

        Assertions.assertTrue(tasksManager.getAllEpics().isEmpty(), "Епик не удален!");
        Assertions.assertTrue(tasksManager.getAllSubtasks().isEmpty(), "Подзадача не удалена вместе с эпиком!");
    }

    @Test
    void removeAllTasks() throws TasksValidateException {
        tasksManager.addTask(task1);
        tasksManager.addTask(task2);

        tasksManager.removeAllTasks();

        Assertions.assertTrue(tasksManager.getAllTasks().isEmpty(), "Все задачи не удалены!");
    }

    @Test
    void removeAllSubtasks() throws TasksValidateException {
        epic1Id = tasksManager.addEpic(epic1);
        subtask1.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask1);
        subtask2.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask2);

        tasksManager.removeAllSubtasks();

        Assertions.assertTrue(tasksManager.getAllSubtasks().isEmpty(), "Все подзадачи не удалены!");
        Assertions.assertTrue(epic1.getSubtaskIds().isEmpty(), "Подзадачи не удалены из состава эпика!");
    }

    @Test
    void removeAllEpics() throws TasksValidateException {
        epic1Id = tasksManager.addEpic(epic1);
        subtask2.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask2);

        tasksManager.removeAllEpics();

        Assertions.assertTrue(tasksManager.getAllEpics().isEmpty(), "Все эпики не удалены!");
        Assertions.assertTrue(tasksManager.getAllSubtasks().isEmpty(), "Подзадачи из состава эпика не удалены!");
    }

    @Test
    void getAllTasks() throws TasksValidateException {
        tasksManager.addTask(task1);
        tasksManager.addTask(task2);

        List<SimpleTask> allTasks = new ArrayList<>();
        allTasks.add(task1);
        allTasks.add(task2);

        List<SimpleTask> allReturnTasks = tasksManager.getAllTasks();

        Assertions.assertArrayEquals(allTasks.toArray(), allReturnTasks.toArray(), "Списки задач не совпадают!");
    }

    @Test
    void getAllSubtasks() throws TasksValidateException {
        epic1Id = tasksManager.addEpic(epic1);
        subtask2.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask2);
        subtask3.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask3);

        List<Subtask> allSubtasks = new ArrayList<>();
        allSubtasks.add(subtask2);
        allSubtasks.add(subtask3);

        List<Subtask> allReturnSubtasks = tasksManager.getAllSubtasks();

        Assertions.assertArrayEquals(allSubtasks.toArray(), allReturnSubtasks.toArray(), "Списки подзадач не совпадают!");
    }

    @Test
    void getAllEpics() {
        tasksManager.removeAllEpics();

        tasksManager.addEpic(epic1);
        tasksManager.addEpic(epic2);

        List<Epic> allEpic = new ArrayList<>();
        allEpic.add(epic1);
        allEpic.add(epic2);

        List<Epic> allReturnEpic = tasksManager.getAllEpics();

        Assertions.assertArrayEquals(allEpic.toArray(), allReturnEpic.toArray(), "Списки эпиков не совпадают!");
    }

    @Test
    void getTask() throws TasksValidateException {
        task1Id = tasksManager.addTask(task1);
        Task savedTask = tasksManager.getTask(task1Id);

        assertNotNull(savedTask, "Задача не возвращается!");
        assertEquals(task1, savedTask, "Задачи не совпадают!");
    }

    @Test
    void getSubtask() throws TasksValidateException {
        epic1Id = tasksManager.addEpic(epic1);
        subtask1.setEpicId(epic1Id);
        subtask1Id = tasksManager.addSubtask(subtask1);

        Subtask savedSubtask = tasksManager.getSubtask(subtask1Id);

        assertNotNull(savedSubtask, "Подзадача не возвращается!");
        assertEquals(subtask1, savedSubtask, "Подзадачи не совпадают!");
    }

    @Test
    void getEpic() {
        epic1Id = tasksManager.addEpic(epic1);

        Epic savedEpic = tasksManager.getEpic(epic1Id);

        assertNotNull(savedEpic, "Эпик не возвращается!");
        assertEquals(epic1, savedEpic, "Эпики не совпадают!");
    }

    @Test
    void getEpicSubtasks() throws TasksValidateException {
        epic1Id = tasksManager.addEpic(epic1);
        subtask1.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask1);
        subtask2.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask2);
        subtask3.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask3);

        assertEquals(StatusTask.NEW, epic1.getStatus());
    }

    @Test
    void getPrioritizedTasks() throws TasksValidateException {
        epic1Id = tasksManager.addEpic(epic1);
        subtask1.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask1);
        subtask2.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask2);
        subtask3.setEpicId(epic1Id);
        tasksManager.addSubtask(subtask3);
        tasksManager.addTask(task1);
        tasksManager.addTask(task2);

        List<Task> allTasksSort = new ArrayList<>();

        allTasksSort.add(subtask1);
        allTasksSort.add(subtask2);
        allTasksSort.add(subtask3);
        allTasksSort.add(task1);
        allTasksSort.add(task2);

        System.out.println(tasksManager.getPrioritizedTasks());
        Assertions.assertArrayEquals(allTasksSort.toArray(), tasksManager.getPrioritizedTasks().toArray(), "Не совпадают отсортированные списки!");
    }

    @Test
    void taskValidateExceptionTasks() {

        TasksValidateException exc = assertThrows(TasksValidateException.class, new Executable() {

            TasksManager tasksManager = new InMemoryTasksManager();

            @Override
            public void execute() throws TasksValidateException {
                final LocalDateTime epic10LocalDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
                final Duration epic10Duration = Duration.ofMinutes(90);
                final Epic epic10 = new Epic("Эпик1", "Проверочный", StatusTask.NEW, epic10LocalDateTime, epic10Duration);
                final int epic10Id = tasksManager.addEpic(epic10);
                final LocalDateTime subtask11LocalDateTime = LocalDateTime.of(2020, 1, 2, 0, 0, 0);
                final Duration subtask11Duration = Duration.ofMinutes(90);
                final Subtask subtask11 = new Subtask("подзадача1", "проверочная", StatusTask.NEW, subtask11LocalDateTime, subtask11Duration, epic10Id);

                final LocalDateTime subtask12LocalDateTime = LocalDateTime.of(2020, 1, 2, 0, 0, 0);
                final Duration subtask12Duration = Duration.ofMinutes(1000);
                final Subtask subtask12 = new Subtask("подзадача2", "проверочная", StatusTask.NEW, subtask12LocalDateTime, subtask12Duration, epic10Id);
                tasksManager.addSubtask(subtask11);
                tasksManager.addSubtask(subtask12);
            }
        });
        assertEquals("Данный временной промежуток занят!", exc.getMessage());
    }
}



