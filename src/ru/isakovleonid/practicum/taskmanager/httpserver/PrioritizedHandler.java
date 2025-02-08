package ru.isakovleonid.practicum.taskmanager.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.isakovleonid.practicum.taskmanager.taskmanager.TaskManager;
import ru.isakovleonid.practicum.taskmanager.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    private String handleGetPrioritized() {
        List<Task> tasks = this.taskManager.getPrioritizedTasks();

        return gson.toJson(tasks);
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
            if (!splitPath[1].equals("prioritized"))
                throw new Exception("Неверный формат запроса");

            switch (method) {
                case "GET":
                    if (splitPath.length == 2) {
                        response = handleGetPrioritized();
                        sendText(exchange, response);
                    } else
                        throw new Exception("Неверный формат запроса");
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
