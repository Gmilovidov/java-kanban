package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskManager {

    // 2.4 Создание задач

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    // 2.1 Получение задач

    Map<Integer, Task> getTaskMap();

    Map<Integer, Epic> getEpicMap();

    Map<Integer, Subtask> getSubtaskMap();

    // 2.2 Удаление задач

    void removeTask();

    void removeEpic();

    void removeSubtask();

    // 2.3 Получение по id

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    Subtask getSubtaskById(Integer id);

    // 2.5 Обновление задач

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    // 2.6 Удаление по id

    void removeTaskById(Integer id);

    void removeEpicById(Integer id);

    void removeSubtaskById(Integer id);

    // 3.1 Получение Subtask по idEpic

    HashMap<Integer, Subtask> getSubtaskByIdEpic(Epic epic);

    void calculateStatEpic(Integer idEpic);

    List<Task> getHistory();

    void setHistory(HistoryManager history);

    boolean checkCrossTIme(Task task);

    Set<Task> getPrioritizedTasks();
}



