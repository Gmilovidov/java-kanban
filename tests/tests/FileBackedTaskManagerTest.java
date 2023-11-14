package tests;

import model.StatusTasks;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager(new File("tests/test.csv"));
    }

    @Test
    public void LoadFromFileShouldReturnIOException() {
        try {
            FileBackedTasksManager.loadFromFile(new File(" "));
        } catch (ManagerSaveException exception) {
            assertEquals("Не удалось считать данные из файла", exception.getMessage());
        }
    }

    @Test
    public void LoadFromFile() {
        Task task = new Task(1,"task1", StatusTasks.NEW, "Купить автомобиль", 10L, LocalDateTime.now());
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File("tests/test.csv"));
        fileBackedTasksManager.createTask(task);
        FileBackedTasksManager.loadFromFile(new File("tests/test.csv"));
        assertEquals(task, fileBackedTasksManager.getTaskById(1));
    }

}
