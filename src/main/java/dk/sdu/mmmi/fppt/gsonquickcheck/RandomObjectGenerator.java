/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Sup
 */
public class RandomObjectGenerator extends Generator<Object> {

    private String[] types = new String[]{"boolean", "int", "String", "float", "String", "double"};
    private String legalNameCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int minimumNameLength = 10;
    private int maximumNameLength = 50;
    private LinkedList<String> names;
    ClassCreatorAndLoader c;

    public RandomObjectGenerator() {
        super(Object.class);
        names = new LinkedList<>();
        c = new ClassCreatorAndLoader();
    }

    @Override
    public Object generate(SourceOfRandomness sor, GenerationStatus gs) {
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
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        try {
            return createObject(path + "." + className, sor, fields, gs);
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
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

    private <T> T createObject(String fullClassName, SourceOfRandomness sor, Map<String, String> fields, GenerationStatus gs) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class clazz = Class.forName(fullClassName);
        Constructor ctorlist[]
                = clazz.getDeclaredConstructors();
        Constructor ct = ctorlist[0];
        T obj = (T) ct.newInstance();

        Field[] flds = obj.getClass().getDeclaredFields();
        Arrays.stream(flds).forEach( //Begin hacking the pentagon
                field -> {
                    field.setAccessible(true); //Access granted
                    try {
                        field.set(obj, handleType(field.getType(), sor, gs)); //Hack complete
                    } catch (IllegalAccessException e) {
                        e.printStackTrace(); // Hack failed
                    }
                }
        );

        return obj;
    }


    private <T> T handleType(Class clazz, SourceOfRandomness sor, GenerationStatus gs) {
        T t = (T) gen().type(clazz).generate(sor, gs);
        if(t instanceof Number) {
        StatCollector.getInstance().addField(clazz.getName(), (Number)t);
        } else if(t instanceof Boolean) {
        StatCollector.getInstance().addField(clazz.getName(), ((Boolean)t?1:0));
        } else if(t instanceof String) {
        StatCollector.getInstance().addField(clazz.getSimpleName(), (((String) t).length()));
        }
        return t;
    }

}
