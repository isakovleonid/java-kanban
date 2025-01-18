
package ru.isakovleonid.practicum.taskmanager.filebackedtaskmanager;
import ru.isakovleonid.practicum.taskmanager.taskmanager.*;
import ru.isakovleonid.practicum.taskmanager.tasks.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;


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
        stn = new SubTask("тестовая подзадача 1"
                , "описание тестовой подазадачи 1"
                , e1_id
                , LocalDateTime.of(2025, 1, 9, 12, 14)
                , Duration.ofMinutes(50));
        tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 2"
                , "описание тестовой подазадачи 2"
                , e1_id
                , LocalDateTime.of(2025, 1, 10, 13,50)
                , Duration.ofMinutes(10));
        tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 3"
                , "описание тестовой подазадачи 3"
                , e1_id
                , LocalDateTime.of(2025, 1, 15, 17, 58)
                , Duration.ofMinutes(20));
        tm.addSubTask(stn);

        task = new Task("тестовая задача 1"
                , "описание тестовой задача 1"
                , LocalDateTime.of(2025,1,14,9,18)
                , Duration.ofMinutes(57));
        tm.addTask(task);

        task = new Task("тестовая задача 2"
                , "описание тестовой задача 2"
                , LocalDateTime.of(2025,1,15,9,47)
                , Duration.ofMinutes(17));
        tm.addTask(task);

        Assertions.assertTrue(Files.exists(pathTM),"Не создан файл менеджера задач");

        try {
            Files.copy(pathTM, copyTMPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TaskManager copyTM = FileBackedTaskManager.loadFromFile(new File(copyTMFileName));
        Assertions.assertEquals(tm.toString(), copyTM.toString(), "После копирования через файл наполнение менеджера задача не совпадает");

    }
}