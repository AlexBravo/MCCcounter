package com.google.android.mcccounter;

import android.support.annotation.NonNull;

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
    public static HashMap<String, Integer> sortMap(HashMap<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue()) * -1;
            }
        });
        HashMap<String, Integer> returnVal = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            returnVal.put( entry.getKey(), entry.getValue() );
        }
        return returnVal;
    }
}
