package com.google.android.mcccounter;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/** Created by alex on 3/20/2016. */

public class MccCalculator {
    private List<String> length2 = new ArrayList<>();
    private List<String> length3 = new ArrayList<>();
    private List<String> length4 = new ArrayList<>();

    private HashMap<String, Long> frequencies = new HashMap<>();
    private HashMap<String, Long> confusions = new HashMap<>();
    private HashMap<String, Long> typedChars = new HashMap<>();
    private HashMap<String, Long> typedMccs = new HashMap<>();

    public void calculateFrequenciesAndConfusions(String in) {
        // Go through the input text and find MCCs in them
        frequencies.clear();
        confusions.clear();
        typedChars.clear();
        typedMccs.clear();

        // Go through the input text and find MCCs in them
        int i = 0;
        int length = in.length();

        String typedMcc;

        // Go all the way to one letter before the last one
        while (i + 2 <= length) {
            // First just look for long MCC (they don't cause any confusions)
            typedMcc = lookForLongMcc(in, i, length);

            // Then look for short MCC, but now also look for confusions
            if (typedMcc == null) {
                typedMcc = lookForMccsAndConfusions(in, i, length);
            }

            if (typedMcc != null) {
                // Account for the fact that all MCCs come in pairs,
                // with the second MCC having capitalized first letter
                Utility.addToMap(typedMcc.toLowerCase(), frequencies);

                Utility.addToMap(typedMcc, typedMccs);
                i += typedMcc.length();
            } else {
                String typedChar = in.substring(i, i + 1).toLowerCase();
                Utility.addToMap(typedChar, typedChars);
                i++;
            }
        }
    }

    // Returns null if no MCC was found
    @Nullable
    private String lookForMccsAndConfusions(String in, int i, int length) {

        // Account for the fact that all MCCs come in pairs,
        // with the second MCC having capitalized first letter
        String firstChar = in.substring(i, i + 1);
        String firstOriginalMcc = firstChar + in.substring(i + 1, i + 2);

        String firstLowerChar = firstChar.toLowerCase();
        String firstMcc = firstLowerChar + in.substring(i + 1, i + 2);

        if (!length2.contains(firstMcc)) {
            return null;
        }

        // Look for confusion with 4-letter long MCC
        if (i + 5 <= length) {
            // Look for confusions like "at"-"the " in "rather "

            // Don't count capitalized second MCC
            String secondMCC = in.substring(i + 1, i + 5);
            if (length4.contains(secondMCC)) {
                String confusion = firstMcc + secondMCC.substring(1, 4);
                Utility.addToMap(confusion, confusions);

                return firstOriginalMcc;
            }
        }

        // Look for confusion with 3-letter long MCC
        if (i + 4 <= length) {
            // Look for confusions like "at"-"the" in "rather"

            // Don't count capitalized second MCC
            String secondMCC = in.substring(i + 1, i + 4);
            if (length3.contains(secondMCC)) {
                String confusion = firstMcc + secondMCC.substring(1, 3);
                Utility.addToMap(confusion, confusions);

                return firstOriginalMcc;
            }
        }

        // Look for confusion with short MCC
        if (i + 3 <= length) {
            // Don't count the capitalized second MCC
            String secondMCC = in.substring(i + 1, (i + 3));
            if (!length2.contains(secondMCC)) {
                return firstOriginalMcc;
            }

            // Look for MCCs that don't cause confusions

            //noinspection StatementWithEmptyBody
            if (secondMCC.equals("th") || secondMCC.equals("st") || secondMCC.equals("ch")) {
                // secondMCC will be typed in the next loop iteration
                // in this iteration just type one char
                return null;
            } else {
                String confusion = firstMcc + secondMCC.charAt(1);
                Utility.addToMap(confusion, confusions);

                return firstOriginalMcc;
            }
        }

        //if (i + 2 <= length) {
        return firstOriginalMcc;
        //}
    }

    // Returns null if no 4-letter or 3-letter MCC was found
    @Nullable
    private String lookForLongMcc(String in, int i, int length) {

        // Account for the fact that all MCCs come in pairs,
        // with the second MCC having capitalized first letter
        String firstChar = in.substring(i, i + 1);
        String firstLowerChar = firstChar.toLowerCase();

        // It's OK for the second parameter of substring() to go all the way to be equal length()
        if (i + 4 <= length) {
            String mccSuffix = in.substring(i + 1, i + 4);
            if (length4.contains(firstLowerChar + mccSuffix)) {
                // Don't look for 5th letter
                return firstChar + mccSuffix;
            }
        }

        if (i + 3 <= length) {
            String mccSuffix = in.substring(i + 1, i + 3);
            if (length3.contains(firstLowerChar + mccSuffix)) {
                // Don't look for 4th letter, because
                // "thet" is not confusing if there are only "the" and "et",
                // as there needs to be "th", "the" and "et" to be confusing
                return firstChar + mccSuffix;
            }
        }

        return null;
    }

    public LinkedHashMap<String, Long> getSortedFrequencies(int minMccFrequency) {
        // Is each MCC still used enough?

        // Avoid this situation:
        //    Assume input string is "the".
        //    "he" will be added first to the list of MCCs.
        //    then when "the" is added to the list "he", savings are increased from 1 to 2,
        //    but "he" is not encountered anymore, making the list "he, "the" not a valid list
        if (frequencies.size() != length4.size() + length3.size() + length2.size()) {
            return null;
        }
        // Now actually check min frequencies
        for (Long frequency : frequencies.values()) {
            if (frequency < minMccFrequency) {
                return null;
            }
        }
        return Utility.sortMap(frequencies);
    }

    public LinkedHashMap<String, Long> getSortedConfusions() {
        return Utility.sortMap(confusions);
    }

    public LinkedHashMap<String, Long> getSortedTypedChars() {
        return Utility.sortMap(typedChars);
    }

    public LinkedHashMap<String, Long> getSortedTypedMccs() {
        return Utility.sortMap(typedMccs);
    }

    public void add(String candidate) {
        switch (candidate.length()) {
            case 2:
                length2.add(candidate);
                break;
            case 3:
                length3.add(candidate);
                break;
            case 4:
                length4.add(candidate);
                break;
            default:
                break;
        }
    }

    public void remove(String candidate) {
        switch (candidate.length()) {
            case 2:
                length2.remove(candidate);
                break;
            case 3:
                length3.remove(candidate);
                break;
            case 4:
                length4.remove(candidate);
                break;
            default:
                break;
        }
    }
}
