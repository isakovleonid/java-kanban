package ru.isakovleonid.practicum.taskmanager.taskmanager;

import ru.isakovleonid.practicum.taskmanager.historymanager.HistoryManager;
import ru.isakovleonid.practicum.taskmanager.tasks.*;

import java.util.*;

public interface TaskManager {
    void deleteAll();

    Task getById(Integer id);

    HistoryManager getHistoryManager();

    Task getTask(Integer id);

    List<Task> getTasks();

    SubTask getSubTask(Integer id);

    List<SubTask> getSubTasks();

    Epic getEpic(Integer id);

    List<Epic> getEpics();

    Integer addTask(Task newTask);

    Integer addEpic(Epic newEpic);

    Integer addSubTask(SubTask newSubTask);

    void deleteById(Integer id);

    List<SubTask> getEpicSubTasks(Epic epic);

    List<Task> getPrioritizedTasks();
}
