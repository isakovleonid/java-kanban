import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Epic  extends Task{
    // список подзадач эпика
    Set<Integer> subTasks;

    public Epic(String name, String description) {
        super(name, description);

        subTasks = new HashSet<>();
    }

    @Override
    public String toString() {
        return "Epic{" +
                " id=" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", subTasks=" + subTasks + '\'' +
                '}';
    }


    /* Добавление подзадачи в эпик*/
    public void addSubTask(Integer subTaskId) {
        // добавляем в список подзадачу
        subTasks.add(subTaskId);
    }

    /*Удаление подзадачи по Id*/
    public void deleteSubTaskById(Integer subTaskId) {
        subTasks.remove(subTaskId);
    }

    /*Удаление всех подзадач*/
    public void deleteSubTaskAll() {
        subTasks.clear();
    }

    public Set<Integer> getSubTasks() {
        return subTasks;
    }

    /*TODO: Обновляем статус по переданному массиву объектов, т.к. из массива ссылок выйти на объект нет возможности*/
    public void updateStatus(List<SubTask> subTasks) {
        int countDone = 0, countNew = 0;
        TaskStatus tempStatus = TaskStatus.IN_PROGRESS;

        // обновляем статус исходя из состояний подзадач subTasks
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() == TaskStatus.DONE)
                countDone++;
            else if (subTask.getStatus() == TaskStatus.NEW)
                countNew++;
        }

        if (subTasks.size() == countDone)
            tempStatus = TaskStatus.DONE;
        else if (subTasks.size() == countNew)
            tempStatus = TaskStatus.NEW;

        status = tempStatus;
    }
}
