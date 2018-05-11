/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.text.StringEscapeUtils;

/**
 *
 * @author Bogs
 */
public class JSONGenerator extends Generator<String> {

    TestObject obj;
    
    public JSONGenerator() {
        super(String.class);
    }
    
    @Override
    public String generate(SourceOfRandomness sor, GenerationStatus gs) {
        String text = StringEscapeUtils.escapeJava(gen().type(String.class).generate(sor, gs));
        int number = sor.nextInt();
        int arrayNumber = sor.nextInt(10);
        String[] texts = new String[arrayNumber];
        for(int i = 0; i<arrayNumber; i++) {
            texts[i] = StringEscapeUtils.escapeJava(gen().type(String.class).generate(sor, gs));
        }
        boolean bool = sor.nextBoolean();
        obj = new TestObject(text, number, texts, bool);
        String objToJson = "{\"text\"" + ":\"" + StringEscapeUtils.escapeJava(obj.getText()) +"\"," 
                + "\"number\"" + ":" + obj.getNumber() + "," 
                + "\"texts\"" + ":" + obj.getJsonTextFromTexts()+ ","
                + "\"bool\"" + ":" + obj.isBool() + "}";
                return objToJson;
    }
    
    public TestObject getObject() {
       return obj; 
    }

   
    
}
