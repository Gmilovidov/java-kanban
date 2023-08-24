package Model;

public class Subtask extends Task {

    private Integer idEpic;

    public Subtask(String taskName, String taskDescription, String statusSubtask, Integer idEpic) {
        super(taskName, taskDescription, statusSubtask);
        this.idEpic = idEpic;
    }

    public Subtask(String taskName, String taskDescription, Integer id, String statusTask, Integer idEpic) {
        super(taskName, taskDescription, id, statusTask);
        this.idEpic = idEpic;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
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
