import model.*;
import service.FileBackedTasksManager;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        String fileName = "baseTasks.csv";

        File file = new File("./resources/" + fileName);

        FileBackedTasksManager taskManager1 = new FileBackedTasksManager(file);

        FileBackedTasksManager taskManager2 = FileBackedTasksManager.loadFromFile(file);


        Task task = new Task(0, TypeTasks.TASK, "Закончить учебу", StatusTasks.NEW,
                "Иначе пока-пока моим деньгам");
        Task task1 = new Task( 1,TypeTasks.TASK, "Покормить кота", StatusTasks.IN_PROGRESS,
                "немного а то он жирный");
        Epic epic = new Epic(2, TypeTasks.EPIC, "Переезд", StatusTasks.IN_PROGRESS,
                "Нужно переехать в новую квартиру");
        Subtask subtask = new Subtask(3, TypeTasks.SUBTASK, "Собрать", StatusTasks.IN_PROGRESS,
                "оставить так как развожусь", 2);


        taskManager1.createTask(task);
        taskManager1.createTask(task1);
        taskManager1.createEpic(epic);
        taskManager1.createSubtask(subtask);

        taskManager2.createTask(task);
        taskManager2.createTask(task1);
        taskManager2.createEpic(epic);
        taskManager2.createSubtask(subtask);

        taskManager1.getTaskById(0);
        taskManager1.getTaskById(1);
        taskManager1.getEpicById(2);
        taskManager1.getSubtaskById(3);

        taskManager2.getTaskById(0);
        taskManager2.getTaskById(1);
        taskManager2.getEpicById(2);
        taskManager2.getSubtaskById(3);


        if (taskManager1.equals(taskManager2)) {
            System.out.println("Проверка прошла успешно");
        } else {
            System.out.println("Проверка завершилась неудачей");
        }

        taskManager1.save();

    }
}
