package com.google.android.mcccounter;

import java.util.HashMap;
import java.util.Map;

//Created by Maxim on 12/12/2015.

public class MccCounter {
    public static Map<String, Integer> calculateMCCs(String in) {
        Map<String, Integer> mccs = new HashMap<>();
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

    public static Map<String, Integer> ridOfShortRepetition(Map<String, Integer> list){
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
    public static Map<String, Integer> searchThroughShort(String toSearch){
        Map<String, Integer> results = new HashMap<>();
        for(int i = 0; i < toSearch.length() - 1; i++){
            String searchable = toSearch.substring(i, i + 2);
            for(int j = 0; j < MccLists.shortMccList.size(); j++){
                String mcc = MccLists.shortMccList.get(j);
                if(searchable.equals(mcc)){
                    if (results.containsKey(mcc)) {
                        results.put(mcc, results.get(mcc + 1));
                        i++;
                    } else {
                        results.put(mcc, 1);
                        i++;
                    }
                }
            }
        }
        return results;
    }

    public static Map<String, Integer> searchThroughShortR(String toSearch){
        Map<String, Integer> results = new HashMap<>();
        for(int i = toSearch.length() - 1; i > 2; i--){
            String searchable = toSearch.substring(i - 2, i);
            for(int j = 0; j < MccLists.shortMccList.size(); j++){
                String mcc = MccLists.shortMccList.get(j);
                if(searchable.equals(mcc)){
                    if (results.containsKey(mcc)) {
                        results.put(mcc, results.get(mcc) + 1);
                        i--;
                    } else {
                        results.put(mcc, 1);
                        i--;
                    }
                }
            }
        }
        return results;
    }



    public static Map<String, Integer> searchThroughLong(String toSearch){
        Map<String, Integer> results = new HashMap<>();

        for(int i = 0; i < toSearch.length() - 2; i++){
            String searchable = toSearch.substring(i, i + 3);
            for(int j = 0; j < MccLists.longMccList.size(); j++){
                String mcc = MccLists.shortMccList.get(j);
                if(searchable.equals(mcc)){
                    if (results.containsKey(mcc)) {
                        results.put(mcc, results.get(mcc) + 1);
                        i+=2;
                    } else {
                        results.put(mcc, 1);
                        i+=2;
                    }
                }
            }
        }
        return results;
    }

    @SuppressWarnings("unused")
    public static Map<String, Integer> searchThroughLongR(String toSearch){
        Map<String, Integer> results = new HashMap<>();
        for(int i = toSearch.length(); i > 0; i--){
            String searchable = toSearch.substring(i - 3, i);
            for(int j = 0; j < MccLists.longMccList.size(); j++){
                String mcc = MccLists.shortMccList.get(j);
                if(searchable.equals(mcc)){
                    if (results.containsKey(mcc)) {
                        results.put(mcc, results.get(mcc) + 1);
                        i-=2;
                    } else {
                        results.put(mcc, 1);
                        i-=2;
                    }
                }
            }
        }
        return results;
    }
}