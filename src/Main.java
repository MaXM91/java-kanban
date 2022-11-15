import Store.Epic;
import Store.Subtask;
import Store.Task;
import Managers.Manager;
public class Main {

    public static void main(String[] args) {
     printAll();
    }

    public static void printAll() {
        Manager manager = new Manager();

        Task simpleTask1 = new Task(0, "Кошка", "Серая", "New");                         //1 Задача 1
        manager.addTask(simpleTask1);

        Subtask subtask1 = new Subtask(0, "Вино", "Красное сухое", "New");               //2 Подзадача 1
        manager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask(0, "Вино", "Красное полусухое", "inProgress");    //3 Подзадача 2
        manager.addSubtask(subtask2);

        Task simpleTask2 = new Task(0, "Собака", "Черная", "New");                       //4 Задача 2
        manager.addTask(simpleTask2);

        Subtask subtask3 = new Subtask(0, "Вино", "Красное сладкое", "New");             //5 Подзадача 3
        manager.addSubtask(subtask3);

        Epic epic1 = new Epic(0, "Купить вино", "Красное", "New");                       //6 Составная 1
        manager.addEpic(epic1);

        Subtask subtask4 = new Subtask(0, "Крыло", "Заднее", "inProgress");              //7 Подзадача 3
        manager.addSubtask(subtask4);

        Epic epic2 = new Epic(0, "Покупки для велосипеда", "Крыло", "New");              //8 Составная 2
        manager.addEpic(epic2);


        System.out.println(manager.outputAllTasks());
        System.out.println(manager.outputAllSubtasks());
        System.out.println(manager.outputAllEpics());

        manager.removeTaskById(4);                                                                                // Удаляем по Ид
        manager.removeSubtaskById(3);
        manager.removeEpicById(8);

        System.out.println(manager.outputAllTasks());
        System.out.println(manager.outputAllSubtasks());
        System.out.println(manager.outputAllEpics());

        Task simple = new Task(1, "Мышка", "Серая", "New");                             // Обновляем объекты
        manager.updateTaskById(simple);

        Subtask subtask = new Subtask(2, "Квас", "Вятский", "New");
        manager.updateSubtask(subtask);

        Epic epic = new Epic(6, "В магазин за вином", "Купить разное", "New");
        manager.updateEpicTask(epic);

        System.out.println(manager.outputAllTasks());
        System.out.println(manager.outputAllSubtasks());
        System.out.println(manager.outputAllEpics());

        System.out.println("Вывод по Ид:");
        System.out.println(manager.outputTask(1));                                                         // Вывод по ид
        System.out.println(manager.outputSubtask(2));
        System.out.println(manager.outputEpic(6));

        manager.removeAllTask();

        System.out.println(manager.outputAllTasks());
        System.out.println(manager.outputAllSubtasks());
        System.out.println(manager.outputAllEpics());
    }
}