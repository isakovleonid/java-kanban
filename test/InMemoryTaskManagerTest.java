import ru.isakovleonid.practicum.taskmanager.historymanager.HistoryManager;
import ru.isakovleonid.practicum.taskmanager.taskmanager.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    static HistoryManager historyManager;

    @BeforeAll
    static void beforeAll() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void taskAreEqualsById() {
        TaskManager tm;
        tm = Managers.getDefault();

        int t1_id = tm.addTask(new Task("задача 1", "описание задачи 1"));
        int t2_id = tm.addTask(new Task("задача 2", "описание задачи 2"));
        int e1_id = tm.addEpic(new Epic("эпик 1", "описание эпика 1"));
        int e2_id = tm.addEpic(new Epic("эпик 2", "описание эпика 2"));
        int st1_id = tm.addSubTask(new SubTask("подзадача 4", "описание подзадачи 4 эпика 3", e1_id));
        int st2_id = tm.addSubTask(new SubTask("подзадача 5", "описание подзадачи 5 эпика 3", e1_id));

        Task t1 = tm.getTask(t1_id, historyManager);
        Task tt = new Task(t1_id,"test", "test", TaskStatus.NEW);
        Task t2 = tm.getTask(t2_id, historyManager);

        assertEquals(t1, tt, "Задачи не равны при одинаковом id");
        assertNotEquals(t1, t2, "Задачи равны при разных id");

        Epic e1 = tm.getEpic(e1_id, historyManager);
        Epic et = new Epic(e1_id, "test", "test");
        Epic e2 = tm.getEpic(e2_id, historyManager);

        assertEquals(e1, et, "Задачи не равны при одинаковом id");
        assertNotEquals(e1, e2, "Задачи равны при разных id");

        SubTask st1 = tm.getSubTask(st1_id, historyManager);
        SubTask stt = new SubTask(st1_id,"test", "test", TaskStatus.NEW, e1);
        SubTask st2 = tm.getSubTask(st2_id, historyManager);

        assertEquals(st1, stt, "Подзадачи не равны при одинаковом id");
        assertNotEquals(st1, st2, "Подзадачи равны при разных id");
    }

    void taskManagerTest() {
        TaskManager tm;
        tm = Managers.getDefault();

        int t1_id = tm.addTask(new Task("задача 1", "описание задачи 1"));
        int e1_id = tm.addEpic(new Epic("эпик 1", "описание эпика 1"));
        int st1_id = tm.addSubTask(new SubTask("подзадача 4", "описание подзадачи 4 эпика 3", e1_id));

        Task test_t1 = tm.getTask(t1_id, historyManager);
        assertNotNull(test_t1,"Не найдена задача с индексом " + t1_id);

        SubTask test_st1 = tm.getSubTask(st1_id, historyManager);
        assertNotNull(test_t1,"Не найдена подзадача с индексом " + st1_id);

        Epic test_epic = tm.getEpic(e1_id, historyManager);
        assertNotNull(test_t1,"Не найден эпик с индексом " + e1_id);
    }


    @Test
    void checkManagerObjects() {

        assertNotNull(Managers.getDefault(),"В утилитарном классе не создан менеджер задача по умолчанию");
        assertNotNull(Managers.getDefaultHistory(),"В утилитарном классе не создан менеджер задача по умолчанию");
    }

    @Test
    void checkNotChangedAfterAdd(){
        TaskManager tm;
        tm = Managers.getDefault();

        Task tnBefore = new Task("задача n", "описание задачи n");
        String expectationVal = tnBefore.getName()+"^"+ tnBefore.getDescription()+"^"+ tnBefore.getStatus();
        int tn_id = tm.addTask(tnBefore);
        Task tnAfter = tm.getTask(tn_id, historyManager);
        String resultVal = tnAfter.getName()+"^"+ tnAfter.getDescription()+"^"+ tnAfter.getStatus();

        assertEquals(expectationVal, resultVal, "Задача после добавления изменилась");

    }

    @Test
    void checkTaskUpdate(){
        TaskManager tm;
        tm = Managers.getDefault();

        int t1_id = tm.addTask(new Task("задача 1", "описание задачи 1"));

        Task tnUpdate = new Task(t1_id, "задача 1 обновление", "описание обновления задачи 1", TaskStatus.NEW);
        String expectationVal = tnUpdate.getName()+"^"+ tnUpdate.getDescription()+"^"+ tnUpdate.getStatus();

        tm.updateTask(tnUpdate);
        Task tnAfter = tm.getTask(t1_id, historyManager);
        String resultVal = tnAfter.getName()+"^"+ tnAfter.getDescription()+"^"+ tnAfter.getStatus();

        assertEquals(expectationVal, resultVal, "Задача после обновления изменилась");

    }

    @Test
    void checkNotAddSubTaskAsEpic(){
        TaskManager tm;
        tm = Managers.getDefault();

        Epic en = new Epic("тестовый эпик", "Описание тестового эпика");
        SubTask stn = new SubTask("тестовая подзадача", "описание тестовой подазадачи", en.getId());

        Integer stn_id = tm.addSubTask(stn);

        assertNull(stn_id, "Смогли добавить в task manager подазадчу, у которой эпик не добавлен в task manager");
    }

}