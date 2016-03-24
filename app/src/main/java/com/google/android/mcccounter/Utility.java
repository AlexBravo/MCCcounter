package com.google.android.mcccounter;

/**
 * Created by wendy on 3/20/2016.
 */
public class Utility {
    static String[] shortMccList = {"in", "or", "an", "th", "al", "at", "ma", "ar", "es",
            "st", "co", "il", "ac", "ch", "no", "de", "se", "ed",
            "of", "le", "ro", "ou", "ti", "is", "it", "en", "re",
            "er", "on", "to"};
    static String[] longMccList = {"ing", "and", "the", "ion"};

    public static boolean lookThroughShorts(String in) {
        if(in.length() != 2) {
            return false;
        }
        for(String s : shortMccList){
            if(s.equals(in)) {
                return true;
            }
        }
        return false;
    }

    public static boolean lookThroughLongs(String in) {
        if(in.length() != 3) {
            return false;
        }
        for(String s : longMccList){
            if(s.equals(in)) {
                return true;
            }
        }
        return false;
    }
    /*public static HashMap<String, Integer> sort(HashMap<String, Integer> hashMap) {
        int largest = 0;
        String largestKey = "";
        for(String s : hashMap.keySet()) {
            if(hashMap.get(s) > largest){
                largest = hashMap.get(s);
                largestKey = s;
            }
        }
    }*/
}
