package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import model.StatusTasks;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer kvServer;
    Gson gson;
    HttpTaskManager httpTaskManager;


    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new DataTimeAdapter())
                .create();
        taskManager = new HttpTaskManager("http://localhost:8077/");
    }

    @AfterEach
    public void afterEach() {
        kvServer.close();
    }

    @Test
    public void createTasks() {
        Task task = new Task(1, "task1", StatusTasks.NEW, "Купить автомобиль", 10L, LocalDateTime.now());
        taskManager.createTask(task);
        taskManager.save();
        taskManager.loadFromServer();
        Task taskLoaded = taskManager.getTaskById(1);
        assertEquals(task, taskLoaded, "таски не равны");
    }

    @Test
    public void createSubtask() {
        Epic epic = new Epic(1,"new Epic1", "Новый Эпик");
        Subtask subtask1 = new Subtask(2, "New Subtask", StatusTasks.NEW, "Подзадача", 10L, LocalDateTime.now(), 1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.save();
        taskManager.loadFromServer();
        Subtask subtaskLoaded = taskManager.getSubtaskById(2);
        assertEquals(subtask1, subtaskLoaded, "сабтаски не равны");
    }

    @Test
    public void createEpic() {
        Epic epic = new Epic(1,"new Epic1", "Новый Эпик");
        taskManager.createEpic(epic);
        taskManager.save();
        taskManager.loadFromServer();
        Epic epicLoaded = taskManager.getEpicById(1);
        assertEquals(epic, epicLoaded, "эпики не равны");
    }
}
