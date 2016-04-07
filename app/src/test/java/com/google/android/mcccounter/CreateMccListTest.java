package com.google.android.mcccounter;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;

//Created by alex on 4/6/2016
public class CreateMccListTest {
//    @Before
//    public void setUp() throws Exception {
//        mccCounter = new MccCounter();
//    }

//    @Test
//    public void testEmptyString() throws Exception {
//        assertEquals(new HashMap<>(), mccCounter.calculateMCCs(""));
//    }

    @Test
    public void testSimpleString() throws Exception {
        final String in = "thetheinarto";

        List<String> createdList = MccListCreator.createMccList(in);
        Collections.sort(createdList);

        List<String> expectedList = Arrays.asList("the", "in", "ar", "to");
        Collections.sort(expectedList);

        assertEquals(expectedList, createdList);
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void testSmallFile() throws Exception {
        String fileName = "aliceinwonderland.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        List<String> createdList = MccListCreator.createMccList(in);
        Collections.sort(createdList);

        List<String> allMCCs = new ArrayList<>(MccLists.fullLongMccList);
        allMCCs.addAll(MccLists.fullShortMccList);
        Collections.sort(allMCCs);

        assertEquals(allMCCs, createdList);
    }

    @Test
    public void testBigFile() throws Exception {
        String fileName = "812_notes.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        List<String> createdList = MccListCreator.createMccList(in);
        Collections.sort(createdList);

        List<String> allMCCs = new ArrayList<>(MccLists.fullLongMccList);
        allMCCs.addAll(MccLists.fullShortMccList);
        Collections.sort(allMCCs);

        assertEquals(allMCCs, createdList);
    }
}