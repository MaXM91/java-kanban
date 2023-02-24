package managers;

import managers.exceptions.ManagerSaveException;
import managers.exceptions.TaskNotFoundException;
import managers.exceptions.TasksValidateException;
import tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager implements TasksManager {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) throws ManagerSaveException, IOException {

        final TasksManager tasksManager = Managers.getDefault();

        final Path pathSaveFile = Path.of("save.csv");

        File saveFile = new File(String.valueOf(pathSaveFile));

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(saveFile);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    final private Path pathFileForSave;
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    public FileBackedTasksManager(Path pathFileForSave) {
        this.pathFileForSave = pathFileForSave;
    }

    @Override
    public Integer addTask(SimpleTask simpleTask) throws TasksValidateException {
        super.addTask(simpleTask);
        save();
        return simpleTask.getId();
    }

    @Override
    public Integer addSubtask(Subtask subtask) throws TasksValidateException {
        super.addSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public Integer addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public void updateTask(SimpleTask simpleTask) throws TasksValidateException, TaskNotFoundException {
        super.updateTask(simpleTask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws TasksValidateException, TaskNotFoundException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpicTask(Epic epic) throws TaskNotFoundException {
        super.updateEpicTask(epic);
        save();
    }

    @Override
    public void removeTaskById(int id) throws TaskNotFoundException {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubtaskById(Integer id) throws TaskNotFoundException {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) throws TaskNotFoundException {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public List<SimpleTask> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return super.getAllSubtasks();
    }

    @Override
    public List<Epic> getAllEpics() {
        return super.getAllEpics();
    }

    @Override
    public SimpleTask getTask(Integer taskId) throws TaskNotFoundException {
        SimpleTask simpleTask = super.getTask(taskId);
        save();
        return simpleTask;
    }

    @Override
    public Subtask getSubtask(Integer subtaskId) {
        Subtask subtask = super.getSubtask(subtaskId);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(Integer epicId) {
        Epic epic = super.getEpic(epicId);
        save();
        return epic;
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        return super.getEpicSubtasks(epicId);
    }

    protected void save() throws ManagerSaveException {
        try (FileWriter fileWriter = new FileWriter(pathFileForSave.toFile(), StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,startDateTime,duration,epic\n");

            for (Integer id : InMemoryTasksManager.tasks.keySet()) {
                fileWriter.write(toString(InMemoryTasksManager.tasks.get(id)) + "\n");
            }

            for (Integer id : InMemoryTasksManager.epics.keySet()) {
                fileWriter.write(toString(InMemoryTasksManager.epics.get(id)) + "\n");
            }

            for (Integer id : InMemoryTasksManager.subtasks.keySet()) {
                fileWriter.write(toString(InMemoryTasksManager.subtasks.get(id)) + "\n");
            }

            fileWriter.write("\n");

            if (!(historyToString().isEmpty() || historyToString().isBlank())) {
                fileWriter.write(historyToString());
            } else {
                fileWriter.write("HistoryIsEmpty");
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Ошибка записи данных");
        }

    }

    private String toString(Task task) {
        return task.getId() + "," + task.getTypeTask() + "," + task.getName() + "," +
                task.getStatus() + "," + task.getDescription() + "," + task.getStartDateTime().format(formatter) + "," +
                task.getDuration().getSeconds() / 60 + "," + task.getEpicId();
    }

    private static String historyToString() {
        StringBuilder stringHistoryIds = new StringBuilder();

        for (Task task : InMemoryTasksManager.historyManager.getHistory()) {
            stringHistoryIds.append(task.getId()).append(",");
        }
        return stringHistoryIds.toString();
    }

    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        final Path pathSaveFile = Path.of(String.valueOf(file));

        FileBackedTasksManager saveOfLastManager = new FileBackedTasksManager(pathSaveFile);

        String lineTasks = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        if (lineTasks.isBlank() || lineTasks.isEmpty()) {
            return saveOfLastManager;
        }

        String[] lineTask = lineTasks.split("\n");

        for (int i = 1; i < lineTask.length - 2; i++) {
            if (i < lineTask.length - 1) {
                Task task = fromString(lineTask[i]);
                if (task instanceof SimpleTask) {
                    SimpleTask simpleTask = (SimpleTask) task;
                    InMemoryTasksManager.tasks.put(simpleTask.getId(), simpleTask);
                    InMemoryTasksManager.validatedPrioritizedTasks.put(simpleTask.getStartDateTime(), simpleTask);
                } else if (task instanceof Subtask) {
                    Subtask subtask = (Subtask) task;
                    InMemoryTasksManager.subtasks.put(subtask.getId(), subtask);
                    InMemoryTasksManager.epics.get(subtask.getEpicId()).setSubtaskIds(subtask.getId());
                    InMemoryTasksManager.validatedPrioritizedTasks.put(subtask.getStartDateTime(), subtask);
                } else if (task instanceof Epic) {
                    Epic epic = (Epic) task;
                    InMemoryTasksManager.epics.put(epic.getId(), epic);
                }
            }
        }

        if (!lineTask[lineTask.length - 1].equals("HistoryIsEmpty")) {
            List<Integer> historyIds = historyFromString(lineTask[lineTask.length - 1]);
            for (Integer historyId : historyIds) {
                if (InMemoryTasksManager.tasks.get(historyId) != null) {
                    InMemoryTasksManager.historyManager.add(InMemoryTasksManager.tasks.get(historyId));
                } else if (InMemoryTasksManager.subtasks.get(historyId) != null) {
                    InMemoryTasksManager.historyManager.add(InMemoryTasksManager.subtasks.get(historyId));
                } else if (InMemoryTasksManager.epics.get(historyId) != null) {
                    InMemoryTasksManager.historyManager.add(InMemoryTasksManager.epics.get(historyId));
                }
            }
        }
        return saveOfLastManager;
    }

    private static Task fromString(String value) {

        String[] partsOfTask = value.split(",");
        int id = Integer.parseInt(partsOfTask[0]);
        String type = partsOfTask[1];
        String name = partsOfTask[2];
        StatusTask status = checkStatus(partsOfTask[3]);
        String description = partsOfTask[4];
        LocalDateTime startTime = LocalDateTime.parse(partsOfTask[5], formatter);
        Duration durations = Duration.ofMinutes(Integer.parseInt(partsOfTask[6]));

        switch (type) {
            case "SIMPLE_TASK": {
                SimpleTask task = new SimpleTask(name, description, status, startTime, durations);
                task.setId(id);
                return task;
            }
            case "SUBTASK": {
                Subtask subtask = new Subtask(name, description, status, startTime, durations, Integer.parseInt(partsOfTask[7]));
                subtask.setId(id);
                return subtask;
            }
            case "EPIC": {
                Epic epic = new Epic(name, description, status, startTime, durations);
                epic.setId(id);
                return epic;
            }
        }
        return null;
    }

    private static StatusTask checkStatus(String status) {
        switch (status) {
            case "NEW":
                return StatusTask.NEW;
            case "DONE":
                return StatusTask.DONE;
            case "IN_PROGRESS":
                return StatusTask.IN_PROGRESS;
        }
        return null;
    }

    private static List<Integer> historyFromString(String value) {
        String[] partsOfIds = value.split(",");
        List<Integer> historyIds = new ArrayList<>();
        for (String partsOfId : partsOfIds) {
            historyIds.add(Integer.parseInt(partsOfId));
        }
        return historyIds;
    }
}

