package com.google.android.mcccounter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/** Created by wendy on 3/20/2016. */

public class Utility {
    @SuppressWarnings("unused")
    public static boolean lookThroughShorts(String in) {
        if(in.length() != 2) {
            return false;
        }
        for(String s : MccLists.shortMccList){
            if(s.equals(in)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    public static boolean lookThroughLongs(String in) {
        if(in.length() != 3) {
            return false;
        }
        for(String s : MccLists.longMccList){
            if(s.equals(in)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    public static boolean isMccInList(String in, List<String> mccs) {
        return mccs.contains(in);
//        if(in.length() != shortMccLength) {
//            return false;
//        }
//        for(String s : mccs){
//            if(s.equals(in)) {
//                return true;
//            }
//        }
//        return false;
    }

    static String toString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @NonNull
    public static LinkedHashMap<String, Integer> sortMap(HashMap<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        LinkedHashMap<String, Integer> returnVal = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            returnVal.put( entry.getKey(), entry.getValue() );
        }
        return returnVal;
    }

    @NonNull
    public static LinkedHashMap<String, Integer> sortMapReverse(HashMap<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        LinkedHashMap<String, Integer> returnVal = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            returnVal.put( entry.getKey(), entry.getValue() );
        }
        return returnVal;
    }

    public static int calculateTotalOfValues(HashMap<String, Integer> map) {
        int mapTotal = 0;
        for(Integer i : map.values()){
            mapTotal += i;
        }
        return mapTotal;
    }

    public static int calculateMccSavings(HashMap<String, Integer> map) {
        int mapTotal = 0;
        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            // We have 1 press saved for each 2-letter MCC
            // and 2 press saved for each 3-letter MCC
            int mccSaving = entry.getKey().length() - 1;
            mapTotal += entry.getValue() * mccSaving;
        }
        return mapTotal;
    }

    // Less confusing and more frequent MCCs has the lowest confusion rank
    // TODO: Find a way to tell between MCC with low confusion and low frequency
    // TODO: and high confusion and high frequency
    @Nullable public static Integer calculateConfusionRank(double confusion, int frequency) {
        if(frequency == 0) {
            return null;
        }

        double dFrequency = (double)(frequency)/ 1000000.0; // in 1 Million
        if (confusion == 0.0) {
            // So we don't lose influence of frequency
            confusion = 1;
        }

        return (int)(confusion/dFrequency);
    }
}
