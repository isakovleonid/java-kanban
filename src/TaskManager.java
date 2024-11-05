import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private int counter;

    // список Задач, Эпиков и Подзадач
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

        // в остальных случаях возвращем пусто
        return null;
    }

    /* 4. Создание */
    public void addTask(Task newTask) {
        // идентификатор задачи присваиваем перед ее добавления в менеджер задача
        newTask.setId(++counter);
        // вставляем через update
        updateTask(newTask);
    }

    public void addEpic(Epic newEpic) {
        // идентификатор задачи присваиваем перед ее добавления в менеджер задача
        newEpic.setId(++counter);
        // вставляем через update
        updateEpic(newEpic);
    }

    public void addSubTask(SubTask newSubTask) {
        // идентификатор задачи присваиваем перед ее добавления в менеджер задача
        newSubTask.setId(++counter);
        // вставляем через update
        updateSubTask(newSubTask);
    }

    /* 5. Обновление */
    /*Обновление задачи*/
    public void updateTask(Task newTask){
        // по id перезатираем объект
        tasks.put(newTask.getId(), newTask);
    }

    public void updateEpic(Epic newEpic){
        // по id перезатираем объект
        epics.put(newEpic.getId(), newEpic);
    }

    /*Обновление подзадачи*/
    public void updateSubTask(SubTask newSubTask){
        // статус эпика уже будет обновлен при создании подзадачи
        // по id перезатираем объект
        Integer subTaskId = newSubTask.getId();
        subTasks.put(subTaskId, newSubTask);

        // добавляем подазадачу в массив подзадач эпика
        Integer epicId = newSubTask.getEpic();
        Epic epic = epics.get(epicId);
        epic.addSubTask(subTaskId);

        updateEpicStatus(epicId);
    }

    /* Обновление статуса эпика после обновления подзадач*/
    private void updateEpicStatus(Integer epicId) {
        Epic epic = epics.get(epicId);
        // отбираем подзадачи эпика
        List<SubTask> subTaskList = new ArrayList<SubTask>();
        for (Integer subTaskId : epic.getSubTasks()) {
            subTaskList.add(subTasks.get(subTaskId));
        }
        // по отобранным подзадачам обновляем статус
        epic.updateStatus(subTaskList);
    }

    /*Обновление эпика*/
    public void updateTask(Epic newEpic){
        // по id перезатираем объект
        epics.put(newEpic.getId(), newEpic);
    }

    /* 6. Удаление по идентификатору */
    public void deleteById(int id) {
        // удаляем задачу из списка задач Task Manager, если она есть
        tasks.remove(id);

        // удаляем подзадачу, если она есть
        // 1. Удаляем подзадачу их эпика
        Integer epicId = subTasks.get(id).getEpic();
        Epic epic =  epics.get(epicId);
        epic.deleteSubTaskById(id);
        updateEpicStatus(epicId);
        // 2. удаляем подзадачу из Task Manager
        subTasks.remove(id);


        // Удаляем эпик (вместе с подзадачами), если он есть
        // 1. Удаляем все подзадачи эпика из списка подзадач Task Manager
        for (Integer subTask : epics.get(id).getSubTasks()) {
            subTasks.remove(subTask);
        }
        // 2. удаляем все подзадачи из эпика
        epics.get(id).deleteSubTaskAll();
        // 3. Удаляем эпик из Task Manager
        epics.remove(id);
    }

    /* 7. Получение списка всех подзадач определенного эпика*/
    public String getEpicSubTasks(Epic epic) {
        return epic.toString();
    }



}
