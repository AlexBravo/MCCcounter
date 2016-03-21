package com.google.android.mcccounter;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by wendy on 3/20/2016.
 */
public class Confusions {



    public static ArrayList<String> calculateConfusions(String in) {
        ArrayList<String> result = new ArrayList<String>();
        int i = 0;
        while(i < in.length()-2) {
            String first = in.substring(i,i+2);
            if(!isMcc(first)) {
                i++;
                continue;
            }
            String second = in.substring(i+1,i+3);
            if(isMcc(second)){
                if(i < in.length()-3) {
                    String toAdd = first+second.charAt(1);
                    result.add(toAdd);
                }
            }
            i+=2;
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
