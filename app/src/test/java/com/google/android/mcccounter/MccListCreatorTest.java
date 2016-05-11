package com.google.android.mcccounter;

import java.util.HashMap;
import java.util.LinkedHashMap;

// Created by alex on 5/11/2016

public class MccListCreatorTest {
    protected void outputResults(double percentToDiscard, long length) {
        System.out.println();
        System.out.println("maxSavings=" + MccListCreator.maxSavings
                + " mccLists.size=" + MccListCreator.mccListsSavings.size()
                + " duplicateBranchesCount=" + MccListCreator.duplicateBranchesCount);

        HashMap<String, Long> mccLists = new HashMap<>();
        long maxSavings = 0;
        for (LinkedHashMap.Entry<String, Long> mccList : MccListCreator.mccListsSavings.entrySet()) {
            String mccListKey = mccList.getKey();

            long listSavings = mccList.getValue();

            if (maxSavings < listSavings) {
                maxSavings = listSavings;
            }

            long listConfusions = MccListCreator.mccListsConfusions.get(mccListKey);

            int numberOfMccs = mccListKey.length() - mccListKey.replace(",", "").length() + 1;

            // Best lists should be at the top. It would have most savings per number of MCCs
            // and the least number of confusions
            // Make savings weigh more than numberOfMccs
            long listScore = (2 * listSavings - (numberOfMccs - 1)) * (length - (listConfusions - 1)) ;

            mccLists.put(mccListKey, listScore);
        }
        LinkedHashMap<String, Long> sortedMccLists = Utility.sortMap(mccLists);

        //System.out.print("mccListsSavings=");

        long previousSavings = 0;
        long previousConfusions = 0;
        int previousNumberOfMccs = 0;
        for (LinkedHashMap.Entry<String, Long> mccList : sortedMccLists.entrySet()) {
            long listScore = mccList.getValue();

            String mccListKey = mccList.getKey();

            long listSavings = MccListCreator.mccListsSavings.get(mccListKey);
            long listConfusions = MccListCreator.mccListsConfusions.get(mccListKey);

            int numberOfMccs = mccListKey.length() - mccListKey.replace(",", "").length() + 1;

            if (listSavings >= (long) (maxSavings * percentToDiscard)) {
                System.out.print(mccListKey + "(" + numberOfMccs + ")" + listSavings + ","  + listConfusions);

                if (numberOfMccs != previousNumberOfMccs) {
                    if (previousNumberOfMccs > 2) {
                        System.out.print(";" + (listSavings - previousSavings)
                                + "," + (listConfusions - previousConfusions));
                    }

                    previousSavings = listSavings;
                    previousConfusions = listConfusions;
                    previousNumberOfMccs = numberOfMccs;
                }
                System.out.print(";" + listScore + " ");

                if (mccListKey.length() > 30) {
                    System.out.println();
                }
            }
        }
    }
}