package ru.isakovleonid.practicum.taskmanager.taskmanager;

import ru.isakovleonid.practicum.taskmanager.historymanager.HistoryManager;

import java.util.*;

public class InMemoryTaskManager implements TaskManager{
    private int counter;

    Map<Integer, Task> tasks;
    Map<Integer, SubTask> subTasks;
    Map<Integer, Epic> epics;


    public InMemoryTaskManager() {
        counter = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
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
    public Task getById(int id, HistoryManager historyManager) {
        Task task;
        task = getTask(id, historyManager);

        if (task != null)
                return task;

        SubTask subTask;
        subTask = getSubTask(id, historyManager);

        if (subTask != null)
            return subTask;

        Epic epic;
        epic = getEpic(id, historyManager);

        return epic;
    }

    @Override
    public Task getTask(int id, HistoryManager historyManager) {
        Task task;
        task = tasks.get(id);

        if (task != null)
            historyManager.add(task);

        return task;
    }

    @Override
    public SubTask getSubTask(int id, HistoryManager historyManager) {
        SubTask subTask;
        subTask = subTasks.get(id);

        if (subTask != null)
            historyManager.add(subTask);

        return subTask;
    }

    @Override
    public Epic getEpic(int id, HistoryManager historyManager) {
        Epic epic;
        epic = epics.get(id);

        if (epic != null)
            historyManager.add(epic);

        return epic;
    }

    @Override
    public Integer addTask(Task newTask) {
        int id = counter++;
        newTask.setId(id);
        updateTask(newTask);

        return id;
    }

    @Override
    public Integer addEpic(Epic newEpic) {
        int id = counter++;
        newEpic.setId(id);
        updateEpic(newEpic);

        return id;
    }

    @Override
    public Integer addSubTask(SubTask newSubTask) {
        Integer id = null;

        if (epics.containsKey(newSubTask.getEpic())) {

            id = counter++;
            newSubTask.setId(id);
            updateSubTask(newSubTask);
        }

        return id;
    }

    @Override
    public void updateTask(Task newTask){
        tasks.put(newTask.getId(), newTask);
    }
    @Override
    public void updateEpic(Epic newEpic){
        epics.put(newEpic.getId(), newEpic);
    }

    @Override
    public void updateSubTask(SubTask newSubTask){
        if (epics.containsKey(newSubTask.getEpic())) {
            Integer subTaskId = newSubTask.getId();
            subTasks.put(subTaskId, newSubTask);

            Integer epicId = newSubTask.getEpic();
            Epic epic = epics.get(epicId);
            epic.addSubTask(subTaskId);

            updateEpicStatus(epicId);
        }
    }

    private void updateEpicStatus(Integer epicId) {
        Epic epic = epics.get(epicId);

        List<SubTask> subTaskList = getEpicSubTasks(epic);
        epic.updateStatus(subTaskList);
    }

    @Override
    public void deleteById(int id) {
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
            for (Integer subTaskId : epic.getSubTasks()) {
                subTasks.remove(subTaskId);
            }
            epic.deleteSubTaskAll();
            epics.remove(id);
        }
    }

    @Override
    public  List<SubTask> getEpicSubTasks(Epic epic) {
        List<SubTask> subTaskList = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTasks()) {
            subTaskList.add(subTasks.get(subTaskId));
        }

        return subTaskList;
    }
}
