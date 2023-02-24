/*package http;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;

public class Main {
static Gson gson = new Gson();
    public static void main (String[] args) throws IOException {

        KVServer server = new KVServer();
        server.start();

        HttpTaskServer()

        String data = gson.toJson(info);
        taskClient.put("task", data);
        String pepa = taskClient.load("task");
        System.out.println(pepa);

        Integer[] info2 = {5,4,3,2,1};

        String data2 = gson.toJson(info2);
        taskClient.put("subtask", data2);
        String pepa2 = taskClient.load("subtask");
        System.out.println(pepa2);
    }
}*/