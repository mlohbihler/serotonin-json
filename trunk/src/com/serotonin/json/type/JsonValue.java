package com.serotonin.json.type;

import com.serotonin.json.JsonException;

/**
 * The base class of all native JSON types.
 * 
 * @author Matthew Lohbihler
 */
abstract public class JsonValue {
    public boolean isNull() {
        return false;
    }

    public JsonArray toJsonArray() {
        return (JsonArray) this;
    }

    public JsonBoolean toJsonBoolean() {
        return (JsonBoolean) this;
    }

    public JsonNull toJsonNull() {
        return (JsonNull) this;
    }

    public JsonNumber toJsonNumber() {
        return (JsonNumber) this;
    }

    public JsonObject toJsonObject() {
        return (JsonObject) this;
    }

    public JsonString toJsonString() {
        return (JsonString) this;
    }

    public JsonValue getJsonValue(String... path) throws JsonException {
        if (path.length == 0)
            return this;
        return getJsonValue(this, path, 0);
    }

    private JsonValue getJsonValue(JsonValue value, String[] path, int index) throws JsonException {
        if (value instanceof JsonObject) {
            JsonObject o = (JsonObject) value;
            value = o.getValue(path[index]);
        }
        else if (value instanceof JsonArray) {
            int arrIndex;
            try {
                arrIndex = Integer.parseInt(path[index]);
            }
            catch (NumberFormatException e) {
                throw new JsonException("Could not convert path element '" + path[index] + " to integer for JsonArray");
            }

            JsonArray a = (JsonArray) value;
            value = a.getElements().get(arrIndex);
        }
        else
            return null;

        if (value == null || index == path.length - 1)
            return value;

        return getJsonValue(value, path, index + 1);
    }
}
