package service;

import java.io.File;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import model.Epic;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {

        FileBackedTasksManager fileManager = new FileBackedTasksManager(new File("saveTasks2.csv"));
        fileManager.createTask(new Task(1,"task1", "Купить автомобиль",
                10L, LocalDateTime.now()));
        fileManager.createEpic(new Epic(2, "new Epic1", "Новый Эпик"));
        fileManager.createSubtask(new Subtask(3,"New Subtask", "Подзадача",
                10L, LocalDateTime.now().plusMinutes(20), 2));
        fileManager.createSubtask(new Subtask(4,"New Subtask2", "Подзадача2",
                10L, LocalDateTime.now().plusMinutes(60), 2));

        fileManager.getTaskById(1);
        fileManager.getEpicById(2);
        fileManager.getSubtaskById(3);
        System.out.println(fileManager.getTaskMap().values());
        System.out.println(fileManager.getEpicMap().values());
        System.out.println(fileManager.getSubtaskMap().values());
        System.out.println(fileManager.getHistory());
        System.out.println("\n\nnew\n\n");
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager
                .loadFromFile(new File("saveTasks2.csv"));
        System.out.println(fileBackedTasksManager.getTaskMap().values());
        System.out.println(fileBackedTasksManager.getEpicMap().values());
        System.out.println(fileBackedTasksManager.getSubtaskMap().values());
        System.out.println(fileBackedTasksManager.getHistory());

    }
}
