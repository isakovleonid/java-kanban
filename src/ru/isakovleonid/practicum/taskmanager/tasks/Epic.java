package ru.isakovleonid.practicum.taskmanager.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import static java.lang.String.join;

public class Epic  extends Task {
    private Set<Integer> subTasks;
    private LocalDateTime endTime;

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
                ", startTime=" + (this.startTime != null ? this.startTime.format(TASK_DATE_TIME) : null) + '\'' +
                ", duration=" + (this.duration != null ? this.duration.toMinutes() : null) + '\'' +
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

    public void updateEpicBySubtask(List<SubTask> subTasks) {
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

        startTime =  subTasks.stream()
                .map(subTask -> subTask.startTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        long durationSubTasksInMinutes = subTasks.stream()
                .map(subtask -> subtask.duration)
                .map(Duration::toMinutes)
                .mapToLong(Long::valueOf)
                .sum();

        duration = Duration.ofMinutes(durationSubTasksInMinutes);

        endTime = subTasks.stream()
                .map(Task::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public String stringForFile() {
        return join(","
                , String.valueOf(this.id)
                , TaskType.EPIC.toString()
                , this.name
                , this.status.toString()
                , this.description
                , this.startTime != null ? this.startTime.format(TASK_DATE_TIME) : null
                , String.valueOf(this.duration != null ? this.duration.toMinutes() : null)
        );
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
