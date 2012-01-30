package com.serotonin.json.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;

import com.serotonin.NotImplementedException;
import com.serotonin.io.StreamUtils;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonWriter;
import com.serotonin.json.spi.ClassConverter;
import com.serotonin.json.type.JsonValue;

/**
 * 
 * 
 * @author Matthew Lohbihler
 */
public class ThrowableSerializingConverter implements ClassConverter {
    @Override
    public void jsonWrite(JsonWriter writer, Object value) throws IOException {
        Throwable t = (Throwable) value;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(t);
        oos.flush();
        String hex = StreamUtils.toHex(baos.toByteArray());
        writer.quote(hex);
    }

    @Override
    public Object jsonRead(JsonReader reader, JsonValue jsonValue, Type type) throws JsonException {
        String hex = jsonValue.toJsonString().getValue();
        byte[] bs = StreamUtils.fromHex(hex);
        ByteArrayInputStream bais = new ByteArrayInputStream(bs);
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e) {
            throw new JsonException(e);
        }
    }

    @Override
    public void jsonRead(JsonReader reader, JsonValue jsonValue, Object obj, Type type) {
        throw new NotImplementedException();
    }
}
//{
//"stackTrace":[
//{
//  "className":"test.ExceptionTest",
//  "fileName":"ExceptionTest.java",
//  "lineNumber":29,
//  "methodName":"createException3",
//  "nativeMethod":false
//},
//{
//  "className":"test.ExceptionTest",
//  "fileName":"ExceptionTest.java",
//  "lineNumber":25,
//  "methodName":"createException2",
//  "nativeMethod":false
//},
//{
//  "className":"test.ExceptionTest",
//  "fileName":"ExceptionTest.java",
//  "lineNumber":21,
//  "methodName":"createException1",
//  "nativeMethod":false
//},
//{
//  "className":"test.ExceptionTest",
//  "fileName":"ExceptionTest.java",
//  "lineNumber":16,
//  "methodName":"main",
//  "nativeMethod":false
//}
//]
//}
