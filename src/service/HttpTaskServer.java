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
    private final HttpTaskManager httpTaskManager = new HttpTaskManager("http://localhost:8077/");

    public HttpTaskServer() {
        taskManager = httpTaskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Endpoint endpoint = getEndpoint(httpExchange
                .getRequestURI()
                .getPath(), httpExchange
                .getRequestMethod(), httpExchange);

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
            case GET_PRIORITIZED_TASKS:{
                taskServerGetPrioritizedTask(httpExchange);
            }

            default:
                writeResponse(httpExchange,"Такого эндпоинта не существует", 404);

        }
    }

    private void taskServerGetPrioritizedTask(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        String json = gson.toJson(taskManager.getPrioritizedTasks());

        if (!json.isEmpty() && !json.equals("[]")) {
            writeResponse(httpExchange, json, 200);
        } else {
            writeResponse(httpExchange, "список приоритетных задач пустой", 400);
        }
    }

    private void taskServerGetAllTasks(HttpExchange httpExchange) throws IOException {
       httpTaskManager.loadFromServer();

       String gsonTasks;
       String gsonSubtask;
       String gsonEpic;
       gsonTasks = gson.toJson(taskManager.getTaskMap());
       gsonSubtask = gson.toJson(taskManager.getSubtaskMap());
       gsonEpic = gson.toJson(taskManager.getEpicMap());
        if (taskManager.getTaskMap().isEmpty() && taskManager.getSubtaskMap().isEmpty() && taskManager.getEpicMap().isEmpty()) {
            writeResponse(httpExchange, "задач нет", 400);
        }
        writeResponse(httpExchange, gsonTasks + gsonSubtask + gsonEpic, 200);
    }

    private void taskServerCreateTask(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
        try {
            Task task = gson.fromJson(body, Task.class);
            if (taskManager.getTaskMap().containsKey(task.getId())) {
                taskManager.updateTask(task);
                writeResponse(httpExchange, "Таска обновлена", 201);
            } else {
                taskManager.createTask(task);
                writeResponse(httpExchange, "Таска добавлена", 201);
            }
        } catch (JsonSyntaxException exception) {
            writeResponse(httpExchange, "Получен некорректный JSON", 400);
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
                writeResponse(httpExchange, "сабтаска обновлена", 201);
            } else {
                taskManager.createSubtask(subtask);
                writeResponse(httpExchange, "сабтаска добавлена", 201);
            }
        } catch (JsonSyntaxException exception) {
            writeResponse(httpExchange, "Получен некорректный JSON", 400);
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
                taskManager.updateEpic(epic);
                writeResponse(httpExchange, "эпик обновлен", 201);
            } else {
                taskManager.createEpic(epic);
                writeResponse(httpExchange, "эпик добавлен", 201);
            }

        } catch (JsonSyntaxException exception) {
            writeResponse(httpExchange, "Получен некорректный JSON", 400);
        }

        httpTaskManager.save();
    }

    private void taskServerGetTaskById(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        if (getIdUrl(httpExchange).isPresent()) {
            if (taskManager.getTaskMap().containsKey(getIdUrl(httpExchange).get())) {
                String json = gson.toJson(taskManager.getTaskById(getIdUrl(httpExchange).get()));
                writeResponse(httpExchange, json, 200);
            } else {
                writeResponse(httpExchange, "по заданному id таска не найдена",
                        400);
            }
        } else {
            writeResponse(httpExchange, "не верно введен id", 400);
        }

        httpTaskManager.save();
    }

    private void taskServerGetSubtaskById(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        if (getIdUrl(httpExchange).isPresent()) {
            if (taskManager.getSubtaskMap().containsKey(getIdUrl(httpExchange).get())) {
                String json = gson.toJson(taskManager.getSubtaskById(getIdUrl(httpExchange).get()));
                writeResponse(httpExchange, json, 200);
            } else {
                writeResponse(httpExchange, "по заданному id сабтаска не найдена", 400);
            }
        } else {
            writeResponse(httpExchange, "не верно введен id", 400);
        }

        httpTaskManager.save();
    }

    private void taskServerGetEpicById(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        if (getIdUrl(httpExchange).isPresent()) {
            if (taskManager.getEpicMap().containsKey(getIdUrl(httpExchange).get())) {
                String json = gson.toJson(taskManager.getEpicById(getIdUrl(httpExchange).get()));
                writeResponse(httpExchange, json, 200);
            } else {
                writeResponse(httpExchange, "по заданному id эпик не найден", 400);
            }
        } else {
            writeResponse(httpExchange, "не верно введен id", 400);
        }

        httpTaskManager.save();
    }

    private void taskServerDeleteTask(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        if (getIdUrl(httpExchange).isPresent()) {
            if (taskManager.getTaskMap().containsKey(getIdUrl(httpExchange).get())) {
                taskManager.removeTaskById(getIdUrl(httpExchange).get());
                writeResponse(httpExchange, "Таска удалена", 200);
            } else {
                writeResponse(httpExchange, "По заданному id таска не найдена", 400);
            }
        } else {
            writeResponse(httpExchange, "не верно введен id", 400);
        }
        httpTaskManager.save();
    }

    private void taskServerDeleteSubtask(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        if (getIdUrl(httpExchange).isPresent()) {
            if (taskManager.getSubtaskMap().containsKey(getIdUrl(httpExchange).get())) {
                taskManager.removeSubtaskById(getIdUrl(httpExchange).get());
                writeResponse(httpExchange, "сабтаска удалена", 200);
            } else {
                writeResponse(httpExchange, "по заданному id сабтаски не найдено", 400);
            }
        } else {
            writeResponse(httpExchange, "не верно введен id", 400);
        }
        httpTaskManager.save();
    }

    private void taskServerDeleteEpic(HttpExchange httpExchange) throws IOException {
        httpTaskManager.loadFromServer();

        if (getIdUrl(httpExchange).isPresent()) {
            if (taskManager.getEpicMap().containsKey(getIdUrl(httpExchange).get())) {
                taskManager.removeEpicById(getIdUrl(httpExchange).get());
                writeResponse(httpExchange, "эпик удален", 200);
            } else {
                writeResponse(httpExchange, "по заданному id эпик не найден", 400);
            }
        } else {
            writeResponse(httpExchange, "не верно введен id", 400);
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
             if (!json.equals("[]")) {
                 writeResponse(httpExchange, json, 200);
             } else {
                 writeResponse(httpExchange, "список истории пустой", 400);
             }
         }
    }

    private Endpoint getEndpoint(String requestPath, String requestMethod, HttpExchange httpExchange) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2) {
            return Endpoint.GET_PRIORITIZED_TASKS;
        }

        if (requestMethod.equals("GET") && pathParts[2].equals("history") && pathParts[1].equals("tasks")) {
            return Endpoint.GET_HISTORY;
        }

        if (pathParts[2].equals("task") && requestMethod.equals("GET") && getIdUrl(httpExchange).isEmpty()) {
            return Endpoint.GET_TASKS;
        }

        if (pathParts[2].equals("task") && requestMethod.equals("GET") && getIdUrl(httpExchange).isPresent()) {
            return Endpoint.GET_TASK_BY_ID;
        }

        if ( pathParts[2].equals("task") && requestMethod.equals("POST")) {
            return Endpoint.POST_ADD_UPDATE_TASK;
        }
        if (pathParts[2].equals("task") && requestMethod.equals("DELETE") && getIdUrl(httpExchange).isPresent()) {
            return Endpoint.DELETE_TASK_BY_ID;
        }
        if (pathParts[2].equals("subtask") && requestMethod.equals("GET") && getIdUrl(httpExchange).isPresent()) {
            return Endpoint.GET_SUBTASK_BY_ID;
        }
        if (pathParts[2].equals("subtask") && requestMethod.equals("POST")) {
            return Endpoint.POST_ADD_UPDATE_SUBTASK;
        }
        if (pathParts[2].equals("subtask") && requestMethod.equals("DELETE") && getIdUrl(httpExchange).isPresent()) {
            return Endpoint.DELETE_SUBTASK;
        }
        if (pathParts[2].equals("epic") && requestMethod.equals("POST")) {
            return Endpoint.POST_ADD_UPDATE_EPIC;
        }
        if (pathParts[2].equals("epic") && requestMethod.equals("GET") && getIdUrl(httpExchange).isPresent()) {
            return Endpoint.GET_EPIC_BY_ID;
        }
        if (pathParts[2].equals("epic") && requestMethod.equals("DELETE") && getIdUrl(httpExchange).isPresent()) {
            return Endpoint.DELETE_EPIC_BY_ID;
        }
        if (pathParts[1].equals("subtask")
                && pathParts[2].equals("epic")
                && requestMethod.equals("GET") && getIdUrl(httpExchange).isPresent()) {
            return Endpoint.GET_EPIC_SUBTASK_ID;
        }
        if (requestMethod.equals("DELETE") && pathParts[2].equals("task") && getIdUrl(httpExchange).isEmpty() ) {
            return Endpoint.DELETE_ALL_TASKS;
        }


        return Endpoint.UNKNOWN;
    }

    private Optional<Integer> getIdUrl(HttpExchange httpExchange) {

        try  {
            String pathPart = httpExchange.getRequestURI().getQuery();
            String idPart = pathPart.substring(3);
            if (!idPart.isEmpty()) {
                return Optional.of(Integer.parseInt(idPart));
            } else {
                return Optional.empty();
            }
        } catch (NullPointerException exception) {
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
                new HttpTaskServer());
        httpServer.start();
        System.out.println("Сервер на порту 8081 запущен");

    }
}
