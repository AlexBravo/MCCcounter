package com.google.android.mcccounter;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;

//Created by alex on 4/6/2016
public class CreateMccListTest {

//    private MccListCreator mccListCreator;
//    @Before
//    public void setUp() throws Exception {
//        mccListCreator = new MccListCreator();
//    }

//    @Test
//    public void testEmptyString() throws Exception {
//        assertEquals(new HashMap<>(), mccCounter.calculateMCCs(""));
//    }

    // TODO: Test number of confusions MccListCreator thinks there are

    @Test
    public void testSimpleString() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String in = "thetheininareto";

        MccListCreator mccListCreator = new MccListCreator(in, 1, 10);
        mccListCreator.createMccList(new ArrayList<String>(), 0, 0, 0);

        System.out.println("maxSavings=" + MccListCreator.maxSavings
                + " branchesCount=" + MccListCreator.branchesCount
                + " duplicateBranchesCount=" + MccListCreator.duplicateBranchesCount);

        //Collections.sort(createdList);
        //List<String> expectedList = Arrays.asList("the", "in", "ar", "to");
        // Q: Why does it add "to" before "et"?
        // A: Because both "to" and "et" have the same rank, but "to" was looked at first

        // It should try adding "et", but fail as no savings.

        //Collections.sort(expectedList);

        //assertEquals(expectedList, createdList);
    }

    @Test
    public void testSmallFile() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String fileName = "aliceinwonderland.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccValue = in.length()/1000; // 0.1%
        int maxConfusionDelta = in.length()/1000; // 0.1%
        MccListCreator mccListCreator = new MccListCreator(in, minMccValue, maxConfusionDelta);
        mccListCreator.createMccList(new ArrayList<String>(), 0, 0, 0);

        System.out.println("maxSavings=" + MccListCreator.maxSavings
                + " branchesCount=" + MccListCreator.branchesCount
                + " duplicateBranchesCount=" + MccListCreator.duplicateBranchesCount);

        System.out.println("mccLists=" + MccListCreator.mccLists);
    }

    @Test
    public void testBigFile() throws Exception {
        String fileName = "812_notes.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccValue = in.length()/1000; // 0.1%
        int maxConfusionDelta = in.length()/1000; // 0.1%
        MccListCreator mccListCreator = new MccListCreator(in, minMccValue, maxConfusionDelta);
        mccListCreator.createMccList(new ArrayList<String>(), 0, 0, 0);

        System.out.println("maxSavings=" + MccListCreator.maxSavings
                + " branchesCount=" + MccListCreator.branchesCount
                + " duplicateBranchesCount=" + MccListCreator.duplicateBranchesCount);
    }
}