package Service;

import Model.Epic;
import Model.Subtask;
import Model.Task;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    protected String status = "NEW";
    protected Integer id = 0;
    protected HashMap<Integer, Task> dataTask = new HashMap<>();
    protected HashMap<Integer, Epic> dataEpic = new HashMap<>();
    protected HashMap<Integer, Subtask> dataSubtask = new HashMap<>();

    // 2.4 Создание задач

    public void saveTask(@NotNull Task task) {
        id++;
        task.setId(id);
        dataTask.put(id, task);
    }

    public void saveEpic(@NotNull Epic epic) {
        id++;
        epic.setId(id);
        epic.setStatusEpic(status);
        dataEpic.put(id, epic);
    }

    public void saveSubtask(@NotNull Subtask subtask) {
        id++;
        subtask.setId(id);
        int idMentor = subtask.getIdEpic();
        dataEpic.get(idMentor).addSubtask(subtask.getId());
        dataSubtask.put(id, subtask);
    }

    // 2.1 Получение задач

    public HashMap<Integer, Task> getTaskMap(Task task) {
        return dataTask;
    }

    public HashMap<Integer, Epic> getEpicMap(Epic epic) {
        return dataEpic;
    }

    public HashMap<Integer, Subtask> getSubtaskMap(Subtask subtask) {
        return dataSubtask;
    }

    // 2.2 Удаление задач

    public void removeTask() {
        dataTask.clear();
    }

    public void removeEpic() {
        for(Integer id : dataSubtask.keySet()) {
            Subtask subtask = dataSubtask.get(id);
            subtask.setIdEpic(null);
        }
        dataEpic.clear();
    }

    public void removeSubtask() {
        ArrayList<Integer> idSubtask = new ArrayList<>();
        dataSubtask.clear();
        for(Integer id : dataEpic.keySet()) {
            Epic ep = dataEpic.get(id);
            ep.setIdSubtask(idSubtask);
            calculateStatEpic(id);
        }

    }

    // 2.3 Получение по id

    public Task getTaskById(Integer id) {
        return dataTask.get(id);
    }

    public Epic getEpicById(Integer id) {
        return dataEpic.get(id);
    }

    public Subtask getSubtaskById(Integer id) {
        return dataSubtask.get(id);
    }

    // 2.5 Обновление задач

    public void updateTask(@NotNull Task task) {
        dataTask.remove(task.getId());
        dataTask.put(task.getId(), task);
    }

    public void updateEpic(@NotNull Epic epic) {
        Epic oldEpic = dataEpic.get(epic.getId());
        epic.setIdSubtask(oldEpic.getIdSubtask());
        dataEpic.remove(epic.getId());
        dataEpic.put(epic.getId(), epic);
        calculateStatEpic(epic.getId());
    }

    public void updateSubtask(@NotNull Subtask subtask) {
        dataSubtask.remove(subtask.getId());
        dataSubtask.put(subtask.getId(), subtask);
        calculateStatEpic(subtask.getIdEpic());
    }

    // 2.6 Удаление по id

    public void removeTaskById(Integer id) {
        dataTask.remove(id);
    }

    public void removeEpicById(Integer id) {
        Epic epic =  dataEpic.get(id);
        ArrayList<Integer> curSubId =  epic.getIdSubtask();
        for(Integer idSub : curSubId) {
            dataSubtask.get(idSub).setIdEpic(null);
        }
        dataEpic.remove(id);
    }

    public void removeSubtaskById(Integer id) {
        Integer EpId = dataSubtask.get(id).getIdEpic();
        Epic curEpic = dataEpic.get(EpId); // получили нужный эпик
        ArrayList<Integer> subList =  curEpic.getIdSubtask();
        subList.remove(id);
        dataSubtask.remove(id);
        calculateStatEpic(EpId);
    }

    // 3.1 Получение Subtask по idEpic

    public HashMap<Integer, Subtask> getSubtaskByIdEpic(@NotNull Epic epic) {
        HashMap<Integer, Subtask> subtasksByIdEpic = new HashMap<>();
        for (Integer i : epic.getIdSubtask()) {
            subtasksByIdEpic.put(i, dataSubtask.get(i));
        }
        return subtasksByIdEpic;
    }

    public void calculateStatEpic(@NotNull Integer idEpic) {
        Epic curEpic = dataEpic.get(idEpic);// получили эпик к которому относится сабтаск
        ArrayList<Integer> idSubtask = curEpic.getIdSubtask();
        if (idSubtask.isEmpty()) {
            curEpic.setStatusEpic("NEW");
            return;
        }
        if (!dataSubtask.isEmpty()) {
            Subtask sub1 = dataSubtask.get(idSubtask.get(0));
            String stat = sub1.getStatusTask();
            if (stat.equals("IN_PROGRESS")) {
                curEpic.setStatusEpic("IN_PROGRESS");
                return;
            }
            for (Integer idSub : idSubtask) {
                Subtask sub = dataSubtask.get(idSub);
                if (!sub.getStatusTask().equals(stat)) {
                    curEpic.setStatusEpic("IN_PROGRESS");
                }
            }
            curEpic.setStatusEpic(stat);
        }
    }
}



