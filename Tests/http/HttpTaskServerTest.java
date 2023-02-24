package http;

import com.google.gson.Gson;
import managers.Managers;
import managers.StatusTask;
import managers.exceptions.TaskNotFoundException;
import managers.TasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SimpleTask;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

class HttpTaskServerTest {
    Gson gson = Managers.getGson();
    static KVServer server;
    HttpTaskServer serverHttp;
    TasksManager tasksManager;
    Epic epic1;
    int epic1Id;
    Subtask subtask1;
    int subtask1Id;
    Subtask subtask2;
    int subtask2Id;
    Subtask subtask3;
    int subtask3Id;
    SimpleTask task1;
    int task1Id;
    SimpleTask task2;
    int task2Id;
    Epic epic2;

    @BeforeEach
    public void startManager() throws IOException {
        server = new KVServer();
        server.start();

        tasksManager = Managers.getDefault(URI.create("http://localhost:8081"));

        serverHttp = new HttpTaskServer(tasksManager);
        serverHttp.start();

        LocalDateTime epic1LocalDateTime = LocalDateTime.of(2010, 1, 1, 1, 22, 0);
        Duration epic1Duration = Duration.ofMinutes(90);
        epic1 = new Epic("Эпик1", "Проверочный", StatusTask.NEW, epic1LocalDateTime, epic1Duration);

        LocalDateTime subtask1LocalDateTime = LocalDateTime.of(2010, 1, 2, 2, 7, 0);
        Duration subtask1Duration = Duration.ofMinutes(90);
        subtask1 = new Subtask("подзадача1", "проверочная", StatusTask.NEW, subtask1LocalDateTime, subtask1Duration, epic1Id);

        LocalDateTime subtask2LocalDateTime = LocalDateTime.of(2010, 1, 3, 1, 9, 0);
        Duration subtask2Duration = Duration.ofMinutes(1000);
        subtask2 = new Subtask("подзадача2", "проверочная", StatusTask.NEW, subtask2LocalDateTime, subtask2Duration, epic1Id);

        LocalDateTime subtask3LocalDateTime = LocalDateTime.of(2010, 1, 9, 5, 5, 0);
        Duration subtask3Duration = Duration.ofMinutes(600);
        subtask3 = new Subtask("подзадача3", "проверочная", StatusTask.NEW, subtask3LocalDateTime, subtask3Duration, epic1Id);

        LocalDateTime task1LocalDateTime = LocalDateTime.of(2010, 1, 11, 8, 7, 0);
        Duration task1Duration = Duration.ofMinutes(450);
        task1 = new SimpleTask("задача1", "проверочная", StatusTask.NEW, task1LocalDateTime, task1Duration);

        LocalDateTime task2LocalDateTime = LocalDateTime.of(2010, 1, 22, 6, 9, 0);
        Duration task2Duration = Duration.ofMinutes(150);
        task2 = new SimpleTask("задача2", "тестовая2", StatusTask.NEW, task2LocalDateTime, task2Duration);

        LocalDateTime epic2LocalDateTime = LocalDateTime.of(2010, 1, 24, 7, 8, 0);
        Duration epic2Duration = Duration.ofMinutes(90);
        epic2 = new Epic("Эпик2", "Проверочный", StatusTask.DONE, epic2LocalDateTime, epic2Duration);

        tasksManager.removeAllTasks();
        tasksManager.removeAllSubtasks();
        tasksManager.removeAllEpics();
    }

    @AfterEach
    void stop() {
        server.stop();
        serverHttp.stop();
    }

    @Test
    void postTask() {
        String jsonOutput = gson.toJson(task1);
        String key = "tasks/task";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);

        int idTask = Integer.parseInt(response.body());

        task1.setId(idTask);

        String jsonForAssert = gson.toJson(task1);

        HttpResponse<String> responseBack = backFromServer(key + "/?id=" + idTask);
        String jsonInput = responseBack.body();

        Assertions.assertEquals(jsonForAssert, jsonInput, "Задачи не совпадают!");
    }

    @Test
    void postEpic() {
        String jsonOutput = gson.toJson(epic1);
        String key = "tasks/epic";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);

        int idEpic = Integer.parseInt(response.body());

        epic1.setId(idEpic);

        String jsonForAssert = gson.toJson(epic1);

        HttpResponse<String> responseBack = backFromServer(key + "/?id=" + idEpic);
        String jsonInput = responseBack.body();

        Assertions.assertEquals(jsonForAssert, jsonInput, "Эпики не совпадают!");
    }

    @Test
    void postSubtask() {
        String jsonOutput = gson.toJson(epic1);
        String key = "tasks/epic";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);

        int epicId = Integer.parseInt(response.body());

        subtask1.setEpicId(epicId);
        String jsonOutput1 = gson.toJson(subtask1);
        String key1 = "tasks/subtask";

        HttpResponse<String> response1 = sendOnServer(key1, jsonOutput1);
        int idSubtask = Integer.parseInt(response1.body());

        subtask1.setId(idSubtask);

        String jsonForAssert = gson.toJson(subtask1);

        HttpResponse<String> responseBack = backFromServer(key1 + "/?id=" + idSubtask);
        String jsonInput = responseBack.body();

        Assertions.assertEquals(jsonForAssert, jsonInput, "Подзадачи не совпадают!");
    }

    @Test
    void updateTask() {
        String jsonOutput = gson.toJson(task1);
        String key = "tasks/task";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);

        int idTask = Integer.parseInt(response.body());

        task2.setId(idTask);

        String jsonOutput1 = gson.toJson(task2);
        String key1 = "tasks/task" + "/?id=" + idTask;
        HttpResponse<String> response1 = sendOnServer(key1, jsonOutput1);

        String jsonForAssert = gson.toJson(task2);

        HttpResponse<String> responseBack = backFromServer(key + "/?id=" + idTask);
        String jsonInput = responseBack.body();

        Assertions.assertEquals(jsonForAssert, jsonInput, "Задачи не совпадают!");
    }

    @Test
    void updateEpic() {
        String jsonOutput = gson.toJson(epic1);
        String key = "tasks/epic";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);

        int idEpic = Integer.parseInt(response.body());

        epic2.setId(idEpic);

        String jsonOutput1 = gson.toJson(epic2);
        String key1 = "tasks/epic" + "/?id=" + idEpic;
        HttpResponse<String> response1 = sendOnServer(key1, jsonOutput1);

        String jsonForAssert = gson.toJson(epic2);

        HttpResponse<String> responseBack = backFromServer(key + "/?id=" + idEpic);
        String jsonInput = responseBack.body();

        Assertions.assertEquals(jsonForAssert, jsonInput, "Эпики не совпадают!");
    }

    @Test
    void updateSubtask() {
        String jsonOutput = gson.toJson(epic1);
        String key = "tasks/epic";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);

        int idEpic = Integer.parseInt(response.body());

        subtask1.setEpicId(idEpic);

        String jsonOutput1 = gson.toJson(subtask1);
        String key1 = "tasks/subtask";
        HttpResponse<String> response1 = sendOnServer(key1, jsonOutput1);
        int subtaskId = Integer.parseInt(response1.body());

        subtask2.setId(subtaskId);
        subtask2.setEpicId(idEpic);
        String jsonForAssert = gson.toJson(subtask2);
        String key2 = "tasks/subtask" + "/?id=" + subtaskId;
        HttpResponse<String> response2 = sendOnServer(key2, jsonForAssert);

        HttpResponse<String> responseBack = backFromServer(key2);
        String jsonInput = responseBack.body();

        Assertions.assertEquals(jsonForAssert, jsonInput, "Эпики не совпадают!");
    }

    @Test
    void deleteTask() {
        String jsonOutput = gson.toJson(task1);
        String key = "tasks/task";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);

        int idTask = Integer.parseInt(response.body());

        String key1 = "tasks/task/?id=" + idTask;

        HttpResponse<String> response1 = deleteOnServer(key1);

        HttpResponse<String> response2 = backFromServer(key);
        String forAssert = response2.body();

        Assertions.assertEquals("[]", forAssert, "Задача не удалена");
    }

    @Test
    void deleteEpic() {
        String jsonOutput = gson.toJson(epic1);
        String key = "tasks/epic";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);

        int idEpic = Integer.parseInt(response.body());

        String key1 = "tasks/epic/?id=" + idEpic;

        HttpResponse<String> response1 = deleteOnServer(key1);

        HttpResponse<String> response2 = backFromServer(key);
        String forAssert = response2.body();

        Assertions.assertEquals("[]", forAssert, "Задача не удалена");
    }

    @Test
    void deleteSubtask() {
        String jsonOutput = gson.toJson(epic1);
        String key = "tasks/epic";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);

        int idEpic = Integer.parseInt(response.body());

        subtask1.setEpicId(idEpic);
        String jsonOutput1 = gson.toJson(subtask1);
        String key1 = "tasks/subtask";

        HttpResponse<String> response1 = sendOnServer(key1, jsonOutput1);

        int idSubtask = Integer.parseInt(response1.body());

        HttpResponse<String> response2 = deleteOnServer("tasks/subtask/?id=" + idSubtask);

        HttpResponse<String> response3 = backFromServer("tasks/subtask");
        String forAssert = response3.body();

        Assertions.assertEquals("[]", forAssert, "Задача не удалена");
    }

    @Test
    void deleteAllTask() {
        String jsonOutput = gson.toJson(task1);
        String jsonOutput1 = gson.toJson(task2);

        String key = "tasks/task";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);
        HttpResponse<String> response1 = sendOnServer(key, jsonOutput1);

        HttpResponse<String> response2 = deleteOnServer("tasks/task");

        HttpResponse<String> response3 = backFromServer("tasks/task");
        String forAssert = response3.body();

        Assertions.assertEquals("[]", forAssert, "Задача не удалена");
    }

    @Test
    void deleteAllEpic() {
        String jsonOutput = gson.toJson(epic1);
        String jsonOutput1 = gson.toJson(epic2);

        String key = "tasks/epic";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);
        HttpResponse<String> response1 = sendOnServer(key, jsonOutput1);

        HttpResponse<String> response2 = deleteOnServer("tasks/epic");

        HttpResponse<String> response3 = backFromServer("tasks/epic");
        String forAssert = response3.body();

        Assertions.assertEquals("[]", forAssert, "Задача не удалена");
    }

    @Test
    void deleteAllSubtask() {
        String jsonOutput = gson.toJson(epic1);

        String key = "tasks/epic";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);
        int epicId = Integer.parseInt(response.body());

        subtask1.setEpicId(epicId);
        subtask2.setEpicId(epicId);

        String jsonOutput1 = gson.toJson(subtask1);
        String jsonOutput2 = gson.toJson(subtask2);

        HttpResponse<String> response1 = sendOnServer("tasks/subtask", jsonOutput1);
        HttpResponse<String> response2 = sendOnServer("tasks/subtask", jsonOutput2);

        HttpResponse<String> response3 = deleteOnServer("tasks/subtask");

        HttpResponse<String> response4 = backFromServer("tasks/subtask");
        String forAssert = response4.body();

        Assertions.assertEquals("[]", forAssert, "Задача не удалена");
    }

    @Test
    void getHistory() throws TaskNotFoundException {
        String jsonOutput = gson.toJson(epic1);

        String key = "tasks/epic";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);
        int epicId = Integer.parseInt(response.body());
        epic1.setId(epicId);

        subtask1.setEpicId(epicId);
        subtask2.setEpicId(epicId);

        String jsonOutput1 = gson.toJson(subtask1);
        String jsonOutput2 = gson.toJson(subtask2);

        HttpResponse<String> response1 = sendOnServer("tasks/subtask", jsonOutput1);
        HttpResponse<String> response2 = sendOnServer("tasks/subtask", jsonOutput2);

        int subtaskId1 = Integer.parseInt(response1.body());
        subtask1.setId(subtaskId1);
        int subtaskId2 = Integer.parseInt(response2.body());
        subtask2.setId(subtaskId2);
        epic1.setSubtaskIds(subtaskId1);
        epic1.setSubtaskIds(subtaskId2);

        HttpResponse<String> response3 = backFromServer("tasks/subtask/?id=" + subtaskId1);
        HttpResponse<String> response4 = backFromServer("tasks/subtask/?id=" + subtaskId2);
        HttpResponse<String> response5 = backFromServer("tasks/epic/?id=" + epicId);

        tasksManager.updateEpicTask(epic1);

        List<Task> historyAssert = new ArrayList<>();
        historyAssert.add(subtask1);
        historyAssert.add(subtask2);
        historyAssert.add(epic1);
        String outAssert = gson.toJson(historyAssert);

        HttpResponse<String> response6 = backFromServer("tasks/history");
        String inAssert = response6.body();

        Assertions.assertEquals(outAssert, inAssert, "История не совпадает");
    }

    @Test
    void getPrioritizedTasks() throws TaskNotFoundException {
        String jsonOutput = gson.toJson(epic1);

        String key = "tasks/epic";

        HttpResponse<String> response = sendOnServer(key, jsonOutput);
        int epicId = Integer.parseInt(response.body());

        subtask1.setEpicId(epicId);
        subtask2.setEpicId(epicId);

        String jsonOutput1 = gson.toJson(subtask1);
        String jsonOutput2 = gson.toJson(subtask2);

        HttpResponse<String> response1 = sendOnServer("tasks/subtask", jsonOutput1);
        HttpResponse<String> response2 = sendOnServer("tasks/subtask", jsonOutput2);

        int subtaskId1 = Integer.parseInt(response1.body());
        subtask1.setId(subtaskId1);
        int subtaskId2 = Integer.parseInt(response2.body());
        subtask2.setId(subtaskId2);
        epic1.setSubtaskIds(subtaskId1);
        epic1.setSubtaskIds(subtaskId2);

        String jsonTask = gson.toJson(task1);
        HttpResponse<String> response0 = sendOnServer("tasks/task", jsonTask);
        int taskId = Integer.parseInt(response0.body());
        task1.setId(taskId);

        TreeMap<LocalDateTime, Task> historyAssert = new TreeMap<>();
        historyAssert.put(subtask1.getStartDateTime(), subtask1);
        historyAssert.put(subtask2.getStartDateTime(), subtask2);
        historyAssert.put(task1.getStartDateTime(), task1);
        String outAssert = gson.toJson(historyAssert);

        HttpResponse<String> response6 = backFromServer("tasks");
        String inAssert = response6.body();

        Assertions.assertEquals(outAssert, inAssert, "История не совпадает");
    }

    @Test
    void getEpicSubtask() {

    }


    private HttpResponse<String> sendOnServer(String key, String json) {
        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create("http://localhost:8080/" + key);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Неудачный запрос. Код ошибки:" + response.statusCode());
                return null;
            }
            return response;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpResponse<String> backFromServer(String key) {
        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create("http://localhost:8080/" + key);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Неудачный запрос. Код ошибки:" + response.statusCode());
                return null;
            }
            return response;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpResponse<String> deleteOnServer(String key) {
        HttpClient client = HttpClient.newHttpClient();

        URI uri = URI.create("http://localhost:8080/" + key);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Неудачный запрос. Код ошибки:" + response.statusCode());
                return null;
            }
            return response;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}