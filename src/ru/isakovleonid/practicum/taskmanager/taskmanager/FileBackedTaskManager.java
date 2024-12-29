package ru.isakovleonid.practicum.taskmanager.taskmanager;
import ru.isakovleonid.practicum.taskmanager.tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static java.lang.String.join;

public class FileBackedTaskManager extends InMemoryTaskManager {
    String fileName;
    private static final String delimiter = ",";

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager result = new FileBackedTaskManager("tempFile.csv");
        try (FileReader reader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(reader);
            int cntLine = 0;
            while (br.ready()) {
                String line = br.readLine();
                cntLine++;
                if (cntLine > 1) {
                    String[] taskParams = line.split(delimiter);

                    if (5 == taskParams.length && taskParams[1].equals(TaskType.TASK.toString())) {
                        Task task = new Task(Integer.parseInt(taskParams[0]),
                                taskParams[2],
                                taskParams[4],
                                TaskStatus.valueOf(taskParams[3]));
                        result.addTask(task);
                    } else if (5 == taskParams.length && taskParams[1].equals(TaskType.EPIC.toString())) {
                        Epic epic = new Epic(Integer.parseInt(taskParams[0]),
                                taskParams[2],
                                taskParams[4]);
                        result.addEpic(epic);
                    } else if (6 == taskParams.length && taskParams[1].equals(TaskType.SUBTASK.toString())) {
                        SubTask subTask = new SubTask(Integer.parseInt(taskParams[0]),
                                taskParams[2],
                                taskParams[4],
                                TaskStatus.valueOf(taskParams[3]),
                                Integer.parseInt(taskParams[5]));
                        result.addSubTask(subTask);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }

        return result;
    }

    public FileBackedTaskManager(String fileName) {
        super();
        this.fileName = fileName;
        Path path = Paths.get(fileName);
        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new ManagerSaveException(e);
            }
        }
    }

    private String stringHeader() {
        return join(delimiter, "id", "type", "name", "status","description","epic");
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(fileName)) {
            fileWriter.write(stringHeader() + "\n");
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                fileWriter.write(entry.getValue().stringForFile() + "\n");
            }

            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                fileWriter.write(entry.getValue().stringForFile() + "\n");
            }

            for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
                fileWriter.write(entry.getValue().stringForFile() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    @Override
    public Integer addEpic(Epic newEpic) {
        Integer id = super.addEpic(newEpic);

        save();

        return id;
    }

    @Override
    public Integer addSubTask(SubTask newSubTask) {
        Integer id = super.addSubTask(newSubTask);

        save();

        return id;
    }

    @Override
    public Integer addTask(Task newTask) {
        Integer id =  super.addTask(newTask);

        save();

        return id;
    }

    @Override
    public void deleteAll() {
        super.deleteAll();

        save();
    }

    @Override
    public void deleteById(Integer id) {
        super.deleteById(id);

        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);

        save();
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        super.updateSubTask(newSubTask);

        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);

        save();
    }

    public static void main(String[] args) {
        File file = new File("test.csv");
        TaskManager tm = /*new FileBackedTaskManager("test.csv")*/FileBackedTaskManager.loadFromFile(file);
/*
        SubTask stn;
        Task task;

        Integer e1id = tm.addEpic(new Epic("эпик 1", "описание эпика 1"));
        stn = new SubTask("тестовая подзадача 1", "описание тестовой подазадачи 1", e1id);
        tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 2", "описание тестовой подазадачи 2", e1id);
        tm.addSubTask(stn);

        stn = new SubTask("тестовая подзадача 3", "описание тестовой подазадачи 3", e1id);
        tm.addSubTask(stn);

        task = new Task("тестовая задача 1", "описание тестовой задача 1");
        tm.addTask(task);

        task = new Task("тестовая задача 2", "описание тестовой задача 2");
        tm.addTask(task);*/
        System.out.println(tm.toString());;
    }
}