package com.google.android.mcccounter;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

//@author Alex on 12/12/2015.
public class MccCounterTest {
//    @Before
//    public void setUp() throws Exception {
//        mccCounter = new MccCounter();
//    }

//    @Test
//    public void testEmptyString() throws Exception {
//        assertEquals(new HashMap<>(), mccCounter.calculateMCCs(""));
//    }

    @Test
    public void testString() throws Exception {
        Map<String, Long> map = new HashMap<>();

        //map.put("ing", 2);
        map.put("ing", 1L);
        Map<String, Long> mccCounterMap = MccCounter.calculateMCCs("sing");
        assertEquals(map, mccCounterMap);
    }

}