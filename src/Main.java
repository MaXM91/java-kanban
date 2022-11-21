import tasks.*;
import managers.*;


public class Main {

    public static void main(String[] args) {

        printAll();

    }


    public static void printAll() {
        Managers managers = new Managers();

        TaskManager taskManager = managers.getDefault();
        HistoryManager historyManager = managers.getDefaultHistory();

        SimpleTask simpleTask1 = new SimpleTask("Кошка", "Серая", StatusTask.NEW);                       //1 Задача 1
        SimpleTask simpleTask2 = new SimpleTask("Собака", "Черная", StatusTask.NEW);                     //2 Задача 2

        int taskId1 = taskManager.addTask(simpleTask1);
        int taskId2 = taskManager.addTask(simpleTask2);

        Epic epic1 = new Epic("Купить вино", "Красное", StatusTask.NEW);                                 //3 Составная 1
        int epicId1 = taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Вино", "Красное сухое", StatusTask.NEW, epicId1);                //4 Подзадача 1
        Subtask subtask2 = new Subtask("Вино", "Красное полусухое", StatusTask.IN_PROGRESS, epicId1);    //5 Подзадача 2
        Subtask subtask3 = new Subtask("Вино", "Красное сладкое", StatusTask.NEW, epicId1);              //6 Подзадача 3

        int sub1Id = taskManager.addSubtask(subtask1);
        int sub2Id = taskManager.addSubtask(subtask2);
        int sub3Id = taskManager.addSubtask(subtask3);

        Epic epic2 = new Epic("Покупки для велосипеда", "Крыло", StatusTask.NEW);                        //7 Составная 2

        int epicId2 = taskManager.addEpic(epic2);

        Subtask subtask4 = new Subtask("Крыло", "Заднее", StatusTask.IN_PROGRESS, epicId2);              //8 Подзадача 4

        int sub4Id = taskManager.addSubtask(subtask4);


        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getAllEpics());

        simpleTask1.setName("Собака");
        simpleTask1.setDescription("Черная");
        simpleTask1.setStatus(StatusTask.DONE);
        taskManager.updateTask(simpleTask1);

        subtask1.setName("Шоколад");
        subtask1.setDescription("Черный");
        subtask1.setStatus(StatusTask.NEW);
        taskManager.updateSubtask(subtask1);

        subtask2.setName("Шоколад");
        subtask2.setDescription("Белый");
        subtask2.setStatus(StatusTask.NEW);
        taskManager.updateSubtask(subtask2);

        simpleTask2.setName("Пиво");
        simpleTask2.setDescription("Чешское");
        simpleTask2.setStatus(StatusTask.NEW);
        taskManager.updateTask(simpleTask2);

        subtask3.setName("Колбаса");
        subtask3.setDescription("Копченая");
        subtask3.setStatus(StatusTask.NEW);
        taskManager.updateSubtask(subtask3);

        epic1.setName("Купить еду");
        epic1.setDescription("На день");
        epic1.setStatus(StatusTask.DONE);
        taskManager.updateEpicTask(epic1);

        subtask4.setName("Руль");
        subtask4.setDescription("Сплав аллюминия");
        subtask4.setStatus(StatusTask.DONE);
        taskManager.updateSubtask(subtask4);

        epic2.setName("Покупки для велосипеда");
        epic2.setDescription("Руль");
        epic2.setStatus(StatusTask.NEW);
        taskManager.updateEpicTask(epic2);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getAllEpics());

        taskManager.removeTaskById(taskId2);
        taskManager.removeSubtaskById(sub2Id);
        taskManager.removeEpicById(epicId2);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getAllEpics());

        System.out.println("Вывод по Ид:");
        System.out.println(taskManager.getTask(taskId1));
        historyManager.add(taskManager.getTask(taskId1));

            for(Task task:historyManager.getHistory()) {
                System.out.println(task);
            }

        System.out.println(taskManager.getSubtask(sub1Id));
        historyManager.add(taskManager.getSubtask(sub1Id));

            for(Task task:historyManager.getHistory()) {
                System.out.println(task);
            }

        System.out.println(taskManager.getEpic(epicId1));
        historyManager.add(taskManager.getEpic(epicId1));

            for(Task task:historyManager.getHistory()) {
                System.out.println(task);
            }

        System.out.println(taskManager.getEpicSubtasks(epicId1));





        taskManager.removeAllTasks();
        taskManager.removeAllSubtasks();
        taskManager.removeAllEpics();

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getAllEpics());
    }
}