package Managers;
import Store.Epic;
import Store.Subtask;
import Store.Task;

import java.util.HashMap;

public class Manager {
    protected int nextTask = 1;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();

    public void addTask(Task simpleTask) {                                     // Присваиваем полученной задаче Ид и
        simpleTask.setId(nextTask++);                                          // записываем в tasks.
        tasks.put(simpleTask.getId(), simpleTask);
    }

    public void addSubtask(Subtask subtask) {                                  // Присваиваем полученной подзадаче Ид,
        if (epics.size() == 0) {
            Epic epic1 = new Epic(0, "Купить вино", "Красное", "New");
            epics.put(0, epic1);
        }

        subtask.setId(nextTask);                                               // записываем Ид в список подзадач эпика под Ид 0,
        epics.get(0).setSubtaskIds(nextTask++);                                // записываем подзадачу в subtasks.
        subtasks.put(subtask.getId(), subtask);
    }

    public void addEpic(Epic epic) {                                           // Присваиваем полученной подзадаче Ид,
        epic.setId(nextTask);                                                  // заменяем список подзадач текущего Ид с Ид 0,
        epics.put(epic.getId(), epic);
        epics.get(epic.getId()).replaceSubtaskIds(epics.get(0).getSubtaskIds());   // записываем новый эпик в epics, определяем статут,
        epics.get(0).getSubtaskIds().clear();
        checkStatus(nextTask);                                                 // сообщаем входящим в состав подзадачам Ид эпика.
        informEachOther(nextTask++);
    }

    public void updateTaskById(Task task) {                                    // Обновляем простую задачу.
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {                               // Обновляем подзадачу.
        int epicId = subtasks.get(subtask.getId()).getEpicId();
        subtasks.put(subtask.getId(), subtask);
        subtasks.get(subtask.getId()).setEpicId(epicId);
    }

    public void updateEpicTask(Epic epic) {                                    // Обновляем составную задачу.
        int epicId = epic.getId();
        System.out.println(epics.get(0).getSubtaskIds());
        epics.get(0).replaceSubtaskIds(epics.get(epicId).getSubtaskIds());
        epics.put(epic.getId(), epic);
        epics.get(epicId).replaceSubtaskIds(epics.get(0).getSubtaskIds());
        checkStatus(epicId);                                                   // Обновляем статус по статусу подзадач.
        epics.get(0).getSubtaskIds().clear();
    }

    public void removeTaskById(int id) {                                       // 2.6 Удаляем простую задачу по Id.
        tasks.remove(id);
    }

    public void removeSubtaskById(Integer id) {                                // 2.6 Удаляем подзадачу по Id.
        int idEpic = subtasks.get(id).getEpicId();
        epics.get(idEpic).removeSubtaskId(id);
        subtasks.remove(id);
        checkStatus(idEpic);
    }

    public void removeEpicById(int id) {                                       //  2.6 Удаляем составную задачу по Id.
        for (Integer idSub: epics.get(id).getSubtaskIds()){                    // Также удаляем её подзадачи.
            subtasks.remove(idSub);
        }
        epics.remove(id);
    }

    public void removeAllTask() {                                              // Удаление всех задач
        tasks.clear();
        subtasks.clear();
        epics.clear();
    }

    public HashMap<Integer, Task> outputAllTasks() {                           // Возвращаем список задач
        return tasks;
    }

    public HashMap<Integer, Subtask> outputAllSubtasks() {                     // Возвращаем список подзадач
        return subtasks;
    }

    public HashMap<Integer, Epic> outputAllEpics() {                           // Возвращаем список эпиков
        HashMap<Integer, Epic> outputEpic = new HashMap<>();
        int j;
            for (Integer i: epics.keySet()) {
                j = i;
                outputEpic.put(j, epics.get(i));
            }
            outputEpic.remove(0);
        return outputEpic;
    }

    public Task outputTask(Integer taskId) {                                   // Возвращаем задачу по ИД
        return tasks.get(taskId);
    }

    public Subtask outputSubtask(Integer subtaskId) {                          // Возвращаем подзадачу по ИД
        return subtasks.get(subtaskId);
    }

    public Epic outputEpic(Integer epicId) {                                   // Возвращаем составную задачу по ИД
        return epics.get(epicId);
    }


    private void checkStatus(int nextTask) {                                   // Определение статуса составной
        int i = 0;                                                             // задачи по статусу подзадач.
        int j = 0;

        int list = epics.get(nextTask).getSubtaskIds().size();
            for (Integer idSub: epics.get(nextTask).getSubtaskIds()) {
                if (subtasks.get(idSub).getStatus().equals("Done")) {
                    ++i;
                }

                if (subtasks.get(idSub).getStatus().equals("New")) {
                    ++j;
                }
            }
                    if(list == 0) {
                        epics.get(nextTask).setStatus("Done");
                    } else if (i == list) {
                        epics.get(nextTask).setStatus("Done");
                    } else if (j == list) {
                        epics.get(nextTask).setStatus("New");
                    } else {
                        epics.get(nextTask).setStatus("inProgress");
                    }


    }

    private void informEachOther(int nextTask) {                               // Метод передачи Id составной задачи
        for (Integer id : epics.get(nextTask).getSubtaskIds()) {               // подзадачам по списку подзадач.
            subtasks.get(id).setEpicId(epics.get(nextTask).getId());
        }
    }

}