package ru.isakovleonid.practicum.taskmanager.httpserver;

import com.sun.net.httpserver.HttpServer;
import ru.isakovleonid.practicum.taskmanager.Managers;
import ru.isakovleonid.practicum.taskmanager.historymanager.HistoryManager;
import ru.isakovleonid.practicum.taskmanager.taskmanager.TaskManager;
import ru.isakovleonid.practicum.taskmanager.tasks.Epic;
import ru.isakovleonid.practicum.taskmanager.tasks.SubTask;
import ru.isakovleonid.practicum.taskmanager.tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private int port;
    private HttpServer  server;
    private TaskManager taskManager;

    public HttpTaskServer(int port, TaskManager taskManager) {
        try {
            this.port = port;
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            System.out.println("Не удалось запустить сервер на порту " + port);;
        }

        this.taskManager = taskManager;

        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubTasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history",new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

    public static void main(String[] args) {
        int runPort = 8080;
        TaskManager tm = Managers.getDefault();
        HistoryManager hm = tm.getHistoryManager();

        HttpTaskServer httpTaskServer = new HttpTaskServer(runPort, tm);

        httpTaskServer.start();

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

        System.out.println("Сервер запущен на порту " + runPort);
    }
}
