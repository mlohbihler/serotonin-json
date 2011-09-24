package com.serotonin.json.convert;

import java.io.IOException;
import java.lang.reflect.Type;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonWriter;
import com.serotonin.json.type.JsonValue;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.web.i18n.LocalizableMessageParseException;

/**
 * Implementation of a LocalizableMessage converter. See I18N in the Serotonin Utils package for more information.
 * 
 * @author Matthew Lohbihler
 */
public class LocalizableMessageJsonConverter extends ImmutableClassConverter {
    public void jsonWrite(JsonWriter writer, Object value) throws IOException {
        writer.quote(((LocalizableMessage) value).serialize());
    }

    @Override
    public Object jsonRead(JsonReader reader, JsonValue jsonValue, Type type) throws JsonException {
        LocalizableMessage m = null;

        String s = jsonValue.toJsonString().getValue();
        try {
            m = LocalizableMessage.deserialize(s);
        }
        catch (LocalizableMessageParseException e) {
            // Wrap in json exception and rethrow
            throw new JsonException("Failed to deserialize error message '" + m + "'", e);
        }

        return m;
    }
}
