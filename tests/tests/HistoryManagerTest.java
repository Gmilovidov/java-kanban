package tests;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    private static HistoryManager historyManager;
    private static Task task;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Test addNewTask", "Test addNewTask description");
    }

    @Test
    public void addTasksShouldReturn1HistoryFor1Task() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void addTaskShouldReturn0ForHistorySize() {
        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size(), "История не пустая");
    }

    @Test
    public void removeTasksFromStartEndMiddle() {
        Task task2 = new Task("Test addNewTask", "Test addNewTask description");
        Task task3 = new Task("Test addNewTask", "Test addNewTask description");
        Task task4 = new Task("Test addNewTask", "Test addNewTask description");
        Task task5 = new Task("Test addNewTask", "Test addNewTask description");

        task.setId(1);
        task2.setId(2);
        task3.setId(3);
        task4.setId(4);
        task5.setId(5);

        final List<Task> history = new ArrayList<>();
        history.add(task4);
        history.add(task2);

        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);

        historyManager.remove(3);
        historyManager.remove(1);
        historyManager.remove(5);

        final List<Task> savedHistory = historyManager.getHistory();

        assertEquals(history, savedHistory, "Истории не совпадают после удаления с середины, начала и конца");
    }

    @Test
    public void shouldDeleteDuplicate() {
        historyManager.add(task);
        historyManager.add(task);
        historyManager.add(task);

        final List<Task> savedHistory = historyManager.getHistory();

        assertEquals(1, savedHistory.size(), "Дубликаты не удалились");
    }

}

