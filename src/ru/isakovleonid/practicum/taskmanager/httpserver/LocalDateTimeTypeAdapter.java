package ru.isakovleonid.practicum.taskmanager.httpserver;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.isakovleonid.practicum.taskmanager.tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
    DateTimeFormatter dateTimeFormatter = Task.TASK_DATE_TIME;

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        String text = jsonReader.nextString();

        if (text.equals(""))
            return null;
        else
            return LocalDateTime.parse(text, dateTimeFormatter);
    }

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime o) throws IOException {
        if (o == null)
            jsonWriter.value("");
        else
            jsonWriter.value(o.format(dateTimeFormatter));
    }
}
