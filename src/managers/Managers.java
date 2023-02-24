package managers;

//import http.HttpTaskManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.HttpTasksManager;
import http.typeadapters.DurationTypeAdapter;
import http.typeadapters.LocalDateTimeTypeAdapter;
import http.typeadapters.StatusTaskTypeAdapter;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    public static TasksManager getDefault() {
        return new InMemoryTasksManager();
    }

    public static TasksManager getDefault(Path pathSaveFile) {
        return new FileBackedTasksManager(pathSaveFile);
    }

    public static TasksManager getDefault(URI uriKVServer) {
        return new HttpTasksManager(uriKVServer);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .registerTypeAdapter(StatusTask.class, new StatusTaskTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
        return gson;
    }
}
