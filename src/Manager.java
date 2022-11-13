import java.util.HashMap;
import java.util.ArrayList;

public class Manager {
    protected HashMap<Integer, SimpleTask> simpleTask = new HashMap<>();
    protected HashMap<Integer, SubTask> subTask = new HashMap<>();
    protected HashMap<Integer, EpicTask> epicTask = new HashMap<>();
    protected ArrayList<Integer> subIdList = new ArrayList<>();                // Для накопления и записи списка подзадач в составной задаче
    protected ArrayList<Integer> updateSubIdList = new ArrayList<>();          // Для обновления составной задачи
    protected int subEpic;                                                     // Для переопределения подзадачи
    protected int nextTask = 1;

    public void addSimpleTask(SimpleTask simTask) {                            // Добавляем простую задачу (приходит со значением null в idTask)
        simTask.setIdTask(nextTask++);                                         // и присваиваем idTask.
        simpleTask.put(simTask.getIdTask(), simTask);
    }

    public void addSubTask(SubTask sTask) {                                    // Добавляем подзадачу (приходит со значением null в idTask)
        subIdList.add(nextTask);                                               // и присваиваем idTask.
        sTask.setIdTask(nextTask++);                                           // Собираем в список idTask для передачи в epicTask.
        subTask.put(sTask.getIdTask(), sTask);
    }

    public void addEpicTask(EpicTask eTask) {                                  // Добавляем составную задачу (приходит со значением null в idTask)
        eTask.setIdTask(nextTask);                                             // и присваиваем idTask.
        eTask.setIdList(subIdList);                                            // Присваиваем статус на основе статуса подзадач.
        epicTask.put(eTask.getIdTask(), eTask);                                // Сообщаем подзадачам Id составной задачи.
        checkStatus(nextTask);
        informEachOther(nextTask++);
    }

    public void updateSimpleTask(SimpleTask simTask) {                         // Обновляем простую задачу.
        simpleTask.put(simTask.getIdTask(), simTask);
    }

    public void updateSubTask(SubTask sTask) {                                 // Обновляем подзадачу.
        subEpic = subTask.get(sTask.getIdTask()).getIdEpic();
        subTask.put(sTask.getIdTask(), sTask);
        subTask.get(sTask.getIdTask()).setIdEpic(subEpic);
    }

    public void updateEpicTask(EpicTask eTask) {                               // Обновляем составную задачу.
        updateSubIdList(eTask.getIdTask());
        epicTask.put(eTask.getIdTask(), eTask);
        eTask.setIdList(updateSubIdList);
        checkStatus(eTask.getIdTask());                                        // Обновляем статус по статусу подзадач.
        clearUpdateSubIdList();
    }

    public void deleteAllTask() {                                              // 2.2 Удаление всех задач.
        simpleTask.clear();                                                    // Очищаем таблицу простых задач.
        subTask.clear();                                                       // Очищаем таблицу подзадач.
        epicTask.clear();                                                      // Очищаем таблицу составных задач.
    }

    public void removeByIdSimpleTask(int id) {                                 // 2.6 Удаляем простую задачу по Id.
        simpleTask.remove(id);
    }

    public void removeByIdSubTask(Integer id) {                                // 2.6 Удаляем подзадачу по Id.
        Integer idEpic = subTask.get(id).getIdEpic();
        epicTask.get(idEpic).removeIdList(id);
        subTask.remove(id);
    }

    public void removeByIdEpicTask(int id) {                                   //  2.6 Удаляем составную задачу по Id.
        for (Integer idSub: epicTask.get(id).getIdList()){                     // Также удаляем её подзадачи.
            subTask.remove(idSub);
        }
        epicTask.remove(id);
    }

    public void printAllTask() {                                               // 2.1 Получение списка всех задач.
        for (Integer id: simpleTask.keySet()) {
            System.out.println("Задача " + id);
            System.out.println("Имя " + simpleTask.get(id).getNameTask());
            System.out.println("Описание " + simpleTask.get(id).getDescriptionTask());
            System.out.println("ID " + simpleTask.get(id).getIdTask());
            System.out.println("Статус " + simpleTask.get(id).getStatusTask() + "\n");
        }

        for (Integer id: epicTask.keySet()) {
            System.out.println("    Составная задача " + id);
            System.out.println("    Имя " + epicTask.get(id).getNameTask());
            System.out.println("    Описание " + epicTask.get(id).getDescriptionTask());
            System.out.println("    ID " + epicTask.get(id).getIdTask());
            System.out.println("    Входят ID " + epicTask.get(id).getIdList());
            System.out.println("    Статус " + epicTask.get(id).getStatusEpicTask() + "\n");
            printSubTaskByEpicId(id);
        }
    }

    public void printTask(int idSimpleTask) {                                  // 2.3 Получение простых задачь по идентификатору.
            System.out.println("Задача " + idSimpleTask);
            System.out.println("Имя " + simpleTask.get(idSimpleTask).getNameTask());
            System.out.println("Описание " + simpleTask.get(idSimpleTask).getDescriptionTask());
            System.out.println("ID " + simpleTask.get(idSimpleTask).getIdTask());
            System.out.println("Статус " + simpleTask.get(idSimpleTask).getStatusTask() + "\n");

    }

    public void printEpicTask(int idEpicTask) {                                // 2.3 Получение составных задачь по идентификатору.
            System.out.println("    Составная задача " + idEpicTask);
            System.out.println("    Имя " + epicTask.get(idEpicTask).getNameTask());
            System.out.println("    Описание " + epicTask.get(idEpicTask).getDescriptionTask());
            System.out.println("    ID " + epicTask.get(idEpicTask).getIdTask());
            System.out.println("    Статус " + epicTask.get(idEpicTask).getStatusEpicTask() + "\n");

    }

    public void printSubTask(int idSubTask) {                                  // 2.3 Получение подзадачь по идентификатору.
        System.out.println("        " + "Имя " + subTask.get(idSubTask).getNameTask());
        System.out.println("        " + "Описание " + subTask.get(idSubTask).getDescriptionTask());
        System.out.println("        " + "ID " + subTask.get(idSubTask).getIdTask());
        System.out.println("        " + "ID составной задачи " + subTask.get(idSubTask).getIdEpic());
        System.out.println("        " + "Статус " + subTask.get(idSubTask).getStatusSubTask() + "\n");
    }

    public void printSubTaskByEpicId(Integer idEpic) {                         // 3.1 Получение списка всех подзадач определённого эпика.
        for (Integer idSub: epicTask.get(idEpic).getIdList()) {
            System.out.println("        " + "Имя " + subTask.get(idSub).getNameTask());
            System.out.println("        " + "Описание " + subTask.get(idSub).getDescriptionTask());
            System.out.println("        " + "ID " + subTask.get(idSub).getIdTask());
            System.out.println("        " + "ID составной задачи " + subTask.get(idSub).getIdEpic());
            System.out.println("        " + "Статус " + subTask.get(idSub).getStatusSubTask() + "\n");
        }
    }

    public void clearUpdateSubIdList() {
        updateSubIdList.clear();
    }

    private void updateSubIdList(int idEpic) {                                 // Запоминаем предыдущие subId в составе задачи
        for(Integer idSub:epicTask.get(idEpic).getIdList()) {
            int j = idSub;
            updateSubIdList.add(j);
        }
        epicTask.get(idEpic).clearIdList();
    }

    private void checkStatus(int nextTask) {                                   // Определение статуса составной
        int i = 0;                                                             // задачи по статусу подзадач.
        int j = 0;
        int list = epicTask.get(nextTask).getIdList().size();
            for (Integer idSub: epicTask.get(nextTask).getIdList()) {
                if (subTask.get(idSub).getStatusSubTask().equals("Done")) {
                    ++i;
                }

                if (subTask.get(idSub).getStatusSubTask().equals("New")) {
                    ++j;
                }
            }
                    if(list == 0) {
                        epicTask.get(nextTask).setStatusEpicTask("Done");
                    } else if (i == list) {
                        epicTask.get(nextTask).setStatusEpicTask("Done");
                    } else if (j == list) {
                        epicTask.get(nextTask).setStatusEpicTask("New");
                    } else {
                        epicTask.get(nextTask).setStatusEpicTask("inProgress");
                    }


    }


    private void informEachOther(int nextTask) {                               // Метод передачи Id составной задачи
        for (Integer id : epicTask.get(nextTask).getIdList()) {                // подзадачам по списку подзадач.
            subTask.get(id).setIdEpic(epicTask.get(nextTask).getIdTask());

        }
        subIdList.clear();                                                     // После оформления составной задачи
    }                                                                          // и обмена данными между составной задачей и подзадачей
                                                                               // зачищаем список для возможности нового набора для
                                                                               // следующей составной задачи.


}