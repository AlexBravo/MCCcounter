package com.google.android.mcccounter;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by wendy on 3/20/2016.
 */
public class ConfusionsTest {
    private Confusions confusion;
    private Context context = this.context;
    @Before
    public void setUp() throws Exception {
        confusion = new Confusions();
    }

    @Test
    public void testRandomString() throws Exception {
        ArrayList<String> arrayList = new ArrayList<String>();
        //AssetManager am = context.getAssets();
        //InputStream is = am.open("test.txt");
        //String input = convertStreamToString(is);
        //map.put("ing", 2);
        //arrayList.add();
        //Like "le-r" (and "l-er"), "de-d", "to-r", "co-n", "co-r", "a-th" (and "at-h"), "at-i", "de-n", etc.
        //String in = "ler ded tor con cor ath ati den";
        //String in = "lerdedtorconcorathatiden";
        String in = "conor";
        ArrayList<String> confusionArrayList = confusion.calculateConfusions(in);
        assertEquals(arrayList, confusionArrayList);
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
