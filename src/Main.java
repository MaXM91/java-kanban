public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        // Ввод данных
        SimpleTask simpleTask1 = new SimpleTask("Поход в магазин","за продуктами",0 , "New");     // Задача 1
        manager.addSimpleTask(simpleTask1);

        SubTask subTask1 = new SubTask("Купить вино","красное полусладкое",0 , "inProgress");  // Подзадача 1
        manager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Купить сыр","какой-нибудь",0 , "New");                 // Подзадача 2
        manager.addSubTask(subTask2);

        SimpleTask simpleTask2 = new SimpleTask("Поход на шашлыки","лучше завтра",0 , "New");    // Задача 2
        manager.addSimpleTask(simpleTask2);

        SubTask subTask3 = new SubTask("Купить колбасу","копчёную",0 , "New");                 // Подзадача 3
        manager.addSubTask(subTask3);

        EpicTask epicTask1 = new EpicTask("Посидеть вместе","почаще",0);                                   // Составная 1
        manager.addEpicTask(epicTask1);

        SubTask subTask4 = new SubTask("Купить педали","с фиксацией ступни",0 , "Done");       // Подзадача 3
        manager.addSubTask(subTask4);

        EpicTask epicTask2 = new EpicTask("Починить велосипед","замена педалей",0);                        // Составная 2
        manager.addEpicTask(epicTask2);

        // Вывод данных
        manager.printAllTask();                 // Выводим все данные
        System.out.println(manager.epicTask.get(6).getIdList()+"\n");    // Проверяем таблицы подзадачь составных задачь
        System.out.println(manager.epicTask.get(8).getIdList()+"\n");    // Проверяем таблицы подзадачь составных задачь
        manager.printTask(1);        // Простая задача по идентификатору
        manager.printEpicTask(8);      // Составная задача по идентификатору
        manager.printSubTask(3);       // Подзадача по идентификатору
        manager.printSubTaskByEpicId(6);  // Список подзадачь по иду составной задачи




        // Обновление
        simpleTask1 = new SimpleTask("Поход","в лес",1 , "inProgress");     // Перезапись простой задачи
        manager.updateSimpleTask(simpleTask1);

        subTask2 = new SubTask("Купить","руль",2 , "New");                // Перезапись подзадачи
        manager.updateSubTask(subTask2);

        epicTask1 = new EpicTask("Купить колбасу","из бобра",6);                      // Перезапись составной задачи
        manager.updateEpicTask(epicTask1);

        manager.removeByIdSimpleTask(4);                                                                          // Удаление простой задачи по ид
        manager.removeByIdEpicTask(8);                                                                            // Удаление составной задачи по ид
        manager.removeByIdSubTask(3);                                                                             // Удаление подзадачи по ид

        //Вывод данных
        manager.printAllTask();
        manager.deleteAllTask();

    }

}
