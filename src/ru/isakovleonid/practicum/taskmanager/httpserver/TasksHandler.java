package ru.isakovleonid.practicum.taskmanager.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.isakovleonid.practicum.taskmanager.taskmanager.ManagerSaveException;
import ru.isakovleonid.practicum.taskmanager.taskmanager.TaskManager;
import ru.isakovleonid.practicum.taskmanager.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    private String handleGetTasks() {
        List<Task> tasks = this.taskManager.getTasks();

        return gson.toJson(tasks);
    }

    private String handleGetTaskId(int id) throws ClassNotFoundException {
        Task task = this.taskManager.getTask(id);

        if (task == null)
            throw new ClassNotFoundException("Не найдена задача с id = " + id);

        return gson.toJson(task);
    }

    public String handlePostTaskId(String body) throws ClassNotFoundException {
        Task task = gson.fromJson(body, Task.class);

        if (task == null)
            throw new ClassNotFoundException("Не удалось создать задачу из JSON");

        taskManager.addTask(task);

        return gson.toJson(task);
    }

    public void handleDeleteTaskId(int id) throws ClassNotFoundException {
        Task task = this.taskManager.getTask(id);

        if (task == null)
            throw new ClassNotFoundException("Не найдена задача с id = " + id);

        taskManager.deleteById(id);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] splitPath = path.split("/");
        String response = "";
        InputStream request;

        try {
            if (!splitPath[1].equals("tasks"))
                throw new Exception("Неверный формат запроса");

            switch (method) {
                case "GET":
                    if (splitPath.length == 2) {
                        response = handleGetTasks();
                        sendText(exchange, response);
                    } else if (splitPath.length == 3) {
                        int id = Integer.parseInt(splitPath[2]);

                        response = handleGetTaskId(id);
                        sendText(exchange, response);
                    } else
                        throw new Exception("Неверный формат запроса");
                    break;
                case "POST":
                    if (splitPath.length != 2)
                        throw new Exception("Неверный формат запроса");
                    else {
                        request = exchange.getRequestBody();
                        String body = new String(request.readAllBytes(), StandardCharsets.UTF_8);

                        response = handlePostTaskId(body);
                        super.sendCreated(exchange, response);
                    }
                    break;
                case "DELETE":
                    if (splitPath.length != 3)
                        throw new Exception("Неверный формат запроса");
                    else {
                        int id = Integer.parseInt(splitPath[2]);

                        handleDeleteTaskId(id);

                        super.sendText(exchange, "Задача удалена");
                    }
                    break;
                default:
                    super.sendNotFound(exchange, "Неверный формат запроса");
            }
        } catch (NumberFormatException e) {
            super.sendNotFound(exchange, "Ошибка преобразования числа");
        } catch (ManagerSaveException e) {
            super.sendHasInteractions(exchange, e.getMessage());
        } catch (Exception e) {
            super.sendNotFound(exchange, e.getMessage());
        }
    }
}
