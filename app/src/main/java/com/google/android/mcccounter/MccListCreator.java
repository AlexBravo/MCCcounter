package com.google.android.mcccounter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Created by alex on 4/6/16. */
public class MccListCreator {

    public static List<String> createMccList(String in, int minMccValue, int maxConfusionDelta) {
        long fileLength = in.length();

        List<String> allMCCs = new ArrayList<>(MccLists.fullLongMccList);
        allMCCs.addAll(MccLists.fullShortMccList);

        List<String> finalList = new ArrayList<>();

        HashMap<String, Long> ranks = new HashMap<>();
        HashMap<String, Long> newConfusions = new HashMap<>();
        HashMap<String, Long> mccSavings = new HashMap<>();

        long confusionSoFar = 0;
        long savingSoFar = 0;
        MccCalculator mccCalculator = new MccCalculator();
        while (!allMCCs.isEmpty()) {
            ranks.clear();
            newConfusions.clear();
            mccSavings.clear();

            for (String candidate : allMCCs) {
                mccCalculator.add(candidate);

                // TODO: Number of occurrence of an MCCs should be calculated for each MCC based on the current list of MCCs
                //int occurrences = 1; //mccCalculator.calculateChords(in, candidate);
                //int occurrences = frequencies.get(candidate);


                // Calculate the sum of all confusions
                HashMap<String, Long> confusions = mccCalculator.calculateSortedConfusions(in);
                long newConfusionTotal = Utility.calculateTotalOfValues(confusions);
                long confusionsDelta = newConfusionTotal - confusionSoFar;

                // Calculate the new frequencies of all MCCs using the new lists
                LinkedHashMap<String, Long> allFrequencies = mccCalculator.calculateSortedFrequencies(in);

                // Was this chord encountered?
                Long frequency = allFrequencies.get(candidate);
                if (frequency != null) {
                    long newSavingsTotal = Utility.calculateMccSavings(allFrequencies);
                    long savingsDelta = newSavingsTotal - savingSoFar;

                    // Add chord only if it gains us something
                    if (savingsDelta < minMccValue) {
                        //System.out.println("'" + candidate + "' too low savingsDelta=" + savingsDelta);
                    } else if (confusionsDelta > maxConfusionDelta) {
                        System.out.println("'" + candidate + "' too high confusionsDelta=" + confusionsDelta);
                        
                    // TODO: Think about how to implement this restraint better
                    //} else if (confusionsDelta > savingsDelta) {
                        //System.out.println("'" + candidate + "' confusionsDelta=" + confusionsDelta
                        //        + " > savingsDelta=" + savingsDelta);
                    } else {
                        long typedChords = fileLength - newSavingsTotal;

                        // Rank is a product of confusions and how many chords need to be pressed to type a given text
                        // Rank needs to be minimized.
                        // Add 1 to confusionsDelta to make sure typedChords is taken into account
                        long rank = (newConfusionTotal + 1) * typedChords;
                        ranks.put(candidate, rank);
                        newConfusions.put(candidate, newConfusionTotal);
                        mccSavings.put(candidate, newSavingsTotal);
                    }
                }

                // Now that calculations are done, candidate can be removed
                mccCalculator.remove(candidate);
            }

//            LinkedHashMap<String, Long> sortedCandidateFrequencies = Utility.sortMap(candidateFrequencies);
//            System.out.println("sortedCandidateFrequencies " + sortedCandidateFrequencies.toString());

            LinkedHashMap<String, Long> sortedRanks = Utility.sortMapReverse(ranks);
            //System.out.println("sortedRanks " + sortedRanks.toString());

            if (sortedRanks.isEmpty()) {
                System.out.println("sortedRanks empty. Done.");
                allMCCs.clear();
            } else {
                // Take MCC with the smallest rank
                Iterator<Map.Entry<String, Long>> iterator = sortedRanks.entrySet().iterator();
                Map.Entry<String, Long> entry = iterator.next();

                String mccToAdd = entry.getKey();
                long rank = entry.getValue();

                // See if we have more choices than one
                // TODO: Add processing for all choices.
                // TODO: The ranks don't have to be equal, they can be close enough to create a "choice"
                while (iterator.hasNext()) {
                    Map.Entry<String, Long> nextEntry = iterator.next();
                    if (nextEntry.getValue() == rank) {
                        System.out.println("Alternative choice found for '" + mccToAdd
                                + "' : '" + nextEntry.getKey() + "'");
                    }
                }

                long newConfusionTotal = newConfusions.get(mccToAdd);
                long confusionsDelta = newConfusionTotal - confusionSoFar;

                long newSavingsTotal = mccSavings.get(mccToAdd);
                long savingsDelta = newSavingsTotal - savingSoFar;

                System.out.println("'" + mccToAdd
                        + "' savingsDelta=" + savingsDelta
                        + ", confusionsDelta=" + confusionsDelta
                        + ", rank=" + rank);
                confusionSoFar = newConfusionTotal;
                savingSoFar = newSavingsTotal;

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

        System.out.println("MCC finalList(" + finalList.size() + "): " + finalList.toString());


        return finalList;
    }
}
