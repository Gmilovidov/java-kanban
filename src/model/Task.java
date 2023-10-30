package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
//    private static int count = 0; нужный
    protected int id;
    protected TaskType taskType;
    protected String taskName;
    protected String taskDescription;
    protected StatusTasks statusTask;
    protected long duration;
    LocalDateTime startTime;

//    public Task(String name, String desc, Long duration) {  нужный
//        this.id = generateId();
//        this.taskType = TaskType.TASK;
//        this.taskName = name;
//        this.statusTask = StatusTasks.NEW;
//        this.taskDescription = desc;
//        this.duration = duration;
//    }

    public Task(int id, String name, String desc, Long duration, LocalDateTime startTime) {
        this.id = id;
        this.taskType = TaskType.TASK;
        this.taskName = name;
        this.statusTask = StatusTasks.NEW;
        this.taskDescription = desc;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, String taskName, StatusTasks statusTask,
                String taskDescription, Long duration, LocalDateTime startTime) {
        this.id = id;
        this.taskType = model.TaskType.TASK;
        this.taskName = taskName;
        this.statusTask = statusTask;
        this.taskDescription = taskDescription;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    public Task(int id, String name, String desc) {
        this.id = id;
        this.taskName = name;
        this.taskDescription = desc;
    }


//    public static int getCount() {  нужный
//        return count;
//    }

    public TaskType getTaskType() {
        return taskType;
    }

//    private Integer generateId() { нужный
//        return ++count;
//    }

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        return startTime.format(formatter);
    }

    @Override
    public String toString() {
        return id + "," +
                TaskType.TASK + "," +
                taskName + "," +
                statusTask + "," +
                taskDescription + "," +
                duration + "," +
                convertTimeFormat() + ",";
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
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
                && Objects.equals(taskDescription, task.taskDescription)
                && statusTask == task.statusTask
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskType, taskName, taskDescription, statusTask, duration, startTime);
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
