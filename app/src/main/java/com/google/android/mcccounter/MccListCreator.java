package com.google.android.mcccounter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Created by alex on 4/6/16. */
public class MccListCreator {

    public static List<String> createMccList(String in) {
        int fileLength = in.length();

        List<String> allMCCs = new ArrayList<>(MccLists.fullLongMccList);
        allMCCs.addAll(MccLists.fullShortMccList);

        List<String> shortList = new ArrayList<>();
        List<String> longList = new ArrayList<>();

        HashMap<String, Integer> confusionRanks = new HashMap<>();
//        HashMap<String, Integer> candidateFrequencies = new HashMap<>();

        int confusionTotal = 0;
        MccCalculator mccCalculator = new MccCalculator(shortList, longList);
        while (!allMCCs.isEmpty()) {
            confusionRanks.clear();
//            candidateFrequencies.clear();

            for (String candidate : allMCCs) {
                mccCalculator.add(candidate);

                // TODO: Number of occurrence of an MCCs should be calculated for each MCC based on the current list of MCCs
                int occurrences = 1; //mccCalculator.calculateChords(in, candidate);
                //int occurrences = frequencies.get(candidate);

                HashMap<String, Integer> confusions = mccCalculator.calculateSortedConfusions(in);

                //Log.i("MCC confusions", "Adding " + candidate + " resulted in " + confusions.size() + " confusions " + confusions.toString());

                // Calculate the sum of all confusions
                int newConfusionTotal = Utility.calculateTotalOfValues(confusions);
                int addedConfusion = newConfusionTotal - confusionTotal;

                // Calculate the new frequencies of all MCCs using the new lists
                LinkedHashMap<String, Integer> allFrequencies = mccCalculator.calculateSortedFrequencies(in);

                // Was this chord encountered?
                Integer frequency = allFrequencies.get(candidate);
                if (frequency != null) {
                    int mccSavings = Utility.calculateMccSavings(allFrequencies);

                    int typedChords = fileLength - mccSavings;
                    // Add 1 to avoid 0 rank
                    int rank = (addedConfusion + 1) * typedChords;
                    if (rank != 0) {
                        confusionRanks.put(candidate, rank);
                    }

                    // Store frequency to see if frequencies are changing as we add more MCC
//                    candidateFrequencies.put(candidate, frequency);
//
//                    //Integer confusionRank = Utility.calculateConfusionRank(addedConfusion, frequency);
//                    //if (confusionRank != null) {
//                    //    confusionRanks.put(candidate, confusionRank);
//                    //}
                }

                // Now that calculations are done, candidate can be removed
                mccCalculator.remove(candidate);
            }

//            LinkedHashMap<String, Integer> sortedCandidateFrequencies = Utility.sortMap(candidateFrequencies);
//            System.out.println("sortedCandidateFrequencies " + sortedCandidateFrequencies.toString());

            LinkedHashMap<String, Integer> sortedConfusionRanks = Utility.sortMapReverse(confusionRanks);
            System.out.println("sortedConfusionRanks " + sortedConfusionRanks.toString());

            if (sortedConfusionRanks.isEmpty()) {
                System.out.println("Error sortedConfusionRanks empty");
                allMCCs.clear();
            } else {
                // Take MCC with the smallest confusion rank
                Map.Entry<String, Integer> entry = sortedConfusionRanks.entrySet().iterator().next();
                String mccToAdd = entry.getKey();
                System.out.println("'" + mccToAdd + "' has rank " + entry.getValue());

                allMCCs.remove(mccToAdd);
                mccCalculator.add(mccToAdd);

                // Now we can calculate new confusions
                HashMap<String, Integer> confusions = mccCalculator.calculateSortedConfusions(in);
                confusionTotal = Utility.calculateTotalOfValues(confusions);
            }

// Calculate new frequencies to prune the new list from too rare MCCs
//            HashMap<String, Integer> frequencies = mccCalculator.calculateSortedFrequencies(in);
//            System.out.println("New MCC frequencies " + frequencies.toString());
//            Set<String> allSortedMCCs = frequencies.keySet();
//            System.out.println("allSortedMCCs " + allSortedMCCs.toString());
//            for (each MCC)
//                removeTooRareMcc()
        }

        System.out.println("MCC longList " + longList.toString() + " shortList " + shortList.toString());
        List<String> finalList = new ArrayList<>(longList);
        finalList.addAll(shortList);

        return finalList;
    }
}
