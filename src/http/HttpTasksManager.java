package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.*;

import managers.exceptions.ManagerSaveException;
import managers.exceptions.TasksValidateException;
import tasks.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.nio.file.Path;

import java.util.List;

public class HttpTasksManager extends FileBackedTasksManager implements TasksManager {
    URI uriKVServer;
    static KVTaskClient kvTaskClient;
    static Gson gson = Managers.getGson();
    static String tasksForServer;
    static String subtasksForServer;
    static String epicsForServer;
    static String historyForServer;

    public HttpTasksManager(URI uriKVServer) {
        super(Path.of("save.csv"));
        this.uriKVServer = uriKVServer;
        kvTaskClient = new KVTaskClient(uriKVServer);
    }

    @Override
    protected void save() throws ManagerSaveException {

        tasksForServer = gson.toJson(getAllTasks());
        subtasksForServer = gson.toJson(getAllSubtasks());
        epicsForServer = gson.toJson(getAllEpics());
        historyForServer = gson.toJson(getHistory());

        System.out.println(tasksForServer);
        System.out.println(subtasksForServer);
        System.out.println(epicsForServer);
        System.out.println(historyForServer);

        kvTaskClient.put("tasks", tasksForServer);
        kvTaskClient.put("subtasks", subtasksForServer);
        kvTaskClient.put("epics", epicsForServer);
        kvTaskClient.put("history", historyForServer);

    }

    public static HttpTasksManager loadFromServer(URI uriKVServer) throws TasksValidateException {

        tasksForServer = kvTaskClient.load("tasks");
        epicsForServer = kvTaskClient.load("epics");
        subtasksForServer = kvTaskClient.load("subtasks");
        historyForServer = kvTaskClient.load("history");

        if (!tasksForServer.isEmpty() || !subtasksForServer.isEmpty() || !epicsForServer.isEmpty()) {

            Type taskType = new TypeToken<List<SimpleTask>>() {
            }.getType();
            Type epicType = new TypeToken<List<Epic>>() {
            }.getType();
            Type subtaskType = new TypeToken<List<Subtask>>() {
            }.getType();
            Type historyType = new TypeToken<List<Task>>() {
            }.getType();

            List<SimpleTask> listTasksServer = gson.fromJson(tasksForServer, taskType);
            List<Epic> listEpicsServer = gson.fromJson(epicsForServer, epicType);
            List<Subtask> listSubtasksServer = gson.fromJson(subtasksForServer, subtaskType);
            List<Task> listHistoryServer = gson.fromJson(historyForServer, historyType);

            HttpTasksManager httpTasksManager = new HttpTasksManager(uriKVServer);


            for (SimpleTask task : listTasksServer) {
                httpTasksManager.tasks.put(task.getId(), task);
            }

            for (Epic epic : listEpicsServer) {
                httpTasksManager.epics.put(epic.getId(), epic);
            }

            for (Subtask subtask : listSubtasksServer) {
                httpTasksManager.subtasks.put(subtask.getEpicId(), subtask);
            }

            for (Task task : listHistoryServer) {
                httpTasksManager.historyManager.add(task);
            }

            return httpTasksManager;
        }
        return new HttpTasksManager(uriKVServer);
    }

}
