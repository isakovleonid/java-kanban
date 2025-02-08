package ru.isakovleonid.practicum.taskmanager.httpserver;

import com.google.gson.*;
import org.junit.jupiter.api.*;
import ru.isakovleonid.practicum.taskmanager.Managers;
import ru.isakovleonid.practicum.taskmanager.taskmanager.TaskManager;
import ru.isakovleonid.practicum.taskmanager.tasks.Epic;
import ru.isakovleonid.practicum.taskmanager.tasks.SubTask;
import ru.isakovleonid.practicum.taskmanager.tasks.Task;
import ru.isakovleonid.practicum.taskmanager.tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    final int PORT = 8080;
    TaskManager tm = Managers.getDefault();

    HttpTaskServer httpTaskServer;

    @BeforeEach
    void setUp() {
        httpTaskServer = new HttpTaskServer(PORT, tm);
        tm.deleteAll();
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
        httpTaskServer.start();
    }

    @AfterEach
    void tearDown() {
        httpTaskServer.stop();
    }

    @Test
    void testGetTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse response;

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks/1"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Для существующей задачи вернулся ошибочный статус");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks/100"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Для несуществующей задачи вернулся ошибочный статус");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasksInvalidURI/"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Для необрабатываемого URI вернулся неверный статус");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks/s34"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Для некорректного с точки зрения типа значений в uri вернулся неверный статус");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks/"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Для запроса всех задач вернулся неверный статус");
    }

    @Test
    void testGetSubTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse response;

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/subtasks/5"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Для существующей подзадачи вернулся ошибочный статус");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/subtasks/100"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Для несуществующей подзадачи вернулся ошибочный статус");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/subtasksInvalidURI/"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Для необрабатываемого URI вернулся неверный статус");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/subtasks/s34"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Для некорректного с точки зрения типа значений в uri вернулся неверный статус");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/subtasks/"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Для запроса всех подзадач вернулся неверный статус");
    }

    @Test
    void testGetEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse response;

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/epics/3"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Для существующего эпика вернулся ошибочный статус");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/epics/100"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Для несуществующего эпика вернулся ошибочный статус");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/epicsInvalidURI/"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Для необрабатываемого URI вернулся неверный статус");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/epics/s34"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Для некорректного с точки зрения типа значений в uri вернулся неверный статус");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/epics/"))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "Для запроса всех подзадач вернулся неверный статус");
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse<String> response;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();

        Task task = new Task("задача на добавление", "описание задачи на добавление", LocalDateTime.of(2025,2,1,15,45), Duration.ofMinutes(20));
        String requestBody = gson.toJson(task);

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Для корректного формата собщения не удалось создать задачу");

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject(), "В результате создания не вернулся JSON");

        int taskAddedId = jsonElement.getAsJsonObject().get("id").getAsInt();
        Task taskAdded = tm.getTask(taskAddedId);
        assertNotNull(taskAdded, "Не добавлена новая задача");


        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasksInvalidURI/"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Для необрабатываемого URI вернулся ошибочный статус");

    }

    @Test
    void testSubTaskAdd() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse<String> response;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();

        SubTask task = new SubTask("подзадача на добавление", "описание подзадачи на добавление", 3, LocalDateTime.of(2025,2,1,15,45), Duration.ofMinutes(20));
        String requestBody = gson.toJson(task);

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Для корректного формата собщения не удалось создать подзадачу");

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject(), "В результате создания не вернулся JSON");

        int subTaskAddedId = jsonElement.getAsJsonObject().get("id").getAsInt();
        SubTask subTaskAdded = tm.getSubTask(subTaskAddedId);
        assertNotNull(subTaskAdded, "Не добавлена новая подзадача");


        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/subtasksInvalidURI/"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Для необрабатываемого URI вернулся ошибочный статус");

    }

    @Test
    void testEpicAdd() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse<String> response;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();

        Epic epic = new Epic("эпик на добавление", "описание эпика на добавление");
        String requestBody = gson.toJson(epic);

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Для корректного формата собщения не удалось создать эпик");

        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject(), "В результате создания эпика не вернулся JSON");

        int epicAddedId = jsonElement.getAsJsonObject().get("id").getAsInt();
        Epic epicAdded = tm.getEpic(epicAddedId);
        assertNotNull(epicAdded, "Не добавлен новый эпик");


        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/epicsInvalidURI/"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Для необрабатываемого URI вернулся ошибочный статус");

    }

    @Test
    void testDeleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse<String> response;

        int taskId = 1;

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks/" + taskId))
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Некорреткный статус ответа при удалении существующей задачи");

        Task task = tm.getTask(taskId);

        assertNull(task,"Не удалось удалить существующую задачу");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks/100"))
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Некорректный статус запроса при поытке удаления несуществующей задачи");
    }

    @Test
    void testDeleteSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse<String> response;

        int subTaskId = 5;

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/subtasks/" + subTaskId))
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Некорреткный статус ответа при удалении существующей подзадачи");

        SubTask subTask = tm.getSubTask(subTaskId);

        assertNull(subTask,"Не удалось удалить существующую задачу");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/subtasks/100"))
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Некорректный статус запроса при поытке удаления несуществующей подзадачи");
    }

    @Test
    void testDeleteEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        HttpResponse<String> response;

        int epicId = 3;

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/epics/" + epicId))
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Некорреткный статус ответа при удалении существующего эпика");

        Epic epic = tm.getEpic(epicId);

        assertNull(epic,"Не удалось удалить существующую задачу");

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/epics/100"))
                .DELETE()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Некорректный статус запроса при поытке удаления несуществующей подзадачи");
    }
}

