package com.google.android.mcccounter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Created by alex on 4/6/16. */
public class MccListCreator {

    private static final long MIN_MCC_VALUE = 1;

    public static List<String> createMccList(String in) {
        long fileLength = in.length();

        List<String> allMCCs = new ArrayList<>(MccLists.fullLongMccList);
        allMCCs.addAll(MccLists.fullShortMccList);

        List<String> finalList = new ArrayList<>();

        HashMap<String, Long> ranks = new HashMap<>();
        HashMap<String, Long> extraConfusions = new HashMap<>();
        HashMap<String, Long> savingFromMCCs = new HashMap<>();

        long confusionSoFar = 0;
        long mccSavingSoFar = 0;
        MccCalculator mccCalculator = new MccCalculator();
        while (!allMCCs.isEmpty()) {
            ranks.clear();
            extraConfusions.clear();
            savingFromMCCs.clear();

            for (String candidate : allMCCs) {
                mccCalculator.add(candidate);

                // TODO: Number of occurrence of an MCCs should be calculated for each MCC based on the current list of MCCs
                //int occurrences = 1; //mccCalculator.calculateChords(in, candidate);
                //int occurrences = frequencies.get(candidate);


                // Calculate the sum of all confusions
                HashMap<String, Long> confusions = mccCalculator.calculateSortedConfusions(in);
                long newConfusionTotal = Utility.calculateTotalOfValues(confusions);
                long addedConfusion = newConfusionTotal - confusionSoFar;

                // Calculate the new frequencies of all MCCs using the new lists
                LinkedHashMap<String, Long> allFrequencies = mccCalculator.calculateSortedFrequencies(in);

                // Was this chord encountered?
                Long frequency = allFrequencies.get(candidate);
                if (frequency != null) {
                    long mccSavings = Utility.calculateMccSavings(allFrequencies);
                    long gainedSavings = mccSavings - mccSavingSoFar;

                    // Add chord only if it gains us something
                    if (gainedSavings >= MIN_MCC_VALUE) {
                        long typedChords = fileLength - mccSavings;

                        // Rank is a product of confusions and how many chords need to be typed for a given text
                        // Rank needs to be minimized.
                        // Add 1 to addedConfusion to make sure typedChords is taken into account
                        long rank = (addedConfusion + 1) * typedChords;
                        ranks.put(candidate, rank);
                        extraConfusions.put(candidate, newConfusionTotal);
                        savingFromMCCs.put(candidate, mccSavings);
                    }
                }

                // Now that calculations are done, candidate can be removed
                mccCalculator.remove(candidate);
            }

//            LinkedHashMap<String, Long> sortedCandidateFrequencies = Utility.sortMap(candidateFrequencies);
//            System.out.println("sortedCandidateFrequencies " + sortedCandidateFrequencies.toString());

            LinkedHashMap<String, Long> sortedRanks = Utility.sortMapReverse(ranks);
            System.out.println("sortedRanks " + sortedRanks.toString());

            if (sortedRanks.isEmpty()) {
                System.out.println("sortedRanks empty. Done.");
                allMCCs.clear();
            } else {
                // Take MCC with the smallest rank
                Map.Entry<String, Long> entry = sortedRanks.entrySet().iterator().next();
                String mccToAdd = entry.getKey();

                long newConfusionTotal = extraConfusions.get(mccToAdd);
                long mccSavings = savingFromMCCs.get(mccToAdd);

                System.out.println("'" + mccToAdd + "' rank=" + entry.getValue()
                        + "' confusionSoFar=" + (newConfusionTotal - confusionSoFar)
                        + "' savings from adding=" + (mccSavings - mccSavingSoFar));
                confusionSoFar = newConfusionTotal;
                mccSavingSoFar = mccSavings;

                allMCCs.remove(mccToAdd);
                mccCalculator.add(mccToAdd);
                finalList.add(mccToAdd);

//                // Now we can calculate new confusions
//                HashMap<String, Long> confusions = mccCalculator.calculateSortedConfusions(in);
//                confusionSoFar = Utility.calculateTotalOfValues(confusions);

            }

// Calculate new frequencies to prune the new list from too rare MCCs
//            HashMap<String, Long> frequencies = mccCalculator.calculateSortedFrequencies(in);
//            System.out.println("New MCC frequencies " + frequencies.toString());
//            Set<String> allSortedMCCs = frequencies.keySet();
//            System.out.println("allSortedMCCs " + allSortedMCCs.toString());
//            for (each MCC)
//                removeTooRareMcc()
        }

        System.out.println("MCC finalList " + finalList.toString());


        return finalList;
    }
}
