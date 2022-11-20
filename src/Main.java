import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import managers.Manager;

public class Main {

    public static void main(String[] args) {
        printAll();
    }

    public static void printAll() {
        Manager manager = new Manager();

        // Создаем объекты

        Task task1 = new Task(0, "Кошка", "Серая", "New");                         //1 Задача 1
        Task task2 = new Task(0, "Собака", "Черная", "New");                       //4 Задача 2
        Subtask subtask1 = new Subtask(0, "Вино", "Красное сухое", "New");               //2 Подзадача 1
        Subtask subtask2 = new Subtask(0, "Вино", "Красное полусухое", "inProgress");    //3 Подзадача 2
        Subtask subtask3 = new Subtask(0, "Вино", "Красное сладкое", "New");             //5 Подзадача 3
        Epic epic1 = new Epic(0, "Купить вино", "Красное", "New");                       //6 Составная 1
        Subtask subtask4 = new Subtask(0, "Крыло", "Заднее", "inProgress");              //7 Подзадача 3
        Epic epic2 = new Epic(0, "Покупки для велосипеда", "Крыло", "New");              //8 Составная 2

        // Добавляем объекты (присваиваются Ид)

        int taksId1 = manager.addTask(task1);

        int taskId2 = manager.addTask(task2);

        int epicId1 = manager.addEpic(epic1);

        int sub1Id = manager.addSubtask(subtask1, epicId1);

        int sub2Id = manager.addSubtask(subtask2, epicId1);

        int sub3Id = manager.addSubtask(subtask3, epicId1);

        int epicId2 = manager.addEpic(epic2);

        int sub4Id = manager.addSubtask(subtask4, epicId2);


        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtasks());
        System.out.println(manager.getAllEpics());

        task1.setName("Собака");
        task1.setDescription("Черная");
        task1.setStatus("Done");
        manager.updateTask(task1);

        subtask1.setName("Шоколад");
        subtask1.setDescription("Черный");
        subtask1.setStatus("New");
        manager.updateSubtask(subtask1);

        subtask2.setName("Шоколад");
        subtask2.setDescription("Белый");
        subtask2.setStatus("New");
        manager.updateSubtask(subtask2);

        task2.setName("Пиво");
        task2.setDescription("Чешское");
        task2.setStatus("New");
        manager.updateTask(task2);

        subtask3.setName("Колбаса");
        subtask3.setDescription("Копченая");
        subtask3.setStatus("New");
        manager.updateSubtask(subtask3);

        epic1.setName("Купить еду");
        epic1.setDescription("На день");
        epic1.setStatus("New");
        manager.updateEpicTask(epic1);

        subtask4.setName("Руль");
        subtask4.setDescription("Сплав аллюминия");
        subtask4.setStatus("Done");
        manager.updateSubtask(subtask4);

        epic2.setName("Покупки для велосипеда");
        epic2.setDescription("Руль");
        epic2.setStatus("New");
        manager.updateEpicTask(epic2);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtasks());
        System.out.println(manager.getAllEpics());

        manager.removeTaskById(taskId2);
        manager.removeSubtaskById(sub2Id);
        manager.removeEpicById(epicId2);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtasks());
        System.out.println(manager.getAllEpics());

        System.out.println("Вывод по Ид:");
        System.out.println(manager.getTask(taksId1));
        System.out.println(manager.getSubtask(sub1Id));
        System.out.println(manager.getEpic(epicId1));
        System.out.println(manager.getEpicSubtasks(epicId1));

        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtasks());
        System.out.println(manager.getAllEpics());
    }
}