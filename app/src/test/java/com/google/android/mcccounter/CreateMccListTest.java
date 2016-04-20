package com.google.android.mcccounter;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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

    private void outputResults(double percentToDiscard) {
        System.out.println();
        System.out.println("        // maxSavings=" + MccListCreator.maxSavings
                + " evaluatedMccLists.size=" + MccListCreator.evaluatedMccLists.size()
                + " duplicateBranchesCount=" + MccListCreator.duplicateBranchesCount);

        LinkedHashMap<String, Long> mccLists = Utility.sortMap(MccListCreator.evaluatedMccLists);

        System.out.print("        // evaluatedMccLists=");
        long maxSavings = 0;
        for (LinkedHashMap.Entry<String, Long> mccList : mccLists.entrySet()) {
            long listSavings = mccList.getValue();
            if (maxSavings < listSavings) {
                maxSavings = listSavings;
            }
            if (listSavings >= (long)(maxSavings * percentToDiscard)) {
                System.out.print(mccList.getKey() + "=" + listSavings + " ");
                if (mccList.getKey().length() > 30) {
                    System.out.println();
                }
            }
        }
    }

    @Test
    public void test_the_1() throws Exception {

        @SuppressWarnings("SpellCheckingInspection")
        final String in = "the";

        MccListCreator mccListCreator = new MccListCreator(in, 1, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0);

        // Correct results
        // maxSavings=2 evaluatedMccLists.size=3 duplicateBranchesCount=0
        // evaluatedMccLists=[the]=2 [th]=1 [he]=1
    }

    @Test
    public void test_there_1() throws Exception {

        @SuppressWarnings("SpellCheckingInspection")
        final String in = "there";

        MccListCreator mccListCreator = new MccListCreator(in, 1, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0);
        // Correct results
        // maxSavings=3 evaluatedMccLists.size=9 duplicateBranchesCount=4
        // evaluatedMccLists=[re, the]=3 [re, th]=2 [he, re]=2 [er, th]=2 [the]=2
        // [re]=1 [th]=1 [er]=1 [he]=1
    }
    @Test
    public void test_thethe_1() throws Exception {

        @SuppressWarnings("SpellCheckingInspection")
        final String in = "thethe";

        MccListCreator mccListCreator = new MccListCreator(in, 1, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0);

        // Correct results
        // maxSavings=4 evaluatedMccLists.size=6 duplicateBranchesCount=0
        // evaluatedMccLists=[the]=4 [et, he, th]=3 [th]=2 [et, th]=2 [he]=2 [et]=1
    }

    @Test
    public void test_thethe_2() throws Exception {

        @SuppressWarnings("SpellCheckingInspection")
        final String in = "thethe";


        MccListCreator mccListCreator = new MccListCreator(in, 2, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0);

        // Correct results
        // maxSavings=4 evaluatedMccLists.size=3 duplicateBranchesCount=0
        // evaluatedMccLists=[the]=4 [th]=2 [he]=2
    }

    @Test
    public void test_thethehe_3() throws Exception {

        @SuppressWarnings("SpellCheckingInspection")
        final String in = "thethehe";

        MccListCreator mccListCreator = new MccListCreator(in, 3, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0);

        // Correct results
        // maxSavings=3 evaluatedMccLists.size=1 duplicateBranchesCount=0
        // evaluatedMccLists=[he]=3
    }

    @Test
    public void test_thetheininareto() throws Exception {

        @SuppressWarnings("SpellCheckingInspection")
        final String in = "thetheininareto";

        MccListCreator mccListCreator = new MccListCreator(in, 1, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        //outputResults(0.95);
        outputResults(0);

        // Correct results
        // maxSavings=8 evaluatedMccLists.size=79 duplicateBranchesCount=117
        // evaluatedMccLists=[ar, in, the, to]=8 [ar, et, in, the]=8 [in, re, the, to]=8
        // [ar, in, the]=7 [in, re, the]=7 [ar, et, he, in, th]=7 [et, he, in, re, th, to]=7
        // [et, in, the]=7 [in, the, to]=7 [in, re, th, to]=6 [et, he, in, re, th]=6
        // [ar, in, th, to]=6 [he, in, re, to]=6 [et, he, in, th]=6 [ar, et, the]=6
        // [re, the, to]=6 [ar, et, he, in]=6 [et, in, re, th, to]=6 [ar, he, in, to]=6
        // [ar, et, in, th]=6 [in, the]=6 [ar, the, to]=6 [ar, he, in]=5 [he, in, re]=5
        // [in, th, to]=5 [in, re, th]=5 [he, in, to]=5 [re, the]=5 [ar, in, th]=5
        // [et, in, re, to]=5 [et, the]=5 [ar, et, he, th]=5 [et, in, th]=5 [the, to]=5
        // [et, he, re, th, to]=5 [et, he, in]=5 [et, in, re, th]=5 [ar, the]=5 [ar, et, in]=5
        // [in, re, to]=4 [ar, in, to]=4 [et, in]=4 [et, he, re, th]=4 [in, th]=4 [he, re, to]=4
        // [he, in]=4 [ar, th, to]=4 [et, re, th, to]=4 [et, in, re]=4 [ar, et, th]=4
        // [ar, et, he]=4 [the]=4 [et, he, th]=4 [ar, he, to]=4 [re, th, to]=4 [re, th]=3
        // [ar, et]=3 [ar, he]=3 [et, re, to]=3 [he, re]=3 [in, to]=3 [et, th]=3 [et, re, th]=3
        // [et, he]=3 [th, to]=3 [ar, in]=3 [he, to]=3 [ar, th]=3 [in, re]=3 [th]=2 [re, to]=2
        // [in]=2 [et, re]=2 [ar, to]=2 [he]=2 [et]=2 [to]=1 [ar]=1 [re]=1

        // 3 lists with savings = 8
    }

    @Test
    public void test_alice_1() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        //String fileName = "aliceinwonderland.txt";
        //String fileName = "aliceinwonderland_half.txt";
        //String fileName = "aliceinwonderland_quarter.txt";
        String fileName = "aliceinwonderland_tenth.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccFrequency = in.length()/100; // 1%
        int maxConfusionDelta = 1;
        double rankIncreasePercent = 1.01; // 1%
        MccListCreator mccListCreator =
                new MccListCreator(in, minMccFrequency, maxConfusionDelta, rankIncreasePercent);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0.9);

        // TODO: Why are there both "he" and "the" in [an, he, in, the]?
        // Correct results (take 3 seconds)
        // maxSavings=988 evaluatedMccLists.size=28 duplicateBranchesCount=19
        // evaluatedMccLists=[an, he, in, the]=988 [an, er, in, the]=974
        // [an, he, in, it]=956 [an, er, in, th]=950
    }

    @Test
    public void test_alice_smallConfusion() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        //String fileName = "aliceinwonderland.txt";
                //String fileName = "aliceinwonderland_half.txt";
                //String fileName = "aliceinwonderland_quarter.txt";
                String fileName = "aliceinwonderland_tenth.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccFrequency = in.length()/200; // 0.5%
        int maxConfusionDelta = in.length()/10000; // 0.01%
        double rankIncreasePercent = 1.001; // 0.1%
        MccListCreator mccListCreator =
                new MccListCreator(in, minMccFrequency, maxConfusionDelta, rankIncreasePercent);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0.9);

        // Correct results (took 6 min 45 sec on home Mac)
        // maxSavings=2151 evaluatedMccLists.size=2344 duplicateBranchesCount=6536
        // evaluatedMccLists=[al, and, as, he, in, ing, le, on, ou, re, th, the, to, ve]=2151
        // [al, and, as, he, in, ing, le, on, or, ou, th, the, to, ve]=2143
        // [al, and, he, in, ing, le, on, ou, re, se, th, the, to, ve]=2124
        // [al, and, he, in, ing, le, on, or, ou, se, th, the, to, ve]=2116
        // [al, and, as, he, in, ing, le, on, ou, re, th, the, to]=2071
        // [al, and, as, he, in, le, on, ou, re, th, the, to, ve]=2049
        // [al, and, as, he, in, ing, le, on, ou, th, the, to, ve]=2049
        // [al, and, he, in, ing, le, on, ou, re, se, th, the, to]=2044
        // [al, and, as, he, in, ing, le, ou, re, th, the, to, ve]=2044
        // [al, and, as, he, in, ing, on, or, ou, th, the, to, ve]=2044
        // ...
    }

    @Test
    public void test_alice() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String fileName = "aliceinwonderland.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccFrequency = in.length()/1000; // 0.1%
        int maxConfusionDelta = in.length()/2000; // 0.05%
        double rankIncreasePercent = 1.00001; // 0.001%
        MccListCreator mccListCreator =
                new MccListCreator(in, minMccFrequency, maxConfusionDelta, rankIncreasePercent);
        ArrayList<String> initialMccList = new ArrayList<>(MccLists.nonControversialMccList);
        mccListCreator.createMccList(initialMccList);

        outputResults(0.95);
    }

    @Test
    public void testSmallFileCloseToBest() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String fileName = "aliceinwonderland.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccFrequency = in.length()/1000; // 0.1%
        int maxConfusionDelta = in.length()/2000; // 0.05%
        double rankIncreasePercent = 1.00001; // 0.001%
        MccListCreator mccListCreator =
                new MccListCreator(in, minMccFrequency, maxConfusionDelta, rankIncreasePercent);
        ArrayList<String> initialMccList = new ArrayList<>(MccLists.closeToBest);
        mccListCreator.createMccList(initialMccList);

        outputResults(0.95);
    }

    @Test
    public void testBigFile() throws Exception {
        String fileName = "812_notes.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccFrequency = in.length()/1000; // 0.1%
        //int maxConfusionDelta = 0;
        int maxConfusionDelta = in.length()/2000; // 0.05%
        double rankIncreasePercent = 1.00001; // 0.001%
        MccListCreator mccListCreator =
                new MccListCreator(in, minMccFrequency, maxConfusionDelta, rankIncreasePercent);
        ArrayList<String> initialMccList = new ArrayList<>(MccLists.nonControversialMccList);
        mccListCreator.createMccList(initialMccList);

        outputResults(0.95);
    }
}