import model.*;
import service.FileBackedTasksManager;

import java.io.File;

import static service.FileBackedTasksManager.loadFromFile;


public class Main {

    public static void main(String[] args) {

        FileBackedTasksManager fileManager = new FileBackedTasksManager(new File("saveTasks2.csv"));
        fileManager.createTask(new Task("task1", "Купить автомобиль"));
        fileManager.createEpic(new Epic("new Epic1", "Новый Эпик"));
        fileManager.createSubtask(new Subtask("New Subtask", "Подзадача", 2));
        fileManager.createSubtask(new Subtask("New Subtask2", "Подзадача2", 2));
        fileManager.getTaskById(1);
        fileManager.getEpicById(2);
        fileManager.getSubtaskById(3);
        System.out.println(fileManager.getTaskMap().values());
        System.out.println(fileManager.getEpicMap().values());
        System.out.println(fileManager.getSubtaskMap().values());
        System.out.println(fileManager.getHistory());
        System.out.println("\n\n" + "new" + "\n\n");
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(new File("saveTasks2.csv"));
        System.out.println(fileBackedTasksManager.getTaskMap().values());
        System.out.println(fileBackedTasksManager.getEpicMap().values());
        System.out.println(fileBackedTasksManager.getSubtaskMap().values());
        System.out.println(fileBackedTasksManager.getHistory());
    }
}
