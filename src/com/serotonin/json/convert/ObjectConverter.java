package com.serotonin.json.convert;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonWriter;
import com.serotonin.json.type.JsonArray;
import com.serotonin.json.type.JsonBoolean;
import com.serotonin.json.type.JsonNull;
import com.serotonin.json.type.JsonNumber;
import com.serotonin.json.type.JsonObject;
import com.serotonin.json.type.JsonString;
import com.serotonin.json.type.JsonValue;

public class ObjectConverter extends ImmutableClassConverter {
    @Override
    public void jsonWrite(JsonWriter writer, Object object) throws IOException {
        throw new RuntimeException("Cannot write object of class " + object.getClass());
    }

    @Override
    public Object jsonRead(JsonReader reader, JsonValue jsonValue, Type type) throws JsonException {
        if (jsonValue instanceof JsonArray) {
            List<JsonValue> values = ((JsonArray) jsonValue).getElements();
            List<Object> converted = new ArrayList<Object>(values.size());

            for (JsonValue value : values)
                converted.add(reader.read(Object.class, value));

            return converted;
        }

        if (jsonValue instanceof JsonBoolean)
            return jsonValue.toJsonBoolean().getValue();

        if (jsonValue instanceof JsonNull)
            return null;

        if (jsonValue instanceof JsonNumber) {
            try {
                return jsonValue.toJsonNumber().getIntValue();
            }
            catch (NumberFormatException e) {
                try {
                    return jsonValue.toJsonNumber().getLongValue();
                }
                catch (NumberFormatException e1) {
                    try {
                        return jsonValue.toJsonNumber().getBigInteger();
                    }
                    catch (NumberFormatException e2) {
                        try {
                            return jsonValue.toJsonNumber().getDoubleValue();
                        }
                        catch (NumberFormatException e3) {
                            return jsonValue.toJsonNumber().getBigDecimal();
                        }
                    }
                }
            }
        }

        if (jsonValue instanceof JsonObject) {
            Map<String, JsonValue> values = jsonValue.toJsonObject().getProperties();
            Map<String, Object> converted = new HashMap<String, Object>(values.size());

            for (Map.Entry<String, JsonValue> entry : values.entrySet())
                converted.put(entry.getKey(), reader.read(Object.class, entry.getValue()));

            return converted;
        }

        if (jsonValue instanceof JsonString)
            return jsonValue.toJsonString().getValue();

        throw new RuntimeException("Unknown ");
    }
}
