package Model;

import java.util.ArrayList;

public class Epic extends Task {

    private String statusEpic;
    private ArrayList<Integer> idSubtask = new ArrayList<>();



    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }


    @Override
    public void setId(Integer id) {
        this.id = id;
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

    public void setStatusEpic(String statusEpic) {
        this.statusEpic = statusEpic;
    }

    public String getStatusEpic() {
        return statusEpic;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "statusEpic='" + statusEpic + '\'' +
                ", idSubtask=" + idSubtask +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", id=" + id +
                '}';
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
}
