package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    // 2.4 Создание задач

    void createTask(@NotNull Task task);

    void createEpic(@NotNull Epic epic);

    void createSubtask(@NotNull Subtask subtask);

    // 2.1 Получение задач

    Map<Integer, Task> getTaskMap(Task task);

    Map<Integer, Epic> getEpicMap(Epic epic);

    Map<Integer, Subtask> getSubtaskMap(Subtask subtask);

    // 2.2 Удаление задач

    void removeTask();

    void removeEpic();

    void removeSubtask();

    // 2.3 Получение по id

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    Subtask getSubtaskById(Integer id);

    // 2.5 Обновление задач

    void updateTask(@NotNull Task task);

    void updateEpic(@NotNull Epic epic);

    void updateSubtask(@NotNull Subtask subtask);

    // 2.6 Удаление по id

    void removeTaskById(Integer id);

    void removeEpicById(Integer id);

    void removeSubtaskById(Integer id);

    // 3.1 Получение Subtask по idEpic

    HashMap<Integer, Subtask> getSubtaskByIdEpic(@NotNull Epic epic);

    void calculateStatEpic(@NotNull Integer idEpic);

    List<Task> getHistory();



}



