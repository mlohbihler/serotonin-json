package com.serotonin.json.convert;

import java.lang.reflect.Type;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.type.JsonArray;
import com.serotonin.json.type.JsonValue;

/**
 * Default implementation of a JsonString converter
 * 
 * @author Matthew Lohbihler
 */
public class JsonArrayConverter extends CollectionConverter {
    @Override
    public Object jsonRead(JsonReader reader, JsonValue jsonValue, Type type) {
        return jsonValue;
    }

    @Override
    public void jsonRead(JsonReader reader, JsonValue jsonValue, Object obj, Type type) throws JsonException {
        JsonArray jsonArray = (JsonArray) obj;
        jsonArray.getElements().clear();
        jsonArray.getElements().addAll(jsonValue.toJsonArray().getElements());
    }
}
