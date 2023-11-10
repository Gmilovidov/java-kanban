package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Task;

import java.io.File;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

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

            default:
                writeResponse(httpExchange,"Такого эндпоинта не существует", 404);

        }
    }

    private void taskServerGetAllTasks(HttpExchange httpExchange) throws IOException {

       writeResponse(httpExchange, gson.toJson(taskManager.getTaskMap()), 200);
    }

    private void taskServerCreateTask(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        try {
            Task task = gson.fromJson(body, Task.class);
            taskManager.createTask(task);
        } catch (JsonSyntaxException exception) {
            writeResponse(httpExchange, "Получен некорректный JSON", 400);
        }
        writeResponse(httpExchange, "Таска добавлена", 200);
        httpTaskManager.save();
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (pathParts[2].equals("task") && requestMethod.equals("GET") && pathParts.length == 3) {
            return Endpoint.GET_TASKS;
        }
        if (pathParts[2].equals("task") && requestMethod.equals("GET") && pathParts.length == 4) {
            return Endpoint.GET_TASK_BY_ID;
        }
        if (requestMethod.equals("POST") &&pathParts[2].equals("task")) {
            return Endpoint.POST_ADD_UPDATE_TASK;
        }
        return Endpoint.UNKNOWN;
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
        DELETE_EPIC, GET_EPIC_SUBTASK_ID, GET_HISTORY,
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
