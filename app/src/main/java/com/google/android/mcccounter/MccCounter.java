package com.google.android.mcccounter;

import android.support.annotation.NonNull;

import org.w3c.dom.ls.LSException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

//Created by Kids on 12/12/2015.

public class MccCounter {
    String[] shortMccList = {"in", "th", "or","an", "st", "co", "il", "ac",
                            "ch","no", "de","se", "ed","of", "le", "ro", "ou",
                            "ti", "is", "it", "en","re", "er", "on", "to"};
    String[] longMccList = {"ing", "and", "the", "ion"};

    public Map<String, Integer> calculateMCCs(String in){
        Map<String, Integer> mccs = new HashMap<>();
        String[] lResult = findInLong(in);
        String lFResult = findInLong(in, lResult);
        if(lResult.length != 0) {
            if(mccs.containsKey(lFResult)) {
                mccs.put(lFResult, mccs.get(lFResult) + 1);
            } else {
                mccs.put(lFResult, 1);
            }
        } else if (findInShort(in).length != 0) {
            String[] sResult = findInShort(in);
            if(mccs.containsKey(findInShort(in, sResult))) {
                mccs.put(findInShort(in, sResult), mccs.get(findInShort(in, sResult)) + 1);
            } else {
                mccs.put(findInShort(in, sResult), 1);
            }
        }
        return mccs;
    }

    public String[] findInLong(String in){
        String[] lPossibilities = new String[4];
        int j = 0;
        for(int i = 0; i < longMccList.length; i++){
            String fIn = firstLetterIn(in);
            String fList = firstLetterIn(longMccList[i]);
            if(fIn.equals(fList)){
                lPossibilities[j] = longMccList[i];
                j++;
            }
        }
        return lPossibilities;
    }
    public String findInLong(String in, String[] r){
        String answer = "";
        for(int i = 0; i < r.length; i++){
            if(secondLetterIn(in).equals(secondLetterIn(r[i]))){
                answer = r[i];
                break;
            }
        }
        return answer;
    }

    public String[] findInShort(String in){
        String[] sPossibilities = {};
        int j = 0;
        for(int i = 0; i < shortMccList.length; i++){
            if(firstLetterIn(in) == firstLetterIn(shortMccList[i])){
                sPossibilities[j] = shortMccList[i];
                j++;
            }
        }
        return sPossibilities;
    }

    public String findInShort(String in, String[] r){
        String answer = "";
        for(int i = 0; i < r.length; i++){
            if(secondLetterIn(in) == secondLetterIn(r[i])){
                answer = r[i];
                break;
            }
        }
        return answer;
    }

    public String firstLetterIn(String in) {
        if (in.equals("")) {
            return "";
        } else {
            return in.substring(0, 1);
        }
    }

    public String secondLetterIn(String in){
        if (in.equals("")) {
            return "";
        } else {
            return in.substring(1, 2);
        }
    }

}