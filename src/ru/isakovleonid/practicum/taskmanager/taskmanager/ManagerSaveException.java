package ru.isakovleonid.practicum.taskmanager.taskmanager;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(Throwable cause) {
        super(cause);
    }

    public ManagerSaveException(String message) {
        super(message);
    }
}
