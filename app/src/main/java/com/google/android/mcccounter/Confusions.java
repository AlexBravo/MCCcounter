package com.google.android.mcccounter;

import java.util.HashMap;
import java.util.List;

/** Created by wendy on 3/20/2016. */

public class Confusions {
    public static HashMap<String, Integer> calculateConfusions(String in, List<String> mccs) {
        //String in = inp.toLowerCase();
        HashMap<String, Integer> confusions = new HashMap<>();
        // Go through the input text and find MCCs in them
        int i = 0;
        while(i < in.length() - 2) {
            String first = in.substring(i, i + 2);
            if (mccs.contains(first)) {
                String second = in.substring(i + 1, i + 2 + 1);
                if (mccs.contains(second)) {
                    String toAdd = first + second.charAt(1);
                    addToMap(toAdd, confusions);
                }
                i += 2;
            } else {
                i++;
            }
        }

        return Utility.sortMap(confusions);
    }

    public static HashMap<String, Integer> calculateFrequencies(String in, List<String> mccs) {
        // Go through the input text and find MCCs in them
        HashMap<String, Integer> frequencies = new HashMap<>();
        int i = 0;
        while(i < in.length() - 2) {
            String first = in.substring(i, i + 2);
            if (mccs.contains(first)) {
                i += 2;
                addToMap(first, frequencies);
            } else {
                i++;
            }
        }

        return Utility.sortMap(frequencies);
    }

    private static void addToMap(String toAdd, HashMap<String, Integer> map) {
        if(!map.containsKey(toAdd)) {
            map.put(toAdd, 1);
        } else {
            int prev = map.get(toAdd);
            map.put(toAdd, prev + 1);
        }
    }

    @SuppressWarnings("unused")
    public static HashMap<String, Integer> findConfusingPercentage(String in){

        HashMap<String, Integer> confusionRanks = new HashMap<>();
        HashMap<String, Integer> original = calculateConfusions(in, MccLists.shortMccList);
        int originalTotal = 0;
        for(Integer i : original.values()){
            originalTotal += i;
        }

        HashMap<String, Integer> frequencies = Confusions.calculateFrequencies(in, MccLists.shortMccList);

        List<String> mccs = MccLists.shortMccList;
        for(int i = 0; i < MccLists.shortMccList.size(); i++) {
            String mccToRemove = mccs.get(i);
            mccs.set(i, "");
            HashMap<String, Integer> h = calculateConfusions(in, mccs);
            double percent = calculatePercentage(h, originalTotal);
            double frequency = frequencies.get(mccToRemove) / 10000.0; // in 10 thousands
            int rank = (int)(percent/frequency);
            // Don't output not confusing MCCs
            if(rank != 0) {
                confusionRanks.put(mccToRemove, rank);
            }
            mccs.set(i, mccToRemove);
        }

        return Utility.sortMap(confusionRanks);
    }

    public static double calculatePercentage(HashMap<String, Integer> h, int originalTotal) {
        int hTotal = 0;
        for(Integer j : h.values()){
            hTotal += j;
        }
        @SuppressWarnings("UnnecessaryLocalVariable")
        double d = ((originalTotal - hTotal) / (double) originalTotal) * 100;
        return d;
    }
}
