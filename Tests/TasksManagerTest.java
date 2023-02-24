
import managers.*;
import managers.exceptions.TaskNotFoundException;
import managers.exceptions.TasksValidateException;
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
import java.util.Map;
import java.util.TreeMap;

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
    void addTask() throws TasksValidateException, TaskNotFoundException {
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
    void updateTask() throws TasksValidateException, TaskNotFoundException {
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
    void updateSubtask() throws TasksValidateException, TaskNotFoundException {
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
    void updateEpicTask() throws TasksValidateException, TaskNotFoundException {
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
    void removeTaskById() throws TasksValidateException, TaskNotFoundException {
        task1Id = tasksManager.addTask(task1);

        tasksManager.removeTaskById(task1Id);

        Assertions.assertTrue(tasksManager.getAllTasks().isEmpty(), "Задача не удалена!");
    }

    @Test
    void removeSubtaskById() throws TasksValidateException, TaskNotFoundException {
        epic1Id = tasksManager.addEpic(epic1);
        subtask1.setEpicId(epic1Id);
        subtask1Id = tasksManager.addSubtask(subtask1);

        tasksManager.removeSubtaskById(subtask1Id);

        Assertions.assertTrue(tasksManager.getAllSubtasks().isEmpty(), "Подзадача не удалена!");
        Assertions.assertTrue(epic1.getSubtaskIds().isEmpty(), "Подзадача не удалена из списка подзадачь эпика!");
    }

    @Test
    void removeEpicById() throws TasksValidateException, TaskNotFoundException {
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
    void getTask() throws TasksValidateException, TaskNotFoundException {
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

        Map<LocalDateTime, Task> allTasksSort = new TreeMap<>();

        allTasksSort.put(subtask1.getStartDateTime(), subtask1);
        allTasksSort.put(subtask2.getStartDateTime(), subtask2);
        allTasksSort.put(subtask3.getStartDateTime(), subtask3);
        allTasksSort.put(task1.getStartDateTime(), task1);
        allTasksSort.put(task2.getStartDateTime(), task2);

        System.out.println(tasksManager.getPrioritizedTasks());
        System.out.println(tasksManager.getAllSubtasks());
        Assertions.assertArrayEquals(new Map[]{allTasksSort}, new Map[]{tasksManager.getPrioritizedTasks()}, "Не совпадают отсортированные списки!");
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
                final LocalDateTime subtask11LocalDateTime = LocalDateTime.of(2020, 1, 2, 2, 3, 0);
                final Duration subtask11Duration = Duration.ofMinutes(90);
                final Subtask subtask11 = new Subtask("подзадача11", "проверочная", StatusTask.NEW, subtask11LocalDateTime, subtask11Duration, epic10Id);

                final LocalDateTime subtask12LocalDateTime = LocalDateTime.of(2020, 1, 2, 2, 3, 0);
                final Duration subtask12Duration = Duration.ofMinutes(90);
                final Subtask subtask12 = new Subtask("подзадача12", "проверочная", StatusTask.NEW, subtask12LocalDateTime, subtask12Duration, epic10Id);
                tasksManager.addSubtask(subtask11);
                tasksManager.addSubtask(subtask12);
            }
        });
        assertEquals("Данный временной промежуток занят!", exc.getMessage());

        tasksManager.removeAllTasks();
        tasksManager.removeAllSubtasks();
        tasksManager.removeAllEpics();

        TasksValidateException exc1 = assertThrows(TasksValidateException.class, new Executable() {
            TasksManager tasksManager = new InMemoryTasksManager();

            @Override
            public void execute() throws TasksValidateException {
                final LocalDateTime epic20LocalDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
                final Duration epic20Duration = Duration.ofMinutes(90);
                final Epic epic20 = new Epic("Эпик1", "Проверочный", StatusTask.NEW, epic20LocalDateTime, epic20Duration);
                final int epic20Id = tasksManager.addEpic(epic20);

                final LocalDateTime subtask21LocalDateTime = LocalDateTime.of(2020, 1, 2, 2, 3, 0);
                final Duration subtask21Duration = Duration.ofMinutes(600);
                final Subtask subtask21 = new Subtask("подзадача21", "проверочная", StatusTask.NEW, subtask21LocalDateTime, subtask21Duration, epic20Id);

                final LocalDateTime subtask22LocalDateTime = LocalDateTime.of(2020, 1, 2, 12, 3, 0);
                final Duration subtask22Duration = Duration.ofMinutes(90);
                final Subtask subtask22 = new Subtask("подзадача22", "проверочная", StatusTask.NEW, subtask22LocalDateTime, subtask22Duration, epic20Id);
                tasksManager.addSubtask(subtask21);
                tasksManager.addSubtask(subtask22);
            }
        });
        assertEquals("Данный временной промежуток занят!", exc1.getMessage());

        tasksManager.removeAllTasks();
        tasksManager.removeAllSubtasks();
        tasksManager.removeAllEpics();

        TasksValidateException exc2 = assertThrows(TasksValidateException.class, new Executable() {
            TasksManager tasksManager = new InMemoryTasksManager();

            @Override
            public void execute() throws TasksValidateException {
                final LocalDateTime epic30LocalDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
                final Duration epic30Duration = Duration.ofMinutes(90);
                final Epic epic30 = new Epic("Эпик1", "Проверочный", StatusTask.NEW, epic30LocalDateTime, epic30Duration);
                final int epic30Id = tasksManager.addEpic(epic30);

                final LocalDateTime subtask31LocalDateTime = LocalDateTime.of(2020, 1, 2, 12, 3, 0);
                final Duration subtask31Duration = Duration.ofMinutes(600);
                final Subtask subtask31 = new Subtask("подзадача31", "проверочная", StatusTask.NEW, subtask31LocalDateTime, subtask31Duration, epic30Id);

                final LocalDateTime subtask32LocalDateTime = LocalDateTime.of(2020, 1, 2, 2, 3, 0);
                final Duration subtask32Duration = Duration.ofMinutes(600);
                final Subtask subtask32 = new Subtask("подзадача32", "проверочная", StatusTask.NEW, subtask32LocalDateTime, subtask32Duration, epic30Id);
                tasksManager.addSubtask(subtask31);
                tasksManager.addSubtask(subtask32);
            }
        });
        assertEquals("Данный временной промежуток занят!", exc2.getMessage());

        tasksManager.removeAllTasks();
        tasksManager.removeAllSubtasks();
        tasksManager.removeAllEpics();

        TasksValidateException exc3 = assertThrows(TasksValidateException.class, new Executable() {
            TasksManager tasksManager = new InMemoryTasksManager();

            @Override
            public void execute() throws TasksValidateException {
                final LocalDateTime epic35LocalDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
                final Duration epic35Duration = Duration.ofMinutes(90);
                final Epic epic35 = new Epic("Эпик1", "Проверочный", StatusTask.NEW, epic35LocalDateTime, epic35Duration);
                final int epic35Id = tasksManager.addEpic(epic35);

                final LocalDateTime subtask36LocalDateTime = LocalDateTime.of(2020, 1, 2, 2, 3, 0);
                final Duration subtask36Duration = Duration.ofMinutes(600);
                final Subtask subtask36 = new Subtask("подзадача36", "проверочная", StatusTask.NEW, subtask36LocalDateTime, subtask36Duration, epic35Id);

                final LocalDateTime subtask37LocalDateTime = LocalDateTime.of(2020, 1, 2, 3, 3, 0);
                final Duration subtask37Duration = Duration.ofMinutes(300);
                final Subtask subtask37 = new Subtask("подзадача37", "проверочная", StatusTask.NEW, subtask37LocalDateTime, subtask37Duration, epic35Id);
                tasksManager.addSubtask(subtask36);
                tasksManager.addSubtask(subtask37);
            }
        });
        assertEquals("Данный временной промежуток занят!", exc3.getMessage());

        tasksManager.removeAllTasks();
        tasksManager.removeAllSubtasks();
        tasksManager.removeAllEpics();

        TasksValidateException exc5 = assertThrows(TasksValidateException.class, new Executable() {
            TasksManager tasksManager = new InMemoryTasksManager();

            @Override
            public void execute() throws TasksValidateException {
                LocalDateTime task12LocalDateTime = LocalDateTime.of(2010, 1, 22, 8, 0, 0);
                Duration task12Duration = Duration.ofMinutes(600);
                SimpleTask task12 = new SimpleTask("задача2", "тестовая2", StatusTask.NEW, task12LocalDateTime, task12Duration);

                LocalDateTime task21LocalDateTime = LocalDateTime.of(2010, 1, 22, 18, 0, 0);
                Duration task21Duration = Duration.ofMinutes(90);
                SimpleTask task21 = new SimpleTask("задача2", "тестовая2", StatusTask.NEW, task21LocalDateTime, task21Duration);

                tasksManager.addTask(task12);
                tasksManager.addTask(task21);
            }
        });
        assertEquals("Данный временной промежуток занят!", exc5.getMessage());
    }
}



