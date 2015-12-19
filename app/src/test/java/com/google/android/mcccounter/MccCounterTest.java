package com.google.android.mcccounter;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

//Created by Kids on 12/12/2015.
public class MccCounterTest {
    private MccCounter mccCounter;
    @Before
    public void setUp() throws Exception {
        mccCounter = new MccCounter();
    }

//    @Test
//    public void testEmptyString() throws Exception {
//        assertEquals(new HashMap<>(), mccCounter.calculateMCCs(""));
//    }

    @Test
    public void testKing() throws Exception {
        Map<String, Integer> map = new HashMap<>();

        //map.put("ing", 2);
        map.put("ing", 1);
        Map<String, Integer> mccCounterMap = mccCounter.calculateMCCs("sing");
        assertEquals(map, mccCounterMap);
    }


//    @Test
//    public void testSinging() throws Exception {
//        Map<String, Integer> map = new HashMap<>();
//        map.put("ing", 1);
//        assertEquals(map, mccCounter.calculateMCCs("ing"));
//    }
}