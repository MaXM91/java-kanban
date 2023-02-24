package managers;

import managers.exceptions.TaskNotFoundException;
import managers.exceptions.TasksValidateException;
import tasks.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTasksManager implements TasksManager {

    private int nextTask = 1;
    protected static final HistoryManager historyManager = Managers.getDefaultHistory();
    protected static HashMap<Integer, SimpleTask> tasks = new HashMap<>();
    protected static HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected static HashMap<Integer, Epic> epics = new HashMap<>();
    private static final Comparator<LocalDateTime> comparator = (o1, o2) -> {
        if (o1.isBefore(o2)) {
            return -1;
        } else if (o1.isAfter(o2)) {
            return 1;
        }
        return 0;
    };
    protected static Map<LocalDateTime, Task> validatedPrioritizedTasks = new TreeMap<>(comparator);

    @Override
    public Integer addTask(SimpleTask simpleTask) throws TasksValidateException { // Присваиваем полученной задаче Ид и
        if (validation(simpleTask)) {
            throw new TasksValidateException("Данный временной промежуток занят!");
        }
        simpleTask.setId(nextTask++);                                          // записываем в tasks.
        tasks.put(simpleTask.getId(), simpleTask);
        addValidatedPrioritizedTasks(simpleTask);
        return simpleTask.getId();
    }

    @Override
    public Integer addSubtask(Subtask subtask) throws TasksValidateException { // Присваиваем полученной подзадаче Ид,
        if (validation(subtask)) {
            throw new TasksValidateException("Данный временной промежуток занят!");
        }
        subtask.setId(nextTask++);                                             // Присваиваем Ид.
        epics.get(subtask.getEpicId()).setSubtaskIds(subtask.getId());         // Записываем Ид подзадачи в список составной задачи.
        subtasks.put(subtask.getId(), subtask);                                // Записываем подзадачу в subtasks.
        addValidatedPrioritizedTasks(subtask);
        updateEpicStatus(subtask.getEpicId());                                 // Переопределяем статус составной задачи в
        updateEpicTime(subtask.getEpicId());
        return subtask.getId();                                                // которую входит эта подзадача. Возвращаем Ид.
    }

    @Override
    public Integer addEpic(Epic epic) {                                        // Присваиваем полученной составной
        epic.setId(nextTask++);                                                // задаче Ид, записываем в epics.
        epics.put(epic.getId(), epic);                                         // Возвращаем Ид.
        return epic.getId();
    }

    @Override
    public void updateTask(SimpleTask simpleTask) throws TasksValidateException, TaskNotFoundException { // Обновляем простую задачу.
        if (validation(simpleTask)) {
            throw new TasksValidateException("Данный временной промежуток занят!");
        }
        if (tasks.containsKey(simpleTask.getId())) {
            tasks.put(simpleTask.getId(), simpleTask);
            addValidatedPrioritizedTasks(simpleTask);
        } else {
            throw new TaskNotFoundException("Task с таким ИД отсутствует");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) throws TasksValidateException, TaskNotFoundException { // Обновляем подзадачу.
        if (validation(subtask)) {
            throw new TasksValidateException("Данный временной промежуток занят!");
        }
        if (subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
            updateEpicTime(subtask.getEpicId());
        } else {
            throw new TaskNotFoundException("Task с таким ИД отсутствует");
        }
    }

    @Override
    public void updateEpicTask(Epic epic) throws TaskNotFoundException {                                    // Обновляем составную задачу.
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);                                     // Обновляем статус по статусу подзадач.
            updateEpicStatus(epic.getId());
            updateEpicTime(epic.getId());
        }else {
            throw new TaskNotFoundException("Task с таким ИД отсутствует");
        }
    }

    @Override
    public void removeTaskById(int id) throws TaskNotFoundException {          // 2.6 Удаляем простую задачу по Id.
        if (tasks.containsKey(id)) {
            deleteValidatedPrioritizedTasks(tasks.get(id));
            historyManager.remove(id);
            tasks.remove(id);
        } else {
            throw new TaskNotFoundException("Задача с таким ИД отсутствует");
        }
    }

    @Override
    public void removeSubtaskById(Integer id) throws TaskNotFoundException {   // 2.6 Удаляем подзадачу по Id.
        if (subtasks.containsKey(id)) {
            int idEpic = subtasks.get(id).getEpicId();                         // Обновляем статус составной задачи.
            deleteValidatedPrioritizedTasks(subtasks.get(id));
            historyManager.remove(id);
            epics.get(idEpic).removeSubtaskId(id);
            subtasks.remove(id);
            updateEpicStatus(idEpic);
            updateEpicTime(idEpic);
        } else {
            throw new TaskNotFoundException("Подзадача с таким ИД отсутствует");
        }
    }

    @Override
    public void removeEpicById(int id) throws TaskNotFoundException {          //  2.6 Удаляем составную задачу по Id.
        if (epics.containsKey(id)) {
            for (Integer idSub : epics.get(id).getSubtaskIds()) {              // Также удаляем её подзадачи.
                historyManager.remove(idSub);
                deleteValidatedPrioritizedTasks(subtasks.get(idSub));
                subtasks.remove(idSub);
            }
            historyManager.remove(id);
            epics.remove(id);
        } else {
            throw new TaskNotFoundException("Эпик с таким ИД отсутствует");
        }
    }

    @Override
    public void removeAllTasks() {                                             // Удаление всех задач.
        for (Integer taskId : tasks.keySet()) {
            deleteValidatedPrioritizedTasks(tasks.get(taskId));
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {                                          // Удаление всех подзадач.
        for (Integer subtaskId : subtasks.keySet()) {                          // Обновляем статус составной задачи.
            if (epics.get(subtasks.get(subtaskId).getEpicId()).getSubtaskIds().contains(subtaskId)) {
                epics.get(subtasks.get(subtaskId).getEpicId()).removeSubtaskId(subtaskId);
                deleteValidatedPrioritizedTasks(subtasks.get(subtaskId));
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
                deleteValidatedPrioritizedTasks(subtasks.get(subtaskId));
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
    public SimpleTask getTask(Integer taskId) throws TaskNotFoundException {                                // Возвращаем задачу по ИД
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
    public List<Task> getHistory() {                                           // Возвращаем список запросов(историю)
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {                    // Возвращаем список подзадач по Ид
        ArrayList<Subtask> subtasks = new ArrayList<>();                       // составной задачи

        if (epics.containsKey(epicId)) {
            for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {      // Записываем в новый лист
                subtasks.add(InMemoryTasksManager.subtasks.get(subtaskId));    // подзадачи эпика
            }
        }
        return subtasks;                                                       //  Возвращаем новый лист с данными
    }

    @Override
    public Map<LocalDateTime, Task> getPrioritizedTasks() {                    // Вывод упорядоченного по времени
        return validatedPrioritizedTasks;                                      // списка задач и подзадач
    }

    private void updateEpicStatus(int epicId) {                                // Определение статуса составной
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

    private void updateEpicTime(int epicId) {                                  // Корректировка временного интервала
        if (!epics.get(epicId).getSubtaskIds().isEmpty()) {                    // эпика по интервалам входящих подзадач
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

    private boolean validation(Task task) {                                    // Валидация задач по временным
        List<Task> intersections = findIntersectionsTime(task);                // интервалам
        Task frontTask;

        if (intersections.size() > 1) {
            return true;
        } else if (intersections.size() == 0) {
            if (task.getId() == 0) {
                return false;
            } else {
                if (task instanceof SimpleTask) {
                    deleteValidatedPrioritizedTasks(tasks.get(task.getId()));
                    return false;
                } else if (task instanceof Subtask) {
                    deleteValidatedPrioritizedTasks(subtasks.get(task.getId()));
                    return false;
                }
            }
        } else {
            frontTask = intersections.get(0);

            if (task.getId() != 0) {
                if (task instanceof SimpleTask) {
                    if (frontTask.getId() == task.getId()) {
                        deleteValidatedPrioritizedTasks(tasks.get(task.getId()));
                        return false;
                    }
                } else if (task instanceof Subtask) {
                    if (frontTask.getId() == task.getId()) {
                        deleteValidatedPrioritizedTasks(subtasks.get(task.getId()));
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private List<Task> findIntersectionsTime(Task task) {                            // Поиск пересечения интервалов
        LocalDateTime timeStartTask = task.getStartDateTime();                 // и граничных точек
        LocalDateTime timeEndTask = timeStartTask.plusMinutes(task.getDuration().toMinutes());
        LocalDateTime taskStart;
        LocalDateTime taskEnd;
        List<Task> intersectionsTaskIds = new ArrayList<>();

        for (Task taskValidation : validatedPrioritizedTasks.values()) {
            taskStart = taskValidation.getStartDateTime();
            taskEnd = taskStart.plusMinutes(taskValidation.getDuration().toMinutes());

            if ((timeStartTask.isAfter(taskStart) || timeStartTask.equals(taskStart)) &&
                    (timeStartTask.isBefore(taskEnd) || timeStartTask.equals(taskEnd))) {
                intersectionsTaskIds.add(taskValidation);
            }
            if ((timeEndTask.isAfter(taskStart) || timeEndTask.equals(taskStart)) &&
                    (timeEndTask.isBefore(taskEnd) || timeEndTask.equals(taskEnd))) {
                intersectionsTaskIds.add(taskValidation);
            }
        }
        return intersectionsTaskIds;
    }

    private void deleteValidatedPrioritizedTasks(Task task) {                  // Удаление задачь из списка валидации
        validatedPrioritizedTasks.remove(task.getStartDateTime());
    }

    private void addValidatedPrioritizedTasks(Task task) {                     // Добавление задач в список валидации
        validatedPrioritizedTasks.put(task.getStartDateTime(), task);
    }
}
