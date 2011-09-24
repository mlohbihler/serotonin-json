package com.serotonin.json.type;

/**
 * Extends JsonValue to represent null.
 * 
 * @author Matthew Lohbihler
 */
public class JsonNull extends JsonValue {
    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public JsonArray toJsonArray() {
        return null;
    }

    @Override
    public JsonBoolean toJsonBoolean() {
        return null;
    }

    @Override
    public JsonNumber toJsonNumber() {
        return null;
    }

    @Override
    public JsonObject toJsonObject() {
        return null;
    }

    @Override
    public JsonString toJsonString() {
        return null;
    }
}
