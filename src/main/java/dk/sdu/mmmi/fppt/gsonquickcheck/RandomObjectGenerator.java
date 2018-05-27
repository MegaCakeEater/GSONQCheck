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

/**
 * @author Sup
 */
public class RandomObjectGenerator extends Generator<Object> {

    private final String[] types = new String[]{"boolean", "int", "String", "float", "byte", "double", "short", "long", "char",
            "Boolean[]", "Integer[]", "String[]", "Float[]", "Byte[]", "Double[]", "Short[]", "Long[]", "Character[]"};
    private String legalNameCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private int minimumNameLength = 10;
    private int maximumNameLength = 50;
    private LinkedList<String> names;
    private ClassCreatorAndLoader c;

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
            return createObject(path + "." + className, sor, gs);
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String generateDataType(SourceOfRandomness sor) {
        return types[sor.nextInt(0, 17)];
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

    private <T> T createObject(String fullClassName, SourceOfRandomness sor, GenerationStatus gs) throws ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
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

        if (t instanceof Boolean)
            StatCollector.getInstance().addField(clazz.getName(), ((Boolean) t ? 1 : 0));
        else if (t instanceof String) {
            StatCollector.getInstance().addField(clazz.getSimpleName(), (((String) t).length()));
            t = (T) replaceJSONChars((String) t);
        } else if (t instanceof Character)
            StatCollector.getInstance().addField(clazz.getSimpleName(), (Character.getNumericValue((Character) t)));
        else if (t instanceof Byte)
            StatCollector.getInstance().addField(clazz.getName(), Byte.toUnsignedInt((Byte) t));
        else if (t instanceof Number)
            StatCollector.getInstance().addField(clazz.getName(), (Number) t);
        else if (t instanceof Boolean[])
            StatCollector.getInstance().addField(clazz.getName(), ((Boolean[]) t).length);
        else if (t instanceof Integer[])
            StatCollector.getInstance().addField(clazz.getName(), ((Integer[]) t).length);
        else if (t instanceof String[]) {
            t = (T) Arrays.stream((String[]) t).map(this::replaceJSONChars).toArray(String[]::new);
            StatCollector.getInstance().addField(clazz.getName(), ((String[]) t).length);
        } else if (t instanceof Float[])
            StatCollector.getInstance().addField(clazz.getName(), ((Float[]) t).length);
        else if (t instanceof Byte[])
            StatCollector.getInstance().addField(clazz.getName(), ((Byte[]) t).length);
        else if (t instanceof Double[])
            StatCollector.getInstance().addField(clazz.getName(), ((Double[]) t).length);
        else if (t instanceof Short[])
            StatCollector.getInstance().addField(clazz.getName(), ((Short[]) t).length);
        else if (t instanceof Long[])
            StatCollector.getInstance().addField(clazz.getName(), ((Long[]) t).length);
        else if (t instanceof Character[])
            StatCollector.getInstance().addField(clazz.getName(), ((Character[]) t).length);
        else
            System.out.println("oops");

        return t;
    }

    private String replaceJSONChars(String s) {
        return s.replaceAll(",", "")
                .replaceAll("}", "")
                .replaceAll("\\{", "")
                .replaceAll(":", "")
                .replaceAll("\\[", "")
                .replaceAll("]", ""); //Alternativt bliv god til regex...
    }

}
