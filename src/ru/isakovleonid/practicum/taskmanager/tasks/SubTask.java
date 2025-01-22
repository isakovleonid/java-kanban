package ru.isakovleonid.practicum.taskmanager.tasks;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.lang.String.join;

public class SubTask extends Task {
    private Integer epic;

    public SubTask(String name, String description, Integer epic, LocalDateTime startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.epic = epic;
    }

    /*Конструктор для тестирования обновления через создание нового объекта с тем же id и для записи в файл*/
    public SubTask(Integer id, String name, String description, TaskStatus status, Integer epic, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
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
                ", startTime=" + (this.startTime != null ? this.startTime.format(TASK_DATE_TIME) : null) + '\'' +
                ", duration=" + (this.duration != null ? this.duration.toMinutes() : null) + '\'' +
                ", epic=" + epic + '\'' +
                "} ";
    }

    @Override
    public String stringForFile() {
        return join(",",
                String.valueOf(this.id),
                TaskType.SUBTASK.toString(),
                this.name,
                this.status.toString(),
                this.description,
                (this.startTime != null ? this.startTime.format(TASK_DATE_TIME) : null),
                String.valueOf((this.duration != null ? this.duration.toMinutes() : null)),
                String.valueOf(this.epic));
    }
}
