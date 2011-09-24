package com.serotonin.json.convert;

import java.io.IOException;
import java.lang.reflect.Type;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonWriter;
import com.serotonin.json.spi.ClassConverter;
import com.serotonin.json.type.JsonBoolean;
import com.serotonin.json.type.JsonValue;

/**
 * Default implementation of a JsonBoolean converter
 * 
 * @author Matthew Lohbihler
 */
public class JsonBooleanConverter implements ClassConverter {
    @Override
    public void jsonWrite(JsonWriter writer, Object value) throws IOException {
        writer.append(((JsonBoolean) value).toString());
    }

    @Override
    public Object jsonRead(JsonReader reader, JsonValue jsonValue, Type type) {
        return jsonValue;
    }

    @Override
    public void jsonRead(JsonReader reader, JsonValue jsonValue, Object obj, Type type) throws JsonException {
        ((JsonBoolean) obj).setValue(jsonValue.toJsonBoolean().getValue());
    }
}
