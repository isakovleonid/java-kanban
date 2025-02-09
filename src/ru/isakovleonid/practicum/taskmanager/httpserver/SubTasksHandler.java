package ru.isakovleonid.practicum.taskmanager.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.isakovleonid.practicum.taskmanager.taskmanager.TaskManager;
import ru.isakovleonid.practicum.taskmanager.tasks.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubTasksHandler extends BaseHttpHandler implements HttpHandler {
    public SubTasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    private String handleGetSubTasks() {
        List<SubTask> subTasks = this.taskManager.getSubTasks();

        return gson.toJson(subTasks);
    }

    private String handleGetSubTaskId(int id) throws ClassNotFoundException {
        SubTask subTask = this.taskManager.getSubTask(id);

        if (subTask == null)
            throw new ClassNotFoundException("Не найдена подзадача с id = " + id);

        return gson.toJson(subTask);
    }

    public String handlePostSubTaskId(String body) throws ClassNotFoundException {
        SubTask subTask = gson.fromJson(body, SubTask.class);

        if (subTask == null)
            throw new ClassNotFoundException("Не удалось создать подзадачу");

        taskManager.addSubTask(subTask);

        return gson.toJson(subTask);
    }

    public void handleDeleteSubTaskId(int id) throws ClassNotFoundException {
        SubTask subTask = this.taskManager.getSubTask(id);

        if (subTask == null)
            throw new ClassNotFoundException("Не найдена подзадача с id = " + id);

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
            if (!splitPath[1].equals("subtasks"))
                throw new Exception("Неверный формат запроса");

            switch (method) {
                case "GET":
                    if (splitPath.length == 2) {
                        response = handleGetSubTasks();
                        sendText(exchange, response);
                    } else if (splitPath.length == 3) {
                        int id = Integer.parseInt(splitPath[2]);

                        response = handleGetSubTaskId(id);
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

                        response = handlePostSubTaskId(body);
                        super.sendCreated(exchange, response);
                    }
                    break;
                case "DELETE":
                    if (splitPath.length != 3)
                        throw new Exception("Неверный формат запроса");
                    else {
                        int id = Integer.parseInt(splitPath[2]);

                        handleDeleteSubTaskId(id);

                        super.sendText(exchange, "Задача удалена");
                    }
                    break;
                default:
                    super.sendNotFound(exchange, "Неверный формат запроса");
            }
        } catch (NumberFormatException e) {
            super.sendNotFound(exchange, "Ошибка преобразования числа");
        } catch (Exception e) {
            super.sendNotFound(exchange, e.getMessage());
        }
    }
}
