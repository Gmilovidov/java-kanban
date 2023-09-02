package Service;

import Model.Epic;
import Model.StatusTasks;
import Model.Subtask;
import Model.Task;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> dataTask;
    private final Map<Integer, Epic> dataEpic;
    private final Map<Integer, Subtask> dataSubtask;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        dataTask = new HashMap<>();
        dataEpic = new HashMap<>();
        dataSubtask = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    // 2.4 Создание задач
    @Override
    public void createTask(@NotNull Task task) {
        dataTask.put(task.getId(), task);
    }

    @Override
    public void createEpic(@NotNull Epic epic) {
        dataEpic.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(@NotNull Subtask subtask) {
        int idMentor = subtask.getIdEpic();
        dataEpic.get(idMentor).addSubtask(subtask.getId());
        dataSubtask.put(subtask.getId(), subtask);
    }

    // 2.1 Получение задач
    @Override
    public Map<Integer, Task> getTaskMap(Task task) {
        return dataTask;
    }

    @Override
    public Map<Integer, Epic> getEpicMap(Epic epic) {
        return dataEpic;
    }

    @Override
    public Map<Integer, Subtask> getSubtaskMap(Subtask subtask) {
        return dataSubtask;
    }

    // 2.2 Удаление задач
    @Override
    public void removeTask() {
        dataTask.clear();
    }

    @Override
    public void removeEpic() {
        for(Integer id : dataSubtask.keySet()) {
            Subtask subtask = dataSubtask.get(id);
            subtask.setIdEpic(null);
        }
        dataEpic.clear();
    }

    @Override
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
    @Override
    public Task getTaskById(Integer id) {
        historyManager.add(dataTask.get(id));
        return dataTask.get(id);
    }

    @Override
    public Epic getEpicById(Integer id) {
        historyManager.add(dataEpic.get(id));
        return dataEpic.get(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        historyManager.add(dataSubtask.get(id));
        return dataSubtask.get(id);
    }

    // 2.5 Обновление задач
    @Override
    public void updateTask(@NotNull Task task) {
        dataTask.remove(task.getId());
        dataTask.put(task.getId(), task);
    }

    @Override
    public void updateEpic(@NotNull Epic epic) {
        Epic oldEpic = dataEpic.get(epic.getId());
        epic.setIdSubtask(oldEpic.getIdSubtask());
        dataEpic.remove(epic.getId());
        dataEpic.put(epic.getId(), epic);
        calculateStatEpic(epic.getId());
    }

    @Override
    public void updateSubtask(@NotNull Subtask subtask) {
        dataSubtask.remove(subtask.getId());
        dataSubtask.put(subtask.getId(), subtask);
        calculateStatEpic(subtask.getIdEpic());
    }

    // 2.6 Удаление по id
    @Override
    public void removeTaskById(Integer id) {
        dataTask.remove(id);
    }
    @Override
    public void removeEpicById(Integer id) {
        Epic epic =  dataEpic.get(id);
        ArrayList<Integer> curSubId =  epic.getIdSubtask();
        for(Integer idSub : curSubId) {
            dataSubtask.get(idSub).setIdEpic(null);
        }
        dataEpic.remove(id);
    }

    @Override
    public void removeSubtaskById(Integer id) {
        Integer EpId = dataSubtask.get(id).getIdEpic();
        Epic curEpic = dataEpic.get(EpId); // получили нужный эпик
        ArrayList<Integer> subList =  curEpic.getIdSubtask();
        subList.remove(id);
        dataSubtask.remove(id);
        calculateStatEpic(EpId);
    }

    // 3.1 Получение Subtask по idEpic
    @Override
    public HashMap<Integer, Subtask> getSubtaskByIdEpic(@NotNull Epic epic) {
        HashMap<Integer, Subtask> subtasksByIdEpic = new HashMap<>();
        for (Integer i : epic.getIdSubtask()) {
            subtasksByIdEpic.put(i, dataSubtask.get(i));
        }
        return subtasksByIdEpic;
    }

    @Override
    public void calculateStatEpic(@NotNull Integer idEpic) {
        Epic curEpic = dataEpic.get(idEpic);// получили эпик к которому относится сабтаск
        ArrayList<Integer> idSubtask = curEpic.getIdSubtask();
        if (idSubtask.isEmpty()) {
            curEpic.setStatusTask(StatusTasks.NEW);
            return;
        }
        if (!dataSubtask.isEmpty()) {
            Subtask sub1 = dataSubtask.get(idSubtask.get(0));
            StatusTasks stat = sub1.getStatusTask();
            if (stat.equals(StatusTasks.IN_PROGRESS)) {
                curEpic.setStatusTask(StatusTasks.IN_PROGRESS);
                return;
            }
            for (Integer idSub : idSubtask) {
                Subtask sub = dataSubtask.get(idSub);
                if (!sub.getStatusTask().equals(stat)) {
                    curEpic.setStatusTask(StatusTasks.IN_PROGRESS);
                }
            }
            curEpic.setStatusTask(stat);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
