package com.google.android.mcccounter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Created by alex on 4/6/16. */

public class MccListCreator {
    public static final double BRANCH_TOLERANCE = 1.01; // 1%

    private HashMap<String, Long> ranks = new HashMap<>();
    private HashMap<String, Long> newConfusions = new HashMap<>();
    private HashMap<String, Long> mccSavings = new HashMap<>();

    private MccCalculator mccCalculator = new MccCalculator();

    private int minMccValue;
    private int maxConfusionDelta;

    private String in;
    private long fileLength;

    public List<String> createMccList(String in, int minMccValue, int maxConfusionDelta) {
        this.in = in;
        this.fileLength = in.length();
        this.minMccValue = minMccValue;
        this.maxConfusionDelta = maxConfusionDelta;

        List<String> finalList = new ArrayList<>();

        long confusionSoFar = 0;
        long savingSoFar = 0;

        List<String> allMCCs = new ArrayList<>(MccLists.fullLongMccList);
        allMCCs.addAll(MccLists.fullShortMccList);

        while (!allMCCs.isEmpty()) {
            ranks.clear();
            newConfusions.clear();
            mccSavings.clear();

            // Evaluate all candidates
            for (String candidate : allMCCs) {
                mccCalculator.add(candidate);

                evaluateCandidate(candidate, savingSoFar, confusionSoFar);

                mccCalculator.remove(candidate);
            }

            String mccToAdd = findMccToAdd(ranks);
            if (mccToAdd.isEmpty()) {
                System.out.println("mccToAdd is empty. Done.");
                System.out.println("discarded MCCs " + allMCCs);

                allMCCs.clear();
            } else {
                long newConfusionTotal = newConfusions.get(mccToAdd);
                long confusionsDelta = newConfusionTotal - confusionSoFar;

                long newSavingsTotal = mccSavings.get(mccToAdd);
                long savingsDelta = newSavingsTotal - savingSoFar;

                System.out.println("'" + mccToAdd
                        + "' savingsDelta=" + savingsDelta
                        + ", confusionsDelta=" + confusionsDelta
                        + ", rank=" + ranks.get(mccToAdd));
                confusionSoFar = newConfusionTotal;
                savingSoFar = newSavingsTotal;

                allMCCs.remove(mccToAdd);
                mccCalculator.add(mccToAdd);
                finalList.add(mccToAdd);
            }

// Calculate new frequencies to prune the new list from too rare MCCs
//            HashMap<String, Long> frequencies = mccCalculator.calculateSortedFrequencies(in);
//            System.out.println("New MCC frequencies " + frequencies);
//            Set<String> allSortedMCCs = frequencies.keySet();
//            System.out.println("allSortedMCCs " + allSortedMCCs);
//            for (each MCC)
//                removeTooRareMcc()
        }

        System.out.println("MCC finalList(" + finalList.size() + "): " + finalList);

        return finalList;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private void evaluateCandidate(String candidate, long savingSoFar, long confusionSoFar) {
        // TODO: Number of occurrence of an MCCs should be calculated for each MCC
        // TODO: based on the current list of MCCs
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
            } else if (confusionsDelta > savingsDelta) {
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
    }

    public String findMccToAdd(HashMap<String, Long> ranks) {
        LinkedHashMap<String, Long> sortedRanks = Utility.sortMapReverse(ranks);
        //System.out.println("sortedRanks " + sortedRanks);

        if (sortedRanks.isEmpty()) {
            return "";
        }

        // Look for MCCs with the smallest rank
        Iterator<Map.Entry<String, Long>> iterator = sortedRanks.entrySet().iterator();
        Map.Entry<String, Long> firstEntry = iterator.next();

        ArrayList<String> branches = new ArrayList<>();

        String firstEntryKey = firstEntry.getKey();
        long firstEntryRank = firstEntry.getValue();

        branches.add(firstEntryKey);

        // See if we have more choices than one
        // TODO: Add processing for all choices.
        // TODO: The ranks don't have to be equal, they can be close enough to create a "choice"
        while (iterator.hasNext()) {
            Map.Entry<String, Long> nextEntry = iterator.next();
            if (nextEntry.getValue() < firstEntryRank * BRANCH_TOLERANCE) {
                System.out.println("Alternative choice found for '" + firstEntryKey
                        + "' : '" + nextEntry.getKey() + "'");

                branches.add(nextEntry.getKey());
            }
        }

        // TODO: Use relative rank to decide which branch to use
        //noinspection UnnecessaryLocalVariable
        String mccToAdd = branches.get(0);

        return mccToAdd;
    }
}
