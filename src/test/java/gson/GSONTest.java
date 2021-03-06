/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import dk.sdu.mmmi.fppt.gsonquickcheck.*;
import dk.sdu.mmmi.fppt.gsonquickcheck.TestObjectGenerator.TestObjectInterface;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Bogs
 */
@RunWith(JUnitQuickcheck.class)
public class GSONTest {

    public static final int trials = 100;

    @AfterClass
    public static void statistics() {
        StatCollector.getInstance().printResults();
        StatCollector.getInstance().printDistribution();
    }

    @Property(trials = trials)
    public void testRandomJsonGenerator(@From(RandomJsonGenerator.class) String json) throws ClassNotFoundException {
        Gson gson = new GsonBuilder().create();
        String className = json.substring(0, json.indexOf("|"));
        String splitJson = json.substring(json.indexOf("|") + 1);
        System.out.println(splitJson);
        Class generatedClass = Class.forName(className);
        assertEquals(StringEscapeUtils.unescapeJava(splitJson), StringEscapeUtils.unescapeJava(gson.toJson(gson.fromJson(splitJson, generatedClass))));
    }

    /**
     * Testing the primitive conversion of an integer
     * To do this we use the JsonPrimitive which is a subtype of JsonElement
     * More information here:
     * https://static.javadoc.io/com.google.code.gson/gson/2.6.2/com/google/gson/JsonPrimitive.html
     *
     * @param primitive
     */
    @Property(trials = trials)
    public void testPrimitiveIntToJSON(int primitive) {
        JsonElement element = new JsonPrimitive(primitive);
        assertEquals(element.getAsInt(), primitive);
    }

    /**
     * Testing the primitive conversion of a Double
     * To do this we use the JsonPrimitive which is a subtype of JsonElement
     * More information here:
     * https://static.javadoc.io/com.google.code.gson/gson/2.6.2/com/google/gson/JsonPrimitive.html
     *
     * @param primitive
     */
    @Property(trials = trials)
    public void testPrimitiveDoubleToJSON(double primitive) {
        JsonElement element = new JsonPrimitive(primitive);
        assertEquals(element.getAsDouble(), primitive, 0.00);
    }

    /**
     * Testing the primitive conversion of a String
     * To do this we use the JsonPrimitive which is a subtype of JsonElement
     * More information here:
     * https://static.javadoc.io/com.google.code.gson/gson/2.6.2/com/google/gson/JsonPrimitive.html
     *
     * @param primitive
     */
    @Property(trials = trials)
    public void testPrimitiveStringToJSON(String primitive) {
        JsonElement element = new JsonPrimitive(primitive);
        assertEquals(primitive, element.getAsString());
    }

    /**
     * Testing conversion of a json primitive to a primitive by
     * first creating a json primitive before converting it back
     * using the gson instance.
     * To ensure the conversion happens correctly we check that the
     * object has been converted to a json primitive before reverting and comparing
     *
     * @param primitive
     */
    @Property(trials = trials)
    public void testJsonToPrimitiveInt(int primitive) {
        JsonElement element = new JsonPrimitive(primitive);
        Gson gson = new Gson();
        assertTrue(element.isJsonPrimitive());
        int backToPrimitive = gson.fromJson(element.toString(), int.class);
        assertEquals(primitive, backToPrimitive);
    }

    /**
     * Testing conversion of a json primitive to a primitive by
     * first creating a json primitive before converting it back
     * using the gson instance.
     * To ensure the conversion happens correctly we check that the
     * object has been converted to a json primitive before reverting and comparing
     *
     * @param primitive
     */
    @Property(trials = trials)
    public void testJsonToPrimitiveDouble(double primitive) {
        JsonElement element = new JsonPrimitive(primitive);
        Gson gson = new Gson();
        assertTrue(element.isJsonPrimitive());
        double backToPrimitive = gson.fromJson(element.toString(), double.class);
        assertEquals(primitive, backToPrimitive, 0.00);
    }

    /**
     * Testing conversion of a json primitive to a primitive by
     * first creating a json primitive before converting it back
     * using the gson instance.
     * To ensure the conversion happens correctly we check that the
     * object has been converted to a json primitive before reverting and comparing
     *
     * @param primitive
     */
    @Property(trials = trials)
    public void testJsonToPrimitiveString(String primitive) {
        JsonElement element = new JsonPrimitive(primitive);
        Gson gson = new Gson();
        assertTrue(element.isJsonPrimitive());
        String backToPrimitive = gson.fromJson(element.toString(), String.class);
        assertEquals(primitive, backToPrimitive);
    }

    @Property(trials = trials)
    public void testTestObjectGenerator(@TestObjectInterface TestObject t1) {
        Gson gson = new Gson();
        String serialized = gson.toJson(t1);
        TestObject deserialized = gson.fromJson(serialized, TestObject.class);
        assertTrue(t1.equals(deserialized));
    }

    @Property(trials = trials)
    public void testJsonToObjectToJson(@From(JSONGenerator.class) String json) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        TestObject obj = gson.fromJson(json, TestObject.class);
        assertEquals(gson.toJson(obj), json);
    }

    @Property(trials = trials)
    public void testObjectToJson(@TestObjectInterface TestObject obj) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        assertEquals(gson.toJson(obj), obj.toJson());
    }

    @Property(trials = trials)
    public void nullFields(@TestObjectInterface TestObject obj) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        obj.setText(null);
        obj.setTexts(null);
        String serialized = gson.toJson(obj);
        System.out.println(serialized);
        TestObject deserialized = gson.fromJson(serialized, TestObject.class);
        assertEquals(obj, deserialized);
    }

    @Property(trials = trials)
    public void nullFieldsInJson(@TestObjectInterface TestObject obj) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String serialized = gson.toJson(obj);
        serialized = serialized.replaceAll(":\".*?\"", ":\"\"");
        obj.setText("");
        TestObject deserialized = gson.fromJson(serialized, TestObject.class);
        assertEquals(obj, deserialized);
    }

    @Property(trials = trials)
    public void nullNull() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String s = gson.toJson(null);
    }

    @Property(trials = trials)
    public void floatingPoint(double aFloat) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        FloatTestObject f = new FloatTestObject(aFloat);
        String json = gson.toJson(f);
        FloatTestObject des = gson.fromJson(json, FloatTestObject.class);
        assertEquals(aFloat, aFloat, 0.0000000000000000000000000000001);
        assertEquals(aFloat, des.getaFloat(), 0.0000000000000000000000000000001);
    }

    @Property(trials = trials)
    public void hexFields(@TestObjectInterface TestObject obj) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        String serialized = gson.toJson(obj);
        TestObject deserialized = gson.fromJson(serialized, TestObject.class);
        assertEquals(obj, deserialized);

        try {
            int number = new Random().nextInt(255);
            String s = "0x" + Integer.toHexString(number);
            obj.setNumber(number);
            serialized = gson.toJson(obj);
            serialized = serialized.replace("" + number, s);
            TestObject testObject = gson.fromJson(serialized, TestObject.class);
            assertEquals(number, testObject.getNumber());

        } catch (Exception e) {
            System.out.println("serialization fail");
            return; //Serialization should fail/throw exception
        }
        throw new AssertionError("hex shouldn't be supported");
    }


    @Property(trials = trials)
    public void testNullSerialization() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        assertEquals("null", gson.toJson(null));
    }


    @Property(trials = trials)
    public void testJsonToObjectNested(@From(TestObject2Generator.class) TestObject2 obj) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        assertEquals(gson.fromJson(gson.toJson(obj), obj.getClass()), obj);
    }

    @Property(trials = trials)
    public void testObjectToJsonToObject(@TestObjectInterface TestObject obj) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String jsonObj = gson.toJson(obj);
        TestObject obj2 = gson.fromJson(jsonObj, TestObject.class);
        assertEquals(obj, obj2);
    }

    @Property(trials = trials)
    public void testObjectToJsonToOtherObject(@TestObjectInterface TestObject obj) {
        Gson gson = new Gson();
        String jsonObj = gson.toJson(obj);
        TestObjectOther objOther = new TestObjectOther(obj.getText(), obj.getNumber(), obj.getTexts(), obj.isBool());
        TestObjectOther objOtherTwo = gson.fromJson(jsonObj, TestObjectOther.class);
        assertEquals(objOther, objOtherTwo);
    }

    @Property(trials = trials)
    public void randomObjects(@From(RandomObjectGenerator.class) Object obj) throws ClassNotFoundException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Class generatedClass = Class.forName(obj.getClass().getName());
        String json = gson.toJson(generatedClass.cast(obj));
        assertEquals(generatedClass.cast(obj), gson.fromJson(json, generatedClass));
    }

    @Property(trials = trials)
    public void noisyJSON(@From(RandomObjectGenerator.class) Object object) throws ClassNotFoundException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Class generatedClass = Class.forName(object.getClass().getName());
        String json = gson.toJson(generatedClass.cast(object));
        String noisyJSON = randomNoisyJsonGenerator.scramble(json, sor);
        assertEquals(gson.fromJson(noisyJSON, generatedClass), gson.fromJson(json, generatedClass));
    }

    private RandomNoisyJsonGenerator randomNoisyJsonGenerator = new RandomNoisyJsonGenerator();
    private SourceOfRandomness sor = new SourceOfRandomness(new Random());

    private String toString(Object o) {
        Field[] flds = o.getClass().getDeclaredFields();
        return Arrays.stream(flds).map(
                fld -> {
                    try {
                        fld.setAccessible(true);
                        return fld.getName() + ":" + printString(fld.get(o));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return "";
                    }
                }
        ).reduce("", String::concat);
    }

    private static String printString(Object o) {
        try {
            Object[] os = (Object[]) o;
            return Arrays.toString(os);
        } catch (Exception e) {
            return o.toString();
        }

    }
}
