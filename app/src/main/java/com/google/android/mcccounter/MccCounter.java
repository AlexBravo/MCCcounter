package com.google.android.mcccounter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Created by Maxim on 12/12/2015.

class MccCounter {
    private static List<String> shortMccList = Arrays.asList(
        "re", "in", "or", "an", "al", "at", "ar", "co", "ch", "de",
        "of", "ro", "it", "er", "on", "to", "st", "me", "il", "en");

    private static List<String> longMccList = Arrays.asList("ing", "and", "the");

    static Map<String, Long> calculateMCCs(String in) {
        Map<String, Long> mccs = new HashMap<>();
        if(in.length() > 1) {
            if (in.length() < 3) {
                mccs.putAll(searchThroughShortR(in));
            } else {
                mccs.putAll(searchThroughLong(in));
                mccs.putAll(searchThroughShortR(in));
                mccs = ridOfShortRepetition(mccs);
            }
        }
        return mccs;
    }

    private static Map<String, Long> ridOfShortRepetition(Map<String, Long> list){
        if(list.containsKey("in") && list.containsKey("ing")){
            if(list.get("in") <= list.get("ing")){
                list.remove("in");
            } else {
                list.put("in", list.get("in")-list.get("ing"));
            }
        }
        if(list.containsKey("an") && list.containsKey("and")){
            if(list.get("an") <= list.get("and")){
                list.remove("an");
            } else {
                list.put("an", list.get("an")-list.get("and"));
            }
        }
        if(list.containsKey("th") && list.containsKey("the")){
            if(list.get("th") <= list.get("the")){
                list.remove("th");
            } else {
                list.put("th", list.get("th")-list.get("the"));
            }
        }
        if(list.containsKey("on") && list.containsKey("ion")){
            if(list.get("on") <= list.get("ion")){
                list.remove("on");
            } else {
                list.put("on", list.get("on")-list.get("ion"));
            }
        }
        return list;
    }

    @SuppressWarnings("unused")
    public static Map<String, Long> searchThroughShort(String toSearch){
        Map<String, Long> results = new HashMap<>();
        for(int i = 0; i < toSearch.length() - 1; i++){
            String searchable = toSearch.substring(i, i + 2);
            for(int j = 0; j < shortMccList.size(); j++){
                String mcc = shortMccList.get(j);
                if(searchable.equals(mcc)){
                    if (results.containsKey(mcc)) {
                        results.put(mcc, results.get(mcc + 1));
                        i++;
                    } else {
                        results.put(mcc, 1L);
                        i++;
                    }
                }
            }
        }
        return results;
    }

    private static Map<String, Long> searchThroughShortR(String toSearch){
        Map<String, Long> results = new HashMap<>();
        for(int i = toSearch.length() - 1; i > 2; i--){
            String searchable = toSearch.substring(i - 2, i);
            for(int j = 0; j < shortMccList.size(); j++){
                String mcc = shortMccList.get(j);
                if(searchable.equals(mcc)){
                    if (results.containsKey(mcc)) {
                        results.put(mcc, results.get(mcc) + 1);
                        i--;
                    } else {
                        results.put(mcc, 1L);
                        i--;
                    }
                }
            }
        }
        return results;
    }

    private static Map<String, Long> searchThroughLong(String toSearch){
        Map<String, Long> results = new HashMap<>();

        for(int i = 0; i < toSearch.length() - 2; i++){
            String searchable = toSearch.substring(i, i + 3);
            for(int j = 0; j < longMccList.size(); j++){
                String mcc = shortMccList.get(j);
                if(searchable.equals(mcc)){
                    if (results.containsKey(mcc)) {
                        results.put(mcc, results.get(mcc) + 1);
                        i+=2;
                    } else {
                        results.put(mcc, 1L);
                        i+=2;
                    }
                }
            }
        }
        return results;
    }

    @SuppressWarnings("unused")
    public static Map<String, Long> searchThroughLongR(String toSearch){
        Map<String, Long> results = new HashMap<>();
        for(int i = toSearch.length(); i > 0; i--){
            String searchable = toSearch.substring(i - 3, i);
            for(int j = 0; j < longMccList.size(); j++){
                String mcc = shortMccList.get(j);
                if(searchable.equals(mcc)){
                    if (results.containsKey(mcc)) {
                        results.put(mcc, results.get(mcc) + 1);
                        i-=2;
                    } else {
                        results.put(mcc, 1L);
                        i-=2;
                    }
                }
            }
        }
        return results;
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
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
}