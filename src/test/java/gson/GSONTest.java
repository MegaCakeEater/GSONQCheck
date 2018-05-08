/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import dk.sdu.mmmi.fppt.gsonquickcheck.TestObject;
import dk.sdu.mmmi.fppt.gsonquickcheck.TestObjectGenerator.TestObjectInterface;
import java.nio.charset.StandardCharsets;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Bogs
 */
@RunWith(JUnitQuickcheck.class)
public class GSONTest {
    /*@Property
    public void concatenationLength(String s1, String s2) {
        assertEquals(s1.length() + s2.length(), (s1 + s2).length());
    }*/
    
    
    /**
     * Testing the primitive conversion of an integer
     * To do this we use the JsonPrimitive which is a subtype of JsonElement
     * More information here:
     * https://static.javadoc.io/com.google.code.gson/gson/2.6.2/com/google/gson/JsonPrimitive.html
     * @param primitive 
     */
    @Property
    public void testPrimitiveIntToJSON(int primitive){
        JsonElement element = new JsonPrimitive(primitive);
        assertEquals(element.getAsInt(),primitive);
    }
    
    /**
     * Testing the primitive conversion of a Double
     * To do this we use the JsonPrimitive which is a subtype of JsonElement
     * More information here:
     * https://static.javadoc.io/com.google.code.gson/gson/2.6.2/com/google/gson/JsonPrimitive.html
     * @param primitive 
     */
    @Property
    public void testPrimitiveDoubleToJSON(double primitive){
        JsonElement element = new JsonPrimitive(primitive);
        assertEquals(element.getAsDouble(),primitive, 0.00);
    }
    
    /**
     * Testing the primitive conversion of a String
     * To do this we use the JsonPrimitive which is a subtype of JsonElement
     * More information here:
     * https://static.javadoc.io/com.google.code.gson/gson/2.6.2/com/google/gson/JsonPrimitive.html
     * @param primitive 
     */
    @Property
    public void testPrimitiveStringToJSON(String primitive) {
        JsonElement element = new JsonPrimitive(primitive);
        assertEquals(primitive,element.getAsString());
    }
    
    /**
     * Testing conversion of a json primitive to a primitive by
     * first creating a json primitive before converting it back
     * using the gson instance.
     * To ensure the conversion happens correctly we check that the
     * object has been converted to a json primitive before reverting and comparing
     * @param primitive 
     */
    @Property
    public void testJsonToPrimitiveInt(int primitive){
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
     * @param primitive 
     */
    @Property
    public void testJsonToPrimitiveDouble(double primitive){
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
     * @param primitive 
     */
    @Property
    public void testJsonToPrimitiveString(String primitive){
      JsonElement element = new JsonPrimitive(primitive);
      Gson gson = new Gson();
      assertTrue(element.isJsonPrimitive());
      String backToPrimitive = gson.fromJson(element.toString(), String.class);
      assertEquals(primitive, backToPrimitive);
    }
    
    /**
     * Test to convert a simple object to JSON and check that each of the
     * properties of the object is still intact.
     */
    /*
    @Property
    public void testSimpleObjectToJSON(@TestObjectInterface TestObject testObject){
        Gson gson = new Gson();
        String startText = testObject.getText();
        int startInteger = testObject.getNumber();
        String[] startTextArray = testObject.getTexts();
        boolean startBool = testObject.isBool();
        String jsonObject = gson.toJson(testObject);
        
    }
    
    */
    @Property
    public void testTestObjectGenerator(@TestObjectInterface TestObject t1) {
        Gson gson = new Gson();
        String serialized = gson.toJson(t1);
        TestObject deserialized = gson.fromJson(serialized, TestObject.class);
        assertTrue(t1.equals(deserialized));
    }
    
    
    /**
     * fails due to unicode chars such as \u001a becomming &#26;
     * It also has a hard time handling the conversion of " characters due to
     * the strings in java washing them out as they are being compiled
     * this can be overcome by using a JsonPrimitive object as shown
     * in the test for the primitive types, though this is an issue.
     * @param t1 
     */
    @Property(maxShrinks= 100)
    public void testTestObjectIsAsExpected(@TestObjectInterface TestObject t1){
        Gson gson = new Gson();
        TestObject test = new TestObject("hej",123,new String[]{"a","b","c"},true);
        String serialized = gson.toJson(t1);
        System.out.println(serialized);
        assertEquals(serialized, "{\"text\"" + ":\"" + t1.getText() +"\"," 
                + "\"number\"" + ":" + t1.getNumber() + "," 
                + "\"texts\"" + ":" + t1.getJsonTextFromTexts()+ ","
                + "\"bool\"" + ":" + t1.isBool() + "}");
    }
    
    /**
     * Made as a control test for the previous test to ensure the structure of
     * the test is correct, because this test passes we can conclude that 
     * it is the properties of GSON that is failing the test.
     */
    @Property
    public void testTestObjectIsAsExceptedControlTest(){
        Gson gson = new Gson();
        TestObject test = new TestObject("hej",123,new String[]{"a","b","c"},true);
        String serialized = gson.toJson(test);
        assertEquals(serialized, "{\"text\"" + ":\"" + test.getText() +"\"," 
                + "\"number\"" + ":" + test.getNumber() + "," 
                + "\"texts\"" + ":" + test.getJsonTextFromTexts()+ ","
                + "\"bool\"" + ":" + test.isBool() + "}");
    }
    

}
