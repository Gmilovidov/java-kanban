package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> idSubtask = new ArrayList<>();
    private LocalDateTime endTimeEpic;

    public Epic(int id, String name, String desc) {
        super(id, name, desc);

    }

    public Epic(int id, String taskName, StatusTasks statusTask, String taskDescription, Long duration, LocalDateTime startTime) {
        super(id, taskName, statusTask, taskDescription, duration, startTime);
    }

    public Epic(int id, String name, String desc, ArrayList<Integer> idSubtask) {
        super(id, name, desc);
        this.idSubtask = idSubtask;
    }

    @Override
    public String toString() {
        return id + "," +
                TaskType.EPIC + "," +
                taskName + "," +
                statusTask + "," +
                taskDescription + "," +
                duration + "," +
                convertTimeFormat() + ",";
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

    @Override
    public void setDuration(long duration) {
        super.setDuration(duration);
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

}
