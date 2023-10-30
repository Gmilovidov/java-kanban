package service;

import model.Epic;
import model.StatusTasks;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> dataTask;
    protected final Map<Integer, Epic> dataEpic;
    protected final Map<Integer, Subtask> dataSubtask;
    protected HistoryManager historyManager;
    protected List<Task> tasksList;

    public InMemoryTaskManager() {
        dataTask = new HashMap<>();
        dataEpic = new HashMap<>();
        dataSubtask = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        tasksList = new ArrayList<>();
    }

    // 2.4 Создание задач
    @Override
    public void createTask(Task task) {
        if (tasksList.isEmpty()) {
            dataTask.put(task.getId(), task);
            tasksList.add(task);
        } else if (checkCrossTIme(task)) {
            dataTask.put(task.getId(), task);
            tasksList.add(task);
        } else {
            System.out.println("Нельзя создать задачу с пересечением времени уже сохраненных задач");
        }
    }

    @Override
    public void createEpic(Epic epic) {
        dataEpic.put(epic.getId(), epic);
        calculatorEpicDurationAndStart(epic.getId());
        calculateStatEpic(epic.getId());
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (tasksList.isEmpty()) {
            int idMentor = subtask.getIdEpic();
            dataEpic.get(idMentor).addSubtask(subtask.getId());
            dataSubtask.put(subtask.getId(), subtask);
            tasksList.add(subtask);
            calculatorEpicDurationAndStart(idMentor);
        } else if (checkCrossTIme(subtask)) {
            int idMentor = subtask.getIdEpic();
            dataEpic.get(idMentor).addSubtask(subtask.getId());
            dataSubtask.put(subtask.getId(), subtask);
            tasksList.add(subtask);
            calculatorEpicDurationAndStart(idMentor);
        } else {
            System.out.println("Нельзя создать задачу с пересечением времени уже сохраненных задач");
        }
    }

    // 2.1 Получение задач
    @Override
    public Map<Integer, Task> getTaskMap() {
        return dataTask;
    }

    @Override
    public Map<Integer, Epic> getEpicMap() {
        return dataEpic;
    }

    @Override
    public Map<Integer, Subtask> getSubtaskMap() {
        return dataSubtask;
    }

    // 2.2 Удаление задач
    @Override
    public void removeTask() {
        for(Task task : dataTask.values()) {
            tasksList.remove(task);
        }
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
        for(Task subtask : dataSubtask.values()) {
            tasksList.remove(subtask);
        }
        dataSubtask.clear();
        for(Integer id : dataEpic.keySet()) {
            Epic ep = dataEpic.get(id);
            ep.setIdSubtask(idSubtask);
            calculateStatEpic(id);
            calculatorEpicDurationAndStart(id);
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
        if (dataEpic.containsKey(id)) {
            historyManager.add(dataEpic.get(id));
        }
        return dataEpic.get(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        historyManager.add(dataSubtask.get(id));
        return dataSubtask.get(id);
    }

    // 2.5 Обновление задач
    @Override
    public void updateTask(Task task) {
        if (checkCrossTIme(task)) {
            tasksList.remove(dataTask.get(task.getId()));
            tasksList.add(task);
            dataTask.remove(task.getId());
            dataTask.put(task.getId(), task);
        } else {
            System.out.println("Нельзя создать задачу с пересечением времени уже сохраненных задач");
        }

    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = dataEpic.get(epic.getId());
        epic.setIdSubtask(oldEpic.getIdSubtask());
        dataEpic.remove(epic.getId());
        dataEpic.put(epic.getId(), epic);
        calculateStatEpic(epic.getId());
        calculatorEpicDurationAndStart(epic.getId());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (checkCrossTIme(subtask)) {
            tasksList.remove(subtask);
            tasksList.add(subtask);
            dataSubtask.remove(subtask.getId());
            dataSubtask.put(subtask.getId(), subtask);
            calculateStatEpic(subtask.getIdEpic());
            calculatorEpicDurationAndStart(subtask.getIdEpic());
        } else {
            System.out.println("Нельзя создать задачу с пересечением времени уже сохраненных задач");
        }
    }

    // 2.6 Удаление по id
    @Override
    public void removeTaskById(Integer id) {
        historyManager.remove(id);
        dataTask.remove(id);
    }
    @Override
    public void removeEpicById(Integer id) {
        Epic epic =  dataEpic.get(id);
        ArrayList<Integer> curSubId =  epic.getIdSubtask();
        for(Integer idSub : curSubId) {
            dataSubtask.get(idSub).setIdEpic(null);
            historyManager.remove(idSub);
        }
        dataEpic.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtaskById(Integer id) {
        Integer EpId = dataSubtask.get(id).getIdEpic();
        Epic curEpic = dataEpic.get(EpId); // получили нужный эпик
        ArrayList<Integer> subList =  curEpic.getIdSubtask();
        subList.remove(id);
        dataSubtask.remove(id);
        historyManager.remove(id);
        calculateStatEpic(EpId);
        calculatorEpicDurationAndStart(EpId);
    }

    // 3.1 Получение Subtask по idEpic
    @Override
    public HashMap<Integer, Subtask> getSubtaskByIdEpic(Epic epic) {
        HashMap<Integer, Subtask> subtasksByIdEpic = new HashMap<>();
        for (Integer i : epic.getIdSubtask()) {
            subtasksByIdEpic.put(i, dataSubtask.get(i));
        }
        return subtasksByIdEpic;
    }

    @Override
    public void calculateStatEpic(Integer idEpic) {
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
                    curEpic.setStatusTask(StatusTasks.DONE);
                }
            }
            curEpic.setStatusTask(stat);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void setHistory(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public Set<Task> getPrioritizedTasks() {
        TaskComparatorTime taskComparatorTime = new TaskComparatorTime();
        Set<Task> priorityTask = new TreeSet<>(taskComparatorTime);
        priorityTask.addAll(tasksList);
        return priorityTask;
    }

    @Override
    public boolean checkCrossTIme(Task task) {
        for (Task savedTask : tasksList) {
            boolean cross1 = task.getStartTime().isAfter(savedTask.getStartTime()) //1
                    && task.getEndTime().isBefore(savedTask.getStartTime());
            boolean cross2 = task.getStartTime().isBefore(savedTask.getStartTime())  //2
                    && task.getEndTime().isAfter(savedTask.getEndTime());
            boolean cross3 = task.getStartTime().isBefore(savedTask.getStartTime())  //3
                    && task.getEndTime().isAfter(savedTask.getStartTime())
                    && task.getEndTime().isBefore(savedTask.getEndTime());
            boolean cross4 = task.getStartTime().isAfter(savedTask.getStartTime()) //4
                    && task.getEndTime().isAfter(savedTask.getEndTime())
                    && task.getStartTime().isBefore(savedTask.getEndTime());

                if (cross1 || cross2 || cross3 || cross4) {
                    return false;
                }
            }
        return true;
    }

    public void calculatorEpicDurationAndStart(Integer idEpic) {
        Epic curEpic = dataEpic.get(idEpic);
        ArrayList<Integer> idSubtask = curEpic.getIdSubtask();
        if (idSubtask.isEmpty()) {
            curEpic.setDuration(0);
            curEpic.setStartTime(LocalDateTime.now());
        }
        if (!idSubtask.isEmpty() && !dataSubtask.isEmpty()) {
            long durationEpic = 0L;
            LocalDateTime startTimeFirst = LocalDateTime.now();
            for (Integer id:
                    idSubtask) {
                    durationEpic += dataSubtask.get(id).getDuration();
                 durationEpic += dataSubtask.get(id).getDuration();
                if (dataSubtask.get(id).getStartTime().isBefore(startTimeFirst)) {
                    startTimeFirst = dataSubtask.get(id).getStartTime();
                }
            }
            curEpic.setDuration(durationEpic);
            curEpic.setStartTime(startTimeFirst);
        }
    }
}

