package ru.isakovleonid.practicum.taskmanager.httpserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.isakovleonid.practicum.taskmanager.taskmanager.TaskManager;
import ru.isakovleonid.practicum.taskmanager.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDateTime;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    private String handleGetTaskId(int id) throws ClassNotFoundException {
        Task task = this.taskManager.getTask(id);

        if (task == null)
            throw new ClassNotFoundException("Не найдена задача с id = " + id);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();

        return gson.toJson(task);
    }

    public String handlePostTaskId(String body) throws ClassNotFoundException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        Task task = gson.fromJson(body, Task.class);

        if (task == null)
            throw new ClassNotFoundException("Не удалось создать задачу");
        else
            taskManager.addTask(task);

        return gson.toJson(task.getId());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] splitedPath = path.split("/");
        String response = "";
        InputStream request;

        try {
            switch (method) {
                case "GET":
                    if (splitedPath.length == 2)
                        throw new Exception("Метод еще не реализован");
                    else if (splitedPath.length == 3) {
                        int id = Integer.parseInt(splitedPath[2]);

                        response = handleGetTaskId(id);
                        sendText(exchange, response);
                    }
                    break;
                case "POST":
                    if (splitedPath.length != 2)
                        super.sendNotFound(exchange, "Неверный формат запроса");
                    else {
                        request = exchange.getRequestBody();
                        String body = new String(request.readAllBytes(), StandardCharsets.UTF_8);

                        response = handlePostTaskId(body);
                        super.sendCreated(exchange, response);
                    }
                    break;
                case "DELETE":
                    throw new Exception("Метод еще не реализован");
                    //break;
                default:
                    super.sendNotFound(exchange, "Неверный формат запроса");
            }
        }
        catch (NumberFormatException e) {
            super.sendNotFound(exchange, "Ошибка преобразования числа");
        }
        catch (Exception e) {
            super.sendNotFound(exchange, e.getMessage());
        }

    }
}
