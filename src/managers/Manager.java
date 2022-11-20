package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {
    protected int nextTask = 1;
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();

    public Integer addTask(Task task) {                                           // Присваиваем полученной задаче Ид и
        task.setId(nextTask++);                                                // записываем в tasks.
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public Integer addSubtask(Subtask subtask, int epicId) {                   // Присваиваем полученной подзадаче Ид,
        subtask.setId(nextTask++);                                             // Присваиваем Ид.
        subtask.setEpicId(epicId);                                             // Присваиваем Ид составной задачи.
        epics.get(epicId).setSubtaskIds(subtask.getId());                      // Записываем Ид подзадачи в список составной задачи.
        subtasks.put(subtask.getId(), subtask);                                // Записываем подзадачу в subtasks.
        updateEpicStatus(epicId);                                              // Переопределяем статус составной задачи в
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
        } else {
            addTask(task);
        }
    }

    public void updateSubtask(Subtask subtask) {                               // Обновляем подзадачу.
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        } else {
            addSubtask(subtask, subtask.getEpicId());
        }
    }

    public void updateEpicTask(Epic epic) {                                    // Обновляем составную задачу.
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);                                     // Обновляем статус по статусу подзадач.
        } else {
            addEpic(epic);
        }
    }

    public void removeTaskById(int id) {                                       // 2.6 Удаляем простую задачу по Id.
        tasks.remove(id);
    }

    public void removeSubtaskById(Integer id) {                                // 2.6 Удаляем подзадачу по Id.
        int idEpic = subtasks.get(id).getEpicId();
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

    public void removeAllTasks() {                                             // Удаление всех задач
        tasks.clear();
    }

    public void removeAllSubtasks() {                                          // Удаление всех подзадач
        subtasks.clear();
    }

    public void removeAllEpics() {                                             // Удаление всех составных задач
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

    public ArrayList<Integer> getEpicSubtasks(int epicId) {                    // Возвращаем список подзадач по Ид
        return epics.get(epicId).getSubtaskIds();                              // составной задачи
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