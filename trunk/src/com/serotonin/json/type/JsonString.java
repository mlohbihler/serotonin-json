package com.serotonin.json.type;

import com.serotonin.json.JsonException;

/**
 * Extends JsonValue to represent a string.
 * 
 * @author Matthew Lohbihler
 */
public class JsonString extends JsonValue {
    private String value;

    public JsonString(String value) {
        this.value = value;
    }

    public JsonString(JsonTypeReader reader, String element) throws JsonException {
        this.value = reader.readString(element);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
