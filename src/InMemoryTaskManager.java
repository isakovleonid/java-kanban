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
    @Override
    public void deleteAll() {
        tasks.clear();
    }

    /* 3. Получение объекта по идентификатору */
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

        if (epic != null)
            return epic;

        return null;
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

    /*@Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }*/

    /* 4. Создание */
    @Override
    public int addTask(Task newTask) {
        int id = counter++;
        newTask.setId(id);
        updateTask(newTask);

        return id;
    }

    @Override
    public int addEpic(Epic newEpic) {
        int id = counter++;
        newEpic.setId(id);
        updateEpic(newEpic);

        return id;
    }

    @Override
    public int addSubTask(SubTask newSubTask) {
        int id = counter++;
        newSubTask.setId(id);
        updateSubTask(newSubTask);

        return id;
    }

    /* 5. Обновление */
    @Override
    public void updateTask(Task newTask){
        tasks.put(newTask.getId(), newTask);
    }
    @Override
    public void updateEpic(Epic newEpic){
        epics.put(newEpic.getId(), newEpic);
    }

    /*Обновление подзадачи*/
    @Override
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

    /* 7. Получение списка всех подзадач определенного эпика*/
    @Override
    public  List<SubTask> getEpicSubTasks(Epic epic) {
        List<SubTask> subTaskList = new ArrayList<>();
        for (Integer subTaskId : epic.getSubTasks()) {
            subTaskList.add(subTasks.get(subTaskId));
        }

        return subTaskList;
    }
}
