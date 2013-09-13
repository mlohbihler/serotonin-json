package com.serotonin.json.type;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonParseException;
import com.serotonin.json.util.MaxCharacterCountExceededException;
import com.serotonin.json.util.ParsePositionTracker;

/**
 * A reader that converts JSON into a type graph. This class can be used independently, but is typically used in a
 * JsonReader to do the initial document parsing. Can be used to read zero to many JSON documents from a single source.
 * 
 * @author Matthew Lohbihler
 */
public class JsonTypeReader {
    private final Reader reader;
    private final ParsePositionTracker tracker;
    private final int maxCharacterCount;

    /**
     * Convert a string of JSON data into a type graph.
     * 
     * @param data
     *            the JSON data
     */
    public JsonTypeReader(String data) {
        this(new StringReader(data), -1);
    }

    /**
     * Convert the data in an I/O reader into a type graph.
     * 
     * @param reader
     */
    public JsonTypeReader(Reader reader) {
        this(reader, -1);
    }

    /**
     * Convert the data in an I/O reader into a type graph.
     * 
     * @param reader
     */
    public JsonTypeReader(Reader reader, int maxCharacterCount) {
        if (!reader.markSupported())
            reader = new BufferedReader(reader);
        this.reader = reader;

        tracker = new ParsePositionTracker();
        this.maxCharacterCount = maxCharacterCount;
    }

    /**
     * Read the next JsonValue from the input source.
     * 
     * @return the JsonValue that was read. Will not be null (but could be JsonNull).
     * @throws JsonException
     */
    public JsonValue read() throws JsonException, IOException {
        if (testNextChar('{', true))
            return readObject();
        if (testNextChar('[', true))
            return readArray();

        String element = nextElement();
        if (element.startsWith("\""))
            return new JsonString(readString(element));
        if ("null".equals(element))
            return null;
        if ("true".equals(element))
            return new JsonBoolean(true);
        if ("false".equals(element))
            return new JsonBoolean(false);
        try {
            return new JsonNumber(new BigDecimal(element));
        }
        catch (NumberFormatException e) {
            throw new JsonParseException("Value is not null, true, false, or a number", tracker, true);
        }
    }

    private String nextChars(int length) throws JsonException, IOException {
        StringBuilder sb = new StringBuilder();
        while (length-- > 0)
            sb.append(nextChar(true));
        return sb.toString();
    }

    char nextChar(boolean throwOnEos) throws JsonException, IOException {
        char c = readChar();
        if (c == 0xFFFF && throwOnEos)
            throw new JsonParseException("EOS", tracker, false);
        return c;
    }

    boolean testNextChar(char c, boolean throwOnEos) throws JsonException, IOException {
        skipWhitespace(throwOnEos);
        mark(1);
        char n = nextChar(throwOnEos);
        boolean result = n == c;
        reset();
        return result;
    }

    /**
     * Determines if the input source is at the end of the stream or not. Can be used in a loop where there may be
     * multiple JSON documents in a single input source.
     * 
     * @return true if the end of stream has been reached.
     * @throws JsonException
     */
    public boolean isEos() throws JsonException, IOException {
        try {
            return testNextChar((char) 0xFFFF, false);
        }
        catch (IOException e) {
            if ("Stream closed".equals(e.getMessage()))
                return true;
            throw e;
        }
    }

    private void skipWhitespace(boolean throwOnEos) throws JsonException, IOException {
        while (true) {
            mark(2);
            char c = nextChar(throwOnEos);

            if (c == 0xFFFF && !throwOnEos)
                return;
            else if (Character.isWhitespace(c))
                ; // ignore
            else if (c == '/') {
                // Check if this is a comment.
                c = nextChar(true);
                if (c == '*') {
                    // Found a block comment. Look for the terminator.
                    while (true) {
                        c = nextChar(true);
                        if (c == '*') {
                            mark(1);
                            c = nextChar(true);
                            if (c == '/')
                                // Found the terminator
                                break;
                            else if (c == 0xFFFF)
                                throw new JsonParseException("Comment terminator not found", tracker, false);
                            else
                                reset();
                        }
                    }
                }
                else if (c == '/') {
                    // Found a line comment. Continue until the end of the line
                    while (true) {
                        c = nextChar(false);
                        if (c == 0xA || c == 0xFFFF)
                            // End of the line
                            break;
                    }
                }
                else {
                    reset();
                    break;
                }
            }
            else {
                reset();
                break;
            }

            checkCharacterCount();
        }
    }

    String nextElement() throws JsonException, IOException {
        StringBuilder sb = new StringBuilder();

        discardOptionalComma();
        skipWhitespace(false);

        tracker.setElementStart();

        char c = nextChar(true);
        sb.append(c);

        if (c == '"') {
            // Read until the next quote
            boolean done = false;
            while (!done) {
                c = nextChar(true);
                switch (c) {
                case '\\':
                    c = nextChar(true);
                    switch (c) {
                    case 'b':
                        sb.append('\b');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 'u':
                        sb.append((char) Integer.parseInt(nextChars(4), 16));
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case '"':
                        sb.append('"');
                        break;
                    case '/':
                        sb.append('/');
                        break;
                    default:
                        throw new JsonParseException("Unrecognized escape character: " + c, tracker, false);
                    }
                    break;
                default:
                    sb.append(c);
                    if (c == '"')
                        done = true;
                }
                checkCharacterCount();
            }
            skipWhitespace(false);
        }

        boolean done = false;
        while (!done) {
            mark(1);
            c = readChar();
            if (c == 0xFFFF)
                break;

            switch (c) {
            case ',':
            case ']':
            case ':':
            case '}':
            case '"':
            case '/':
                reset();
                done = true;
                break;
            default:
                if (Character.isWhitespace(c))
                    done = true;
                else
                    sb.append(c);
            }

            checkCharacterCount();
        }

        return sb.toString();
    }

    void discardOptionalComma() throws JsonException, IOException {
        skipWhitespace(true);
        mark(1);
        char c = nextChar(true);
        if (c != ',')
            reset();
        else
            checkCharacterCount();
    }

    private void mark(int readAheadLimit) throws IOException {
        reader.mark(readAheadLimit);
        tracker.mark();
    }

    private void reset() throws IOException {
        reader.reset();
        tracker.reset();
    }

    private char readChar() throws IOException {
        char c = (char) reader.read();
        tracker.update(c);
        return c;
    }

    String readString(String element) throws JsonException {
        if (element.charAt(0) != '"' && element.charAt(element.length() - 1) != '"')
            throw new JsonParseException("element is not a string: " + element, tracker, true);
        return element.substring(1, element.length() - 1);
    }

    void validateNextChar(char c) throws JsonException, IOException {
        skipWhitespace(true);
        char n = nextChar(true);
        if (n != c)
            throw new JsonParseException("incorrect next character: expected '" + c + "', found '" + n + "'", tracker,
                    false);
        checkCharacterCount();
    }

    void checkCharacterCount() throws IOException {
        if (maxCharacterCount != -1 && tracker.getCharacterCount() > maxCharacterCount)
            throw new MaxCharacterCountExceededException();
    }

    //
    // Native readers
    private JsonObject readObject() throws JsonException, IOException {
        JsonObject object = new JsonObject();

        validateNextChar('{');
        while (!testNextChar('}', true)) {
            String name = readString(nextElement());
            validateNextChar(':');
            object.put(name, read());
            discardOptionalComma();
        }
        nextChar(true);

        return object;
    }

    private JsonArray readArray() throws JsonException, IOException {
        JsonArray array = new JsonArray();

        validateNextChar('[');
        while (!testNextChar(']', true)) {
            array.add(read());
            discardOptionalComma();
        }
        nextChar(true);

        return array;
    }
}
