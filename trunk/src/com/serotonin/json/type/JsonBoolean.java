package com.serotonin.json.type;

/**
 * Extends JsonValue to represent a boolean.
 * 
 * @author Matthew Lohbihler
 */
public class JsonBoolean extends JsonValue {
    private boolean value;

    public JsonBoolean(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}
