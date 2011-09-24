package com.serotonin.json;

import java.io.IOException;

/**
 * A convenience class for easily writing properties of an object. Proper usage of this class is important!
 * 
 * When constructed, the ObjectWriter will begin the writing of a JSON object. Every instance must be closed using the
 * finish() method or the resulting JSON will be malformed. An arbitrary number of object properties can be written with
 * the writeEntry() method.
 * 
 * @author Matthew Lohbihler
 */
public class ObjectWriter {
    private final JsonWriter writer;
    private boolean first = true;

    /**
     * Constructs an ObjectWriter and starts the object serialization by writing a '{' to the JsonWriter.
     * 
     * @param writer
     * @throws IOException
     */
    public ObjectWriter(JsonWriter writer) throws IOException {
        this.writer = writer;

        writer.append('{');
        writer.increaseIndent();
    }

    /**
     * Writes a single object to the JsonWriter
     * 
     * @param name
     *            the object attribute name
     * @param value
     *            the object to write. This will be serialized like any other object.
     * 
     * @throws IOException
     * @throws JsonException
     */
    public void writeEntry(String name, Object value) throws IOException, JsonException {
        if (first)
            first = false;
        else
            writer.append(',');
        writer.indent();
        writer.quote(name);
        writer.append(':');
        writer.writeObject(value);
    }

    /**
     * Closes the object by writing a '}' to the JsonWriter. Remember to always call this method for every ObjectWriter
     * instance.
     * 
     * @throws IOException
     */
    public void finish() throws IOException {
        writer.decreaseIndent();
        writer.indent();
        writer.append('}');
    }

    /**
     * Access the underlying JsonWriter object. This method is for advanced usage only. For most purposes the writeEntry
     * method is sufficient.
     * 
     * @return the underlying JsonWriter object
     */
    public JsonWriter getWriter() {
        return writer;
    }
}
