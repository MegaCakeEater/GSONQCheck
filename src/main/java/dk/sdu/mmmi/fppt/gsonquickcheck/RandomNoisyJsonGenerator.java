/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Sup
 */
public class RandomNoisyJsonGenerator {

    private String[] noiseArr = new String[]{"\t", "\n", "\r"};

    public String scramble(String json, SourceOfRandomness sor) {
        json = addRandomNoise(json, sor);
        return json;
    }

    private String addRandomNoise(String json, SourceOfRandomness sor) {
        int replacements = sor.nextInt(25);

        for (int i = 0; i < replacements; i++) {
            String noise = noise(sor);
            char toChange = toChange(sor);
            List<Integer> indexes = indicesOf(toChange, json);
            if (indexes.isEmpty()) continue;

            int indexToChange = indexes.get(sor.nextInt(indexes.size()));
            if (indexToChange == 0) continue; //For nemheds skyld

            boolean beforeOrAfter = sor.nextBoolean();
            if (beforeOrAfter) {
                json = json.substring(0, indexToChange) + noise + json.substring(indexToChange);
            } else {
                json = json.substring(0, indexToChange + 1) + noise + json.substring(indexToChange + 1);
            }
        }
        return json;
    }


    private char toChange(SourceOfRandomness sor) {
        return changeablePlaces[sor.nextInt(changeablePlaces.length)];
    }

    private char[] changeablePlaces = new char[]{'{', '}', '[', ']', ',', ':'};

    private List<Integer> indicesOf(char c, String string) { //Find alle placeringer et char eksisterer i en liste
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == c) {
                list.add(i);
            }
        }
        return list;
    }

    private String noise(SourceOfRandomness sor) {
        Type noiseType = Type.values()[sor.nextInt(Type.values().length)];

        String noise;
        switch (noiseType) {
            case tab:
                noise = noiseArr[0];
                break;
            case space:
                noise = " ";
                break;
            case newline:
                noise = noiseArr[1];
                break;
            case carriage:
                noise = noiseArr[2];
                break;
            default:
                noise = "";
        }
        return noise;
    }

    public enum Type {
        tab,
        space,
        newline,
        carriage;
    }

}
