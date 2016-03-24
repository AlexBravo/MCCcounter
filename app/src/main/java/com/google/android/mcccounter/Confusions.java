package com.google.android.mcccounter;

import java.util.HashMap;
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

        /*Collections.sort(result, new Comparator<Map.Entry<K, V>>()
        {
            @Override
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );*/
        return result;
    }

    public static boolean isMcc(String section) {
        if(Utility.lookThroughShorts(section)){
            return true;
        }
        return false;
    }

}
