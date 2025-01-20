package ru.isakovleonid.practicum.taskmanager.taskmanager;

import ru.isakovleonid.practicum.taskmanager.historymanager.HistoryManager;
import ru.isakovleonid.practicum.taskmanager.historymanager.InMemoryHistoryManager;
import ru.isakovleonid.practicum.taskmanager.tasks.*;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private Integer counter;

    private InMemoryHistoryManager historyManager;

    Map<Integer, Task> tasks;
    Map<Integer, SubTask> subTasks;
    Map<Integer, Epic> epics;

    NavigableSet<Task> sortedTasksByStartTime;

    public InMemoryTaskManager() {
        counter = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
        historyManager = new InMemoryHistoryManager();
        sortedTasksByStartTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public void setHistoryManager(InMemoryHistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public String toString() {
        return "TaskManager.TaskManager{" +
                "tasks=" + tasks.toString() + '\'' +
                ", epics=" + epics.toString() + '\'' +
                ", subTasks=" + subTasks.toString() + '\'' +
                '}';
    }

    @Override
    public void deleteAll() {
        tasks.clear();
    }

    @Override
    public Task getById(Integer id) {
        Task task;
        task = getTask(id);

        if (task != null)
                return task;

        SubTask subTask;
        subTask = getSubTask(id);

        if (subTask != null)
            return subTask;

        Epic epic;
        epic = getEpic(id);

        return epic;
    }

    @Override
    public Task getTask(Integer id) {
        Task task;
        task = tasks.get(id);

        if (task != null)
            historyManager.add(task);

        return task;
    }

    @Override
    public SubTask getSubTask(Integer id) {
        SubTask subTask;
        subTask = subTasks.get(id);

        if (subTask != null)
            historyManager.add(subTask);

        return subTask;
    }

    @Override
    public Epic getEpic(Integer id) {
        Epic epic;
        epic = epics.get(id);

        if (epic != null)
            historyManager.add(epic);

        return epic;
    }

    @Override
    public Integer addTask(Task newTask) {
        if (existsIntersectionByPeriod(newTask))
            return null;

        Integer id = newTask.getId();
        if (id == null)
            id = ++counter;

        newTask.setId(id);

        tasks.put(id, newTask);

        if (newTask.getStartTime() != null)
            sortedTasksByStartTime.add(newTask);

        return id;
    }

    @Override
    public Integer addEpic(Epic newEpic) {
        Integer id = newEpic.getId();
        if (id == null)
            id = ++counter;

        newEpic.setId(id);

        epics.put(id, newEpic);

        return id;
    }

    @Override
    public Integer addSubTask(SubTask newSubTask) {
        if (existsIntersectionByPeriod(newSubTask))
            return null;

        Integer id = newSubTask.getId(), epicId;
        epicId = newSubTask.getEpic();
        if (epicId != null) {
            if (epics.containsKey(epicId)) {
                if (id == null)
                    id = ++counter;

                newSubTask.setId(id);

                subTasks.put(id, newSubTask);

                Epic epic = epics.get(epicId);
                epic.addSubTask(id);

                updateEpicStatus(epicId);

                if (newSubTask.getStartTime() != null)
                    sortedTasksByStartTime.add(newSubTask);
            }
        }

        return id;
    }

    private void updateEpicStatus(Integer epicId) {
        Epic epic = epics.get(epicId);

        List<SubTask> subTaskList = getEpicSubTasks(epic);
        epic.updateEpicBySubtask(subTaskList);
    }

    @Override
    public void deleteById(Integer id) {
        tasks.remove(id);

        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            Integer epicId = subTask.getEpic();
            Epic epic = epics.get(epicId);
            epic.deleteSubTaskById(id);
            updateEpicStatus(epicId);
            subTasks.remove(id);
        }

        Epic epic = epics.get(id);
        if (epic != null) {
            epic.getSubTasks().stream()
                            .peek(subTaskId -> subTasks.remove(subTaskId))
            ;
            epic.deleteSubTaskAll();
            epics.remove(id);
        }

        historyManager.remove(id);
    }

    @Override
    public  List<SubTask> getEpicSubTasks(Epic epic) {
        return epic.getSubTasks().stream()
                .map(subTaskId -> subTasks.get(subTaskId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return sortedTasksByStartTime.stream().toList();
    }

    public boolean existsIntersectionByPeriod(Task checkTask) {
        Task taskIntersectionInPeriod;
        taskIntersectionInPeriod = this.getPrioritizedTasks().stream()
                .filter(task -> {   if (task.getStartTime() != null && checkTask.getStartTime() != null && !task.equals(checkTask))
                                        return (task.getEndTime().isAfter(checkTask.getStartTime())
                                                && task.getStartTime().isBefore(checkTask.getEndTime()));
                                    else
                                        return false;
                                } )
                .findAny()
                .orElse(null);

        return taskIntersectionInPeriod != null;
    }

}
