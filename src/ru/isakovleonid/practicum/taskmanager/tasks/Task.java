package ru.isakovleonid.practicum.taskmanager.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.lang.String.join;

public class Task {
    public static final DateTimeFormatter TASK_DATE_TIME = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
        this.status = TaskStatus.NEW;
    }

    /*Конструктор для тестирования обновления через создание нового объекта с тем же id*/
    public Task(int id, String name,  String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return Objects.equals(getId(), task.getId());
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public String toString() {
        return "TaskManager.Task{" +
                " id=" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + (this.startTime != null ? this.startTime.format(TASK_DATE_TIME) : null) + '\'' +
                ", duration=" + (this.duration != null ? this.duration.toMinutes() : null) + '\'' +
                '}';
    }

    public String stringForFile() {
        return join(","
                , String.valueOf(this.id)
                , TaskType.TASK.toString()
                , this.name
                , this.status.toString()
                , this.description
                , (this.startTime != null ? this.startTime.format(TASK_DATE_TIME) : null)
                , String.valueOf((this.duration != null ? this.duration.toMinutes() : null))
        );
    }

    public LocalDateTime getEndTime() {
        return (this.startTime != null && this.duration != null ? this.startTime.plus(this.duration): null);
    }
}
