package ru.isakovleonid.practicum.taskmanager.historymanager;

import ru.isakovleonid.practicum.taskmanager.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(Integer id);

    List<Task> getHistory();
}
