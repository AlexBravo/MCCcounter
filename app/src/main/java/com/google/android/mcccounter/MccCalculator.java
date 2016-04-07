package com.google.android.mcccounter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/** Created by alex on 3/20/2016. */

public class MccCalculator {
    public List<String> shortList = new ArrayList<>();
    public List<String> longList = new ArrayList<>();

    HashMap<String, Long> frequencies = new HashMap<>();

//    public MccCalculator(List<String> shortList, List<String> longList) {
//        this.shortList = shortList;
//        this.longList = longList;
//    }

    public LinkedHashMap<String, Long> calculateSortedConfusions(String in) {
        //String in = inp.toLowerCase();
        HashMap<String, Long> confusions = new HashMap<>();
        // Go through the input text and find MCCs in them
        int i = 0;
        // Go all the way to 2 letters before the last one. That is the place of last possible confusion
        while(i < in.length() - 2) {
            if (i + 3 <= in.length()) {
                //String longMcc = in.substring(i, i + 3);
                // Account for the fact that all MCCs have capitalized first letter
                String longMcc = in.substring(i, i + 1).toLowerCase() + in.substring(i + 1, i + 3);
                if (longList.contains(longMcc)) {
                    // Don't look for 4th letter.
                    // "thet" is not confusing if there are only "the" and "et",
                    // as there needs to be "th", "the" and "et" to be confusing

                    // Don't count capitalized second MCC
//                    String secondShort = in.substring(i + 2, (i + 2) + 2);
//                    if (shortList.contains(secondShort)) {
//                        String toAdd = longMcc + secondShort.charAt(1);
//                        addToMap(toAdd, confusions);
//                    }
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

    public LinkedHashMap<String, Long> calculateSortedFrequencies(String in) {
        // Go through the input text and find MCCs in them
        frequencies.clear();

        int i = 0;
        int length = in.length();
        // Go all the way to one letter before the last one
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
    public LinkedHashMap<String, Long> calculateSortedConfusionRanks(String in){

        HashMap<String, Long> confusionRanks = new HashMap<>();
        HashMap<String, Long> originalConfusions = calculateSortedConfusions(in);
        int originalConfusionsTotal = Utility.calculateTotalOfValues(originalConfusions);


        LinkedHashMap<String, Long> sortedFrequencies = calculateSortedFrequencies(in);
        int frequenciesTotal = Utility.calculateMccSavings(sortedFrequencies);

        for(int i = 0; i < shortList.size(); i++) {
            String mccToRemove = shortList.get(i);
            shortList.set(i, "");
            HashMap<String, Long> confusions = calculateSortedConfusions(in);
            double confusionPercentage = calculateConfusionPercentage(confusions, originalConfusionsTotal);
            Long frequency = sortedFrequencies.get(mccToRemove);
            if (frequency != 0) {
                Long confusionRank = Utility.calculateConfusionRank(confusionPercentage, frequency);
                if (confusionRank != null) {
                    confusionRanks.put(mccToRemove, confusionRank);
                }
            }
            shortList.set(i, mccToRemove);
        }

        return Utility.sortMap(confusionRanks);
    }

    public double calculateConfusionPercentage(HashMap<String, Long> confusions, int originalTotal) {
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


    private static void addToMap(String toAdd, HashMap<String, Long> map) {
        if(!map.containsKey(toAdd)) {
            map.put(toAdd, 1L);
        } else {
            long prev = map.get(toAdd);
            map.put(toAdd, prev + 1L);
        }
    }
}
