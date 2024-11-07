import java.util.*;

public class TaskManager {
    private int counter;

    Map<Integer, Task> tasks;
    Map<Integer, SubTask> subTasks;
    Map<Integer, Epic> epics;

    public TaskManager() {
        counter = 0;
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subTasks = new HashMap<>();
    }

    /* 1. Получение списка всех задач */
    @Override
    public String toString() {
        return "TaskManager{" +
                "tasks=" + tasks.toString() + '\'' +
                ", epics=" + epics.toString() + '\'' +
                ", subTasks=" + subTasks.toString() + '\'' +
                '}';
    }

    /* 2. Удаление всех задач */
    public void deleteAll() {
        tasks.clear();
    }

    /* 3. Получение объекта по идентификатору */
    public Task getById(int id) {
        Task task;
        task = tasks.get(id);

        if (task != null)
                return task;

        SubTask subTask;
        subTask = subTasks.get(id);

        if (subTask != null)
            return subTask;

        Epic epic;
        epic = epics.get(id);

        if (epic != null)
            return epic;

        return null;
    }

    /* 4. Создание */
    public void addTask(Task newTask) {
        newTask.setId(++counter);
        updateTask(newTask);
    }

    public void addEpic(Epic newEpic) {
        newEpic.setId(++counter);
        updateEpic(newEpic);
    }

    public void addSubTask(SubTask newSubTask) {
        newSubTask.setId(++counter);
        updateSubTask(newSubTask);
    }

    /* 5. Обновление */
    public void updateTask(Task newTask){
        tasks.put(newTask.getId(), newTask);
    }

    public void updateEpic(Epic newEpic){
        epics.put(newEpic.getId(), newEpic);
    }

    /*Обновление подзадачи*/
    public void updateSubTask(SubTask newSubTask){
        Integer subTaskId = newSubTask.getId();
        subTasks.put(subTaskId, newSubTask);

        Integer epicId = newSubTask.getEpic();
        Epic epic = epics.get(epicId);
        epic.addSubTask(subTaskId);

        updateEpicStatus(epicId);
    }

    /* Обновление статуса эпика после обновления подзадач*/
    private void updateEpicStatus(Integer epicId) {
        Epic epic = epics.get(epicId);

        List<SubTask> subTaskList = getEpicSubTasks(epic);
        epic.updateStatus(subTaskList);
    }

    /* 6. Удаление по идентификатору */
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

    /* 7. Получение списка всех подзадач определенного эпика*/
    public  List<SubTask> getEpicSubTasks(Epic epic) {
        List<SubTask> subTaskList = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTasks()) {
            subTaskList.add(subTasks.get(subTaskId));
        }

        return subTaskList;
    }



}
