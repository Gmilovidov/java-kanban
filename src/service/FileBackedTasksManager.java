package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    protected  File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager() {
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
       try (BufferedReader br = new BufferedReader(new FileReader(file))) {
           br.readLine();
           while (br.ready()) {
               String line = br.readLine();
               if (line.isBlank()) {
                   break;
               }
               Task task = CSVFormatter.fromString(line);
               if (task instanceof Epic) {
                   fileBackedTasksManager.dataEpic.put(task.getId(), (Epic) task);
               } else if (task instanceof Subtask) {
                   fileBackedTasksManager.dataSubtask.put(task.getId(), (Subtask) task);
               } else if (task != null) {
                   fileBackedTasksManager.dataTask.put(task.getId(), task);
               }
           }

           // Привязка id subtask к Epic
           for (Map.Entry<Integer, Subtask> pair : fileBackedTasksManager.dataSubtask.entrySet()) {
               if (fileBackedTasksManager.dataEpic.containsKey(pair.getValue().getIdEpic())) {
                   fileBackedTasksManager.dataEpic.get(pair.getValue().getIdEpic()).addSubtask(pair.getKey());
               }
           }

           String historyLine = br.readLine();
           List<Integer> idHistory = new ArrayList<>();
           if (historyLine != null) {
                idHistory = CSVFormatter.historyFromString(historyLine);
           }
           Collections.reverse(idHistory);
           // Восстановление истории просмотров
           for (Integer id : idHistory) {
               if (fileBackedTasksManager.dataTask.containsKey(id)) {
                  fileBackedTasksManager.historyManager.add(fileBackedTasksManager.dataTask.get(id));
               } else if (fileBackedTasksManager.dataSubtask.containsKey(id)) {
                   fileBackedTasksManager.historyManager.add(fileBackedTasksManager.dataSubtask.get(id));
               } else if (fileBackedTasksManager.dataEpic.containsKey(id)) {
                   fileBackedTasksManager.historyManager.add(fileBackedTasksManager.dataEpic.get(id));
               }
           }
       } catch (IOException e) {
           System.out.println("Не удалось считать данные из файла");
       }
       return fileBackedTasksManager;
    }

    public void save() {
        try {
            if(!file.exists()) {
                Files.createFile(file.toPath());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось создать файл для записи.", e);
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.println(CSVFormatter.HeadSCV);
            for (Task task : getTaskMap().values()) {
                writer.println(task.toString());
            }
            for (Epic epic : getEpicMap().values()) {
                writer.println(epic.toString());
            }
            for (Subtask subtask : getSubtaskMap().values()) {
                writer.println(subtask.toString());
            }
            writer.println();
            writer.println(CSVFormatter.historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл.", e);
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void removeTask() {
        super.removeTask();
        save();
    }

    @Override
    public void removeEpic() {
        super.removeEpic();
        save();
    }

    @Override
    public void removeSubtask() {
        super.removeSubtask();
        save();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(Integer id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public HashMap<Integer, Subtask> getSubtaskByIdEpic(Epic epic) {
        HashMap<Integer, Subtask> subtaskByIdEpic = super.getSubtaskByIdEpic(epic);
        save();
        return subtaskByIdEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        boolean tasksEquals = dataTask.equals(((FileBackedTasksManager) o).dataTask);
        boolean epicEquals = dataEpic.equals(((FileBackedTasksManager) o).dataEpic);
        boolean subtaskEquals = dataSubtask.equals(((FileBackedTasksManager) o).dataSubtask);
        boolean historyEquals = getHistory().equals(((FileBackedTasksManager) o).getHistory());
        return  tasksEquals &&
                epicEquals &&
                subtaskEquals &&
                historyEquals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

}
