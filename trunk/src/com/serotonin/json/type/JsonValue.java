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

    public JsonArray toJsonArray() throws JsonException {
        if (this instanceof JsonArray)
            return (JsonArray) this;
        throw new JsonException("Cannot cast " + getClass() + " to JsonArray");
    }

    public JsonBoolean toJsonBoolean() throws JsonException {
        if (this instanceof JsonBoolean)
            return (JsonBoolean) this;
        throw new JsonException("Cannot cast " + getClass() + " to JsonBoolean");
    }

    public JsonNull toJsonNull() throws JsonException {
        if (this instanceof JsonNull)
            return (JsonNull) this;
        throw new JsonException("Cannot cast " + getClass() + " to JsonNull");
    }

    public JsonNumber toJsonNumber() throws JsonException {
        if (this instanceof JsonNumber)
            return (JsonNumber) this;
        throw new JsonException("Cannot cast " + getClass() + " to JsonNumber");
    }

    public JsonObject toJsonObject() throws JsonException {
        if (this instanceof JsonObject)
            return (JsonObject) this;
        throw new JsonException("Cannot cast " + getClass() + " to JsonObject");
    }

    public JsonString toJsonString() throws JsonException {
        if (this instanceof JsonString)
            return (JsonString) this;
        throw new JsonException("Cannot cast " + getClass() + " to JsonString");
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