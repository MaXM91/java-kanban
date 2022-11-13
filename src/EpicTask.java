import java.util.ArrayList;

public class EpicTask extends Task {
    protected String statusEpicTask;
    protected ArrayList<Integer> idList = new ArrayList<>();

    public EpicTask(String nameTask, String descriptionTask, int idTask) {
        super(nameTask, descriptionTask, idTask);
    }


    public String getStatusEpicTask() {
        return statusEpicTask;
    }

    public void setStatusEpicTask(String statusEpicTask) {
        this.statusEpicTask = statusEpicTask;
    }

    public ArrayList<Integer> getIdList() {
        return idList;
    }
    public void setIdList(ArrayList<Integer> subIdList) {
        for(Integer i :subIdList) {
           int j = i;
           idList.add(j);
       }

    }
    public void removeIdList(Integer id){
        idList.remove(id);
    }

    public void clearIdList() {
        idList.clear();
    }
}
