package com.google.android.mcccounter;

import java.util.ArrayList;

/**
 * Created by wendy on 3/20/2016.
 */
public class Confusions {



    public static ArrayList<String> calculateConfusions(String in) {
        ArrayList<String> result = new ArrayList<String>();
        int i = 0;
        final int mccLength = 2;
        while(i < in.length() - mccLength) {
            String first = in.substring(i,i + mccLength);
            if (isMcc(first)) {
                String second = in.substring(i + 1, i + mccLength + 1);
                if (isMcc(second)) {
                    String toAdd = first + second.charAt(1);
                    result.add(toAdd);
                }
                i += mccLength;
            } else {
                i++;
            }
        }
        return result;
    }

    public static boolean isMcc(String section) {
        if(Utility.lookThroughShorts(section)){
            return true;
        }
        return false;
    }

}
