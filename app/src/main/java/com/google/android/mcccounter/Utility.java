package com.google.android.mcccounter;

/**
 * Created by wendy on 3/20/2016.
 */
public class Utility {
//    static String[] shortMccList = {"in", "or", "an", "th", "al", "at", "ma", "ar", "es",
//            "co", "il", "ac", "ch", "no", "de", "se", "ed",
//            "of", "le", "ro", "ou", "ti", "is", "it", "en",
//            "er", "on", "to"};
    static String[] shortMccList = {"in", "or", "an", "th", "al", "at", "ar", "es",
            "de", "il", "ac", "ch", "no", "ed",
            "of", "ro", "ou", "is", "it", "en",
            "er", "on", "to" }; // Removed: re, ti, le, co, se, ma, st
    static String[] longMccList = {"ing", "and", "the"};

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
    public static boolean lookThroughMccArraylist(String in, String[] mccs) {
        if(in.length() != 2) {
            return false;
        }
        for(String s : mccs){
            if(s.equals(in)) {
                return true;
            }
        }
        return false;
    }


}
