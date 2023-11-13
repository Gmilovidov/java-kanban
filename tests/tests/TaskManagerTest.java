package tests;

import model.*;
import org.junit.jupiter.api.Test;

import service.TaskManager;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    private final T taskManager;

    protected TaskManagerTest(T taskManager){
        this.taskManager = taskManager;
    }

    @Test
    void createTask() {
        Task task = new Task(1,"task1", "Купить автомобиль", 10L, LocalDateTime.now());
        taskManager.createTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);
        savedTask.setId(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        taskManager.createEpic(epic);
        final int epicId = epic.getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);
        savedEpic.setId(epicId);

        assertNotNull(savedEpic, "Эпики не возвращаются");
        assertEquals(epic, savedEpic, "Эпики не совпадают");
        assertEquals(StatusTasks.NEW,savedEpic.getStatusTask());
    }

    @Test
    void createSubtask() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        Subtask subtask1 = (new Subtask(2,"New Subtask", "Подзадача", 10L, LocalDateTime.now(), 1));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        final int subtaskId = subtask1.getId();
        final Subtask savedSubtask1 = taskManager.getSubtaskById(subtaskId);
        savedSubtask1.setId(subtaskId);

        assertNotNull(savedSubtask1, "Подзадачи не возвращаются");
        assertEquals(subtask1, savedSubtask1, "Подзадачи не совпадают");
        assertEquals(1, savedSubtask1.getIdEpic(), "id эпиков не совпадают");
    }

    @Test
    void getTaskMap() {
        Task task = new Task(1,"task1", "Купить автомобиль", 10L, LocalDateTime.now());
        taskManager.createTask(task);
        final Map<Integer, Task> savedDataTask = taskManager.getTaskMap();
        Task savedTask = savedDataTask.get(1);

        assertNotNull(savedDataTask, "Мапа с задачами не возвращается");
        assertEquals(task, savedTask, "Мапы не совпадают");
    }

    @Test
    void getEpicMap() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        taskManager.createEpic(epic);
        final Map<Integer, Epic> savedDataEpic = taskManager.getEpicMap();
        Epic savedEpic = savedDataEpic.get(1);

        assertNotNull(savedDataEpic, "Мапа с 'эпиками' не возвращается");
        assertEquals(epic, savedEpic, "Мапы не совпадают");
    }

    @Test
    void getSubtaskMap() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        Subtask subtask1 = (new Subtask(2,"New Subtask", "Подзадача", 10L, LocalDateTime.now(), 1));

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        final Map<Integer, Subtask> savedDataSubtask = taskManager.getSubtaskMap();
        Subtask savedSubtask = savedDataSubtask.get(2);

        assertNotNull(savedDataSubtask, "Мапа с подзадачами не возвращается");
        assertEquals( 1, savedSubtask.getIdEpic(), "id эпиков не совпадают");
        assertEquals(subtask1, savedSubtask, "Мапы не совпадают");
    }

    @Test
    void removeTask() {
        Task task = new Task(1,"task1", "Купить автомобиль", 10L, LocalDateTime.now());
        taskManager.createTask(task);
        taskManager.removeTask();
        final Map<Integer, Task> savedDataTask = taskManager.getTaskMap();

        assertEquals("{}" , savedDataTask.toString(), "Мапа не пустая");
    }

    @Test
    void removeEpic() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        Subtask subtask1 = (new Subtask(2,"New Subtask", "Подзадача", 10L, LocalDateTime.now(), 1));

        epic.setId(1);
        subtask1.setId(2);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.removeEpic();
        final Integer idEpic = subtask1.getIdEpic();
        final Map<Integer, Epic> savedDataEpic = taskManager.getEpicMap();

        assertEquals("{}", savedDataEpic.toString(), "Мапа не пустая");
        assertNull(idEpic, "idEpic не пустой");
    }

    @Test
    void removeSubtask() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        Subtask subtask1 = (new Subtask(2,"New Subtask", "Подзадача", 10L, LocalDateTime.now(), 1));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.removeSubtask();

        final Map<Integer, Subtask> savedDataSubtask = taskManager.getSubtaskMap();

        assertEquals("{}", savedDataSubtask.toString(), "Мапа не пустая");
        assertEquals("[]", epic.getIdSubtask().toString(), "idSubtask не пустая");
        assertEquals(StatusTasks.NEW, taskManager.getEpicMap().get(1).getStatusTask(), "Статус не обновился на NEW");
    }

    @Test
    void calculateStatEpicShouldReturnNEWForNullSubtask() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        Subtask subtask1 = (new Subtask(2,"New Subtask", "Подзадача", 10L, LocalDateTime.now(), 1));
        Subtask subtask2 = (new Subtask(3,"New Subtask", "Подзадача", 10L, LocalDateTime.now().plusMinutes(30), 1));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.removeSubtask();

        assertEquals("{}", taskManager.getSubtaskMap().toString(), "Список подзадач не пустой");
        assertEquals(StatusTasks.NEW, taskManager.getEpicMap().get(1).getStatusTask(), "Статус эпика не NEW");
    }

    @Test
    void calculateStatEpicShouldReturnNEWForAllSubtaskStatusNew() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        Subtask subtask1 = (new Subtask(2,"New Subtask", "Подзадача", 10L, LocalDateTime.now(), 1));
        Subtask subtask2 = (new Subtask(3,"New Subtask", "Подзадача", 10L, LocalDateTime.now().plusMinutes(30), 1));

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(StatusTasks.NEW, taskManager.getEpicById(1).getStatusTask(), "Статус эпика не NEW");
    }

    @Test
    void calculateStatEpicShouldReturnDONEForAllSubtaskStatusDone() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        Subtask subtask1 = (new Subtask(2,"New Subtask", "Подзадача", 10L, LocalDateTime.now(), 1));
        Subtask subtask2 = (new Subtask(3,"New Subtask", "Подзадача", 10L, LocalDateTime.now().plusMinutes(30), 1));

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Subtask subtask3 = new Subtask(2, "Test addNewSubtask", StatusTasks.DONE,
                "Test addNewSubtask description", 15L, LocalDateTime.now().plusMinutes(40), 1);
        Subtask subtask4 = new Subtask(3, "Test addNewSubtask", StatusTasks.DONE,
                "Test addNewSubtask description", 15L, LocalDateTime.now().plusMinutes(60),1);

        taskManager.updateSubtask(subtask3);
        taskManager.updateSubtask(subtask4);

        assertEquals(StatusTasks.DONE, taskManager.getEpicById(1).getStatusTask());
    }


    // Должен быть статус NEW?
    @Test
    void calculateStatEpicShouldReturnNEWForStatSubtasksStatusNEWandDONE() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        Subtask subtask1 = (new Subtask(2,"New Subtask", "Подзадача", 10L, LocalDateTime.now(), 1));
        Subtask subtask2 = (new Subtask(3,"New Subtask", "Подзадача", 10L, LocalDateTime.now().plusMinutes(30), 1));

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Subtask subtask4 = new Subtask(3, "Test addNewSubtask", StatusTasks.DONE,
                "Test addNewSubtask description", 15L, LocalDateTime.now().plusMinutes(60), 1);

        taskManager.updateSubtask(subtask4);

        assertEquals(StatusTasks.NEW, taskManager.getEpicById(1).getStatusTask());
    }

    @Test
    void calculateStatEpicShouldReturnIN_PROGRESSForAllSubtaskStatusIN_PROGRESS() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        Subtask subtask1 = (new Subtask(2,"New Subtask", "Подзадача", 10L, LocalDateTime.now(), 1));
        Subtask subtask2 = (new Subtask(3,"New Subtask", "Подзадача", 10L, LocalDateTime.now().plusMinutes(30), 1));

        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Subtask subtask3 = new Subtask(2, "Test addNewSubtask", StatusTasks.IN_PROGRESS,
                "Test addNewSubtask description", 15L, LocalDateTime.now().plusMinutes(40), 1);
        Subtask subtask4 = new Subtask(3, "Test addNewSubtask", StatusTasks.IN_PROGRESS,
                "Test addNewSubtask description", 15L, LocalDateTime.now().plusMinutes(60),1);

        taskManager.updateSubtask(subtask3);
        taskManager.updateSubtask(subtask4);

        assertEquals(StatusTasks.IN_PROGRESS, taskManager.getEpicById(1).getStatusTask());
    }

    @Test
    void getTaskByIdShouldReturnNullPointerExceptionForUnexpectedIdForId2() {
        Task task = new Task(1,"task1", "Купить автомобиль", 10L, LocalDateTime.now());
        taskManager.createTask(task);
        try {
            taskManager.getTaskById(2);
        } catch (NullPointerException exception) {
            assertEquals("Cannot invoke \"model.Task.getId()\" because \"task\" is null", exception.getMessage());
        }
    }

    @Test
    void getSubtaskByIdShouldReturnNullPointerExceptionForUnexpectedIdForId3() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        Subtask subtask1 = (new Subtask(2,"New Subtask", "Подзадача", 10L, LocalDateTime.now(), 1));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        try {
            taskManager.getSubtaskById(3);
        } catch (NullPointerException exception) {
            assertEquals("Cannot invoke \"model.Task.getId()\" because \"task\" is null", exception.getMessage());
        }
    }

    @Test
    void getEpicByIdShouldReturnNullPointerExceptionForUnexpectedIdForId2() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        taskManager.createEpic(epic);
        try {
            taskManager.getEpicById(2);
        } catch (NullPointerException exception) {
            assertEquals("Cannot invoke \"model.Task.getId()\" because \"task\" is null", exception.getMessage());
        }

    }

    @Test
    void getSubtaskByIdEpicShouldReturnNullPointerExceptionForUnexpectedIdEpic2() {
        Epic epic = new Epic(1, "new Epic1", "Новый Эпик");
        Subtask subtask1 = (new Subtask(2,"New Subtask", "Подзадача", 10L, LocalDateTime.now(), 1));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        epic.setId(2);
        try {
            taskManager.getSubtaskByIdEpic(epic);
        } catch (NullPointerException exception) {
            assertEquals("Cannot invoke \"model.Task.getId()\" because \"task\" is null", exception.getMessage());
        }
    }

    @Test
    void CheckCrossTimeShouldReturnFalseForCrossed() {
        Task task = new Task(1,"task1", "Купить автомобиль", 10L, LocalDateTime.now());
        Task task2 = new Task(2,"task1", "Купить автомобиль", 10L, LocalDateTime.now().plusMinutes(1));
        taskManager.createTask(task);
        boolean check = taskManager.checkCrossTIme(task2);

        assertFalse(check);
    }

}