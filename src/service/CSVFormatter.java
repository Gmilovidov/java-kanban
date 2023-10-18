package service;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class CSVFormatter {

    public CSVFormatter() {
    }

    /**
     * @param value формат id,type,name,status,description,epic
     */
    public static Task fromString(String value) {
        int idEpic = -1;
        String[] tokens = value.split(",");
        int id = Integer.parseInt(tokens[0]);
        TypeTasks type = TypeTasks.valueOf(tokens[1]);
        String name = tokens[2];
        StatusTasks status = StatusTasks.valueOf(tokens[3]);
        String desc = tokens[4];
        if (tokens.length == 6) {
            idEpic = Integer.parseInt(tokens[5]);
        }

        return switch (type) {
            case TASK -> new Task(id, type, name, status, desc);
            case EPIC -> new Epic(id, type, name, status, desc);
            case SUBTASK -> new Subtask(id, type, name, status, desc, idEpic);
        };
    }

    public static String historyToString(HistoryManager manager) {
           List<Integer> idHistory = new ArrayList<>();
           List<Task> tasks = manager.getHistory();
           for (Task task : tasks) {
               idHistory.add(task.getId());
           }
           return idHistory.toString().replaceAll(  "^\\[|\\]$", "").replaceAll(" ", "");
    }

//        public static String historyToString(HistoryManager manager) {
//           List<Task> tasks = manager.getHistory();
//           StringBuilder idStr = null;
//           for (Task task : tasks) {
//               assert false;
//               idStr.append(task.getId().toString()).append(",");
//           }
//            assert false;
//            return idStr.toString();
//    }




    public static List<Integer> historyFromString(String historyStr) {
        List<Integer> historyId = new ArrayList<>();
        String[] tokens = historyStr.split(",");
            for (String id : tokens) {
                historyId.add(Integer.parseInt(id));
            }
        return historyId;
    }
}
