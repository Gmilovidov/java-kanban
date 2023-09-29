package model;

public class Subtask extends Task {

    private Integer idEpic;

    public Subtask(String taskName, String taskDescription, Integer idEpic) {
        super(taskName, taskDescription);
        this.idEpic = idEpic;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    public Integer getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Integer idEpic) {
        this.idEpic = idEpic;
    }
}
