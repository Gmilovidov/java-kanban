import Model.Epic;
import Model.Subtask;
import Model.Task;

import Service.TaskManager;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        // Тест

        Task task = new Task("Иначе пока-пока моим деньгам",
                "NEW"); // 1
        Task task1 = new Task("немного, а то он жирный",
                "NEW"); // 2
        Epic epic = new Epic("Переезд", "Нужно переехать в новую квартиру"); // 3
        Subtask subtask = new Subtask("Собрать вещи", "Вещи жены оставить, так как развожусь", 3); // 4
        Subtask subtask1 = new Subtask("Вызвать Грузовик", "Узнать у кореша телефон", 3); // 5
        Epic epic1 = new Epic("Отремонтировать шкаф", "Сломал когда ссорился с женой");
        Subtask subtask2 = new Subtask("Купить запчасти", "В Леруа Мерлен", 6); // 7
        Task newStatTask = new Task("Сделать домашку", "Иначе пока-пока моим деньгам"); // 1
        Subtask newStatSubtask = new Subtask("Купить запчасти", "В Леруа Мерлен", 6); // 7
        Subtask newStatSubtask1 = new Subtask("Собрать вещи",
                "Вещи жены оставить, так как развожусь",3); // 4
        Subtask newStatSubtask2 = new Subtask("Вызвать Грузовик", "Узнать у кореша телефон", 3); // 5


        manager.createTask(task);
        manager.createTask(task1);
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.createSubtask(subtask1);
        manager.createEpic(epic1);
        manager.createSubtask(subtask2);

        HashMap<Integer, Task> taskCheckSave = manager.getTaskMap(task);
        HashMap<Integer, Epic> epicCheckSave = manager.getEpicMap(epic);
        HashMap<Integer, Subtask> subtaskCheckSave = manager.getSubtaskMap(subtask);

        System.out.println();
        System.out.println(taskCheckSave.toString());
        System.out.println();
        System.out.println(epicCheckSave.toString());
        System.out.println();
        System.out.println(subtaskCheckSave.toString());
        System.out.println();

//         Проверка на изменение статуса

        manager.updateTask(newStatTask);
        System.out.println(taskCheckSave);
        System.out.println();

        manager.updateSubtask((newStatSubtask));
        System.out.println(subtaskCheckSave);
        System.out.println();

        manager.updateSubtask(newStatSubtask1);
        manager.updateSubtask(newStatSubtask2);
        System.out.println(epic.getStatusTask());
        System.out.println();

        // Удаление

//        manager.removeSubtask();
//        System.out.println(epicCheckSave);

        manager.removeEpic();
        System.out.println(epicCheckSave);
        System.out.println();
        System.out.println(subtaskCheckSave);


        // Удаление по id сабтаски

//       manager.removeSubtaskById(5);
//       System.out.println(subtaskCheckSave);

//       Удаление Эпика по Id

//        manager.removeEpicById(3);
//        System.out.println(epicCheckSave);
//        System.out.println();
//        System.out.println(subtaskCheckSave);

    }
}
