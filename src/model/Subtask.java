package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private Integer idEpic;

    public Subtask(int id, String taskName, StatusTasks statusTask,
                   String taskDescription, Long duration, LocalDateTime startTime, Integer idEpic) {
        super(id, taskName, statusTask, taskDescription, duration, startTime);
        this.idEpic = idEpic;
        this.taskType = TaskType.SUBTASK;
    }

    public Subtask(int id, String name, String desc, Long duration, LocalDateTime start, Integer idEpic) {
        super(id, name, desc, duration, start);
        this.idEpic = idEpic;
        this.taskType = TaskType.SUBTASK;
    }


    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return id + "," +
                TaskType.SUBTASK + "," +
                taskName + "," +
                statusTask + "," +
                taskDescription + "," +
                duration + "," +
                convertTimeFormat() + "," +
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
