/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import org.apache.commons.text.StringEscapeUtils;

/**
 *
 * @author Bogs
 */
public class Main {

    static final String packageName = "dk.sdu.mmmi.fppt.generated";
    static final String className = "nEJukfKcaUJGfVNpWpOtiTKSLiLOlvncsraeKNQTljxOfM";

    public static void main(String[] args) {
        try {
            Class generatedClass = Class.forName(packageName + "." + className);
            String test ="{\"UAGVgWpGVubHqf\":0.67583185,\"bTZbsNsiOREaDDJlziJtjz\":\"ÇwÀÓ]®m\"\",\"WuVqCwNGubdzWbSmyxaXNzeS\":0.10969859,\"EiObdUXYLVcDuB\":0.5317607,\"isCcxOcydUobflUVJhQllkzDtLkdeFWTdpAErltTfeN\":\"³,U¶Íô\"}";
        System.out.println(test);
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Object spagjet = gson.fromJson(test, Class.forName(packageName + "." + className));
            System.out.println(gson.toJson(spagjet).equals(test));
            System.out.println(gson.toJson(spagjet));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
