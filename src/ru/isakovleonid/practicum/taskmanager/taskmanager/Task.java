package ru.isakovleonid.practicum.taskmanager.taskmanager;

import java.util.Objects;

import static java.lang.String.join;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected TaskStatus status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    /*Конструктор для тестирования обновления через создание нового объекта с тем же id*/
    public Task(int id, String name,  String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
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
                '}';
    }

    public String stringForFile() {
        return join(",", String.valueOf(this.id), TaskType.TASK.toString(), this.name, this.status.toString(),this.description);
    }

}
