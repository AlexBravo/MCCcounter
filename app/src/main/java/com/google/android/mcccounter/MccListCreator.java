package com.google.android.mcccounter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Created by alex on 4/6/16. */

public class MccListCreator {
    public static final double BRANCH_TOLERANCE = 1.01; // 1%

    public static int duplicateBranchesCount = 0;
    public static long maxSavings;

    private MccCalculator mccCalculator = new MccCalculator();

    public static HashMap<String, Long> mccLists = new HashMap<>();

    private HashMap<String, Long> ranks = new HashMap<>();
    private HashMap<String, Long> newConfusions = new HashMap<>();
    private HashMap<String, Long> mccSavings = new HashMap<>();

    private int minMccValue;
    private int maxConfusionDelta;

    private String in;
    private long fileLength;


    public MccListCreator(String in, int minMccValue, int maxConfusionDelta) {
        this.in = in;
        this.fileLength = in.length();

        this.minMccValue = minMccValue;
        this.maxConfusionDelta = maxConfusionDelta;
    }

    public void createMccList(List<String> mccList, long savingsSoFar, long confusionsSoFar,
                              int recursionLevel) {

        List<String> mccsToConsider = new ArrayList<>(MccLists.fullLongMccList);
        mccsToConsider.addAll(MccLists.fullShortMccList);

        // Init mccCalculator lists
        for (String mcc : mccList) {
            mccCalculator.add(mcc);
            mccsToConsider.remove(mcc);
        }

        ranks.clear();
        newConfusions.clear();
        mccSavings.clear();

        // Evaluate all candidates
        for (String candidate : mccsToConsider) {
            String mccListAsString = isDuplicateMccList(mccList, candidate);

            if (mccLists.get(mccListAsString) != null) {
                duplicateBranchesCount++;
                System.out.print(".");
            } else {
                mccCalculator.add(candidate);

                evaluateCandidate(candidate, savingsSoFar, confusionsSoFar);

                mccCalculator.remove(candidate);
            }
        }

        List<String> branches = findMccsToAdd(ranks);
        if (branches.isEmpty()) {
            if (savingsSoFar > maxSavings) {
                maxSavings = savingsSoFar;
            }

            System.out.println("mccList(" + mccList.size() + ") " + mccList);
            System.out.println("   savingsSoFar=" + savingsSoFar + ", maxSavings=" + maxSavings
                    + " mccLists size=" + mccLists.size() + " duplicateBranchesCount=" + duplicateBranchesCount);
            return;
        }

        // TODO: Use relative rank to decide which branch to use first
        for (String mccToAdd : branches) {
            long newConfusionTotal = newConfusions.get(mccToAdd);
            long confusionsDelta = newConfusionTotal - confusionsSoFar;

            long newSavingsTotal = mccSavings.get(mccToAdd);
            long savingsDelta = newSavingsTotal - savingsSoFar;

            String mccListAsString = isDuplicateMccList(mccList, mccToAdd);
            if (mccLists.get(mccListAsString) != null) {
                // We should not get here
                System.out.println("Error: MCC list already exists " + mccListAsString);
                continue;
            }

            mccLists.put(mccListAsString, newSavingsTotal);

            mccList.add(mccToAdd);

            System.out.println("added '" + mccToAdd + "' savingsDelta=" + savingsDelta
                    + ", confusionsDelta=" + confusionsDelta + ", rank=" + ranks.get(mccToAdd)
                    + ", l=" + recursionLevel);
            System.out.println("  savingsSoFar=" + savingsSoFar + "', maxSavings=" + maxSavings
                    + "', confusionsSoFar=" + confusionsSoFar);

            // Go into this branch
            MccListCreator mccListCreator = new MccListCreator(in, minMccValue, maxConfusionDelta);
            mccListCreator.createMccList(mccList, newSavingsTotal, newConfusionTotal, ++recursionLevel);

            mccList.remove(mccToAdd);
        }

        // Many lists were created, so can't just return one list
    }

    private String isDuplicateMccList(List<String> mccList, String candidate) {
        // Have this MCC list been evaluated before
        ArrayList<String> sortedMccList = new ArrayList<>(mccList);
        sortedMccList.add(candidate);
        Collections.sort(sortedMccList);
        return sortedMccList.toString();
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
                //System.out.println("'" + candidate + "' too high confusionsDelta=" + confusionsDelta);

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

    public List<String> findMccsToAdd(HashMap<String, Long> ranks) {
        LinkedHashMap<String, Long> sortedRanks = Utility.sortMapReverse(ranks);
        //System.out.println("sortedRanks " + sortedRanks);

        ArrayList<String> branches = new ArrayList<>();

        if (sortedRanks.isEmpty()) {
            return branches;
        }

        // Look for MCCs with the smallest rank
        Iterator<Map.Entry<String, Long>> iterator = sortedRanks.entrySet().iterator();
        Map.Entry<String, Long> firstEntry = iterator.next();

        String firstEntryKey = firstEntry.getKey();
        long firstEntryRank = firstEntry.getValue();

        branches.add(firstEntryKey);

        // See if we have more choices than one
        // TODO: Add processing for all choices.
        // TODO: The ranks don't have to be equal, they can be close enough to create a "choice"
        while (iterator.hasNext()) {
            Map.Entry<String, Long> nextEntry = iterator.next();
            if (nextEntry.getValue() < firstEntryRank * BRANCH_TOLERANCE) {
                //System.out.println("Alternative choice found for '" + firstEntryKey
                //        + "' : '" + nextEntry.getKey() + "'");

                branches.add(nextEntry.getKey());
            }
        }

        return branches;
    }
}
