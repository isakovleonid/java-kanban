package ru.isakovleonid.practicum.taskmanager.httpserver;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationTypeAdapter extends TypeAdapter<Duration> {
    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        String text = jsonReader.nextString();

        if (text.equals(""))
            return null;
        else
            return Duration.ofMinutes(Integer.parseInt(text));
    }

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null)
            jsonWriter.value("");
        else
            jsonWriter.value(duration.toMinutes());
    }
}
