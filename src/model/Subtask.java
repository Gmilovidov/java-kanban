package model;

import java.util.Objects;

public class Subtask extends Task {

    private Integer idEpic;

    public Subtask(String taskName, String taskDescription, Integer idEpic) {
        super(taskName, taskDescription);
        this.idEpic = idEpic;
    }

    public Subtask(int id, TypeTasks typeTasks, String taskName, StatusTasks statusTask, String taskDescription, Integer idEpic) {
        super(id, typeTasks, taskName, statusTask, taskDescription);
        this.idEpic = idEpic;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return id + "," +
                typeTasks+ "," +
                taskName + "," +
                statusTask + "," +
                taskDescription + "," +
                idEpic;
    }

    public Integer getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Integer idEpic) {
        this.idEpic = idEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(idEpic, subtask.idEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }



}
