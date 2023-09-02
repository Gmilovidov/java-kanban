package Model;

public class Task {
    private static int count = 0;
    protected int id = 0;
    protected String taskName;
    protected String taskDescription;
    protected StatusTasks statusTask;

    public Task(String taskName, String taskDescription) {
        this.id = generateId();
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.statusTask = StatusTasks.NEW;
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
        return "Task{" +
                "id='" + id + '\'' +
                ", statusTask='" + statusTask + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
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
