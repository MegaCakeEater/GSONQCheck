/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;

/**
 *
 * @author Bogs
 */
public class RandomJsonGenerator extends Generator<String> {

    private String[] types = new String[]{"boolean", "int", "String", "float", "String", "double"};
    private String legalNameCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int minimumNameLength = 10;
    private int maximumNameLength = 50;
    private LinkedList<String> names;
    ClassCreatorAndLoader c;

    public RandomJsonGenerator() {
        super(String.class);
        names = new LinkedList<>();
        c = new ClassCreatorAndLoader();
    }

    @Override
    public String generate(SourceOfRandomness sor, GenerationStatus gs) {
        //generate number of fields
        int numberOfFields = sor.nextInt(0, 10);
        HashMap<String, String> fields = new HashMap<>();
        String className = generateFieldName(sor);
        String path = "dk.sdu.mmmi.fppt.generated";
        //generate fields
        for (int i = 0; i < numberOfFields; i++) {
            fields.put(generateFieldName(sor), generateDataType(sor));
        }
        try {
           c.createAndLoadClass(fields, className, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createJson(path + "." + className, sor, fields, gs);
    }

    private String generateDataType(SourceOfRandomness sor) {
        String type = "";
        int random = sor.nextInt(0, 5);
        type = types[random];
        return type;
    }

    private String generateFieldName(SourceOfRandomness sor) {
        String name = "";
        int nameLength = sor.nextInt(minimumNameLength, maximumNameLength);
        boolean nameGenerated = false;
        while (!nameGenerated) {
            for (int i = 0; i < nameLength; i++) {
                name += legalNameCharacters.charAt(sor.nextInt(0, legalNameCharacters.length() - 1));
            }
            nameGenerated = !names.contains(name);
        }

        return name;
    }

    private String createJson(String fullClassName, SourceOfRandomness sor, Map<String, String> fields, GenerationStatus gs) {
        String json = fullClassName + "|" + "{";

        for (Map.Entry<String, String> e : fields.entrySet()) {
            json += "\"" + e.getKey() + "\":" + handleType(e.getValue(), sor, gs) + ",";
        }
        if (json.charAt(json.length()-1) == ",".charAt(0)) {
            json = json.substring(0, json.length() - 1);
        }
        json += "}";
        return json;
    }

    private String handleType(String type, SourceOfRandomness sor, GenerationStatus gs) {
        String value = "";

        switch (type) {
            case "boolean":
                boolean bool = sor.nextBoolean();
                StatCollector.getInstance().addField(type, bool?1:0);
                value = "" + bool;
                break;
            case "float":
                float f = gen().type(float.class).generate(sor, gs);
                StatCollector.getInstance().addField(type, f);
                value = "" + f;
                break;
            case "String":
                String s = gen().type(String.class).generate(sor, gs);
                StatCollector.getInstance().addField(type, s.length());
                value = "\"" + StringEscapeUtils.escapeJava(s) + "\"";
                break;
            case "int":
                int i = gen().type(int.class).generate(sor, gs);
                StatCollector.getInstance().addField(type, i);
                value = "" + i;
                break;
            case "double":
                double d = gen().type(double.class).generate(sor, gs);
                StatCollector.getInstance().addField(type, d);
                value = "" + d;
                break;
            case "object":
                value = "" + gen().type(Object.class).generate(sor, gs);
                break;
            default:
                value = "ERROR";
                break;
        }

        return value;
    }

}
