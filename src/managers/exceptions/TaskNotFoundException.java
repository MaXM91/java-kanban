package managers.exceptions;

public class TaskNotFoundException extends Throwable {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
