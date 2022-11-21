package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private int nextTask = 1;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();

    public Integer addTask(Task task) {                                        // Присваиваем полученной задаче Ид и
        task.setId(nextTask++);                                                // записываем в tasks.
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public Integer addSubtask(Subtask subtask) {                               // Присваиваем полученной подзадаче Ид,
        subtask.setId(nextTask++);                                             // Присваиваем Ид.
        epics.get(subtask.getEpicId()).setSubtaskIds(subtask.getId());         // Записываем Ид подзадачи в список составной задачи.
        subtasks.put(subtask.getId(), subtask);                                // Записываем подзадачу в subtasks.
        updateEpicStatus(subtask.getEpicId());                                 // Переопределяем статус составной задачи в
        return subtask.getId();                                                // которую входит эта подзадача. Возвращаем Ид.
    }

    public Integer addEpic(Epic epic) {                                        // Присваиваем полученной составной задаче Ид,
        epic.setId(nextTask++);                                                // записываем в epics.
        epics.put(epic.getId(), epic);                                         // Возвращаем Ид.
        return epic.getId();
    }

    public void updateTask(Task task) {                                        // Обновляем простую задачу.
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {                               // Обновляем подзадачу.
        if (subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    public void updateEpicTask(Epic epic) {                                    // Обновляем составную задачу.
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);                                     // Обновляем статус по статусу подзадач.
        }
    }

    public void removeTaskById(int id) {                                       // 2.6 Удаляем простую задачу по Id.
        tasks.remove(id);
    }

    public void removeSubtaskById(Integer id) {                                // 2.6 Удаляем подзадачу по Id.
        int idEpic = subtasks.get(id).getEpicId();                             // Обновляем статус составной задачи.
        epics.get(idEpic).removeSubtaskId(id);
        subtasks.remove(id);
        updateEpicStatus(idEpic);
    }

    public void removeEpicById(int id) {                                       //  2.6 Удаляем составную задачу по Id.
        for (Integer idSub : epics.get(id).getSubtaskIds()) {                  // Также удаляем её подзадачи.
            subtasks.remove(idSub);
        }
        epics.remove(id);
    }

    public void removeAllTasks() {                                             // Удаление всех задач.
        tasks.clear();
    }

    public void removeAllSubtasks() {                                          // Удаление всех подзадач.
        for (Integer subtaskId : subtasks.keySet()) {                          // Обновляем статус составной задачи.
            if (epics.get(subtasks.get(subtaskId).getEpicId()).getSubtaskIds().contains(subtaskId)) {
                epics.get(subtasks.get(subtaskId).getEpicId()).removeSubtaskId(subtaskId);
                updateEpicStatus(subtasks.get(subtaskId).getEpicId());
            }
        }
        subtasks.clear();

    }

    public void removeAllEpics() {                                             // Удаление всех составных задач
        for (Integer epicId : epics.keySet()) {
            for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
                    subtasks.remove(subtaskId);
                }
        }
        epics.clear();
    }

    public List<Task> getAllTasks() {                                          // Возвращаем список задач
        return new ArrayList<>(tasks.values());
    }

    public List<Subtask> getAllSubtasks() {                                    // Возвращаем список подзадач
        return new ArrayList<>(subtasks.values());
    }

    public List<Epic> getAllEpics() {                                          // Возвращаем список эпиков
        return new ArrayList<>(epics.values());
    }

    public Task getTask(Integer taskId) {                                      // Возвращаем задачу по ИД
        return tasks.get(taskId);
    }

    public Subtask getSubtask(Integer subtaskId) {                             // Возвращаем подзадачу по ИД
        return subtasks.get(subtaskId);
    }

    public Epic getEpic(Integer epicId) {                                      // Возвращаем составную задачу по ИД
        return epics.get(epicId);
    }

    public ArrayList<Subtask> getEpicSubtasks(int epicId) {                    // Возвращаем список подзадач по Ид
        ArrayList<Subtask> subtasks = new ArrayList<>();                       // составной задачи

        if (epics.containsKey(epicId)) {
            for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {      // Записываем в новый лист подзадачи эпика
                subtasks.add(this.subtasks.get(subtaskId));
            }
        }
        return subtasks;                                                       //  Возвращаем новый лист с данными
    }

    private void updateEpicStatus(int nextTask) {                              // Определение статуса составной
        int i = 0;                                                             // задачи по статусу подзадач.
        int j = 0;

        int list = epics.get(nextTask).getSubtaskIds().size();
        for (Integer idSub : epics.get(nextTask).getSubtaskIds()) {
            if (subtasks.get(idSub).getStatus().equals("Done")) {
                ++i;
            }

            if (subtasks.get(idSub).getStatus().equals("New")) {
                ++j;
            }
        }
        if (list == 0) {
            epics.get(nextTask).setStatus("Done");
        } else if (i == list) {
            epics.get(nextTask).setStatus("Done");
        } else if (j == list) {
            epics.get(nextTask).setStatus("New");
        } else {
            epics.get(nextTask).setStatus("inProgress");
        }
    }

}