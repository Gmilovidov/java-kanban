package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;


public class HttpTaskServer implements HttpHandler {

    TaskManager taskManager;
    private  final static Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private  final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new DataTimeAdapter())
            .create();
    private final HttpTaskManager httpTaskManager = new HttpTaskManager();

    public HttpTaskServer(FileBackedTasksManager fileBackedTasksManager) {
        taskManager = fileBackedTasksManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
        switch (endpoint) {
            case GET_TASKS: {
                taskServerGetAllTasks(httpExchange);
            }
            case POST_ADD_UPDATE_TASK: {
                taskServerCreateTask(httpExchange);
            }
            case GET_TASK_BY_ID: {
                taskServerGetTaskById(httpExchange);
            }
            case DELETE_TASK_BY_ID: {
                taskServerDeleteTask(httpExchange);
            }
            case GET_SUBTASK_BY_ID: {
                taskServerGetSubtaskById(httpExchange);
            }
            case POST_ADD_UPDATE_SUBTASK: {
                taskServerCreateSubtask(httpExchange);
            }
            case DELETE_SUBTASK: {
                taskServerDeleteSubtask(httpExchange);
            }
            case POST_ADD_UPDATE_EPIC: {
                taskServerCreateEpic(httpExchange);
            }
            case GET_EPIC_BY_ID: {
                taskServerGetEpicById(httpExchange);
            }
            case DELETE_EPIC_BY_ID: {
                taskServerDeleteEpic(httpExchange);
            }
            case DELETE_ALL_TASKS: {
                taskServerDeleteAllTask(httpExchange);
            }
            case GET_HISTORY: {
                taskServerGetHistory(httpExchange);
            }

            default:
                writeResponse(httpExchange,"Такого эндпоинта не существует", 404);

        }
    }

    private void taskServerGetAllTasks(HttpExchange httpExchange) throws IOException {
       httpTaskManager.loadFromServer();

       String gsonTasks = gson.toJson(taskManager.getTaskMap());
       if (gsonTasks != null && gsonTasks.length() > 0) {
           gsonTasks = gsonTasks.substring(0, gsonTasks.length() - 1);
       }
       String gsonSubtask = gson.toJson(taskManager.getSubtaskMap());
        if (gsonSubtask != null && gsonSubtask.length() > 0) {
            gsonSubtask = gsonSubtask.substring(1, gsonSubtask.length() - 1);
        }
       String gsonEpic = gson.toJson(taskManager.getEpicMap());
        if (gsonEpic != null && gsonEpic.length() > 0) {
            gsonEpic = gsonEpic.substring(1);
        }
       String AllGson = gsonTasks + "," + gsonSubtask + "," + gsonEpic;
       writeResponse(httpExchange, AllGson, 200);
    }

    private void taskServerCreateTask(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        try {
            Task task = gson.fromJson(body, Task.class);
            if (taskManager.getTaskMap().containsKey(task.getId())) {
                taskManager.updateTask(task);
            } else {
                taskManager.createTask(task);
            }
        } catch (JsonSyntaxException exception) {
            writeResponse(httpExchange, "Получен некорректный JSON", 400);
        }
        writeResponse(httpExchange, "Таска добавлена", 200);

        httpTaskManager.save();
    }

    private void taskServerGetTaskById(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        if (getIdUrl(httpExchange).isPresent()) {
            String json = gson.toJson(taskManager.getSubtaskById(getIdUrl(httpExchange).get()));
            writeResponse(httpExchange, json, 200);
        }
        httpTaskManager.save();
    }

    private void taskServerDeleteTask(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        if (getIdUrl(httpExchange).isPresent()) {
            taskManager.removeTaskById(getIdUrl(httpExchange).get());
            writeResponse(httpExchange, "Таска удалена", 200);
        }
        httpTaskManager.save();
    }

    private void taskServerGetSubtaskById(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        if (getIdUrl(httpExchange).isPresent()) {
           String json = gson.toJson(taskManager.getSubtaskById(getIdUrl(httpExchange).get()));
            writeResponse(httpExchange, json, 200);
        }
        httpTaskManager.save();
    }

    private void taskServerCreateSubtask(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        try {
            Subtask subtask = gson.fromJson(body, Subtask.class);
            if (taskManager.getSubtaskMap().containsKey(subtask.getId())) {
                taskManager.updateSubtask(subtask);
            } else {
                taskManager.createSubtask(subtask);
            }
        } catch (JsonSyntaxException exception) {
            writeResponse(httpExchange, "Получен некорректный JSON", 400);
        }
        writeResponse(httpExchange, "сабтаска добавлена", 200);

        httpTaskManager.save();
    }

    private void taskServerDeleteSubtask(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        if (getIdUrl(httpExchange).isPresent()) {
            taskManager.removeSubtaskById(getIdUrl(httpExchange).get());
            writeResponse(httpExchange, "сабтаска удалена", 200);
        }
        httpTaskManager.save();
    }

    private void taskServerCreateEpic(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        try {
            Epic epic = gson.fromJson(body, Epic.class);
            if (taskManager.getEpicMap().containsKey(epic.getId())) {
                taskManager.updateTask(epic);
            } else {
                taskManager.createTask(epic);
            }

        } catch (JsonSyntaxException exception) {
            writeResponse(httpExchange, "Получен некорректный JSON", 400);
        }
        writeResponse(httpExchange, "эпик добавлен", 200);

        httpTaskManager.save();
    }

    private void taskServerGetEpicById(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        if (getIdUrl(httpExchange).isPresent()) {
            String json = gson.toJson(taskManager.getEpicById(getIdUrl(httpExchange).get()));
            writeResponse(httpExchange, json, 200);
        }

        httpTaskManager.save();
    }

    private void taskServerDeleteEpic(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        if (getIdUrl(httpExchange).isPresent()) {
            taskManager.removeEpicById(getIdUrl(httpExchange).get());
            writeResponse(httpExchange, "эпик удален", 200);
        }

        httpTaskManager.save();
    }

    private void taskServerDeleteAllTask(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        taskManager.removeTask();
        taskManager.removeSubtask();
        taskManager.removeEpic();

        writeResponse(httpExchange, "Все задачи удалены", 200);

        httpTaskManager.save();
    }

    private void taskServerGetHistory(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

         if (!taskManager.getHistory().isEmpty()) {
             String json = gson.toJson(taskManager.getHistory());
             writeResponse(httpExchange, json, 200);
         }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2) {
            return Endpoint.GET_PRIORITIZED_TASKS;
        }

        if (pathParts[2].equals("task") && requestMethod.equals("GET") && pathParts.length == 4 ) {
            return Endpoint.GET_TASK_BY_ID;
        }
        if (pathParts[2].equals("task") && requestMethod.equals("GET") && pathParts.length == 3) {
            return Endpoint.GET_TASKS;
        }
        if ( pathParts[2].equals("task") && requestMethod.equals("POST") ) {
            return Endpoint.POST_ADD_UPDATE_TASK;
        }
        if (pathParts[2].equals("task") && requestMethod.equals("DELETE") && pathParts.length == 4) {
            return Endpoint.DELETE_TASK_BY_ID;
        }
        if (pathParts[2].equals("subtask") && requestMethod.equals("GET") && pathParts.length == 4) {
            return Endpoint.GET_SUBTASK_BY_ID;
        }
        if (pathParts[2].equals("subtask") && requestMethod.equals("POST")) {
            return Endpoint.POST_ADD_UPDATE_SUBTASK;
        }
        if (pathParts[2].equals("subtask") && requestMethod.equals("DELETE")) {
            return Endpoint.DELETE_SUBTASK;
        }
        if (pathParts[2].equals("epic") && requestMethod.equals("POST")) {
            return Endpoint.POST_ADD_UPDATE_EPIC;
        }
        if (pathParts[2].equals("epic") && requestMethod.equals("GET")) {
            return Endpoint.GET_EPIC_BY_ID;
        }
        if (pathParts[2].equals("epic") && requestMethod.equals("DELETE")) {
            return Endpoint.DELETE_EPIC_BY_ID;
        }
        if (pathParts[2].equals("subtask")
                && pathParts[3].equals("epic")
                && requestMethod.equals("GET")) {
            return Endpoint.GET_EPIC_SUBTASK_ID;
        }
        if (requestMethod.equals("DELETE") && pathParts[3].equals("task")) {
            return Endpoint.DELETE_ALL_TASKS;
        }
        if (requestMethod.equals("GET") && pathParts[2].equals("history")) {
            return Endpoint.GET_HISTORY;
        }


        return Endpoint.UNKNOWN;
    }

    private Optional<Integer> getIdUrl(HttpExchange httpExchange) {
        String[] pathParts = httpExchange.getRequestURI().getPath().split("/");
        String URLPart = pathParts[pathParts.length - 1].substring(4);
        try {
            return Optional.of(Integer.parseInt(URLPart));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        if(responseString.isBlank()) {
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


    enum Endpoint {GET_TASKS, GET_TASK_BY_ID,
        GET_SUBTASK_BY_ID, POST_ADD_UPDATE_TASK,
        DELETE_TASK_BY_ID, DELETE_ALL_TASKS,
        GET_EPIC_BY_ID,  POST_ADD_UPDATE_SUBTASK,
        POST_ADD_UPDATE_EPIC, DELETE_SUBTASK,
        DELETE_EPIC_BY_ID, GET_EPIC_SUBTASK_ID, GET_HISTORY,
        GET_PRIORITIZED_TASKS, UNKNOWN
    }

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8081), 0);
        httpServer.createContext("/tasks",
                new HttpTaskServer(FileBackedTasksManager.loadFromFile(new File("saveTasks2.csv"))));
        httpServer.start();
        System.out.println("Сервер на порту 8081 запущен");


    }
}
