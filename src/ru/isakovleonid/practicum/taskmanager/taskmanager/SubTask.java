package ru.isakovleonid.practicum.taskmanager.taskmanager;

import static java.lang.String.join;

public class SubTask extends Task {
    private Integer epic;

    public SubTask(String name, String description, Integer epic) {
        super(name, description);
        this.epic = epic;
    }

    /*Конструктор для тестирования обновления через создание нового объекта с тем же id и для записи в файл*/
    public SubTask(Integer id, String name, String description, TaskStatus status, Integer epic) {
        super(id, name, description, status);
        this.epic = epic;
    }

    public Integer getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "TaskManager.SubTask{" +
                " id=" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", epic=" + epic + '\'' +
                "} ";
    }

    @Override
    public String stringForFile() {
        return join(",", String.valueOf(this.id), TaskType.SUBTASK.toString(), this.name, this.status.toString(),this.description, String.valueOf(this.epic));
    }
}
