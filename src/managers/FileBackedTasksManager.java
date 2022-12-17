package managers;

import tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager implements TasksManager {

    final private Path pathFileForSave;



    public static void main(String[] args) throws ManagerSaveException {

        final Managers managers = new Managers();
        final TasksManager tasksManager = managers.getDefault();

        SimpleTask simpleTask1 = new SimpleTask("Кошка", "Серая", StatusTask.NEW);                       //1 Задача 1
        SimpleTask simpleTask2 = new SimpleTask("Собака", "Черная", StatusTask.NEW);                     //2 Задача 2

        int taskId1 = tasksManager.addTask(simpleTask1);
        int taskId2 = tasksManager.addTask(simpleTask2);

        Epic epic1 = new Epic("Купить вино", "Красное", StatusTask.NEW);                                 //3 Составная 1
        int epicId1 = tasksManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Вино", "Красное сухое", StatusTask.NEW, epicId1);                //4 Подзадача 1
        Subtask subtask2 = new Subtask("Вино", "Красное полусухое", StatusTask.IN_PROGRESS, epicId1);    //5 Подзадача 2
        Subtask subtask3 = new Subtask("Вино", "Красное сладкое", StatusTask.NEW, epicId1);              //6 Подзадача 3

        int sub1Id = tasksManager.addSubtask(subtask1);
        int sub2Id = tasksManager.addSubtask(subtask2);
        int sub3Id = tasksManager.addSubtask(subtask3);

        Epic epic2 = new Epic("Покупки для велосипеда", "Крыло", StatusTask.NEW);                        //7 Составная 2

        int epicId2 = tasksManager.addEpic(epic2);

        Subtask subtask4 = new Subtask("Крыло", "Заднее", StatusTask.IN_PROGRESS, epicId2);              //8 Подзадача 4

        int sub4Id = tasksManager.addSubtask(subtask4);

        System.out.println(tasksManager.getAllTasks());
        System.out.println(tasksManager.getAllSubtasks());
        System.out.println(tasksManager.getAllEpics());
        System.out.println("\n");

        simpleTask1.setName("Собака");
        simpleTask1.setDescription("Черная");
        simpleTask1.setStatus(StatusTask.DONE);
        tasksManager.updateTask(simpleTask1);

        subtask1.setName("Шоколад");
        subtask1.setDescription("Черный");
        subtask1.setStatus(StatusTask.NEW);
        tasksManager.updateSubtask(subtask1);

        subtask2.setName("Шоколад");
        subtask2.setDescription("Белый");
        subtask2.setStatus(StatusTask.NEW);
        tasksManager.updateSubtask(subtask2);

        simpleTask2.setName("Пиво");
        simpleTask2.setDescription("Чешское");
        simpleTask2.setStatus(StatusTask.NEW);
        tasksManager.updateTask(simpleTask2);

        subtask3.setName("Колбаса");
        subtask3.setDescription("Копченая");
        subtask3.setStatus(StatusTask.NEW);
        tasksManager.updateSubtask(subtask3);

        epic1.setName("Купить еду");
        epic1.setDescription("На день");
        epic1.setStatus(StatusTask.DONE);
        tasksManager.updateEpicTask(epic1);

        subtask4.setName("Руль");
        subtask4.setDescription("Сплав аллюминия");
        subtask4.setStatus(StatusTask.DONE);
        tasksManager.updateSubtask(subtask4);

        epic2.setName("Покупки для велосипеда");
        epic2.setDescription("Руль");
        epic2.setStatus(StatusTask.NEW);
        tasksManager.updateEpicTask(epic2);

        System.out.println(tasksManager.getAllTasks());
        System.out.println(tasksManager.getAllSubtasks());
        System.out.println(tasksManager.getAllEpics());
        System.out.println("\n");

        tasksManager.removeTaskById(taskId2);
        tasksManager.removeSubtaskById(sub2Id);
        tasksManager.removeEpicById(epicId2);

        System.out.println(tasksManager.getAllTasks());
        System.out.println(tasksManager.getAllSubtasks());
        System.out.println(tasksManager.getAllEpics());
        System.out.println("\n");

        System.out.println(tasksManager.getEpicSubtasks(epicId1));
        System.out.println("\n");

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println("Вывод по Ид:");

        System.out.println(tasksManager.getTask(taskId1));       // 1

        for (Task task : tasksManager.getHistory()) {            // - 1
            System.out.println(task);
        }
////////////////////////////////////////////////////////////////////////////////
        System.out.println("\n");

        System.out.println(tasksManager.getSubtask(sub1Id));     // 4

        System.out.println(tasksManager.getTask(taskId1));       // 1

        System.out.println(tasksManager.getEpic(epicId1));       // 3

        System.out.println(tasksManager.getEpic(epicId1));       // 3

        System.out.println(tasksManager.getTask(taskId1));       // 1

        System.out.println(tasksManager.getEpic(epicId1));       // 3

        System.out.println(tasksManager.getTask(taskId1));       // 1

        System.out.println(tasksManager.getEpic(epicId1));       // 3

        System.out.println(tasksManager.getTask(taskId1));       // 1

        System.out.println("\n");

        for (Task task : tasksManager.getHistory()) {            // - 4 1 3 3 1 3 1 3 1 --->>>  4 3 1
            System.out.println(task);
        }

        System.out.println("\n");
///////////////////////////////////////////////////////////////////////////

       /* tasksManager.removeEpicById(epicId1);                    // 3 -> 4, ,6


        for (Task task : tasksManager.getHistory()) {            // 1
            System.out.println(task);
        }
*/
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //taskManager.removeAllTasks();
        // tasksManager.removeAllSubtasks();
        // tasksManager.removeAllEpics();

        System.out.println(tasksManager.getAllTasks());
        System.out.println(tasksManager.getAllSubtasks());
        System.out.println(tasksManager.getAllEpics());
        System.out.println("\n");

        for (Task task : tasksManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("\n" + "\n" + "\n");

        Path pathSaveFile = Path.of("save.csv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(pathSaveFile);

        System.out.println(fileBackedTasksManager.getAllTasks());
        System.out.println(fileBackedTasksManager.getAllSubtasks());
        System.out.println(fileBackedTasksManager.getAllEpics());
        System.out.println("\n");

        for (Task task : fileBackedTasksManager.getHistory()) {
            System.out.println(task);
        }

    }




    FileBackedTasksManager(Path pathFileForSave) {
        this.pathFileForSave = pathFileForSave;
        File saveFile = new File(String.valueOf(pathFileForSave));

        try {
                loadFromFile(saveFile);
        } catch (NullPointerException | IOException exp) {
            System.out.println("Ошибка чтения данных");
        }
    }

    @Override
    public Integer addTask(SimpleTask simpleTask) {
        super.addTask(simpleTask);
        save();
        return simpleTask.getId();
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
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
    public void updateTask(SimpleTask simpleTask) {
        super.updateTask(simpleTask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpicTask(Epic epic) {
        super.updateEpicTask(epic);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
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
    public SimpleTask getTask(Integer taskId) {
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


    private void save() {
        try (FileWriter fileWriter = new FileWriter(pathFileForSave.toFile(), StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic\n");

            for (Integer id : tasks.keySet()) {
                fileWriter.write(toString(tasks.get(id)) + "\n");
            }

            for (Integer id : epics.keySet()) {
                fileWriter.write(toString(epics.get(id)) + "\n");
            }

            for (Integer id : subtasks.keySet()) {
                fileWriter.write(toString(subtasks.get(id)) + "\n");
            }

            fileWriter.write("\n");

            fileWriter.write(historyToString(historyManager));

        } catch (IOException exp) {
                throw new ManagerSaveException("Ошибка записи данных");
        }

    }

    private String toString(Task task) {
        String stringForAdd;

        if (task instanceof SimpleTask) {
            SimpleTask simpleTask = (SimpleTask) task;
            stringForAdd = simpleTask.getId() + "," + TypeTask.SIMPLE_TASK + "," + simpleTask.getName() + "," +
                    simpleTask.getStatus() + "," + simpleTask.getDescription() + ",";
            return stringForAdd;
        } else if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            stringForAdd = subtask.getId() + "," + TypeTask.SUBTASK + "," + subtask.getName() + "," +
                    subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpicId() + ",";
            return stringForAdd;
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            stringForAdd = epic.getId() + "," + TypeTask.EPIC + "," + epic.getName() + "," + epic.getStatus() +
                    "," + epic.getDescription() + ",";
            return stringForAdd;
        }
        return null;
    }

    static String historyToString(HistoryManager manager) {

        String stringHistoryIds = "";

        for (int i = 0 ; i < manager.getHistory().size(); i++) {
            if (manager.getHistory().get(i) instanceof SimpleTask) {
                SimpleTask simpleTask = (SimpleTask) manager.getHistory().get(i);
                stringHistoryIds += simpleTask.getId() + ",";
            } else if (manager.getHistory().get(i) instanceof Subtask) {
                Subtask subtask = (Subtask) manager.getHistory().get(i);
                stringHistoryIds += subtask.getId() + ",";
            } else if (manager.getHistory().get(i) instanceof Epic) {
                Epic epic = (Epic) manager.getHistory().get(i);
                stringHistoryIds += epic.getId() + ",";
            }
        }
        return stringHistoryIds;
    }

    private void loadFromFile(File file) throws IOException {
        String lineTasks = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            if(lineTasks.isBlank() || lineTasks.isEmpty()) {
                return;
            }

        String[] lineTask = lineTasks.split("\n");

        for(int i = 1; i < lineTask.length - 2; i++) {
            if (i < lineTask.length - 1) {
                Task task = fromString(lineTask[i]);
                if (task instanceof SimpleTask) {
                    SimpleTask simpleTask = (SimpleTask) task;
                    tasks.put(simpleTask.getId(), simpleTask);
                } else if (task instanceof Subtask) {
                    Subtask subtask = (Subtask) task;
                    subtasks.put(subtask.getId(), subtask);
                    epics.get(subtask.getEpicId()).setSubtaskIds(subtask.getId());
                } else if (task instanceof Epic) {
                    Epic epic = (Epic) task;
                    epics.put(epic.getId(), epic);
                }
            }
        }

        List<Integer> historyIds = historyFromString(lineTask[lineTask.length - 1]);
        for (Integer historyId : historyIds) {
            if (tasks.get(historyId) != null) {
                historyManager.add(tasks.get(historyId));
            } else if (subtasks.get(historyId) != null) {
                historyManager.add(subtasks.get(historyId));
            } else if (epics.get(historyId) != null) {
                historyManager.add(epics.get(historyId));
            }
        }
    }

    private Task fromString(String value) {
        String[] partsOfTask = value.split(",");

        switch (partsOfTask[1]) {
            case "SIMPLE_TASK": {
                StatusTask status = checkStatus(partsOfTask[3]);
                SimpleTask task = new SimpleTask(partsOfTask[2], partsOfTask[4], status);
                task.setId(Integer.parseInt(partsOfTask[0]));
                return task;
            }
            case "SUBTASK": {
                StatusTask status = checkStatus(partsOfTask[3]);
                Subtask subtask = new Subtask(partsOfTask[2], partsOfTask[4], status, Integer.parseInt(partsOfTask[5]));
                subtask.setId(Integer.parseInt(partsOfTask[0]));
                return subtask;
            }
            case "EPIC": {
                StatusTask status = checkStatus(partsOfTask[3]);
                Epic epic = new Epic(partsOfTask[2], partsOfTask[4], status);
                epic.setId(Integer.parseInt(partsOfTask[0]));
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

    static List<Integer> historyFromString(String value) {
        String[] partsOfIds = value.split(",");
        List<Integer> historyIds = new ArrayList<>();
        for (String partsOfId : partsOfIds) {
            historyIds.add(Integer.parseInt(partsOfId));
        }
        return historyIds;
    }

}

