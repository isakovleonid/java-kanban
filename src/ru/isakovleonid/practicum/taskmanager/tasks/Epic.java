package ru.isakovleonid.practicum.taskmanager.tasks;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.join;

public class Epic  extends Task {
    private Set<Integer> subTasks;

    public Epic(String name, String description) {
        super(name, description);

        subTasks = new HashSet<>();
    }

    public Epic(int id, String name, String description) {
        super(name, description);
        this.id = id;
        subTasks = new HashSet<>();
    }

    @Override
    public String toString() {
        return "TaskManager.Epic{" +
                " id=" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", subTasks=" + subTasks + '\'' +
                '}';
    }

    public void addSubTask(Integer subTaskId) {
        subTasks.add(subTaskId);
    }

    public void deleteSubTaskById(Integer subTaskId) {
        subTasks.remove(subTaskId);
    }

    public void deleteSubTaskAll() {
        subTasks.clear();
    }

    public Set<Integer> getSubTasks() {
        return subTasks;
    }

    public void updateStatus(List<SubTask> subTasks) {
        int countDone = 0, countNew = 0;
        TaskStatus tempStatus = TaskStatus.IN_PROGRESS;

        // обновляем статус исходя из состояний подзадач subTasks
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() == TaskStatus.DONE)
                countDone++;
            else if (subTask.getStatus() == TaskStatus.NEW)
                countNew++;
        }

        if (subTasks.size() == countDone)
            tempStatus = TaskStatus.DONE;
        else if (subTasks.size() == countNew)
            tempStatus = TaskStatus.NEW;

        status = tempStatus;
    }

    @Override
    public String stringForFile() {
        return join(",", String.valueOf(this.id), TaskType.EPIC.toString(), this.name, this.status.toString(),this.description);
    }
}
