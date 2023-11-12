package service;

import com.google.gson.*;
import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    final KVTaskClient client;

   private static final Gson gson = new GsonBuilder()
           .registerTypeAdapter(LocalDateTime.class, new DataTimeAdapter())
           .create();


   public HttpTaskManager() {
       client = new KVTaskClient();
   }

    @Override
    public void save() {
        client.put("tasks", gson.toJson(dataTask.values()));
        client.put("subtasks", gson.toJson(dataSubtask.values()));
        client.put("epics", gson.toJson(dataEpic.values()));

        List<Integer> historyId = new ArrayList<>();

        for (Task t : historyManager.getHistory()) {
            historyId.add(t.getId());
        }

        client.put("history", gson.toJson(historyId));
   }


   public  void loadFromServer() {
       JsonElement jsonTasks = JsonParser.parseString(client.load("tasks"));
       if (!jsonTasks.isJsonNull()) {
           JsonArray jsonArrayTasks = jsonTasks.getAsJsonArray();
           for (JsonElement jsonTask : jsonArrayTasks) {
               Task task = gson.fromJson(jsonTask, Task.class);
               dataTask.put(task.getId(), task);
           }
       }

       JsonElement jsonSubtasks = JsonParser.parseString(client.load("subtasks"));
       if (!jsonTasks.isJsonNull()) {
           JsonArray jsonArraySubtasks = jsonSubtasks.getAsJsonArray();
           for (JsonElement jsonSubtask : jsonArraySubtasks) {
               Subtask subtask = gson.fromJson(jsonSubtask, Subtask.class);
               dataSubtask.put(subtask.getId(), subtask);
           }
       }

       JsonElement jsonEpics = JsonParser.parseString(client.load("epics"));
       if (!jsonEpics.isJsonNull()) {
           JsonArray jsonArrayEpics = jsonEpics.getAsJsonArray();
           for (JsonElement jsonEpic : jsonArrayEpics) {
               Epic epic  = gson.fromJson(jsonEpic, Epic.class);
               dataEpic.put(epic.getId(), epic);
           }
       }

       JsonElement jsonHistoryList = JsonParser.parseString(client.load("history"));
       if (!jsonHistoryList.isJsonNull()) {
           JsonArray jsonHistoryArray = jsonHistoryList.getAsJsonArray();
           for (JsonElement jsonId : jsonHistoryArray) {
               int id = jsonId.getAsInt();
               if (dataTask.containsKey(id)) {
                   historyManager.add(dataTask.get(id));
               } else if (dataSubtask.containsKey(id)) {
                   historyManager.add(dataSubtask.get(id));
               } else if (dataEpic.containsKey(id)) {
                   historyManager.add(dataEpic.get(id));
               }
           }
       }
   }
}
