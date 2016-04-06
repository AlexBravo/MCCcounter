package com.google.android.mcccounter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/** Created by alex on 3/20/2016. */

public class MccCalculator {
    public List<String> shortMccList;
    public List<String> longMccList;

    public MccCalculator(List<String> shortMccList, List<String> longMccList) {
        this.shortMccList = shortMccList;
        this.longMccList = longMccList;
    }

    public LinkedHashMap<String, Integer> calculateConfusions(String in) {
        //String in = inp.toLowerCase();
        HashMap<String, Integer> confusions = new HashMap<>();
        // Go through the input text and find MCCs in them
        int i = 0;
        while(i < in.length() - 2) {
            if (i + 3 <= in.length()) {
                String longMcc = in.substring(i, i + 3);
                if (longMccList.contains(longMcc)) {
                    String secondShort = in.substring(i + 2, (i + 2) + 2);
                    if (shortMccList.contains(secondShort)) {
                        String toAdd = longMcc + secondShort.charAt(1);
                        addToMap(toAdd, confusions);
                    }
                    i += 3;
                    continue;
                }
            }

            String first = in.substring(i, i + 2);
            if (shortMccList.contains(first)) {
                String second = in.substring(i + 1, (i + 1) + 2);
                if (shortMccList.contains(second)) {
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

    public LinkedHashMap<String, Integer> calculateFrequencies(String in) {
        // Go through the input text and find MCCs in them
        HashMap<String, Integer> frequencies = new HashMap<>();
        int i = 0;
        int length = in.length();
        while(i < length - 2) {
            if (i + 3 <= length) {
                String longMcc = in.substring(i, i + 3);
                if (longMccList.contains(longMcc)) {
                    i += 3;
                    addToMap(longMcc, frequencies);
                    continue;
                }
            }

            String shortMcc = in.substring(i, i + 2);
            if (shortMccList.contains(shortMcc)) {
                i += 2;
                addToMap(shortMcc, frequencies);
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
    public LinkedHashMap<String, Integer> findConfusionRanks(String in){

        HashMap<String, Integer> confusionRanks = new HashMap<>();
        HashMap<String, Integer> original = calculateConfusions(in);
        int originalTotal = Utility.calculateTotal(original);


        HashMap<String, Integer> frequencies = calculateFrequencies(in);
        int frequenciesTotal = Utility.calculateTotal(frequencies);


        for(int i = 0; i < shortMccList.size(); i++) {
            String mccToRemove = shortMccList.get(i);
            shortMccList.set(i, "");
            HashMap<String, Integer> confusions = calculateConfusions(in);
            double percent = calculatePercentage(confusions, originalTotal);
            double frequency = frequencies.get(mccToRemove) / 100000.0; // in 100 thousands
            int rank = (int)(percent/frequency);
            // Don't output not confusing MCCs
            if(rank != 0) {
                confusionRanks.put(mccToRemove, rank);
            }
            shortMccList.set(i, mccToRemove);
        }

        return Utility.sortMap(confusionRanks);
    }

    public double calculatePercentage(HashMap<String, Integer> confusions, int originalTotal) {
        int confusionsTotal = Utility.calculateTotal(confusions);
        @SuppressWarnings("UnnecessaryLocalVariable")
        double d = ((originalTotal - confusionsTotal) / (double) originalTotal) * 100;
        return d;
    }
}
