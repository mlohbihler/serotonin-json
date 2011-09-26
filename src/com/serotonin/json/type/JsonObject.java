package com.serotonin.json.type;

import java.util.HashMap;
import java.util.Map;

import com.serotonin.json.JsonException;

/**
 * Extends JsonValue to represent an object.
 * 
 * @author Matthew Lohbihler
 */
public class JsonObject extends JsonValue {
    private final Map<String, JsonValue> properties = new HashMap<String, JsonValue>();

    public JsonObject() {
        // no op
    }

    public JsonObject(JsonTypeReader reader) throws JsonException {
        reader.validateNextChar('{');
        while (!reader.testNextChar('}', true)) {
            String name = reader.readString(reader.nextElement());
            reader.validateNextChar(':');
            properties.put(name, reader.read());
            reader.discardOptionalComma();
        }
        reader.nextChar(true);
    }

    @Override
    public String toString() {
        return properties.toString();
    }

    public boolean hasProperty(String name) {
        return properties.containsKey(name);
    }

    public JsonValue getValue(String name) {
        return properties.get(name);
    }

    public void setValue(String name, JsonValue value) {
        properties.put(name, value);
    }

    public JsonValue removeValue(String name) {
        return properties.remove(name);
    }

    public Map<String, JsonValue> getProperties() {
        return properties;
    }

    public boolean isNull(String name) {
        JsonValue value = properties.get(name);
        return value == null || value.isNull();
    }

    public Boolean getBoolean(String name) {
        JsonValue value = properties.get(name);
        if (value == null || value.isNull())
            return null;
        return ((JsonBoolean) value).getValue();
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        Boolean v = getBoolean(name);
        if (v == null)
            return defaultValue;
        return v;
    }

    public JsonObject getJsonObject(String name) {
        JsonValue value = properties.get(name);
        if (value == null || value.isNull())
            return null;
        return (JsonObject) value;
    }

    public JsonArray getJsonArray(String name) {
        JsonValue value = properties.get(name);
        if (value == null || value.isNull())
            return null;
        return (JsonArray) value;
    }

    public String getString(String name) {
        JsonValue value = properties.get(name);
        if (value == null || value.isNull())
            return null;
        return ((JsonString) value).getValue();
    }

    public Long getLong(String name) {
        JsonValue value = properties.get(name);
        if (value == null || value.isNull())
            return null;
        return ((JsonNumber) value).getLongValue();
    }

    public long getLong(String name, long defaultValue) {
        Long v = getLong(name);
        if (v == null)
            return defaultValue;
        return v;
    }

    public Double getDouble(String name) {
        JsonValue value = properties.get(name);
        if (value == null || value.isNull())
            return null;
        return ((JsonNumber) value).getDoubleValue();
    }

    public double getDouble(String name, double defaultValue) {
        Double v = getDouble(name);
        if (v == null)
            return defaultValue;
        return v;
    }

    public Float getFloat(String name) {
        JsonValue value = properties.get(name);
        if (value == null || value.isNull())
            return null;
        return ((JsonNumber) value).getFloatValue();
    }

    public float getFloat(String name, float defaultValue) {
        Float v = getFloat(name);
        if (v == null)
            return defaultValue;
        return v;
    }

    public Integer getInt(String name) {
        JsonValue value = properties.get(name);
        if (value == null || value.isNull())
            return null;
        return ((JsonNumber) value).getIntValue();
    }

    public int getInt(String name, int defaultValue) {
        Integer v = getInt(name);
        if (v == null)
            return defaultValue;
        return v;
    }
}
