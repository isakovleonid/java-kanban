package ru.isakovleonid.practicum.taskmanager;

import ru.isakovleonid.practicum.taskmanager.historymanager.HistoryManager;
import ru.isakovleonid.practicum.taskmanager.historymanager.InMemoryHistoryManager;
import ru.isakovleonid.practicum.taskmanager.taskmanager.InMemoryTaskManager;
import ru.isakovleonid.practicum.taskmanager.taskmanager.TaskManager;

public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){

        return new InMemoryHistoryManager();
    }
}
