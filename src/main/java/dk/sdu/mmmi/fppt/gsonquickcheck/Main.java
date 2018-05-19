/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.sdu.mmmi.fppt.generated.JfhodShXPkzREMHhdImoepSUpklPYLUUCuPRNCpY;
import java.util.HashMap;

/**
 *
 * @author Bogs
 */
public class Main {

    static final String packageName = "dk.sdu.mmmi.fppt.gen";
    static final String className = "asd";

    public static void main(String[] args) {
        HashMap<String, String> fields = new HashMap();
        fields.put("hest", "String");
        ClassCreatorAndLoader cl = new ClassCreatorAndLoader();
        try {
            cl.createAndLoadClass(fields, className, packageName);
            Class generatedClass = Class.forName(packageName + "." + className);
            String test ="{\"pCnLfFRFpXYqTfywKVnmftlRJoOQEVKB\": 0.93821627,\n" +
"\"iSeZaarRAgbiMabCKKOxMqXYZWZ\": 0.41068873481445667,\n" +
"\"dmEhoQCOKRXdJmRJVaRjBAOnghNLPUAYtLeMZrQVRAJWcKC\": \"\",\n" +
"\"GXCNcIzpijTOpnntMNcQtBzJkv\": \"\",\n" +
"\"SkJNWHrLyUmOlNmz\": 0.3274507}";
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        System.out.println(gson.fromJson(test, Class.forName("dk.sdu.mmmi.fppt.generated.JfhodShXPkzREMHhdImoepSUpklPYLUUCuPRNCpY")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String parse = "dk.sdu.mmmi.fppt.generated.iEAASmWvwQHOuG|{\"rgJklUvgqdaccPeTDZTJvGXFjKvRkpPJvRniJiBxtWlgYIJ\": 0.552719594818277,\n" +
"\"ZQbWFpklqfidKkvKmxfnUXYXDaMrl\": true,\n" +
"\"QGenLMiBtTOQCGKfzyJOyLHNMoiPqVcUzBTIWJP\": false,\n" +
"\"rkHDobffNwiywEjYnSdICvTCZdYWVaND\": 1138021833,\n" +
"\"GcKHXoAfyCdMCXIHdEvwGxBokZNP\": 0.053034306,\n" +
"\"xCfofsJUgiSRXXQAkQBPjYBdwrNCWZYBIZUYmblvdyz\": \"\",\n" +
"\"eeQofVWYgvxDCNJBdUqZoDH\": -450497635,\n" +
"\"KynzFuYAxD\": 1207760881,\n" +
"\"OjpbceUqarlrhu\": 0.5746487}], seed = 1517283722626542047]\n" +
"{\"rgJklUvgqdaccPeTDZTJvGXFjKvRkpPJvRniJiBxtWlgYIJ\": 0.552719594818277,\n" +
"\"ZQbWFpklqfidKkvKmxfnUXYXDaMrl\": true,\n" +
"\"QGenLMiBtTOQCGKfzyJOyLHNMoiPqVcUzBTIWJP\": false,\n" +
"\"rkHDobffNwiywEjYnSdICvTCZdYWVaND\": 1138021833,\n" +
"\"GcKHXoAfyCdMCXIHdEvwGxBokZNP\": 0.053034306,\n" +
"\"xCfofsJUgiSRXXQAkQBPjYBdwrNCWZYBIZUYmblvdyz\": \"\",\n" +
"\"eeQofVWYgvxDCNJBdUqZoDH\": -450497635,\n" +
"\"KynzFuYAxD\": 1207760881,\n" +
"\"OjpbceUqarlrhu\": 0.5746487}";
        
        System.out.println(parse.substring(parse.indexOf("|")+1));
    }

}
