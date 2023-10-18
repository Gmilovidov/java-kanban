package model;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> idSubtask = new ArrayList<>();

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    public Epic(int id, TypeTasks typeTasks, String taskName, StatusTasks statusTask, String taskDescription) {
        super(id, typeTasks, taskName, statusTask, taskDescription);
    }

    @Override
    public String toString() {
        return id + "," +
                typeTasks+ "," +
                taskName + "," +
                statusTask + "," +
                taskDescription;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    public void addSubtask(int id) {
        idSubtask.add(id);
    }

    public ArrayList<Integer> getIdSubtask() {
        return idSubtask;
    }

    @Override
    public String getTaskName() {
        return super.getTaskName();
    }

    @Override
    public void setTaskName(String taskName) {
        super.setTaskName(taskName);
    }

    @Override
    public String getTaskDescription() {
        return super.getTaskDescription();
    }

    @Override
    public void setTaskDescription(String taskDescription) {
        super.setTaskDescription(taskDescription);
    }

    public void setIdSubtask(ArrayList<Integer> idSubtask) {
        this.idSubtask = idSubtask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(idSubtask, epic.idSubtask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubtask);
    }
}
