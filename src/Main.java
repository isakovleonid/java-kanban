import ru.isakovleonid.practicum.taskmanager.historymanager.HistoryManager;
import ru.isakovleonid.practicum.taskmanager.taskmanager.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager tm = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        /* Создание TaskManager.Task*/
        Task t1 = new Task("задача 1", "описание задачи 1");
        int t1_id = tm.addTask(t1);

        t1 = new Task("задача 2", "описание задачи 2");
        int t2_id = tm.addTask(t1);

        /* Создание TaskManager.Epic*/
        Epic epic = new Epic("эпик 1", "описание эпика 1");
        int epic_id = tm.addEpic(epic);

        /* Создание TaskManager.SubTask уже созданому TaskManager.Epic*/
        SubTask st = new SubTask("подзадача 4", "описание подзадачи 4 эпика 3", epic_id);
        int st1_id = tm.addSubTask(st);

        st = new SubTask("подзадача 5", "описание подзадачи 5 эпика 3", epic_id);
        int st2_id = tm.addSubTask(st);

        System.out.println(tm.toString());

        System.out.println("Меням статус TaskManager.Task с id = " + t1_id);

        t1 = new Task(t1_id, "Задача 1","обновление задачи 1", TaskStatus.DONE);
        tm.updateTask(t1);

        System.out.println(tm.toString());

        System.out.println("Меняем статус подзадачи с id = " + st1_id + ". Статус эпика должен стать IN_PROGRESS");
        st = new SubTask(st1_id, "подзадача 4","обновление подзадачи 4 эпика 3", TaskStatus.DONE, epic);
        tm.updateSubTask(st);

        System.out.println(tm.toString());

        System.out.println("Меням статус подзадачи с id = " + st1_id + " и id = "+ st2_id +". Статус эпика должен стать DONE");
        st = new SubTask(st1_id, "подзадача 4","обновление подзадачи " +st1_id + " эпика " + epic_id, TaskStatus.DONE, epic);
        tm.updateSubTask(st);
        st = new SubTask(st2_id, "подзадача 5","обновление подзадачи " +st2_id + " эпика " + epic_id, TaskStatus.DONE, epic);
        tm.updateSubTask(st);

        System.out.println("Удаляем с отсутствующим Id");
        tm.deleteById(10);

        System.out.println(tm.toString());

        System.out.println("Чтение задачи 1");
        tm.getTask(t1_id, historyManager);
        System.out.println("Чтение задачи 2");
        tm.getTask(t2_id, historyManager);
        System.out.println("Чтение подзадачи 4");
        tm.getSubTask(st1_id, historyManager);
        System.out.println("Чтение эпика 3");
        tm.getEpic(epic_id, historyManager);
        System.out.println("Чтение подзадачи 4");
        tm.getSubTask(st1_id, historyManager);

        System.out.println("История чтения: " + historyManager.getHistory());
    }
}
