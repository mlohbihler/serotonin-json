package com.serotonin.json.convert;

import java.io.IOException;
import java.lang.reflect.Type;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonWriter;
import com.serotonin.json.type.JsonValue;

/**
 * Default implementation of a BigDecimal converter
 * 
 * @author Matthew Lohbihler
 */
public class BigDecimalConverter extends ImmutableClassConverter {
    @Override
    public void jsonWrite(JsonWriter writer, Object value) throws IOException {
        writer.append(value.toString());
    }

    @Override
    public Object jsonRead(JsonReader reader, JsonValue jsonValue, Type type) throws JsonException {
        return jsonValue.toJsonNumber().getBigDecimal();
    }
}
