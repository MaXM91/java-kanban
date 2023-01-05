package managers;

import tasks.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTasksManager implements TasksManager {

    private int nextTask = 1;
    protected static final HistoryManager historyManager = Managers.getDefaultHistory();
    protected static HashMap<Integer, SimpleTask> tasks = new HashMap<>();
    protected static HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected static HashMap<Integer, Epic> epics = new HashMap<>();
    protected static HashMap<Integer, Task> validateList = new HashMap<>();

    @Override
    public Integer addTask(SimpleTask simpleTask) throws TasksValidateException {                            // Присваиваем полученной задаче Ид и
        if (validation(simpleTask) == true) {
            throw new TasksValidateException("Данный временной промежуток занят!");
        }
        simpleTask.setId(nextTask++);                                          // записываем в tasks.
        tasks.put(simpleTask.getId(), simpleTask);
        addTasksValidationList(simpleTask);
        return simpleTask.getId();
    }

    @Override
    public Integer addSubtask(Subtask subtask) throws TasksValidateException {                               // Присваиваем полученной подзадаче Ид,
        if (validation(subtask) == true) {
            throw new TasksValidateException("Данный временной промежуток занят!");
        }
        subtask.setId(nextTask++);                                             // Присваиваем Ид.
        epics.get(subtask.getEpicId()).setSubtaskIds(subtask.getId());         // Записываем Ид подзадачи в список составной задачи.
        subtasks.put(subtask.getId(), subtask);                                // Записываем подзадачу в subtasks.
        addTasksValidationList(subtask);
        updateEpicStatus(subtask.getEpicId());                                 // Переопределяем статус составной задачи в
        updateEpicTime(subtask.getEpicId());
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
        try {
            validation(simpleTask);
            if (tasks.containsKey(simpleTask.getId())) {
                tasks.put(simpleTask.getId(), simpleTask);
            }
        } catch (TasksValidateException exp) {
            System.out.println(exp.getMessage());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {                               // Обновляем подзадачу.
        try {
            validation(subtask);
            if (subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getEpicId())) {
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(subtask.getEpicId());
                updateEpicTime(subtask.getEpicId());
            }
        } catch (TasksValidateException exp) {
            System.out.println(exp.getMessage());
        }
    }

    @Override
    public void updateEpicTask(Epic epic) {                                    // Обновляем составную задачу.
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);                                     // Обновляем статус по статусу подзадач.
            updateEpicStatus(epic.getId());
            updateEpicTime(epic.getId());
        }
    }

    @Override
    public void removeTaskById(int id) {                                       // 2.6 Удаляем простую задачу по Id.
        deleteTaskValidationList(tasks.get(id));
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void removeSubtaskById(Integer id) {                                // 2.6 Удаляем подзадачу по Id.
        int idEpic = subtasks.get(id).getEpicId();                             // Обновляем статус составной задачи.
        deleteTaskValidationList(subtasks.get(id));
        historyManager.remove(id);
        epics.get(idEpic).removeSubtaskId(id);
        subtasks.remove(id);
        updateEpicStatus(idEpic);
        updateEpicTime(idEpic);
    }

    @Override
    public void removeEpicById(int id) {                                       //  2.6 Удаляем составную задачу по Id.
        for (Integer idSub : epics.get(id).getSubtaskIds()) {                  // Также удаляем её подзадачи.
            historyManager.remove(idSub);
            deleteTaskValidationList(subtasks.get(idSub));
            subtasks.remove(idSub);
        }
        historyManager.remove(id);
        epics.remove(id);
    }

    @Override
    public void removeAllTasks() {                                             // Удаление всех задач.
        for (Integer taskId : tasks.keySet()) {
            deleteTaskValidationList(tasks.get(taskId));
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {                                          // Удаление всех подзадач.
        for (Integer subtaskId : subtasks.keySet()) {                          // Обновляем статус составной задачи.
            if (epics.get(subtasks.get(subtaskId).getEpicId()).getSubtaskIds().contains(subtaskId)) {
                epics.get(subtasks.get(subtaskId).getEpicId()).removeSubtaskId(subtaskId);
                deleteTaskValidationList(subtasks.get(subtaskId));
                updateEpicStatus(subtasks.get(subtaskId).getEpicId());
                updateEpicTime(subtasks.get(subtaskId).getEpicId());
            }
            historyManager.remove(subtaskId);
        }
        subtasks.clear();
    }

    @Override
    public void removeAllEpics() {                                             // Удаление всех составных задач
        for (Integer epicId : epics.keySet()) {
            for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
                historyManager.remove(subtaskId);
                deleteTaskValidationList(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
            }
            historyManager.remove(epicId);
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
        SimpleTask task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtask(Integer subtaskId) {                             // Возвращаем подзадачу по ИД
        Subtask subtask = subtasks.get(subtaskId);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpic(Integer epicId) {                                      // Возвращаем составную задачу по ИД
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
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
                subtasks.add(InMemoryTasksManager.subtasks.get(subtaskId));
            }
        }
        return subtasks;                                                       //  Возвращаем новый лист с данными
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        Comparator<Task> comparator = (o1, o2) -> {
            if (o1.getStartDateTime().isBefore(o2.getStartDateTime())) {
                return -1;
            } else if (o1.getStartDateTime().isAfter(o2.getStartDateTime())) {
                return 1;
            }
            return 0;
        };

        TreeSet<Task> sortedTasksByTimeStart = new TreeSet<>(comparator);

        for (Integer taskId : tasks.keySet()) {
            sortedTasksByTimeStart.add(tasks.get(taskId));
        }

        for (Integer taskId : subtasks.keySet()) {
            sortedTasksByTimeStart.add(subtasks.get(taskId));
        }

        return sortedTasksByTimeStart;
    }


    private void updateEpicStatus(int epicId) {                               // Определение статуса составной
        int i = 0;                                                             // задачи по статусу подзадач.
        int j = 0;

        int list = epics.get(epicId).getSubtaskIds().size();
        for (Integer idSub : epics.get(epicId).getSubtaskIds()) {
            if (subtasks.get(idSub).getStatus().equals(StatusTask.DONE)) {
                ++i;
            }

            if (subtasks.get(idSub).getStatus().equals(StatusTask.NEW)) {
                ++j;
            }
        }
        if (list == 0) {
            epics.get(epicId).setStatus(StatusTask.DONE);
        } else if (i == list) {
            epics.get(epicId).setStatus(StatusTask.DONE);
        } else if (j == list) {
            epics.get(epicId).setStatus(StatusTask.NEW);
        } else {
            epics.get(epicId).setStatus(StatusTask.IN_PROGRESS);
        }
    }

    private void updateEpicTime(int epicId) {
        if (!epics.get(epicId).getSubtaskIds().isEmpty()) {
            LocalDateTime start = subtasks.get(epics.get(epicId).getSubtaskIds().get(0)).getStartDateTime();
            LocalDateTime end = subtasks.get(epics.get(epicId).getSubtaskIds().get(0)).getEndTime();

            for (Integer subtaskIds : epics.get(epicId).getSubtaskIds()) {

                if (start.isAfter(subtasks.get(subtaskIds).getStartDateTime())) {
                    start = subtasks.get(subtaskIds).getStartDateTime();
                }

                if (end.isBefore(subtasks.get(subtaskIds).getEndTime())) {
                    end = subtasks.get(subtaskIds).getEndTime();
                }
            }

            epics.get(epicId).setStartTime(start);
            epics.get(epicId).setEndTime(end);
            epics.get(epicId).setDuration();
        }
    }

    private boolean validation(Task task) throws TasksValidateException {
        int allFullDays = task.getStartDateTime().getDayOfYear();

        if (task.getId() == 0) {
            if (task instanceof SimpleTask) {
                if (validateList.containsKey(allFullDays)) {
                    throw new TasksValidateException("Данный временной промежуток занят!");
                }
            } else if (task instanceof Subtask) {
                if (validateList.containsKey(allFullDays)) {
                    throw new TasksValidateException("Данный временной промежуток занят!");
                }
            }
            return false;
        } else {
            if (task instanceof SimpleTask) {
                if (validateList.containsKey(allFullDays) && !tasks.containsKey(task.getId())) {
                    throw new TasksValidateException("Данный временной промежуток занят!");
                } else if (!validateList.containsKey(allFullDays) && tasks.containsKey(task.getId())) {
                    deleteTaskValidationList(tasks.get(task.getId()));
                    validateList.put(allFullDays, task);
                    return true;
                } else if (!validateList.containsKey(allFullDays) && !tasks.containsKey(task.getId())) {
                    validateList.put(allFullDays, task);
                    return true;
                } else if (validateList.containsKey(allFullDays) && validateList.get(allFullDays).getId() == task.getId()) {
                    deleteTaskValidationList(validateList.get(allFullDays));
                    validateList.put(allFullDays, task);
                    return true;
                }
            } else if (task instanceof Subtask) {
                if (validateList.containsKey(allFullDays) && !subtasks.containsKey(task.getId())) {
                    throw new TasksValidateException("Данный временной промежуток занят!");
                } else if (!validateList.containsKey(allFullDays) && subtasks.containsKey(task.getId())) {
                    deleteTaskValidationList(subtasks.get(task.getId()));
                    validateList.put(allFullDays, task);
                    return true;
                } else if (!validateList.containsKey(allFullDays) && !subtasks.containsKey(task.getId())) {
                    validateList.put(allFullDays, task);
                    return true;
                } else if (validateList.containsKey(allFullDays) && validateList.get(allFullDays).getId() == task.getId()) {
                    deleteTaskValidationList(validateList.get(allFullDays));
                    validateList.put(allFullDays, task);
                    return true;
                }
            }
            return false;
        }
    }

    private void deleteTaskValidationList(Task task) {
        int allFullDays = task.getStartDateTime().getDayOfYear();
        validateList.remove(allFullDays);
    }

    private void addTasksValidationList(Task task) {
        int allFullDays = task.getStartDateTime().getDayOfYear();
        validateList.put(allFullDays, task);
    }
}
