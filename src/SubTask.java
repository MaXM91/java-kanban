public class SubTask extends Task {
    protected String statusSubTask;
    protected int idEpic;

    public SubTask(String nameTask, String descriptionTask, int idTask, String statusSubTask) {
        super(nameTask, descriptionTask, idTask);
        this.statusSubTask = statusSubTask;
    }

    public String getStatusSubTask() {
        return statusSubTask;
    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }
}
