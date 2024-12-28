
package ru.isakovleonid.practicum.taskmanager.filebackedtaskmanager;
import ru.isakovleonid.practicum.taskmanager.taskmanager.*;

import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


class FileBackedTaskManagerTest {

    private static String fileName;
    private static String copyTMFileName;

    private static Path pathTM;
    private static Path copyTMPath;

    static void deleteFiles() {

        try {
            Files.deleteIfExists(copyTMPath);
            Files.deleteIfExists(pathTM);
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }
    @BeforeAll
    static void prepareData() {
        fileName = "testFileBackedTaskManager.csv";
        copyTMFileName = "newFileBackedTaskManager.csv";
        pathTM = Paths.get(fileName);
        copyTMPath = Paths.get(copyTMFileName);

        deleteFiles();
    }


    @AfterAll
    static void emptyData() {
        deleteFiles();
    }
    @Test
    void checkCreateAndCopyFile() {
        TaskManager tm = new FileBackedTaskManager(fileName);

        SubTask stn;
        Task task;

        Integer e1_id = tm.addEpic(new Epic("эпик 1", "описание эпика 1"));
        stn = new SubTask("тестовая подзадача 1", "описание тестовой подазадачи 1", e1_id);
        Integer stn1_id = tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 2", "описание тестовой подазадачи 2", e1_id);
        Integer stn2_id = tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 3", "описание тестовой подазадачи 3", e1_id);
        Integer stn3_id = tm.addSubTask(stn);

        task = new Task("тестовая задача 1", "описание тестовой задача 1");
        Integer task1_id = tm.addTask(task);

        task = new Task("тестовая задача 2", "описание тестовой задача 2");
        Integer task2_id = tm.addTask(task);

        Assertions.assertTrue(Files.exists(pathTM),"Не создан файл менеджера задач");

        try {
            Files.copy(pathTM, copyTMPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TaskManager copyTM = new FileBackedTaskManager(copyTMFileName);
        Assertions.assertEquals(tm.toString(), copyTM.toString(), "После копирования через файл наполнение менеджера задача не совпадает");

    }
}