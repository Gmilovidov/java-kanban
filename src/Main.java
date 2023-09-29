import model.Epic;
import model.Subtask;
import model.Task;

import service.InMemoryTaskManager;

import service.TaskManager;

public class Main {

    public static void main(String[] args) {

          TaskManager taskManager = new InMemoryTaskManager();

        // Тест
        Task task = new Task("Закончить учебу", "Иначе пока-пока моим деньгам"); // 1
        Task task1 = new Task( "Покормить кота","немного, а то он жирный"); // 2
        Epic epic = new Epic("Переезд", "Нужно переехать в новую квартиру"); // 3
        Subtask subtask = new Subtask("Собрать", "оставить, так как развожусь", 3); // 4
        Subtask subtask1 = new Subtask("Вызвать Грузовик", "Узнать у кореша телефон", 3);
        Subtask subtask2 = new Subtask("aaaaaaa", "aaaaaaaaaa", 3);// 5
        Epic epic1 = new Epic("Отремонтировать шкаф", "Сломал когда ссорился с женой");


        taskManager.createTask(task);
        taskManager.createTask(task1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createEpic(epic1);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getEpicById(7);


        System.out.println(taskManager.getHistory());
        System.out.println();

        System.out.println("Размер списка истории просмотра до удаления - " + taskManager.getHistory().size());
        System.out.println();

//        taskManager.removeTaskById(1);
//        taskManager.removeEpicById(3);
//        taskManager.removeEpicById(7);
//
//        System.out.println(taskManager.getHistory());
//        System.out.println();
//
//        System.out.println("Размер списка истории просмотра после удаления - " + taskManager.getHistory().size());
//        System.out.println();

//        System.out.println(taskManager.getTaskMap(task));
        
    }
}
