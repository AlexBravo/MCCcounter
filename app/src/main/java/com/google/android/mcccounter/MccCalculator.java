package com.google.android.mcccounter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/** Created by alex on 3/20/2016. */

public class MccCalculator {
    public List<String> shortList;
    public List<String> longList;

    HashMap<String, Integer> frequencies = new HashMap<>();

    public MccCalculator(List<String> shortList, List<String> longList) {
        this.shortList = shortList;
        this.longList = longList;
    }

    public LinkedHashMap<String, Integer> calculateSortedConfusions(String in) {
        //String in = inp.toLowerCase();
        HashMap<String, Integer> confusions = new HashMap<>();
        // Go through the input text and find MCCs in them
        int i = 0;
        while(i < in.length() - 2) {
            if (i + 3 <= in.length()) {
                //String longMcc = in.substring(i, i + 3);
                // Account for the fact that all MCCs have capitalized first letter
                String longMcc = in.substring(i, i + 1).toLowerCase() + in.substring(i + 1, i + 3);
                if (longList.contains(longMcc)) {
                    // Don't count capitalized second MCC
                    String secondShort = in.substring(i + 2, (i + 2) + 2);
                    if (shortList.contains(secondShort)) {
                        String toAdd = longMcc + secondShort.charAt(1);
                        addToMap(toAdd, confusions);
                    }
                    i += 3;
                    continue;
                }
            }

            //String shortMcc = in.substring(i, i + 2);
            // Account for the fact that all MCCs have capitalized first letter
            String shortMcc = in.substring(i, i + 1).toLowerCase() + in.substring(i + 1, i + 2);
            if (shortList.contains(shortMcc)) {
                // Don't count capitalized second MCC
                String second = in.substring(i + 1, (i + 1) + 2);
                if (shortList.contains(second)) {
                    String toAdd = shortMcc + second.charAt(1);
                    addToMap(toAdd, confusions);
                }
                i += 2;
            } else {
                i++;
            }
        }

        return Utility.sortMap(confusions);
    }

    public LinkedHashMap<String, Integer> calculateSortedFrequencies(String in) {
        // Go through the input text and find MCCs in them
        frequencies.clear();

        int i = 0;
        int length = in.length();
        while(i < length - 1) {
            if (i + 3 <= length) {
                //String longMcc = in.substring(i, i + 3);
                // Account for the fact that all MCCs have capitalized first letter
                String longMcc = in.substring(i, i + 1).toLowerCase() + in.substring(i + 1, i + 3);
                if (longList.contains(longMcc)) {
                    i += 3;
                    addToMap(longMcc, frequencies);
                    continue;
                }
            }

            //String shortMcc = in.substring(i, i + 2);
            // Account for the fact that all MCCs have capitalized first letter
            String shortMcc = in.substring(i, i + 1).toLowerCase() + in.substring(i + 1, i + 2);
            if (shortList.contains(shortMcc)) {
//                if (shortMcc.equals("he")) { // For debugging
//                    String context = in.substring(i - 2, i + 4);
//                    System.out.print("'" + context + "',");
//                }
                i += 2;
                addToMap(shortMcc, frequencies);
            } else {
                i++;
            }
        }

        return Utility.sortMap(frequencies);
    }

    @SuppressWarnings("unused")
    public LinkedHashMap<String, Integer> calculateSortedConfusionRanks(String in){

        HashMap<String, Integer> confusionRanks = new HashMap<>();
        HashMap<String, Integer> originalConfusions = calculateSortedConfusions(in);
        int originalConfusionsTotal = Utility.calculateTotalOfValues(originalConfusions);


        LinkedHashMap<String, Integer> sortedFrequencies = calculateSortedFrequencies(in);
        int frequenciesTotal = Utility.calculateMccSavings(sortedFrequencies);

        for(int i = 0; i < shortList.size(); i++) {
            String mccToRemove = shortList.get(i);
            shortList.set(i, "");
            HashMap<String, Integer> confusions = calculateSortedConfusions(in);
            double confusionPercentage = calculateConfusionPercentage(confusions, originalConfusionsTotal);
            int frequency = sortedFrequencies.get(mccToRemove);
            Integer confusionRank = Utility.calculateConfusionRank(confusionPercentage, frequency);
            if (confusionRank != null) {
                confusionRanks.put(mccToRemove, confusionRank);
            }
            shortList.set(i, mccToRemove);
        }

        return Utility.sortMap(confusionRanks);
    }

    public double calculateConfusionPercentage(HashMap<String, Integer> confusions, int originalTotal) {
        int confusionsTotal = Utility.calculateTotalOfValues(confusions);
        @SuppressWarnings("UnnecessaryLocalVariable")
        double d = ((originalTotal - confusionsTotal) / (double) originalTotal) * 100;
        return d;
    }

    public void add(String candidate) {
        if (candidate.length() == 2) {
            shortList.add(candidate);
        } else {
            longList.add(candidate);
        }
    }

    public void remove(String candidate) {
        if (candidate.length() == 2) {
            shortList.remove(candidate);
        } else {
            longList.remove(candidate);
        }
    }


    private static void addToMap(String toAdd, HashMap<String, Integer> map) {
        if(!map.containsKey(toAdd)) {
            map.put(toAdd, 1);
        } else {
            int prev = map.get(toAdd);
            map.put(toAdd, prev + 1);
        }
    }
}
