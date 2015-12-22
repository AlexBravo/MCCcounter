package com.google.android.mcccounter;

import java.util.HashMap;
import java.util.Map;

//Created by Maxim on 12/12/2015.

public class MccCounter {
    static String[] shortMccList = {"in", "or", "an", "th", "al", "at", "ma", "ar", "es",
                                    "st", "co", "il", "ac", "ch", "no", "de", "se", "ed",
                                    "of", "le", "ro", "ou", "ti", "is", "it", "en", "re",
                                    "er", "on", "to"};
    static String[] longMccList = {"ing", "and", "the", "ion"};

    public static Map<String, Integer> calculateMCCs(String in) {
        String input = in;
        Map<String, Integer> mccs = new HashMap<>();
        if(input.length() > 1) {
            if (input.length() < 3) {
                putInMap(searchThroughShortR(input), mccs);
            } else {
                putInMap(searchThroughLong(input), mccs);
                putInMap(searchThroughShortR(input), mccs);
                mccs = ridOfShortRepitition(mccs);
            }
        }
        return mccs;
    }



    public static Map<String, Integer> ridOfShortRepitition(Map<String, Integer> list){
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


    public static Map<String, Integer> searchThroughShort(String toSearch){
        Map<String, Integer> results = new HashMap<>();
        String searchable = "";
        for(int i = 0; i < toSearch.length() - 1; i++){
            searchable = toSearch.substring(i, i + 2);
            for(int j = 0; j < shortMccList.length; j++){
                if(searchable.equals(shortMccList[j])){
                    if (results.containsKey(shortMccList[j])) {
                        results.put(shortMccList[j], results.get(shortMccList[j]) + 1);
                        i++;
                    } else {
                        results.put(shortMccList[j], 1);
                        i++;
                    }
                }
            }
        }
        return results;
    }

    public static Map<String, Integer> searchThroughShortR(String toSearch){
        Map<String, Integer> results = new HashMap<>();
        String searchable = "";
        for(int i = toSearch.length() - 1; i > 2; i--){
            searchable = toSearch.substring(i - 2, i);
            for(int j = 0; j < shortMccList.length; j++){
                if(searchable.equals(shortMccList[j])){
                    if (results.containsKey(shortMccList[j])) {
                        results.put(shortMccList[j], results.get(shortMccList[j]) + 1);
                        i--;
                    } else {
                        results.put(shortMccList[j], 1);
                        i--;
                    }
                }
            }
        }
        return results;
    }



    public static Map<String, Integer> searchThroughLong(String toSearch){
        Map<String, Integer> results = new HashMap<>();
        String searchable = "";
        for(int i = 0; i < toSearch.length() - 2; i++){
            searchable = toSearch.substring(i, i + 3);
            for(int j = 0; j < longMccList.length; j++){
                if(searchable.equals(longMccList[j])){
                    if (results.containsKey(longMccList[j])) {
                        results.put(longMccList[j], results.get(longMccList[j]) + 1);
                        i+=2;
                    } else {
                        results.put(longMccList[j], 1);
                        i+=2;
                    }
                }
            }
        }
        return results;
    }

    public static Map<String, Integer> searchThroughLongR(String toSearch){
        Map<String, Integer> results = new HashMap<>();
        String searchable = "";
        for(int i = toSearch.length(); i > 0; i--){
            searchable = toSearch.substring(i - 3, i);
            for(int j = 0; j < longMccList.length; j++){
                if(searchable.equals(longMccList[j])){
                    if (results.containsKey(longMccList[j])) {
                        results.put(longMccList[j], results.get(longMccList[j]) + 1);
                        i-=2;
                    } else {
                        results.put(longMccList[j], 1);
                        i-=2;
                    }
                }
            }
        }
        return results;
    }

    public static void putInMap(Map<String, Integer> toPut, Map<String, Integer> toPutIn){
        toPutIn.putAll(toPut);
    }
}