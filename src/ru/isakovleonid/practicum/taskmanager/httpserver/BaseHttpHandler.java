package ru.isakovleonid.practicum.taskmanager.httpserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.isakovleonid.practicum.taskmanager.taskmanager.TaskManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

import static java.net.HttpURLConnection.*;

public class BaseHttpHandler {
    private final Charset charset = StandardCharsets.UTF_8;
    TaskManager taskManager;
    Gson gson;

    public BaseHttpHandler(TaskManager taskManager) {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        this.taskManager = taskManager;
    }

    public Gson getGson() {
        return gson;
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(charset);

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=" + charset.name());
        exchange.sendResponseHeaders(HTTP_OK, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendCreated(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(charset);

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=" + charset.name());
        exchange.sendResponseHeaders(HTTP_CREATED, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(charset);

        exchange.getResponseHeaders().add("Content-Type", "application/jso;charset=" + charset.name());
        exchange.sendResponseHeaders(HTTP_NOT_FOUND, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(charset);

        exchange.getResponseHeaders().add("Content-Type", "application/jso;charset=" + charset.name());
        exchange.sendResponseHeaders(HTTP_NOT_ACCEPTABLE, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }
}
