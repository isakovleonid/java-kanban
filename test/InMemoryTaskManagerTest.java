import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    static TaskManager tm;
    static HistoryManager historyManager;
    static int t1_id;
    static int t2_id;
    static int e_id;
    static int st1_id;
    static int st2_id;


    @BeforeAll
    static void beforeAll() {
        tm = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();

        Task t1 = new Task("задача 1", "описание задачи 1");
        t1_id = tm.addTask(t1);

        t1 = new Task("задача 2", "описание задачи 2");
        t2_id = tm.addTask(t1);

        Epic epic = new Epic("эпик 1", "описание эпика 1");
        e_id = tm.addEpic(epic);

        SubTask st = new SubTask("подзадача 4", "описание подзадачи 4 эпика 3", e_id);
        st1_id = tm.addSubTask(st);

        st = new SubTask("подзадача 5", "описание подзадачи 5 эпика 3", e_id);
        st2_id = tm.addSubTask(st);
    }


    @Test
    void taskAreEqualsById() {
        Task t1 = tm.getTask(1, historyManager);
        Task tt = new Task(1,"test", "test", TaskStatus.NEW);
        Task t2 = tm.getTask(2, historyManager);

        assertEquals(t1, tt, "Задачи не равны при одинаковом id");
        assertNotEquals(t1, t2, "Задачи равны при разных id");
    }

    void taskManagerTest() {
        Task test_t1 = tm.getTask(t1_id, historyManager);
        assertNotNull(test_t1,"Не найдена задача с индексом " + t1_id);

        SubTask test_st1 = tm.getSubTask(st1_id, historyManager);
        assertNotNull(test_t1,"Не найдена подзадача с индексом " + st1_id);

        Epic test_epic = tm.getEpic(e_id, historyManager);
        assertNotNull(test_t1,"Не найден эпик с индексом " + e_id);
    }


    @Test
    void checkManagerObjects() {

        assertNotNull(Managers.getDefault(),"В утилитарном классе не создан менеджер задача по умолчанию");
        assertNotNull(Managers.getDefaultHistory(),"В утилитарном классе не создан менеджер задача по умолчанию");
    }

}