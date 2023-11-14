package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected int id;
    protected TaskType taskType;
    protected String taskName;
    protected String description;
    protected StatusTasks statusTask;
    protected long duration;
    protected LocalDateTime startTime;

    public Task(String taskName, String description, Long duration, LocalDateTime startTime) {
        this.taskType = TaskType.TASK;
        this.taskName = taskName;
        this.statusTask = StatusTasks.NEW;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, String taskName, StatusTasks statusTask,
                String description, Long duration, LocalDateTime startTime) {
        this.id = id;
        this.taskType = TaskType.TASK;
        this.taskName = taskName;
        this.statusTask = statusTask;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }


    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
    }

    public Task(int id, String taskName, String description) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public StatusTasks getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(StatusTasks statusTask) {
        this.statusTask = statusTask;
    }

    public String convertTimeFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return startTime.format(formatter);
    }

    @Override
    public String toString() {
        return id + "," +
                TaskType.TASK + "," +
                taskName + "," +
                statusTask + "," +
                description + "," +
                duration + "," +
                startTime + ",";
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return description;
    }

    public void setTaskDescription(String taskDescription) {
        this.description = taskDescription;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && duration == task.duration
                && taskType == task.taskType
                && Objects.equals(taskName, task.taskName)
                && Objects.equals(description, task.description)
                && statusTask == task.statusTask
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskType, taskName, description, statusTask, duration, startTime);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

}
