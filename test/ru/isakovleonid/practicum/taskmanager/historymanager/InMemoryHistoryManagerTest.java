package ru.isakovleonid.practicum.taskmanager.historymanager;

import org.junit.jupiter.api.Test;
import ru.isakovleonid.practicum.taskmanager.Managers;
import ru.isakovleonid.practicum.taskmanager.taskmanager.*;
import ru.isakovleonid.practicum.taskmanager.tasks.*;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest {

    @Test
    void taskAreEqualsById() {
        TaskManager tm;
        tm = Managers.getDefault();

        Integer t1_id = tm.addTask(new Task("задача 1", "описание задачи 1"));
        Integer t2_id = tm.addTask(new Task("задача 2", "описание задачи 2"));
        Integer e1_id = tm.addEpic(new Epic("эпик 1", "описание эпика 1"));
        Integer e2_id = tm.addEpic(new Epic("эпик 2", "описание эпика 2"));
        Integer st1_id = tm.addSubTask(new SubTask("подзадача 4", "описание подзадачи 4 эпика 3", e1_id));
        Integer st2_id = tm.addSubTask(new SubTask("подзадача 5", "описание подзадачи 5 эпика 3", e1_id));

        Task t1 = tm.getTask(t1_id);
        Task tt = new Task(t1_id,"test", "test", TaskStatus.NEW);
        Task t2 = tm.getTask(t2_id);

        assertEquals(t1, tt, "Задачи не равны при одинаковом id");
        assertNotEquals(t1, t2, "Задачи равны при разных id");

        Epic e1 = tm.getEpic(e1_id);
        Epic et = new Epic(e1_id, "test", "test");
        Epic e2 = tm.getEpic(e2_id);

        assertEquals(e1, et, "Задачи не равны при одинаковом id");
        assertNotEquals(e1, e2, "Задачи равны при разных id");

        SubTask st1 = tm.getSubTask(st1_id);
        SubTask stt = new SubTask(st1_id,"test", "test", TaskStatus.NEW, e1.getId());
        SubTask st2 = tm.getSubTask(st2_id);

        assertEquals(st1, stt, "Подзадачи не равны при одинаковом id");
        assertNotEquals(st1, st2, "Подзадачи равны при разных id");
    }

    @Test
    void taskManagerTest() {
        TaskManager tm;
        tm = Managers.getDefault();

        Integer t1_id = tm.addTask(new Task("задача 1", "описание задачи 1"));
        Integer e1_id = tm.addEpic(new Epic("эпик 1", "описание эпика 1"));
        Integer st1_id = tm.addSubTask(new SubTask("подзадача 4", "описание подзадачи 4 эпика 3", e1_id));

        Task test_t1 = tm.getTask(t1_id);
        assertNotNull(test_t1,"Не найдена задача с индексом " + t1_id);

        tm.getSubTask(st1_id);
        assertNotNull(test_t1,"Не найдена подзадача с индексом " + st1_id);

        tm.getEpic(e1_id);
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
        Integer tn_id = tm.addTask(tnBefore);
        Task tnAfter = tm.getTask(tn_id);
        String resultVal = tnAfter.getName()+"^"+ tnAfter.getDescription()+"^"+ tnAfter.getStatus();

        assertEquals(expectationVal, resultVal, "Задача после добавления изменилась");

    }

    @Test
    void checkTaskUpdate(){
        TaskManager tm;
        tm = Managers.getDefault();

        Integer t1_id = tm.addTask(new Task("задача 1", "описание задачи 1"));

        Task tnUpdate = new Task(t1_id, "задача 1 обновление", "описание обновления задачи 1", TaskStatus.NEW);
        String expectationVal = tnUpdate.getName()+"^"+ tnUpdate.getDescription()+"^"+ tnUpdate.getStatus();

        tm.updateTask(tnUpdate);
        Task tnAfter = tm.getTask(t1_id);
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

    @Test
    void checkEpicNotContainsDeletedSubTasks() {
        TaskManager tm;
        tm = Managers.getDefault();

        Epic en = new Epic("тестовый эпик", "Описание тестового эпика");
        SubTask stn;

        Integer e1_id = tm.addEpic(new Epic("эпик 1", "описание эпика 1"));
        stn = new SubTask("тестовая подзадача 1", "описание тестовой подазадачи 1", e1_id);
        tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 2", "описание тестовой подазадачи 2", e1_id);
        Integer stn2_id = tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 3", "описание тестовой подазадачи 3", e1_id);
        tm.addSubTask(stn);

        tm.deleteById(stn2_id);

        assertFalse(en.getSubTasks().contains(stn2_id), "Удаленная задача в TaskManager осталась у Эпика");
    }

    @Test
    void checkGetHistory() {
        TaskManager tm;
        HistoryManager hm;
        tm = Managers.getDefault();
        hm = tm.getHistoryManager();

        SubTask stn;
        Task task;

        Integer e1_id = tm.addEpic(new Epic("эпик 1", "описание эпика 1"));
        stn = new SubTask("тестовая подзадача 1", "описание тестовой подазадачи 1", e1_id);
        Integer stn1_id = tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 2", "описание тестовой подазадачи 2", e1_id);
        Integer stn2_id = tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 3", "описание тестовой подазадачи 3", e1_id);
        tm.addSubTask(stn);

        task = new Task("тестовая задача 1", "описание тестовой задача 1");
        Integer task1_id = tm.addTask(task);

        task = new Task("тестовая задача 2", "описание тестовой задача 2");
        Integer task2_id = tm.addTask(task);

        tm.getEpic(e1_id);
        tm.getSubTask(stn1_id);
        tm.getSubTask(stn2_id);
        tm.getTask(task1_id);
        tm.getTask(task2_id);

        tm.getTask(task2_id);
        assertEquals("[TaskManager.Epic{ id=1', name='эпик 1', description='описание эпика 1', status=NEW', subTasks=[2, 3, 4]'}"
                        + ", TaskManager.SubTask{ id=2', name='тестовая подзадача 1', description='описание тестовой подазадачи 1', status=NEW', epic=1'} "
                        + ", TaskManager.SubTask{ id=3', name='тестовая подзадача 2', description='описание тестовой подазадачи 2', status=NEW', epic=1'} "
                        + ", TaskManager.Task{ id=5', name='тестовая задача 1', description='описание тестовой задача 1', status=NEW}"
                        + ", TaskManager.Task{ id=6', name='тестовая задача 2', description='описание тестовой задача 2', status=NEW}]"
                , hm.getHistory().toString()
                , "История чтения изменилась, если прочитали еще раз последнюю вычитанную задачу");

        tm.getEpic(e1_id);
        assertEquals("[TaskManager.SubTask{ id=2', name='тестовая подзадача 1', description='описание тестовой подазадачи 1', status=NEW', epic=1'} "
                        + ", TaskManager.SubTask{ id=3', name='тестовая подзадача 2', description='описание тестовой подазадачи 2', status=NEW', epic=1'} "
                        + ", TaskManager.Task{ id=5', name='тестовая задача 1', description='описание тестовой задача 1', status=NEW}"
                        + ", TaskManager.Task{ id=6', name='тестовая задача 2', description='описание тестовой задача 2', status=NEW}"
                        + ", TaskManager.Epic{ id=1', name='эпик 1', description='описание эпика 1', status=NEW', subTasks=[2, 3, 4]'}]"
                , hm.getHistory().toString()
                ,"Некорректная история, если прочитали первую вычитанную запись");

        tm.getSubTask(stn2_id);
        assertEquals(
                "[TaskManager.SubTask{ id=2', name='тестовая подзадача 1', description='описание тестовой подазадачи 1', status=NEW', epic=1'} "
                        + ", TaskManager.Task{ id=5', name='тестовая задача 1', description='описание тестовой задача 1', status=NEW}"
                        + ", TaskManager.Task{ id=6', name='тестовая задача 2', description='описание тестовой задача 2', status=NEW}"
                        + ", TaskManager.Epic{ id=1', name='эпик 1', description='описание эпика 1', status=NEW', subTasks=[2, 3, 4]'}"
                        + ", TaskManager.SubTask{ id=3', name='тестовая подзадача 2', description='описание тестовой подазадачи 2', status=NEW', epic=1'} ]"
                , hm.getHistory().toString()
                , "Некорректная история, если прочитали вычитанную не первой и не последней задачу");

    }
}