package model;

import java.util.Objects;

public class Task {
    private static int count = 0;
    protected int id;
    protected TypeTasks typeTasks;
    protected String taskName;
    protected String taskDescription;
    protected StatusTasks statusTask;

    public Task(String name, String desc) {
        this.id = generateId();
        this.typeTasks = TypeTasks.TASK;
        this.taskName = name;
        this.statusTask = StatusTasks.NEW;
        this.taskDescription = desc;
    }

    public Task(int id, TypeTasks typeTasks, String taskName, StatusTasks statusTask, String taskDescription) {
        this.id = id;
        this.typeTasks = typeTasks;
        this.taskName = taskName;
        this.statusTask = statusTask;
        this.taskDescription = taskDescription;

    }

    public static int getCount() {
        return count;
    }

    public TypeTasks getTypeTasks() {
        return typeTasks;
    }

    private Integer generateId() {
        return ++count;
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

    @Override
    public String toString() {
        return id + "," +
                typeTasks+ "," +
                taskName + "," +
                statusTask + "," +
                taskDescription + ",";
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                Objects.equals(taskName, task.taskName) &&
                Objects.equals(taskDescription, task.taskDescription) &&
                statusTask == task.statusTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeTasks, taskName, taskDescription, statusTask);
    }

    public void setId(int id) {
        this.id = id;
    }
}
