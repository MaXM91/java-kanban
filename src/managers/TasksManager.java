package managers;

import tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TasksManager {

    Integer addTask(SimpleTask simpleTask) throws TasksValidateException;                                    // Присвоить Ид простой задаче и записать в tasks

    Integer addSubtask(Subtask subtask) throws TasksValidateException;                                       // Присвоить Ид простой подзадаче и записать в subtasks

    Integer addEpic(Epic epic);                                                // Присвоить Ид составной задаче и записать в epics

    void updateTask(SimpleTask simpleTask) throws TasksValidateException;                                    // Обновить простую задачу

    void updateSubtask(Subtask subtask) throws TasksValidateException;                                       // Обновить подзадачу и поверить статус составной

    void updateEpicTask(Epic epic);                                            // Обновить составную задачу и проверить статус

    void removeTaskById(int id);                                               // Удалить простую задачу по Ид

    void removeSubtaskById(Integer id);                                        // Удалить составную задачу по Ид, удалить ее из списка

    // подзадач составной задачи
    void removeEpicById(int id);                                               // Удалить составную задачу по Ид и удалить ее подзадачи

    void removeAllTasks();                                                     // Удалить все простые задачи

    void removeAllSubtasks();                                                  // Удалить все подзадачи

    void removeAllEpics();                                                     // Удалить все составные задачи

    List<SimpleTask> getAllTasks();                                            // Вернуть список простых задач

    List<Subtask> getAllSubtasks();                                            // Вернуть список подзадач

    List<Epic> getAllEpics();                                                  // Вернуть список составных задачь

    SimpleTask getTask(Integer taskId);                                        // Вернуть простую задачу по Ид, записать в историю

    Subtask getSubtask(Integer subtaskId);                                     // Вернуть подзадачу по Ид, записать в историю

    Epic getEpic(Integer epicId);                                              // Вернуть составную задачу по Ид, записать в историю

    List<Task> getHistory();

    ArrayList<Subtask> getEpicSubtasks(int epicId);                            // Добавить Ид подзадачи в список подзадачь составной задачи

    Map<LocalDateTime, Task> getPrioritizedTasks();

}