import java.util.*;

public interface TaskManager {
    /* 1. Получение списка всех задач */
    @Override
    public String toString();

    /* 2. Удаление всех задач */
    public void deleteAll() ;

    /* 3. Получение объекта по идентификатору */
    public Task getById(int id);

    public Task getTask(int id) ;
    public SubTask getSubTask(int id) ;
    public Epic getEpic(int id) ;

    /* 4. Создание */
    public int addTask(Task newTask) ;

    public int addEpic(Epic newEpic);

    public int addSubTask(SubTask newSubTask) ;

    /* 5. Обновление */
    public void updateTask(Task newTask);

    public void updateEpic(Epic newEpic);

    /*Обновление подзадачи*/
    public void updateSubTask(SubTask newSubTask);

    /* 6. Удаление по идентификатору */
    public void deleteById(int id);

    /* 7. Получение списка всех подзадач определенного эпика*/
    public  List<SubTask> getEpicSubTasks(Epic epic);

    /* 8. Список просмотров последних задач*/
    public List<Task> getHistory();

}
