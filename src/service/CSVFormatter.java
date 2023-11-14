package service;

import model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVFormatter {

    protected final static String HeadSCV = "id,type,name,status,description,duration,startTime,epic";

    public CSVFormatter() {
    }

    /**
     * @param value формат id,type,name,status,description,duration,startTime,epic
     */
    public static Task fromString(String value) {
        int idEpic = -1;
        String[] tokens = value.split(",");
        int id = Integer.parseInt(tokens[0]);
        TaskType type = TaskType.valueOf(tokens[1]);
        String name = tokens[2];
        StatusTasks status = StatusTasks.valueOf(tokens[3]);
        String desc = tokens[4];
        Long duration = Long.parseLong(tokens[5]);
        LocalDateTime startTime = LocalDateTime.parse(tokens[6]);
        if (tokens.length == 8) {
            idEpic = Integer.parseInt(tokens[7]);
        }

        return switch (type) {
            case TASK -> new Task(id, name, status, desc, duration, startTime);


            case EPIC -> new Epic(id, name, status, desc, duration, startTime);


            case SUBTASK -> new Subtask(id, name, status, desc, duration, startTime, idEpic);
        };
    }

    public static String historyToString(HistoryManager manager) {
           List<Integer> idHistory = new ArrayList<>();
           List<Task> tasks = manager.getHistory();
           for (Task task : tasks) {
               idHistory.add(task.getId());
           }
           return idHistory.toString().replaceAll(  "^\\[|]$", "")
                   .replaceAll(" ", "");
    }

    public static List<Integer> historyFromString(String historyStr) {
        List<Integer> historyId = new ArrayList<>();
        String[] tokens = historyStr.split(",");
            for (String id : tokens) {
                if (!id.equals(""))
                historyId.add(Integer.parseInt(id));
            }
        return historyId;
    }
}
