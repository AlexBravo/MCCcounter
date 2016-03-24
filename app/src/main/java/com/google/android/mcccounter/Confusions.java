package com.google.android.mcccounter;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by wendy on 3/20/2016.
 */
public class Confusions {



    public static HashMap<String, Integer> calculateConfusions(String in) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        int i = 0;
        final int mccLength = 2;
        while(i < in.length() - mccLength) {
            String first = in.substring(i,i + mccLength);
            if (isMcc(first)) {
                String second = in.substring(i + 1, i + mccLength + 1);
                if (isMcc(second)) {
                    String toAdd = first + second.charAt(1);
                    if(!result.containsKey(toAdd)) {
                        result.put(toAdd, 1);
                    } else {
                        int prev = result.get(toAdd);
                        result.put(toAdd, prev+1);
                    }
                }
                i += mccLength;
            } else {
                i++;
            }
        }
        List<Map.Entry<String, Integer>> list = new LinkedList<>( result.entrySet() );
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue())*-1;
            }
        });
        HashMap<String, Integer> returnVal = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            returnVal.put( entry.getKey(), entry.getValue() );
        }
        return returnVal;
    }

    public static boolean isMcc(String section) {
        if(Utility.lookThroughShorts(section)){
            return true;
        }
        return false;
    }

}
