package http.typeadapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import managers.StatusTask;

import java.io.IOException;

public class StatusTaskTypeAdapter extends TypeAdapter<StatusTask> {

    @Override
    public void write(JsonWriter jsonWriter, StatusTask statusTask) throws IOException {
        switch(statusTask) {
            case NEW:
                jsonWriter.value("NEW");
                break;
            case IN_PROGRESS:
                jsonWriter.value("IN_PROGRESS");
                break;
            case DONE:
                jsonWriter.value("DONE");
                break;
            default:
                throw new IOException();
        }
    }

    @Override
    public StatusTask read(JsonReader jsonReader) throws IOException {
        String status = jsonReader.nextString();

        switch (status) {
            case "NEW":
                return StatusTask.NEW;
            case "IN_PROGRESS":
                return StatusTask.IN_PROGRESS;
            case "DONE":
                return StatusTask.DONE;
            default:
                throw new IOException();
        }
    }
}
