package service;

import com.google.gson.*;
import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HttpTaskManager extends FileBackedTasksManager {

    final KVTaskClient client;

   private static final Gson gson = new GsonBuilder()
           .registerTypeAdapter(LocalDateTime.class, new DataTimeAdapter())
           .create();


   public HttpTaskManager(String URL) {
       client = new KVTaskClient(URL);
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
           Optional<String> optTask =  Optional.ofNullable(client.load("tasks"));
           if (optTask.isPresent()) {
               JsonElement jsonTasks = JsonParser.parseString(optTask.get());
               if (!jsonTasks.isJsonNull()) {
                   JsonArray jsonArrayTasks = jsonTasks.getAsJsonArray();
                   for (JsonElement jsonTask : jsonArrayTasks) {
                       Task task = gson.fromJson(jsonTask, Task.class);
                       dataTask.put(task.getId(), task);
                   }
               }
           }


           Optional<String> optSubtask = Optional.ofNullable(client.load("subtasks"));
           if (optSubtask.isPresent()) {
               JsonElement jsonSubtasks = JsonParser.parseString(optSubtask.get());
               if (!jsonSubtasks.isJsonNull()) {
                   JsonArray jsonArraySubtasks = jsonSubtasks.getAsJsonArray();
                   for (JsonElement jsonSubtask : jsonArraySubtasks) {
                       Subtask subtask = gson.fromJson(jsonSubtask, Subtask.class);
                       dataSubtask.put(subtask.getId(), subtask);
                   }
               }
           }

       Optional<String> optEpic = Optional.ofNullable(client.load("epics"));
       if (optEpic.isPresent()) {
           JsonElement jsonEpics = JsonParser.parseString(optEpic.get());
           if (!jsonEpics.isJsonNull()) {
               JsonArray jsonArrayEpics = jsonEpics.getAsJsonArray();
               for (JsonElement jsonEpic : jsonArrayEpics) {
                   Epic epic = gson.fromJson(jsonEpic, Epic.class);
                   dataEpic.put(epic.getId(), epic);
               }
           }
       }

       Optional<String> optHistory = Optional.ofNullable(client.load("history"));
       if (optHistory.isPresent()) {
           JsonElement jsonHistoryList = JsonParser.parseString(optHistory.get());
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
}
