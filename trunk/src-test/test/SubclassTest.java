package test;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.json.JsonContext;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonWriter;
import com.serotonin.json.spi.ObjectFactory;
import com.serotonin.json.type.JsonValue;
import com.serotonin.json.util.TypeDefinition;

public class SubclassTest {
    public static void main(String[] args) throws Exception {
        JsonContext context = new JsonContext();
        context.addFactory(new ObjectFactory() {
            @Override
            public Object create(JsonValue jsonValue) throws JsonException {
                if (jsonValue.toJsonObject().hasProperty("sub1Value"))
                    return new Subclass1();
                if (jsonValue.toJsonObject().hasProperty("sub2Value"))
                    return new Subclass2();
                throw new JsonException("Unknown BaseClass: " + jsonValue);
            }
        }, BaseClass.class);

        List<BaseClass> list = new ArrayList<BaseClass>();
        list.add(new Subclass1());
        list.add(new Subclass2());

        String json = JsonWriter.writeToString(context, list);

        System.out.println(json);

        JsonReader reader = new JsonReader(context, json);
        TypeDefinition type = new TypeDefinition(List.class, BaseClass.class);
        Object read = reader.read(type);

        System.out.println(read);
    }
}
