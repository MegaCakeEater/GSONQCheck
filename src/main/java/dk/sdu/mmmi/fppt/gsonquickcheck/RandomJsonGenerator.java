/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;

/**
 *
 * @author Bogs
 */
public class RandomJsonGenerator extends Generator<String> {

    private final String[] types = new String[]{"boolean", "int", "String", "float", "byte", "double", "short", "long", "char",
        "boolean[]", "int[]", "String[]", "float[]", "byte[]", "double[]", "short[]", "long[]", "char[]"};
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
        int random = sor.nextInt(0, 17);
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
        if (json.charAt(json.length() - 1) == ",".charAt(0)) {
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
                StatCollector.getInstance().addField(type, bool ? 1 : 0);
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
            case "short":
                short a = gen().type(short.class).generate(sor, gs);
                StatCollector.getInstance().addField(type, a);
                value = "" + a;
                break;
            case "long":
                long l = gen().type(long.class).generate(sor, gs);
                StatCollector.getInstance().addField(type, l);
                value = "" + l;
                break;
            case "char":
                char c = gen().type(char.class).generate(sor, gs);
                StatCollector.getInstance().addField(type, Character.getNumericValue(c));
                value = "\"" + StringEscapeUtils.escapeJava(Character.toString(c)) + "\"";
                break;
            case "byte":
                byte by = gen().type(byte.class).generate(sor, gs);
                StatCollector.getInstance().addField(type, Byte.toUnsignedInt(by));
                value = "" + by;
                break;
            case "boolean[]":
                boolean[] ba = gen().type(boolean[].class).generate(sor, gs);
                StatCollector.getInstance().addField(type, ba.length);
                value = "" + Arrays.toString(ba).replace(" ", "");
                ;
                break;
            case "int[]":
                int[] ia = gen().type(int[].class).generate(sor, gs);
                StatCollector.getInstance().addField(type, ia.length);
                value = "" + Arrays.toString(ia).replace(" ", "");
                ;
                break;
            case "String[]":
                String[] sa = gen().type(String[].class).generate(sor, gs);
                for (int n = 0; n < sa.length; n++) {
                    sa[n] = "\"" + StringEscapeUtils.escapeJava(sa[n]) + "\"";
                }
                StatCollector.getInstance().addField(type, sa.length);
                value = "" + Arrays.toString(sa).replace(" ", "");
                ;
                break;
            case "float[]":
                float[] fa = gen().type(float[].class).generate(sor, gs);
                StatCollector.getInstance().addField(type, fa.length);
                value = "" + Arrays.toString(fa).replace(" ", "");
                ;
                break;
            case "byte[]":
                byte[] bya = gen().type(byte[].class).generate(sor, gs);
                StatCollector.getInstance().addField(type, bya.length);
                value = "" + Arrays.toString(bya).replace(" ", "");
                ;
                break;
            case "double[]":
                double[] da = gen().type(double[].class).generate(sor, gs);
                StatCollector.getInstance().addField(type, da.length);
                value = "" + Arrays.toString(da).replace(" ", "");
                ;
                break;
            case "short[]":
                short[] sha = gen().type(short[].class).generate(sor, gs);
                StatCollector.getInstance().addField(type, sha.length);
                value = "" + Arrays.toString(sha).replace(" ", "");
                ;
                break;
            case "long[]":
                long[] la = gen().type(long[].class).generate(sor, gs);
                StatCollector.getInstance().addField(type, la.length);
                value = "" + Arrays.toString(la).replace(" ", "");
                ;
                break;
            case "char[]":
                char[] ca = gen().type(char[].class).generate(sor, gs);
                StatCollector.getInstance().addField(type, ca.length);
                String[] strca = new String[ca.length];
                for (int n = 0; n < ca.length; n++) {
                    strca[n] = "\"" + StringEscapeUtils.escapeJava(Character.toString(ca[n])) + "\"";
                }
                value = "" + Arrays.toString(strca).replace(" ", "");
                break;
            default:
                value = "ERROR";
                break;
        }

        return value;
    }

}
