package com.serotonin.json.convert;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonWriter;
import com.serotonin.json.ObjectWriter;
import com.serotonin.json.type.JsonObject;
import com.serotonin.json.type.JsonValue;
import com.serotonin.json.util.TypeUtils;

/**
 * Default implementation of a Map converter
 * 
 * @author Matthew Lohbihler
 */
public class MapConverter extends AbstractClassConverter {
    @Override
    public void jsonWrite(JsonWriter writer, Object value) throws IOException, JsonException {
        Map<?, ?> map;
        if (value instanceof JsonObject)
            map = ((JsonObject) value).getProperties();
        else
            map = (Map<?, ?>) value;

        ObjectWriter objectWriter = new ObjectWriter(writer);
        for (Map.Entry<?, ?> entry : map.entrySet())
            objectWriter.writeEntry(entry.getKey().toString(), entry.getValue());
        objectWriter.finish();
    }

    @Override
    public void jsonRead(JsonReader reader, JsonValue jsonValue, Object obj, Type type) throws JsonException {
        JsonObject jsonObject = jsonValue.toJsonObject();

        @SuppressWarnings("unchecked")
        Map<Object, Object> map = (Map<Object, Object>) obj;

        Type valueType = TypeUtils.getActualTypeArgument(type, 1);

        for (Map.Entry<String, JsonValue> entry : jsonObject.getProperties().entrySet())
            map.put(entry.getKey(), reader.read(valueType, entry.getValue()));
    }
}
