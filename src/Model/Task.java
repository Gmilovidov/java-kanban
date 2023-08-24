package Model;

public class Task {
    private static int id = 0;
    protected String taskName;
    protected String taskDescription;
    protected String statusTask;

    public Task(String taskName, String taskDescription) {
        this.id = generateId();
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.statusTask = "NEW";
    }

    private Integer generateId() {
        return ++id;
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
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
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
