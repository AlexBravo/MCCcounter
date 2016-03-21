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
    public void testAcount() throws Exception {
        ArrayList<String> arrayList = new ArrayList<String>();

        //map.put("ing", 2);
        arrayList.add("aco");
        ArrayList<String> confusionArrayList = confusion.calculateConfusions("account");
        assertEquals(arrayList, confusionArrayList);
    }
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
