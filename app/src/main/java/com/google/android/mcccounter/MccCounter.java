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

    public Map<String, Integer> calculateMCCs(String in) {
        Map<String, Integer> mccs = new HashMap<>();
        if (!in.equals("")) {
            if(in.length() == 2){
                String sFResult = findInShort(in);
                if (sFResult.length() != 0) {
                    if (mccs.containsKey(sFResult)) {
                        mccs.put(sFResult, mccs.get(sFResult) + 1);
                    } else {
                        mccs.put(sFResult, 1);
                    }
                }
            } else {
                String lFResult = findInLong(in);
                if (!lFResult.equals("")) {
                    if (mccs.containsKey(lFResult)) {
                        mccs.put(lFResult, mccs.get(lFResult) + 1);
                    } else {
                        mccs.put(lFResult, 1);
                    }
                }
            }
        }
        return mccs;
    }

    public String findInLong(String in){
        String[] lPossibilities = new String[4];

        int j = 0;
        for(int i = 0; i < longMccList.length; i++){
            String fIn = in.substring(0, 1);
            String fList = longMccList[i].substring(0, 1);
            if(fIn.equals(fList)){
                lPossibilities[j] = longMccList[i];
                j++;
            }
        }
        String answer = "";
        for(int i = 0; i < j; i++){
            if(in.substring(1, 2).equals(lPossibilities[i].substring(1, 2))){
                answer = lPossibilities[i];
                break;
            }
        }
        return answer;
    }


    public String findInShort(String in){
        String[] sPossibilities = new String[30];
        int j = 0;
        for(int i = 0; i < shortMccList.length; i++){
            if(firstLetterIn(in).equals(firstLetterIn(shortMccList[i]))){
                sPossibilities[j] = shortMccList[i];
                j++;
            }
        }
        String answer = "";
        for(int i = 0; i < j; i++){
            if(secondLetterIn(in).equals(secondLetterIn(sPossibilities[i]))){
                answer = sPossibilities[i];
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