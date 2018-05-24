/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import java.util.Arrays;
import java.util.HashMap;
import java.lang.Number;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Bogs thread safe singleton from
 * https://www.journaldev.com/171/thread-safety-in-java-singleton-classes-with-example-code
 */
public class StatCollector {

    private static volatile StatCollector instance;
    private static final Object MUTEX = new Object();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Number>> generatedFields;

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

    public void addField(String field, Number value) {
        synchronized (MUTEX) {
            if (!generatedFields.containsKey(field)) {
                ConcurrentLinkedQueue<Number> q = new ConcurrentLinkedQueue();
                q.add(value);
                generatedFields.put(field, q);
            } else {
                ConcurrentLinkedQueue<Number> q = generatedFields.get(field);
                q.add(value);
                generatedFields.put(field, q);
            }
        }
    }

    public void printResults() {
        System.out.println("--------------------------------------------<STATISTICS>-----------------------------------------------------");
        System.out.println("Total generated of each field type");
        generatedFields.forEach((key, value) -> {
            int count = 0;
            for (Number n : value) {
            count++;
            }
            System.out.println(key + ": " + count);
        });
        System.out.println("--------------------------------------------<STATISTICS>-----------------------------------------------------");
    }

    public void printDistribution() {
        System.out.println("-------------------------------------------<DISTRIBUTION>----------------------------------------------------");
        System.out.println("Total generated of each field type");
        generatedFields.forEach((key, value) -> {
            double average = 0.0;
            Number median;
            int size = value.size();
            
            Number[] vals = new Number[size];
            value.toArray(vals);
            if(vals.length % 2 == 1) {
                median = vals[(size-1)/2];
            } else {
                median = (vals[size/2].doubleValue() + vals[size/2-1].doubleValue())/2;
            }
            
            for (Number n : value) {
            average += n.doubleValue();
            }
            
            average = average / size;
            System.out.println(key + " AVERAGE: " + average);
            System.out.println(key + " MEDIAN: " + median);
        });
        System.out.println("-------------------------------------------<DISTRIBUTION>----------------------------------------------------");
    }

}
