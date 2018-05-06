/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import com.google.gson.Gson;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import dk.sdu.mmmi.fppt.gsonquickcheck.TestObject;
import dk.sdu.mmmi.fppt.gsonquickcheck.TestObjectGenerator.TestObjectInterface;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Bogs
 */
@RunWith(JUnitQuickcheck.class)
public class GSONTest {
    @Property
    public void concatenationLength(String s1, String s2) {
        assertEquals(s1.length() + s2.length(), (s1 + s2).length());
    }

    @Property(maxShrinks = 10)
    public void testTestObjectGenerator(@TestObjectInterface TestObject t1) {
        Gson gson = new Gson();
        String serialized = gson.toJson(t1);
        TestObject deserialized = gson.fromJson(serialized, TestObject.class);
        deserialized.setText("");

        assertTrue(t1.equals(deserialized));
    }


}
