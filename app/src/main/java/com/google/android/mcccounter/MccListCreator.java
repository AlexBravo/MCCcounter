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
    public static int duplicateBranchesCount = 0;
    public static long maxSavings;
    public static HashMap<String, Long> mccListsSavings = new HashMap<>();
    public static HashMap<String, Long> mccListsConfusions = new HashMap<>();

    private static int minMccFrequency;
    private static int maxConfusionDelta;
    private static double rankIncreasePercent;

    private static String in;
    private static long fileLength;


    public MccListCreator(String in, int minMccFrequency, int maxConfusionDelta, double rankIncreasePercent) {
        MccListCreator.in = in;
        MccListCreator.fileLength = in.length();

        MccListCreator.minMccFrequency = minMccFrequency;
        MccListCreator.maxConfusionDelta = maxConfusionDelta;
        MccListCreator.rankIncreasePercent = rankIncreasePercent;
    }

    public void createMccList(ArrayList<String> mccList) {
        MccCalculator mccCalculator = new MccCalculator();
        for (String mcc : mccList) {
            mccCalculator.add(mcc);
        }

        long newSavingsTotal = 0;
        long newConfusionTotal = 0;
        if (!mccList.isEmpty()) {
            // Calculate the new frequencies of all MCCs using the new lists
            mccCalculator.calculateFrequenciesAndConfusions(in);
            LinkedHashMap<String, Long> frequencies = mccCalculator.getSortedFrequencies(minMccFrequency);
            if (frequencies != null) {

                newSavingsTotal = Utility.calculateMccSavings(frequencies);

                // Calculate the sum of all confusions
                HashMap<String, Long> confusions = mccCalculator.getSortedConfusions();
                newConfusionTotal = Utility.calculateTotalOfValues(confusions);

                System.out.println("frequencies(" + frequencies.size() + ")=" + frequencies.toString());
                System.out.println("confusions(" + confusions.size() + ")="+ confusions.toString());

                System.out.println("newSavingsTotal=" + newSavingsTotal
                        + ", newConfusionTotal=" + newConfusionTotal);

                HashMap<String, Long> typedChars = mccCalculator.getSortedTypedChars();
                System.out.print("typedChars(" + typedChars.size() + ")=");
                // Don't output too rare chars
                for (LinkedHashMap.Entry<String, Long> typedChar : typedChars.entrySet()) {
                    if (typedChar.getValue() > 10) {
                        System.out.print(typedChar.getKey() + "=" + typedChar.getValue() + " ");
                    }
                }
                System.out.println();
            }
        }

        // Call recursive method
        addToMccList(mccList, newSavingsTotal, newConfusionTotal, 0);
    }

    // This is a recursive method
    public void addToMccList(ArrayList<String> mccList, long savingsSoFar, long confusionsSoFar,
                              int recursionLevel) {

        ArrayList<String> mccsToConsider = new ArrayList<>(MccLists.fullLongMccList);
        mccsToConsider.addAll(MccLists.fullShortMccList);

        // Add MCCs from mccList to mccCalculator and remove them from mccsToConsider
        MccCalculator mccCalculator = new MccCalculator();
        for (String mcc : mccList) {
            mccCalculator.add(mcc);
            mccsToConsider.remove(mcc);
        }

        HashMap<String, Long> candidatesRanks = new HashMap<>();
        HashMap<String, Long> candidatesConfusions = new HashMap<>();
        HashMap<String, Long> candidatesSavings = new HashMap<>();

        // Evaluate all candidates
        for (String candidate : mccsToConsider) {
            String mccListAsString = getMccListAsString(mccList, candidate);
            // Have this MCC list been evaluated before?
            if (mccListsSavings.get(mccListAsString) != null) {
                duplicateBranchesCount++;
                System.out.print(".");
            } else {
                evaluateCandidate(mccCalculator, candidate, savingsSoFar, confusionsSoFar,
                        candidatesRanks, candidatesConfusions, candidatesSavings);
            }
        }

        if (candidatesRanks.isEmpty()) {
            // We are done. No other candidates to add.
            return;
        }

        List<String> branches = findMccsToAdd(candidatesRanks);

        for (String mccToAdd : branches) {
            String mccListAsString = getMccListAsString(mccList, mccToAdd);
            // Have this MCC list been evaluated before?
            if (mccListsSavings.get(mccListAsString) != null) {
                //System.out.println("Error: MCC list already exists " + mccListAsString);
                continue;
            }

            mccList.add(mccToAdd);

            long mccToAddConfusions = candidatesConfusions.get(mccToAdd);
            long mccToAddSavings = candidatesSavings.get(mccToAdd);

            System.out.print(mccToAdd + " ");
            //long confusionsDelta = mccToAddConfusions - confusionsSoFar;
            //long savingsDelta = mccToAddSavings - savingsSoFar;
            //
            //System.out.println("added '" + mccToAdd + "' savingsDelta=" + savingsDelta
            //        + ", confusionsDelta=" + confusionsDelta + ", rank=" + ranks.get(mccToAdd)
            //        + ", l=" + recursionLevel);

            // Go into this branch by recursively calling yourself
            addToMccList(mccList, mccToAddSavings, mccToAddConfusions, ++recursionLevel);

            // Add this list at the end, so we don't see the same mccListsSavings.size
            mccListsSavings.put(mccListAsString, mccToAddSavings);
            mccListsConfusions.put(mccListAsString, mccToAddConfusions);

            System.out.println();
            System.out.print("mccList(" + mccList.size() + ") " + mccList);
            if (mccToAddSavings < maxSavings * 0.95) {
                // Not worth printing info for this list that doesn't give enough savings
                System.out.print(" " + mccToAddSavings + " ");
            } else {
                if (maxSavings < mccToAddSavings) {
                    maxSavings = mccToAddSavings;
                }
                System.out.println();
                System.out.println("  maxSavings=" + maxSavings + " savings=" + mccToAddSavings
                        + ", confusions=" + mccToAddConfusions);
                System.out.println("  mccLists.size=" + mccListsSavings.size()
                        + " duplicateBranchesCount=" + duplicateBranchesCount);
            }

            mccList.remove(mccToAdd);
        }

        // Many lists were possibly created, so can't just return one list.
        // Also all different mccToAdd were removed from the final list.
    }

    private String getMccListAsString(List<String> mccList, String candidate) {
        ArrayList<String> sortedMccList = new ArrayList<>(mccList);
        sortedMccList.add(candidate);
        Collections.sort(sortedMccList);
        return sortedMccList.toString();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private void evaluateCandidate(MccCalculator mccCalculator, String candidate,
                                   long savingSoFar, long confusionSoFar,
                                   HashMap<String, Long> ranks,
                                   HashMap<String, Long> newConfusions,
                                   HashMap<String, Long> newMccSavings) {

        mccCalculator.add(candidate);

        // TODO: Number of occurrence of an MCCs should be calculated for each MCC
        // TODO: based on the current list of MCCs
        //int occurrences = 1; //mccCalculator.calculateChords(in, candidate);
        //int occurrences = frequencies.get(candidate);

        // Calculate the new frequencies of all MCCs using the new lists
        mccCalculator.calculateFrequenciesAndConfusions(in);

        LinkedHashMap<String, Long> frequencies = mccCalculator.getSortedFrequencies(minMccFrequency);

        // Were all chords used?
        //Long frequency = allFrequencies.get(candidate);
        if (frequencies != null) {
            long newSavingsTotal = Utility.calculateMccSavings(frequencies);
            long savingsDelta = newSavingsTotal - savingSoFar;

            // Calculate the sum of all confusions
            HashMap<String, Long> confusions = mccCalculator.getSortedConfusions();
            long newConfusionTotal = Utility.calculateTotalOfValues(confusions);
            long confusionsDelta = newConfusionTotal - confusionSoFar;

            // Add chord only if it gains us something

            // 3-letter MCC has double of the value of 2-letter MCC,
            // but the cost of pressing a both long and short chord is the same,
            // so no need to look at the length of MCC
            //if (savingsDelta < minMccFrequency * (candidate.length() - 1)) {

            if (savingsDelta < minMccFrequency) {
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
                newMccSavings.put(candidate, newSavingsTotal);
            }
        }

        mccCalculator.remove(candidate);
    }

    public List<String> findMccsToAdd(HashMap<String, Long> ranks) {
        LinkedHashMap<String, Long> sortedRanks = Utility.sortMapReverse(ranks);
        //System.out.println("sortedRanks " + sortedRanks);

        ArrayList<String> branches = new ArrayList<>();

        // Look for MCCs with the smallest rank
        Iterator<Map.Entry<String, Long>> iterator = sortedRanks.entrySet().iterator();
        Map.Entry<String, Long> firstEntry = iterator.next();

        String firstEntryKey = firstEntry.getKey();
        long firstEntryRank = firstEntry.getValue();

        branches.add(firstEntryKey);

        // See if we have more choices than one
        while (iterator.hasNext()) {
            Map.Entry<String, Long> nextEntry = iterator.next();
            // Reject choices that have too large rank
            if (nextEntry.getValue() <= firstEntryRank * rankIncreasePercent) {
                //System.out.println("Alternative choice found for '" + firstEntryKey
                //        + "' : '" + nextEntry.getKey() + "'");

                branches.add(nextEntry.getKey());
            }
            //else {
            //      System.out.println("Rejected choice for '" + firstEntryKey
            //              + "' : '" + nextEntry.getKey()
            //              + "' nextEntry rank=" + nextEntry.getValue());
            //  }
        }

        return branches;
    }
}
