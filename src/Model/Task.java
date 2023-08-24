package Model;

public class Task {
    protected String taskName;
    protected String taskDescription;
    protected Integer id;
    protected String statusTask;

    public Task(String taskName, String taskDescription, String statusTask) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.statusTask = statusTask;
    }

    public Task(String taskName, String taskDescription, Integer id, String statusTask) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.id = id;
        this.statusTask = statusTask;
    }

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getId() {
        return id;
    }

    public String getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(String statusTask) {
        this.statusTask = statusTask;
    }


    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", id=" + id +
                ", statusTask='" + statusTask + '\'' +
                '}';
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
}
