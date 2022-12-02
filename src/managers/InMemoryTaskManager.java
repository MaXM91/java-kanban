package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int nextTask = 1;
    public static Task historyTask;
    HistoryManager historyManager = Managers.getDefaultHistory();
    protected HashMap<Integer, SimpleTask> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();

    @Override
    public Integer addTask(SimpleTask simpleTask) {                            // Присваиваем полученной задаче Ид и
        simpleTask.setId(nextTask++);                                          // записываем в tasks.
        tasks.put(simpleTask.getId(), simpleTask);
        return simpleTask.getId();
    }

    @Override
    public Integer addSubtask(Subtask subtask) {                               // Присваиваем полученной подзадаче Ид,
        subtask.setId(nextTask++);                                             // Присваиваем Ид.
        epics.get(subtask.getEpicId()).setSubtaskIds(subtask.getId());         // Записываем Ид подзадачи в список составной задачи.
        subtasks.put(subtask.getId(), subtask);                                // Записываем подзадачу в subtasks.
        updateEpicStatus(subtask.getEpicId());                                 // Переопределяем статус составной задачи в
        return subtask.getId();                                                // которую входит эта подзадача. Возвращаем Ид.
    }

    @Override
    public Integer addEpic(Epic epic) {                                        // Присваиваем полученной составной задаче Ид,
        epic.setId(nextTask++);                                                // записываем в epics.
        epics.put(epic.getId(), epic);                                         // Возвращаем Ид.
        return epic.getId();
    }

    @Override
    public void updateTask(SimpleTask simpleTask) {                            // Обновляем простую задачу.
        if (tasks.containsKey(simpleTask.getId())) {
            tasks.put(simpleTask.getId(), simpleTask);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {                               // Обновляем подзадачу.
        if (subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void updateEpicTask(Epic epic) {                                    // Обновляем составную задачу.
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);                                     // Обновляем статус по статусу подзадач.
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public void removeTaskById(int id) {                                       // 2.6 Удаляем простую задачу по Id.
        tasks.remove(id);
    }

    @Override
    public void removeSubtaskById(Integer id) {                                // 2.6 Удаляем подзадачу по Id.
        int idEpic = subtasks.get(id).getEpicId();                             // Обновляем статус составной задачи.
        epics.get(idEpic).removeSubtaskId(id);
        subtasks.remove(id);
        updateEpicStatus(idEpic);
    }

    @Override
    public void removeEpicById(int id) {                                       //  2.6 Удаляем составную задачу по Id.
        for (Integer idSub : epics.get(id).getSubtaskIds()) {                  // Также удаляем её подзадачи.
            subtasks.remove(idSub);
        }
        epics.remove(id);
    }

    @Override
    public void removeAllTasks() {                                             // Удаление всех задач.
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {                                          // Удаление всех подзадач.
        for (Integer subtaskId : subtasks.keySet()) {                          // Обновляем статус составной задачи.
            if (epics.get(subtasks.get(subtaskId).getEpicId()).getSubtaskIds().contains(subtaskId)) {
                epics.get(subtasks.get(subtaskId).getEpicId()).removeSubtaskId(subtaskId);
                updateEpicStatus(subtasks.get(subtaskId).getEpicId());
            }
        }
        subtasks.clear();

    }

    @Override
    public void removeAllEpics() {                                             // Удаление всех составных задач
        for (Integer epicId : epics.keySet()) {
            for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
        epics.clear();
    }

    @Override
    public List<SimpleTask> getAllTasks() {                                    // Возвращаем список задач
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {                                    // Возвращаем список подзадач
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {                                          // Возвращаем список эпиков
        return new ArrayList<>(epics.values());
    }

    @Override
    public SimpleTask getTask(Integer taskId) {                                // Возвращаем задачу по ИД
        historyTask = tasks.get(taskId);
        historyManager.add();
        return tasks.get(taskId);
    }

    @Override
    public Subtask getSubtask(Integer subtaskId) {                             // Возвращаем подзадачу по ИД
        historyTask = subtasks.get(subtaskId);
        historyManager.add();
        return subtasks.get(subtaskId);
    }

    @Override
    public Epic getEpic(Integer epicId) {                                      // Возвращаем составную задачу по ИД
        historyTask = epics.get(epicId);
        historyManager.add();
        return epics.get(epicId);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {                    // Возвращаем список подзадач по Ид
        ArrayList<Subtask> subtasks = new ArrayList<>();                       // составной задачи

        if (epics.containsKey(epicId)) {
            for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {      // Записываем в новый лист подзадачи эпика
                subtasks.add(this.subtasks.get(subtaskId));
            }
        }
        return subtasks;                                                       //  Возвращаем новый лист с данными
    }


    private void updateEpicStatus(int nextTask) {                               // Определение статуса составной
        int i = 0;                                                             // задачи по статусу подзадач.
        int j = 0;

        int list = epics.get(nextTask).getSubtaskIds().size();
        for (Integer idSub : epics.get(nextTask).getSubtaskIds()) {
            if (subtasks.get(idSub).getStatus().equals(StatusTask.DONE)) {
                ++i;
            }

            if (subtasks.get(idSub).getStatus().equals(StatusTask.NEW)) {
                ++j;
            }
        }
        if (list == 0) {
            epics.get(nextTask).setStatus(StatusTask.DONE);
        } else if (i == list) {
            epics.get(nextTask).setStatus(StatusTask.DONE);
        } else if (j == list) {
            epics.get(nextTask).setStatus(StatusTask.NEW);
        } else {
            epics.get(nextTask).setStatus(StatusTask.IN_PROGRESS);
        }
    }

}