package ru.isakovleonid.practicum.taskmanager.historymanager;

import ru.isakovleonid.practicum.taskmanager.taskmanager.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    List<Task> getHistory();
}
