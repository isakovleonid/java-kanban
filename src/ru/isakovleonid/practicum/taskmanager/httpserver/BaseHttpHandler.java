package ru.isakovleonid.practicum.taskmanager.httpserver;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.net.HttpURLConnection.*;

public class BaseHttpHandler {
    private final Charset charset = StandardCharsets.UTF_8;

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(charset);

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=" + charset.name());
        exchange.sendResponseHeaders(HTTP_OK, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendCreated(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(charset);

        exchange.getResponseHeaders().add("Content-Type", "application/json;charset="+ charset.name());
        exchange.sendResponseHeaders(HTTP_CREATED, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(charset);

        exchange.getResponseHeaders().add("Content-Type", "application/jso;charset="+ charset.name());
        exchange.sendResponseHeaders(HTTP_NOT_FOUND, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(charset);

        exchange.getResponseHeaders().add("Content-Type", "application/jso;charset="+ charset.name());
        exchange.sendResponseHeaders(HTTP_NOT_ACCEPTABLE, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }
}
