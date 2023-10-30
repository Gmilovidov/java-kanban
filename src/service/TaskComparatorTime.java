package service;

import model.Task;

import java.util.Comparator;

public class TaskComparatorTime implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        if (o1.getStartTime().equals(o2.getStartTime())) {
            return 0;
        } else if (o1.getStartTime() == null) {
            return 1;
        } else if (o2.getStartTime() == null) {
            return -1;
        } else {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    }
}
