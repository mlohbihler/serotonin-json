package com.serotonin.json.convert;

import java.io.IOException;
import java.lang.reflect.Type;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonWriter;
import com.serotonin.json.ObjectWriter;
import com.serotonin.json.spi.ClassConverter;
import com.serotonin.json.spi.ClassSerializer;
import com.serotonin.json.type.JsonValue;

/**
 * This interface can be used to provide a custom serialization for a class that the developer has no opportunity or
 * desire to explicitly change.
 * 
 * @author Matthew Lohbiher
 */
public class SerializerConverter<T> implements ClassConverter {
    private final ClassSerializer<T> serializer;

    public SerializerConverter(ClassSerializer<T> serializer) {
        this.serializer = serializer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void jsonWrite(JsonWriter writer, Object value) throws IOException, JsonException {
        ObjectWriter objectWriter = new ObjectWriter(writer);
        serializer.jsonWrite(objectWriter, (T) value);
        objectWriter.finish();
    }

    @Override
    public Object jsonRead(JsonReader reader, JsonValue jsonValue, Type type) throws JsonException {
        return serializer.jsonRead(reader, jsonValue.toJsonObject());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void jsonRead(JsonReader reader, JsonValue jsonValue, Object obj, Type type) throws JsonException {
        serializer.jsonRead(reader, jsonValue.toJsonObject(), (T) obj);
    }
}
