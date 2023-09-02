package Service;

import Model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    protected List<Task> dataHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (dataHistory.size() < 10) {
            dataHistory.add(task);
        } else {
            dataHistory.remove(0);
            dataHistory.add(task);
        }

    }

    @Override
    public List<Task> getHistory() {
        return dataHistory;
    }
}
