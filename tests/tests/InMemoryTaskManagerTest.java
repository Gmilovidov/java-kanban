package tests;

import org.junit.jupiter.api.BeforeEach;
import service.InMemoryTaskManager;
import service.TaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

   @BeforeEach
    public void beforeEach() {
       taskManager = new InMemoryTaskManager();
   }


}