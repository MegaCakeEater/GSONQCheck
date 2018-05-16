/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.When;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import dk.sdu.mmmi.fppt.gsonquickcheck.JSONGenerator;
import dk.sdu.mmmi.fppt.gsonquickcheck.TestObject;
import dk.sdu.mmmi.fppt.gsonquickcheck.TestObject2;
import dk.sdu.mmmi.fppt.gsonquickcheck.TestObject2Generator;
import dk.sdu.mmmi.fppt.gsonquickcheck.TestObjectGenerator.TestObjectInterface;
import dk.sdu.mmmi.fppt.gsonquickcheck.TestObjectOther;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Bogs
 */
@RunWith(JUnitQuickcheck.class)
public class GSONTest {
    
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
   
    @Property
    public void testTestObjectGenerator(@TestObjectInterface TestObject t1) {
        Gson gson = new Gson();
        String serialized = gson.toJson(t1);
        TestObject deserialized = gson.fromJson(serialized, TestObject.class);
        assertTrue(t1.equals(deserialized));
    }
    
    @Property
    public void testJsonToObjectToJson(@From(JSONGenerator.class) String json){
        Gson gson =  new GsonBuilder().disableHtmlEscaping().create();
        TestObject obj = gson.fromJson(json, TestObject.class);
        assertEquals(gson.toJson(obj), json);
    }
    
    @Property
    public void testObjectToJson(@TestObjectInterface TestObject obj){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        assertEquals(gson.toJson(obj), obj.toJson());
    }
    
    @Property
    public void testJsonToObject(@TestObjectInterface TestObject obj){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        assertEquals(gson.fromJson(obj.toJson(), obj.getClass()), obj);
    }
    
    @Property
    public void testJsonToObjectNested(@From(TestObject2Generator.class) TestObject2 obj){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        assertEquals(gson.fromJson(gson.toJson(obj), obj.getClass()), obj);
    }
    
    @Property
    public void testObjectToJsonToObject(@TestObjectInterface TestObject obj){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String jsonObj = gson.toJson(obj);
        TestObject obj2 = gson.fromJson(jsonObj, TestObject.class);
        assertEquals(obj,obj2);
    }

    @Property
    public void testNullSerialization(){
        Gson gson = new GsonBuilder().serializeNulls().create();
        assertEquals("null",gson.toJson(null));
    }
    
    @Property
    public void testObjectToJsonToOtherObject(@TestObjectInterface TestObject obj){
        Gson gson = new Gson();
        String jsonObj = gson.toJson(obj);
        TestObjectOther objOther = new TestObjectOther(obj.getText(),obj.getNumber(),obj.getTexts(),obj.isBool());
        TestObjectOther objOtherTwo = gson.fromJson(jsonObj,TestObjectOther.class);
        assertEquals(objOther, objOtherTwo);
    }
}
