package service;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type,
                                     JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(
                localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
    }
}
