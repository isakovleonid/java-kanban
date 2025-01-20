package ru.isakovleonid.practicum.taskmanager.historymanager;

import org.junit.jupiter.api.Test;
import ru.isakovleonid.practicum.taskmanager.Managers;
import ru.isakovleonid.practicum.taskmanager.taskmanager.*;
import ru.isakovleonid.practicum.taskmanager.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

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
        Integer st1_id = tm.addSubTask(new SubTask("подзадача 4", "описание подзадачи 4 эпика 3"
                , e1_id
                , LocalDateTime.of(2025, 1, 9, 12, 14)
                , Duration.ofMinutes(50)));
        Integer st2_id = tm.addSubTask(new SubTask("подзадача 5", "описание подзадачи 5 эпика 3"
                , e1_id
                , LocalDateTime.of(2025, 1, 10, 18, 14)
                , Duration.ofMinutes(40)));

        Task t1 = tm.getTask(t1_id);
        Task tt = new Task(t1_id
                ,"test"
                , "test"
                , TaskStatus.NEW
                , LocalDateTime.of(2025, 1, 14, 10, 54)
                , Duration.ofMinutes(100));
        Task t2 = tm.getTask(t2_id);

        assertEquals(t1, tt, "Задачи не равны при одинаковом id");
        assertNotEquals(t1, t2, "Задачи равны при разных id");

        Epic e1 = tm.getEpic(e1_id);
        Epic et = new Epic(e1_id, "test", "test");
        Epic e2 = tm.getEpic(e2_id);

        assertEquals(e1, et, "Задачи не равны при одинаковом id");
        assertNotEquals(e1, e2, "Задачи равны при разных id");

        SubTask st1 = tm.getSubTask(st1_id);
        SubTask stt = new SubTask(st1_id
                ,"test"
                ,"test"
                , TaskStatus.NEW
                , e1.getId()
                , LocalDateTime.of(2025, 1, 15, 12, 0)
                , Duration.ofMinutes(100));
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
        Integer st1_id = tm.addSubTask(new SubTask("подзадача 4", "описание подзадачи 4 эпика 3"
                , e1_id
                , LocalDateTime.of(2025, 1, 9, 12, 14)
                , Duration.ofMinutes(50)));

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

        Task tnUpdate = new Task(t1_id
                , "задача 1 обновление"
                , "описание обновления задачи 1"
                , TaskStatus.NEW
                , LocalDateTime.of(2025, 1, 13, 9, 30)
                , Duration.ofMinutes(70));
        String expectationVal = tnUpdate.getName()+"^"+ tnUpdate.getDescription()+"^"+ tnUpdate.getStatus();

        tm.addTask(tnUpdate);
        Task tnAfter = tm.getTask(t1_id);
        String resultVal = tnAfter.getName()+"^"+ tnAfter.getDescription()+"^"+ tnAfter.getStatus();

        assertEquals(expectationVal, resultVal, "Задача после обновления изменилась");

    }

    @Test
    void checkNotAddSubTaskAsEpic(){
        TaskManager tm;
        tm = Managers.getDefault();

        Epic en = new Epic("тестовый эпик", "Описание тестового эпика");
        SubTask stn = new SubTask("тестовая подзадача", "описание тестовой подазадачи"
                , en.getId()
                , LocalDateTime.of(2025, 1, 9, 12, 14)
                , Duration.ofMinutes(50));

        Integer stn_id = tm.addSubTask(stn);

        assertNull(stn_id, "Смогли добавить в task manager подзадачу, у которой эпик не добавлен в task manager");
    }

    @Test
    void checkEpicNotContainsDeletedSubTasks() {
        TaskManager tm;
        tm = Managers.getDefault();

        Epic en = new Epic("тестовый эпик", "Описание тестового эпика");
        SubTask stn;

        Integer e1_id = tm.addEpic(en);
        stn = new SubTask("тестовая подзадача 1", "описание тестовой подазадачи 1"
                , e1_id
                , LocalDateTime.of(2025, 1, 9, 12, 14)
                , Duration.ofMinutes(50));
        tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 2", "описание тестовой подазадачи 2"
                , e1_id
                , LocalDateTime.of(2025, 1, 10, 13, 4)
                , Duration.ofMinutes(40));
        Integer stn2_id = tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 3", "описание тестовой подазадачи 3"
                , e1_id
                , LocalDateTime.of(2025, 1, 11, 23, 14)
                , Duration.ofMinutes(50));
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
        stn = new SubTask("тестовая подзадача 1", "описание тестовой подазадачи 1"
                , e1_id
                , LocalDateTime.of(2025, 1, 9, 12, 14)
                , Duration.ofMinutes(50));
        Integer stn1_id = tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 2", "описание тестовой подазадачи 2"
                , e1_id
                , LocalDateTime.of(2025, 1, 10, 8, 14)
                , Duration.ofMinutes(50));
        Integer stn2_id = tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 3", "описание тестовой подазадачи 3"
                , e1_id
                , LocalDateTime.of(2025, 1, 12, 22, 14)
                , Duration.ofMinutes(50));
        tm.addSubTask(stn);

        task = new Task("тестовая задача 1"
                , "описание тестовой задача 1"
                , LocalDateTime.of(2025, 1, 13, 12, 14)
                , Duration.ofMinutes(50));
        Integer task1_id = tm.addTask(task);

        task = new Task("тестовая задача 2"
                , "описание тестовой задача 2"
                , LocalDateTime.of(2025, 1, 14, 17, 14)
                , Duration.ofMinutes(50));
        Integer task2_id = tm.addTask(task);

        tm.getEpic(e1_id);
        tm.getSubTask(stn1_id);
        tm.getSubTask(stn2_id);
        tm.getTask(task1_id);
        tm.getTask(task2_id);

        tm.getTask(task2_id);
        assertEquals("[TaskManager.Epic{ id=1', name='эпик 1', description='описание эпика 1', status=NEW', startTime=09.01.2025 12:14:00', duration=150', subTasks=[2, 3, 4]'}, "
                + "TaskManager.SubTask{ id=2', name='тестовая подзадача 1', description='описание тестовой подазадачи 1', status=NEW', startTime=09.01.2025 12:14:00', duration=50', epic=1'} , "
                + "TaskManager.SubTask{ id=3', name='тестовая подзадача 2', description='описание тестовой подазадачи 2', status=NEW', startTime=10.01.2025 08:14:00', duration=50', epic=1'} , "
                + "TaskManager.Task{ id=5', name='тестовая задача 1', description='описание тестовой задача 1', status=NEW, startTime=13.01.2025 12:14:00', duration=50'}, "
                + "TaskManager.Task{ id=6', name='тестовая задача 2', description='описание тестовой задача 2', status=NEW, startTime=14.01.2025 17:14:00', duration=50'}]"
                , hm.getHistory().toString()
                , "История чтения изменилась, если прочитали еще раз последнюю вычитанную задачу");

        tm.getEpic(e1_id);
        assertEquals("[TaskManager.SubTask{ id=2', name='тестовая подзадача 1', description='описание тестовой подазадачи 1', status=NEW', startTime=09.01.2025 12:14:00', duration=50', epic=1'} , "
                + "TaskManager.SubTask{ id=3', name='тестовая подзадача 2', description='описание тестовой подазадачи 2', status=NEW', startTime=10.01.2025 08:14:00', duration=50', epic=1'} , "
                + "TaskManager.Task{ id=5', name='тестовая задача 1', description='описание тестовой задача 1', status=NEW, startTime=13.01.2025 12:14:00', duration=50'}, "
                + "TaskManager.Task{ id=6', name='тестовая задача 2', description='описание тестовой задача 2', status=NEW, startTime=14.01.2025 17:14:00', duration=50'}, "
                + "TaskManager.Epic{ id=1', name='эпик 1', description='описание эпика 1', status=NEW', startTime=09.01.2025 12:14:00', duration=150', subTasks=[2, 3, 4]'}]"
                , hm.getHistory().toString()
                ,"Некорректная история, если прочитали первую вычитанную запись");

        tm.getSubTask(stn2_id);
        assertEquals(
                "[TaskManager.SubTask{ id=2', name='тестовая подзадача 1', description='описание тестовой подазадачи 1', status=NEW', startTime=09.01.2025 12:14:00', duration=50', epic=1'} , "
                + "TaskManager.Task{ id=5', name='тестовая задача 1', description='описание тестовой задача 1', status=NEW, startTime=13.01.2025 12:14:00', duration=50'}, "
                + "TaskManager.Task{ id=6', name='тестовая задача 2', description='описание тестовой задача 2', status=NEW, startTime=14.01.2025 17:14:00', duration=50'}, "
                + "TaskManager.Epic{ id=1', name='эпик 1', description='описание эпика 1', status=NEW', startTime=09.01.2025 12:14:00', duration=150', subTasks=[2, 3, 4]'}, "
                + "TaskManager.SubTask{ id=3', name='тестовая подзадача 2', description='описание тестовой подазадачи 2', status=NEW', startTime=10.01.2025 08:14:00', duration=50', epic=1'} ]"
                , hm.getHistory().toString()
                , "Некорректная история, если прочитали вычитанную не первой и не последней задачу");

    }

    @Test
    void checkEpicStatus() {
        TaskManager tm;
        HistoryManager hm;
        tm = Managers.getDefault();
        hm = tm.getHistoryManager();

        Epic e1 = new Epic("эпик 1", "описание эпика 1");
        Integer e1_id = tm.addEpic(e1);
        SubTask stn;

        stn = new SubTask("тестовая подзадача 1", "описание тестовой подазадачи 1"
                , e1_id
                , LocalDateTime.of(2025, 1, 9, 12, 14)
                , Duration.ofMinutes(50));
        Integer stn1_id = tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 2", "описание тестовой подазадачи 2"
                , e1_id
                , LocalDateTime.of(2025, 1, 10, 8, 14)
                , Duration.ofMinutes(50));
        Integer stn2_id = tm.addSubTask(stn);


        assertEquals(e1.getStatus()
                        , TaskStatus.NEW
                        , "Все подзадачи в состоянии " + TaskStatus.NEW
                        + ". Состояние эпика должно быть " + TaskStatus.NEW
                        + ". Текущее значение " + e1.getStatus());

        stn = new SubTask(stn1_id
                ,"тестовая подзадача 1", "описание тестовой подазадачи 1"
                , TaskStatus.IN_PROGRESS
                , e1_id
                , LocalDateTime.of(2025, 1, 9, 12, 14)
                , Duration.ofMinutes(50));
        tm.addSubTask(stn);

        assertEquals(e1.getStatus()
                , TaskStatus.IN_PROGRESS
                , "Одна подзадача в состоянии " + TaskStatus.IN_PROGRESS
                        + ", остальные подзадачи в состоянии " + TaskStatus.NEW
                        + ". Состояние эпика должно быть " + TaskStatus.IN_PROGRESS
                        + ". Текущее значение " + e1.getStatus());

        stn = new SubTask(stn1_id
                ,"тестовая подзадача 1", "описание тестовой подазадачи 1"
                , TaskStatus.DONE
                , e1_id
                , LocalDateTime.of(2025, 1, 9, 12, 14)
                , Duration.ofMinutes(50));
        tm.addSubTask(stn);

        assertEquals(e1.getStatus()
                , TaskStatus.IN_PROGRESS
                , "Одна подзадача в состоянии " + TaskStatus.DONE
                        + ", остальные подзадачи в состоянии " + TaskStatus.NEW
                        + ". Состояние эпика должно быть " + TaskStatus.IN_PROGRESS
                        + ". Текущее значение " + e1.getStatus());

        stn = new SubTask(stn2_id
                ,"тестовая подзадача 2", "описание тестовой подазадачи 2"
                , TaskStatus.DONE
                , e1_id
                , LocalDateTime.of(2025, 1, 10, 8, 14)
                , Duration.ofMinutes(50));
        tm.addSubTask(stn);

        assertEquals(e1.getStatus()
                , TaskStatus.DONE
                , "Все подзадачи в состоянии " + TaskStatus.DONE
                        + ". Состояние эпика должно быть " + TaskStatus.DONE
                        + ". Текущее значение " + e1.getStatus());
    }
}