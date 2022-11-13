public class SimpleTask extends Task {
    protected String statusTask;

    public SimpleTask(String nameTask, String descriptionTask, int idTask, String statusTask) {
       super(nameTask, descriptionTask, idTask);
       this.statusTask = statusTask;
    }

    public String getStatusTask() {
        return  statusTask;
    }
}
