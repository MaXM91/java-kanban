package http;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import managers.*;
import managers.exceptions.TaskNotFoundException;
import managers.exceptions.TasksValidateException;
import tasks.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class HttpTaskServer {
    HttpServer httpServer;
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static TasksManager taskManager;

    static Gson gson = Managers.getGson();

    HttpTaskServer(TasksManager manager) throws IOException {
        this.taskManager = manager;

        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.setExecutor(Executors.newCachedThreadPool());
        httpServer.createContext("/tasks", new TasksHandler());

    }

    static class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().toString(), exchange.getRequestMethod());

            switch (endpoint) {
                case POST_TASK: {
                    try (InputStream inputStream = exchange.getRequestBody()) {
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        SimpleTask newSimpleTask = gson.fromJson(body, SimpleTask.class);

                        if (newSimpleTask.getName().isEmpty() || newSimpleTask.getDescription().isEmpty() || newSimpleTask.getStatus() == null) {
                            writeResponse(exchange, "Поля задачи не могут быть пустыми", 400);
                            return;
                        }

                        int taskId = taskManager.addTask(newSimpleTask);

                        writeResponse(exchange, Integer.toString(taskId), 200);
                        return;
                    } catch (JsonSyntaxException exc) {
                        writeResponse(exchange, "Получен некорректный JSON", 400);
                        exc.printStackTrace();
                    } catch (TasksValidateException e) {
                        writeResponse(exchange, "Данный период времени занят", 400);
                    }
                    break;
                }
                case POST_UPDATE_TASK: {
                    try (InputStream inputStream = exchange.getRequestBody()) {
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        SimpleTask simpleTask = gson.fromJson(body, SimpleTask.class);

                        if (simpleTask.getName().isEmpty() || simpleTask.getDescription().isEmpty()) {
                            writeResponse(exchange, "Поля задачи не могут быть пустыми", 400);
                            return;
                        }

                        taskManager.updateTask(simpleTask);

                        writeResponse(exchange, "Задача обновлена", 200);
                        return;
                    } catch (JsonSyntaxException exc) {
                        writeResponse(exchange, "Получен некорректный JSON", 400);
                    } catch (TasksValidateException e) {
                        writeResponse(exchange, "Данный период времени занят", 400);
                    } catch (TaskNotFoundException e) {
                        writeResponse(exchange, "Укажите правильный ИД задачи", 400);
                    }
                    break;
                }
                case GET_TASK_BY_ID: {
                    String uri = exchange.getRequestURI().toString();
                    Integer taskId = searchId(uri);
                    Task task;
                    try {
                        task = taskManager.getTask(taskId);
                        String taskGson = gson.toJson(task);
                        writeResponse(exchange, taskGson, 200);
                        break;
                    } catch (TaskNotFoundException e) {
                        writeResponse(exchange, "Укажите правильный ИД задачи", 400);
                    }
                }
                case GET_ALL_TASKS: {
                    List<SimpleTask> tasks = taskManager.getAllTasks();
                    String tasksGson = gson.toJson(tasks);
                    writeResponse(exchange, tasksGson, 200);
                    break;
                }
                case DELETE_TASK_BY_ID: {
                    try {
                        String uri = exchange.getRequestURI().toString();
                        int taskId = searchId(uri);

                        taskManager.removeTaskById(taskId);
                        writeResponse(exchange, "Задача удалена", 200);
                        break;
                    } catch (TaskNotFoundException e) {
                        writeResponse(exchange, "Задача с таким Ид отсутствует", 400);
                    }
                }
                case DELETE_ALL_TASKS: {
                    taskManager.removeAllTasks();
                    writeResponse(exchange, "Все Задачи удалены", 200);
                    break;
                }
                case POST_SUBTASK: {
                    try (InputStream inputStream = exchange.getRequestBody()) {
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Subtask newSubtask = gson.fromJson(body, Subtask.class);
                        Integer epicId = newSubtask.getEpicId();

                        if (newSubtask.getName().isEmpty() || newSubtask.getDescription().isEmpty() || newSubtask.getStatus() == null || epicId <= 0) {
                            writeResponse(exchange, "Поля подзадачи не могут быть пустыми", 400);
                            return;
                        }

                        int subtaskId = taskManager.addSubtask(newSubtask);

                        writeResponse(exchange, Integer.toString(subtaskId), 200);
                        return;
                    } catch (JsonSyntaxException exc) {
                        writeResponse(exchange, "Получен некорректный JSON", 400);
                    } catch (TasksValidateException e) {
                        writeResponse(exchange, "Данный период времени занят", 400);
                    }
                    break;
                }
                case POST_UPDATE_SUBTASK: {
                    try (InputStream inputStream = exchange.getRequestBody()) {
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        Integer epicId = subtask.getEpicId();

                        if (subtask.getName().isEmpty() || subtask.getDescription().isEmpty() || subtask.getStatus() == null || epicId == null) {
                            writeResponse(exchange, "Поля подзадачи не могут быть пустыми", 400);
                            return;
                        }

                        taskManager.updateSubtask(subtask);

                        writeResponse(exchange, "Подзадача обновлена", 200);
                        return;
                    } catch (JsonSyntaxException exc) {
                        writeResponse(exchange, "Получен некорректный JSON", 400);
                    } catch (TasksValidateException e) {
                        writeResponse(exchange, "Данный период времени занят", 400);
                    } catch (TaskNotFoundException e) {
                        writeResponse(exchange, "Укажите правильный ИД подзадачи", 400);
                    }
                    break;
                }
                case GET_SUBTASK_BY_ID: {
                    String uri = exchange.getRequestURI().toString();
                    Integer subtaskId = searchId(uri);
                    Subtask subtask = taskManager.getSubtask(subtaskId);
                    String subtaskGson = gson.toJson(subtask);
                    writeResponse(exchange, subtaskGson, 200);
                    break;
                }
                case GET_ALL_SUBTASK: {
                    List<Subtask> subtasks = taskManager.getAllSubtasks();
                    String subtaskGson = gson.toJson(subtasks);
                    writeResponse(exchange, subtaskGson, 200);
                    break;
                }
                case DELETE_SUBTASK_BY_ID: {
                    try {
                        String uri = exchange.getRequestURI().toString();
                        int subtaskId = searchId(uri);

                        taskManager.removeSubtaskById(subtaskId);
                        writeResponse(exchange, "Подзадача удалена", 200);
                        break;
                    } catch (TaskNotFoundException e) {
                        writeResponse(exchange, "Подзадача c таким Ид отсутствует", 400);
                    }
                }
                case DELETE_ALL_SUBTASKS: {
                    taskManager.removeAllSubtasks();
                    writeResponse(exchange, "Все подзадачи удалены", 200);
                    break;
                }
                case POST_EPIC: {
                    try (InputStream inputStream = exchange.getRequestBody()) {
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic newEpic = gson.fromJson(body, Epic.class);

                        if (newEpic.getName().isEmpty() || newEpic.getDescription().isEmpty() || newEpic.getStatus() == null || newEpic.getSubtaskIds() == null) {
                            writeResponse(exchange, "Поля эпика не могут быть пустыми", 400);
                            return;
                        }

                        int epicId = taskManager.addEpic(newEpic);

                        writeResponse(exchange, Integer.toString(epicId), 200);
                        return;
                    } catch (JsonSyntaxException exc) {
                        writeResponse(exchange, "Получен некорректный JSON", 400);
                    }
                    break;
                }
                case POST_UPDATE_EPIC: {
                    try (InputStream inputStream = exchange.getRequestBody()) {
                        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(body, Epic.class);

                        if (epic.getName().isEmpty() || epic.getDescription().isEmpty() || epic.getStatus() == null) {
                            writeResponse(exchange, "Поля эпика не могут быть пустыми", 400);
                            return;
                        }

                        taskManager.updateEpicTask(epic);

                        writeResponse(exchange, "Эпик обновлён", 200);
                        return;
                    } catch (JsonSyntaxException exc) {
                        writeResponse(exchange, "Получен некорректный JSON", 400);
                    } catch (TaskNotFoundException e) {
                        writeResponse(exchange, "Укажите правильный ИД эпика", 400);
                    }
                    break;
                }
                case GET_EPIC_BY_ID: {
                    String uri = exchange.getRequestURI().toString();
                    Integer epicId = searchId(uri);
                    Epic epic = taskManager.getEpic(epicId);
                    String epicGson = gson.toJson(epic);
                    writeResponse(exchange, epicGson, 200);
                    break;
                }
                case GET_ALL_EPICS: {
                    List<Epic> epics = taskManager.getAllEpics();
                    String epicsGson = gson.toJson(epics);
                    writeResponse(exchange, epicsGson, 200);
                    break;
                }
                case DELETE_EPIC_BY_ID: {
                    try {
                        String uri = exchange.getRequestURI().toString();
                        int epicId = searchId(uri);

                        taskManager.removeEpicById(epicId);
                        writeResponse(exchange, "Эпик удалена", 200);
                        break;
                    } catch (TaskNotFoundException e) {
                        writeResponse(exchange, "Эпик c таким Ид отсутствует", 400);
                    }
                }
                case DELETE_ALL_EPICS: {
                    taskManager.removeAllEpics();
                    writeResponse(exchange, "Все эпики удалены", 200);
                    break;
                }
                case GET_HISTORY: {
                    List<Task> historyList = taskManager.getHistory();
                    String epicsGson = gson.toJson(historyList);
                    writeResponse(exchange, epicsGson, 200);
                    break;
                }
                case GET_PRIORITIZED_TASK: {
                    Map<LocalDateTime, Task> prioritizedTask = taskManager.getPrioritizedTasks();
                    String prioritizedGson = gson.toJson(prioritizedTask);
                    writeResponse(exchange, prioritizedGson, 200);
                    break;
                }
                case GET_EPIC_SUBTASKS: {
                    String uri = exchange.getRequestURI().toString();
                    Integer epicId = searchId(uri);
                    List<Integer> subtaskIds = taskManager.getEpic(epicId).getSubtaskIds();
                    String epicGson = gson.toJson(subtaskIds);
                    writeResponse(exchange, epicGson, 200);
                    break;
                }
                default:
                    writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        }
    }

    private static Endpoint getEndpoint(String requestUri, String requestMethod) {
        String[] uriParts = requestUri.split("/");

        switch (requestMethod) {
            case "GET": {
                if (uriParts[uriParts.length - 1].contains("?id=") && uriParts[uriParts.length - 2].equals("task") && uriParts.length == 4) {
                    return Endpoint.GET_TASK_BY_ID;
                } else if (uriParts[uriParts.length - 1].contains("?id=") && uriParts[uriParts.length - 2].equals("subtask") && uriParts.length == 4) {
                    return Endpoint.GET_SUBTASK_BY_ID;
                } else if (uriParts[uriParts.length - 1].contains("?id=") && uriParts[uriParts.length - 2].equals("epic") && uriParts.length == 4) {
                    return Endpoint.GET_EPIC_BY_ID;
                } else if (uriParts[uriParts.length - 1].equals("task") && uriParts.length == 3) {
                    return Endpoint.GET_ALL_TASKS;
                } else if (uriParts[uriParts.length - 1].equals("subtask") && uriParts.length == 3) {
                    return Endpoint.GET_ALL_SUBTASK;
                } else if (uriParts[uriParts.length - 1].equals("epic") && uriParts.length == 3) {
                    return Endpoint.GET_ALL_EPICS;
                } else if (uriParts[uriParts.length - 1].equals("history") && uriParts.length == 3) {
                    return Endpoint.GET_HISTORY;
                } else if (uriParts[uriParts.length - 1].equals("tasks") && uriParts.length == 2) {
                    return Endpoint.GET_PRIORITIZED_TASK;
                } else if (uriParts[uriParts.length - 1].contains("?id=") && uriParts[uriParts.length - 2].equals("epic") && uriParts.length == 5 &&
                        uriParts[uriParts.length - 3].equals("subtask")) {
                    return Endpoint.GET_EPIC_SUBTASKS;
                } else {
                    return Endpoint.UNKNOWN;
                }
            }
            case "POST": {
                if (uriParts[uriParts.length - 1].contains("?id=") && uriParts[uriParts.length - 2].equals("task") && uriParts.length == 4) {
                    return Endpoint.POST_UPDATE_TASK;
                } else if (uriParts[uriParts.length - 1].contains("?id=") && uriParts[uriParts.length - 2].equals("subtask") && uriParts.length == 4) {
                    return Endpoint.POST_UPDATE_SUBTASK;
                } else if (uriParts[uriParts.length - 1].contains("?id=") && uriParts[uriParts.length - 2].equals("epic") && uriParts.length == 4) {
                    return Endpoint.POST_UPDATE_EPIC;
                } else if (uriParts[uriParts.length - 1].equals("task") && uriParts.length == 3) {
                    return Endpoint.POST_TASK;
                } else if (uriParts[uriParts.length - 1].equals("subtask") && uriParts.length == 3) {
                    return Endpoint.POST_SUBTASK;
                } else if (uriParts[uriParts.length - 1].equals("epic") && uriParts.length == 3) {
                    return Endpoint.POST_EPIC;
                } else {
                    return Endpoint.UNKNOWN;
                }
            }
            case "DELETE": {
                if (uriParts[uriParts.length - 1].contains("?id=") && uriParts[uriParts.length - 2].equals("task") && uriParts.length == 4) {
                    return Endpoint.DELETE_TASK_BY_ID;
                } else if (uriParts[uriParts.length - 1].contains("?id=") && uriParts[uriParts.length - 2].equals("subtask") && uriParts.length == 4) {
                    return Endpoint.DELETE_SUBTASK_BY_ID;
                } else if (uriParts[uriParts.length - 1].contains("?id=") && uriParts[uriParts.length - 2].equals("epic") && uriParts.length == 4) {
                    return Endpoint.DELETE_EPIC_BY_ID;
                } else if (uriParts[uriParts.length - 1].equals("task") && uriParts.length == 3) {
                    return Endpoint.DELETE_ALL_TASKS;
                } else if (uriParts[uriParts.length - 1].equals("subtask") && uriParts.length == 3) {
                    return Endpoint.DELETE_ALL_SUBTASKS;
                } else if (uriParts[uriParts.length - 1].equals("epic") && uriParts.length == 3) {
                    return Endpoint.DELETE_ALL_EPICS;
                } else {
                    return Endpoint.UNKNOWN;
                }
            }
            default:
                return Endpoint.UNKNOWN;
        }
    }

    private static void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    private static int searchId(String uri) {
        String[] partsOfUri = uri.split("/");

        String taskId = partsOfUri[partsOfUri.length - 1].replace("?id=", "").trim();

        return Integer.parseInt(taskId);
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        try {
            httpServer.stop(0);
            System.out.println("HTTP-сервер на " + PORT + " порту остановлен!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

