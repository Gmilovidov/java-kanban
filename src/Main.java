import Model.Epic;
import Model.Subtask;
import Model.Task;

import Service.InMemoryHistoryManager;
import Service.InMemoryTaskManager;
import Service.Managers;
import Service.TaskManager;


import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {


          TaskManager taskManager = new InMemoryTaskManager();



        // Тест

        Task task = new Task("Закончить учебу", "Иначе пока-пока моим деньгам"); // 1
        Task task1 = new Task( "Покормить кота","немного, а то он жирный"); // 2
        Epic epic = new Epic("Переезд", "Нужно переехать в новую квартиру"); // 3
        Subtask subtask = new Subtask("Собрать вещи", "Вещи жены оставить, так как развожусь", 3); // 4
        Subtask subtask1 = new Subtask("Вызвать Грузовик", "Узнать у кореша телефон", 3); // 5
        Epic epic1 = new Epic("Отремонтировать шкаф", "Сломал когда ссорился с женой");
        Subtask subtask2 = new Subtask("Купить запчасти", "В Леруа Мерлен", 6); // 7
        Task task3 = new Task("ааааааа", "аааааааа");
        Task task4 = new Task("ббббббб", "бббббббб");
        Task task5 = new Task("ввввввв", "вввввввв");
        Task task6 = new Task("ггггггг", "гггггггг");

//        Task newStatTask = new Task("Сделать домашку", "Иначе пока-пока моим деньгам"); // 1
//        Subtask newStatSubtask = new Subtask("Купить запчасти", "В Леруа Мерлен", 6); // 7
//        Subtask newStatSubtask1 = new Subtask("Собрать вещи",
//                "Вещи жены оставить, так как развожусь",3); // 4
//        Subtask newStatSubtask2 = new Subtask("Вызвать Грузовик", "Узнать у кореша телефон", 3); // 5


        taskManager.createTask(task);
        taskManager.createTask(task1);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);

        Map<Integer, Task> taskCheckSave = taskManager.getTaskMap(task);
        Map<Integer, Epic> epicCheckSave = taskManager.getEpicMap(epic);
        Map<Integer, Subtask> subtaskCheckSave = taskManager.getSubtaskMap(subtask);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        taskManager.getEpicById(6);
        taskManager.getSubtaskById(7);
        taskManager.getTaskById(8);
        taskManager.getTaskById(9);
        taskManager.getTaskById(10);
        taskManager.getTaskById(11);





        System.out.println("Список истории " + taskManager.getHistory());

//        System.out.println();
//        System.out.println(taskCheckSave.toString());
//        System.out.println();
//        System.out.println(epicCheckSave.toString());
//        System.out.println();
//        System.out.println(subtaskCheckSave.toString());
//        System.out.println();

//         Проверка на изменение статуса

//        inMemoryTaskManager.updateTask(newStatTask);
//        System.out.println(taskCheckSave);
//        System.out.println();
//
//        inMemoryTaskManager.updateSubtask((newStatSubtask));
//        System.out.println(subtaskCheckSave);
//        System.out.println();
//
//        inMemoryTaskManager.updateSubtask(newStatSubtask1);
//        inMemoryTaskManager.updateSubtask(newStatSubtask2);
//        System.out.println(epic.getStatusTask());
//        System.out.println();

        // Удаление

//        inMemoryTaskManager.removeSubtask();
//        System.out.println(epicCheckSave);

//        inMemoryTaskManager.removeEpic();
//        System.out.println(epicCheckSave);
//        System.out.println();
//        System.out.println(subtaskCheckSave);
//        System.out.println();
//        System.out.println();
//        System.out.println(inMemoryTaskManager.getHistory());


        // Удаление по id сабтаски

//       inMemoryTaskManager.removeSubtaskById(5);
//       System.out.println(subtaskCheckSave);

//       Удаление Эпика по Id

//        inMemoryTaskManager.removeEpicById(3);
//        System.out.println(epicCheckSave);
//        System.out.println();
//        System.out.println(subtaskCheckSave);


    }
}
