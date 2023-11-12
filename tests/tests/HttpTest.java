package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HttpTaskManager;
import service.HttpTaskServer;
import service.KVServer;

import java.io.IOException;

public class HttpTest extends TaskManagerTest<HttpTaskManager> {

    KVServer kvServer;

    protected HttpTest(HttpTaskManager taskManager) {
        super(taskManager);
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterEach
    public void afterEach() {
        kvServer.close();
    }

}
