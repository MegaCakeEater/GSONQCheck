/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Arrays;

/**
 * @author Bogs
 */
public class Main {
    public static void main(String[] args) {
        System.out.println(Arrays.toString((Object[])new String[] {"abc"}) );
        System.out.println(Arrays.toString((Object[])new Integer[] {1}).equals(Arrays.toString((Object[])new Integer[] {1})));
    }

}
