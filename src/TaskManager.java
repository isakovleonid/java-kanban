import java.util.*;

public interface TaskManager {
    @Override
    String toString();

    void deleteAll() ;

    Task getById(int id, HistoryManager historyManager);

    Task getTask(int id, HistoryManager historyManager) ;
    SubTask getSubTask(int id, HistoryManager historyManager) ;
    Epic getEpic(int id, HistoryManager historyManager) ;

    int addTask(Task newTask) ;

    int addEpic(Epic newEpic);

    int addSubTask(SubTask newSubTask) ;

    void updateTask(Task newTask);

    void updateEpic(Epic newEpic);

    void updateSubTask(SubTask newSubTask);

    void deleteById(int id);

    List<SubTask> getEpicSubTasks(Epic epic);
}
