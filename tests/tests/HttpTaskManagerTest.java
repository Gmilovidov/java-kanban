package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    public HttpTaskManagerTest() {
        super(new HttpTaskManager());
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer.start();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new DataTimeAdapter())
                .create();
        httpTaskManager = (HttpTaskManager) Managers.getDefault();
    }

    @AfterEach
    public void afterEach() {
        kvServer.close();
    }

    @Test
    public void createTasks() throws IOException {
        Task task = new Task(1,"name", "desc", 15L, LocalDateTime.now());
        createTask();
        Task taskLoaded = httpTaskManager.getTaskById(1);
        assertEquals(task, taskLoaded);
    }
}
