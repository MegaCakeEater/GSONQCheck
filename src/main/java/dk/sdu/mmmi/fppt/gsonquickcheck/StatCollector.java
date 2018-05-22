/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Bogs thread safe singleton from
 * https://www.journaldev.com/171/thread-safety-in-java-singleton-classes-with-example-code
 */
public class StatCollector {

    private static volatile StatCollector instance;
    private static final Object MUTEX = new Object();
    private ConcurrentHashMap<String, Integer> generatedFields;

    public static StatCollector getInstance() {
        StatCollector result = instance;
        if (result == null) {
            synchronized (MUTEX) {
                result = instance;
                if (result == null) {
                    instance = result = new StatCollector();
                }
            }
        }
        return result;
    }

    private StatCollector() {
        generatedFields = new ConcurrentHashMap();
    }

    public void addField(String field) {
        synchronized (MUTEX) {
            if (!generatedFields.containsKey(field)) {
                generatedFields.put(field, 1);
            } else {
                int count = generatedFields.get(field);
                count++;
                generatedFields.put(field, count);
            }
        }
    }
    
    public void printResults() {
        System.out.println("--------------------------------------------<STATISTICS>-----------------------------------------------------");
        System.out.println("Total generated of each field type");
        generatedFields.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
        System.out.println("--------------------------------------------<STATISTICS>-----------------------------------------------------");
    }

}
