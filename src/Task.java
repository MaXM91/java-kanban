public class Task {
    protected String nameTask;
    protected String descriptionTask;
    protected int idTask;


    public Task(String nameTask, String descriptionTask, int idTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.idTask = idTask;
    }

    public int getIdTask() {
        return idTask;
    }
    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public String getNameTask() {
        return nameTask;
    }





}
