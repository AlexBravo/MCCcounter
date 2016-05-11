package com.google.android.mcccounter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/** Created by alex on 3/20/2016. */

public class MccCalculator {
    private List<String> shortList = new ArrayList<>();
    private List<String> longList = new ArrayList<>();

    private HashMap<String, Long> frequencies = new HashMap<>();
    private HashMap<String, Long> confusions = new HashMap<>();
    private HashMap<String, Long> typedChars = new HashMap<>();

//    public MccCalculator(List<String> shortList, List<String> longList) {
//        this.shortList = shortList;
//        this.longList = longList;
//    }

    public void calculateFrequenciesAndConfusions(String in) {
        // Go through the input text and find MCCs in them
        frequencies.clear();
        confusions.clear();
        typedChars.clear();

        // Go through the input text and find MCCs in them
        int i = 0;
        int length = in.length();
        String longMcc;
        String shortMcc;
        String secondShort;
        String secondLong;

        String typedMcc;
        String confusion;

        String typedChar;
        // Go all the way to one letter before the last one
        while (i < length - 1) {
            typedMcc = null;
            confusion = null;
            // It's OK for the second parameter of substring() to go all the way to be equal lenght()
            if (i + 3 <= length) {
                // Account for the fact that all MCCs have capitalized first letter
                longMcc = in.substring(i, i + 1).toLowerCase() + in.substring(i + 1, i + 3);
                if (longList.contains(longMcc)) {
                    // Don't look for 4th letter.
                    // "thet" is not confusing if there are only "the" and "et",
                    // as there needs to be "th", "the" and "et" to be confusing
                    typedMcc = longMcc;
                }
            }

            if (typedMcc == null) {
                // Account for the fact that all MCCs have capitalized first letter
                shortMcc = in.substring(i, i + 1).toLowerCase() + in.substring(i + 1, i + 2);
                if (shortList.contains(shortMcc)) {
                    // This is what is going to be typed, unless there are exceptions
                    typedMcc = shortMcc;

                    if (i + 3 <= length) {
                        // Don't count the capitalized second MCC
                        secondShort = in.substring(i + 1, (i + 1) + 2);
                        if (shortList.contains(secondShort)) {
                            if (secondShort.equals("th")
                                    || secondShort.equals("st")
                                    || secondShort.equals("ch")) {
                                // secondShort will be typed in the next loop iteration
                                // in this iteration just type one char
                                typedMcc = null;
                            } else {
                                confusion = shortMcc + secondShort.charAt(1);
                            }
                        } else if (i + 4 <= in.length()) {
                            // Look for confusions like "at"-"the" in "rather"

                            // Don't count capitalized second MCC
                            secondLong = in.substring(i + 1, (i + 1) + 3);
                            if (longList.contains(secondLong)) {
                                confusion = shortMcc + secondLong.substring(1, 3);
                                // secondShort will be typed in the next loop iteration
                                // in this iteration just type one char
                                typedMcc = null;
                            }
                        }
                    }
                }
            }

            if (typedMcc != null) {
                Utility.addToMap(typedMcc, frequencies);
                i += typedMcc.length();
            } else {
                typedChar = in.substring(i, i + 1).toLowerCase();
                Utility.addToMap(typedChar, typedChars);
                i++;
            }

            if (confusion != null) {
                Utility.addToMap(confusion, confusions);
            }
        }
    }

    public LinkedHashMap<String, Long> getSortedFrequencies(int minMccFrequency) {
        // Is each MCC still used enough?

        // Avoid this situation:
        //    Assume input string is "the".
        //    "he" will be added first to the list of MCCs.
        //    then when "the" is added to the list "he", savings are increased from 1 to 2,
        //    but "he" is not encountered anymore, making the list "he, "the" not a valid list
        if (frequencies.size() != longList.size() + shortList.size()) {
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
}
