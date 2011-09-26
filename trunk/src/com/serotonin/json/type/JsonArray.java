package com.serotonin.json.type;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.json.JsonException;

/**
 * Extends JsonValue to represent an array.
 * 
 * @author Matthew Lohbihler
 */
public class JsonArray extends JsonValue {
    private final List<JsonValue> elements = new ArrayList<JsonValue>();

    public JsonArray() {
        // no op
    }

    public JsonArray(JsonTypeReader reader) throws JsonException {
        reader.validateNextChar('[');
        while (!reader.testNextChar(']', true)) {
            elements.add(reader.read());
            reader.discardOptionalComma();
        }
        reader.nextChar(true);
    }

    public void add(JsonValue value) {
        elements.add(value);
    }

    public List<JsonValue> getElements() {
        return elements;
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
