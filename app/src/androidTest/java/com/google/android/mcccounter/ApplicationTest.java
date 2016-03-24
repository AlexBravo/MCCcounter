package com.google.android.mcccounter;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.HashMap;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

/**
 * Created by wendy on 3/20/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ApplicationTest {
    private Confusions confusion;
    private Context context = this.context;
    @Before
    public void setUp() throws Exception {
        confusion = new Confusions();
    }

    @Test
    public void testRandomString() throws Exception {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        InputStream is = getInstrumentation().getContext().getResources().openRawResource(com.google.android.mcccounter.test.R.raw.aliceinwonderland);
        //AssetManager am = context.getAssets();
        //InputStream is = am.open("test.txt");
        String in = convertStreamToString(is);
        //map.put("ing", 2);
        //arrayList.add("aco");
        //Like "le-r" (and "l-er"), "de-d", "to-r", "co-n", "co-r", "a-th" (and "at-h"), "at-i", "de-n", etc.
        //String in = "ler ded tor con cor ath ati den";
        //String in = "lerdedtorconcorathatiden";
        //String in = "conor";
        HashMap<String, Integer> confusionArrayList = confusion.calculateConfusions(in);

        assertEquals(hashMap, confusionArrayList);
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
