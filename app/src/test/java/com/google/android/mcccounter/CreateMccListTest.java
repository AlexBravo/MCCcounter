package com.google.android.mcccounter;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

// Created by alex on 4/6/2016
public class CreateMccListTest {
    private void outputResults(double percentToDiscard, long length) {
        System.out.println();
        System.out.println("maxSavings=" + MccListCreator.maxSavings
                + " mccLists.size=" + MccListCreator.mccListsSavings.size()
                + " duplicateBranchesCount=" + MccListCreator.duplicateBranchesCount);

        HashMap<String, Long> mccLists = new HashMap<>();
        long maxSavings = 0;
        for (LinkedHashMap.Entry<String, Long> mccList : MccListCreator.mccListsSavings.entrySet()) {
            String mccListKey = mccList.getKey();

            long listSavings = mccList.getValue();

            if (maxSavings < listSavings) {
                maxSavings = listSavings;
            }

            long listConfusions = MccListCreator.mccListsConfusions.get(mccListKey);

            int numberOfMccs = mccListKey.length() - mccListKey.replace(",", "").length() + 1;

            // Best lists should be at the top. It would have most savings per number of MCCs
            // and the least number of confusions
            // Make savings weigh more than numberOfMccs
            long listScore = (2 * listSavings - (numberOfMccs - 1)) * (length - (listConfusions - 1)) ;

            mccLists.put(mccListKey, listScore);
        }
        LinkedHashMap<String, Long> sortedMccLists = Utility.sortMap(mccLists);

        //System.out.print("mccListsSavings=");

        long previousSavings = 0;
        long previousConfusions = 0;
        int previousNumberOfMccs = 0;
        for (LinkedHashMap.Entry<String, Long> mccList : sortedMccLists.entrySet()) {
            long listScore = mccList.getValue();

            String mccListKey = mccList.getKey();

            long listSavings = MccListCreator.mccListsSavings.get(mccListKey);
            long listConfusions = MccListCreator.mccListsConfusions.get(mccListKey);

            int numberOfMccs = mccListKey.length() - mccListKey.replace(",", "").length() + 1;

            if (listSavings >= (long) (maxSavings * percentToDiscard)) {
                System.out.print(mccListKey + "(" + numberOfMccs + ")" + listSavings + ","  + listConfusions);

                if (numberOfMccs != previousNumberOfMccs) {
                    if (previousNumberOfMccs > 2) {
                        System.out.print(";" + (listSavings - previousSavings)
                                + "," + (listConfusions - previousConfusions));
                    }

                    previousSavings = listSavings;
                    previousConfusions = listConfusions;
                    previousNumberOfMccs = numberOfMccs;
                }
                System.out.print(";" + listScore + " ");

                if (mccListKey.length() > 30) {
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

        outputResults(0, in.length());

        // Expected results
        // maxSavings=2 mccLists.size=3 duplicateBranchesCount=0
        // [the](1)2,0;16 [th](1)1,0;8 [he](1)1,0;8
    }

    @Test
    public void test_the__1() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String in = "the ";

        MccListCreator mccListCreator = new MccListCreator(in, 1, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0, in.length());

        // Expected results

        // maxSavings=3 mccLists.size=4 duplicateBranchesCount=0
        // [the ](1)3,0;30 [the](1)2,0;20 [th](1)1,0;10 [he](1)1,0;10
    }

    @Test
    public void test_there_1() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String in = "there";

        MccListCreator mccListCreator = new MccListCreator(in, 1, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());
        //mccListCreator.createMccList(new ArrayList<>(MccLists.finalList));

        outputResults(0, in.length());
        // Expected results
        // maxSavings=3 mccLists.size=9 duplicateBranchesCount=4
        // [re, the](2)3,0;30 [the](1)2,0;24 [re, th](2)2,0;18 [he, re](2)2,0;18
        // [er, th](2)2,0;18 [re](1)1,0;12 [th](1)1,0;12 [er](1)1,0;12 [he](1)1,0;12
    }

    @Test
    public void test_thethe_1() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String in = "thethe";

        MccListCreator mccListCreator = new MccListCreator(in, 1, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0, in.length());

        // Expected results
        // maxSavings=4 mccLists.size=4 duplicateBranchesCount=0
        // [the](1)4,0;56 [th](1)2,0;28 [he](1)2,0;28 [et](1)1,0;14

        // Lists [et, he, th] and [et, th] are not here because
        // when user sees "eth", it's typed as e+th, so no "et" is added and no confusion counted
    }

    @Test
    public void test_thethe_2() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String in = "thethe";


        MccListCreator mccListCreator = new MccListCreator(in, 2, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0, in.length());

        // Expected results
        // maxSavings=4 mccLists.size=3 duplicateBranchesCount=0
        // [the](1)4,0;56 [th](1)2,0;28 [he](1)2,0;28
    }

    @Test
    public void test_thethehe_3() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String in = "thethehe";

        MccListCreator mccListCreator = new MccListCreator(in, 3, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0, in.length());

        // Expected results
        // maxSavings=3 mccLists.size=1 duplicateBranchesCount=0
        // [he](1)3,0;54
    }

    @Test
    public void test_ing_thetheheing_1() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String in = "ing theing";

        MccListCreator mccListCreator = new MccListCreator(in, 1, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0, in.length());

        // Expected results
        // maxSavings=7 mccLists.size=23 duplicateBranchesCount=23
        // [ing, ing , the](3)7,0;132 [ing, the](2)6,0;-1,0;121 [he, ing, ing ](3)6,0;110
        // [in, ing , the](3)6,0;110 [ing, ing , th](3)6,0;110
        // [he, ing](2)5,0;-1,0;99 [ing, ing ](2)5,0;99 [ing, th](2)5,0;99 [ing , the](2)5,0;99
        // [ing](1)4,0;88 [he, in, ing ](3)5,0;88 [in, ing , th](3)5,0;88
        // ...
    }

    @Test
    public void test_thetheininareto() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String in = "thetheininareto";

        MccListCreator mccListCreator = new MccListCreator(in, 1, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        //outputResults(0.95);
        outputResults(0, in.length());

        // Expected results (nothing was removed)
        // maxSavings=8 mccLists.size=67 duplicateBranchesCount=101
        // [ar, in, the, to](4)8,0;208 [ar, et, in, the](4)8,0;208 [in, re, the, to](4)8,0;208
        // [ar, in, the](3)7,0;-1,0;192 [in, re, the](3)7,0;192 [et, in, the](3)7,0;192
        // [in, the, to](3)7,0;192 [in, the](2)6,0;-1,0;176 [ar, et, the](3)6,0;160
        // [re, the, to](3)6,0;160 [ar, the, to](3)6,0;160 [in, re, th, to](4)6,0;0,0;144
        // [ar, in, th, to](4)6,0;144 [he, in, re, to](4)6,0;144 [re, the](2)5,0;-1,0;144
        // [et, the](2)5,0;144 [ar, he, in, to](4)6,0;144 [the, to](2)5,0;-1,0;144
        // [ar, et, in, th](4)6,0;144 [ar, the](2)5,0;-1,0;144 [ar, et, he, in](4)6,1;135
        // [ar, he, in](3)5,0;-1,-1;128 [he, in, re](3)5,0;128 [in, th, to](3)5,0;128
        // [in, re, th](3)5,0;128 [he, in, to](3)5,0;128 [ar, in, th](3)5,0;128
        // [et, in, th](3)5,0;128 [the](1)4,0;-1,0;128 [ar, et, in](3)5,0;128 [et, he, in](3)5,1;120
        // [et, in](2)4,0;-1,0;112 [in, th](2)4,0;112 [he, in](2)4,0;112 [et, in, re, to](4)5,1;105
        // [in, re, to](3)4,0;-1,-1;96 [ar, in, to](3)4,0;96 [he, re, to](3)4,0;96
        // [ar, th, to](3)4,0;96 [ar, et, th](3)4,0;96 [ar, he, to](3)4,0;96 [re, th, to](3)4,0;96
        // [et, in, re](3)4,1;90 [ar, et, he](3)4,1;90 [re, th](2)3,0;-1,0;80 [ar, et](2)3,0;80
        // [ar, he](2)3,0;80 [he, re](2)3,0;80 [in, to](2)3,0;80 [et, th](2)3,0;80 [th, to](2)3,0;80
        // [ar, in](2)3,0;80 [he, to](2)3,0;80 [ar, th](2)3,0;80 [in, re](2)3,0;80 [et, he](2)3,1;75
        // [th](1)2,0;64 [in](1)2,0;64 [he](1)2,0;64 [et](1)2,0;64 [et, re, to](3)3,1;60
        // [re, to](2)2,0;-1,-1;48 [ar, to](2)2,0;48 [et, re](2)2,1;45 [to](1)1,0;32 [ar](1)1,0;32
        // [re](1)1,0;32


        // 3 lists with savings = 8, 4 lists with savings = 7
    }

    @Test
    public void test_alice_tenth_smallConfusion() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String fileName = "aliceinwonderland_tenth.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccFrequency = (int)(in.length() / 100 * 0.1);
        int maxConfusionDelta = (int)(in.length() / 100 * 0.1);
        double rankIncreasePercent = 1.0001; // 0.01%
        MccListCreator mccListCreator =
                new MccListCreator(in, minMccFrequency, maxConfusionDelta, rankIncreasePercent);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0.95, in.length());

        // For minMccFrequency=0.5, maxConfusionDelta=0.01, rankIncreasePercent=0.1%
        // took 18 min 11 sec on work Mac
        // maxSavings=2403 mccLists.size=8239 duplicateBranchesCount=30765
        // [al, and, as, at, he, in, ing, it, le, on, ou, re, th, the, to, ve](16)2403,0
        // [al, and, as, at, he, in, ing, it, le, on, or, ou, th, the, to, ve](16)2395,1
        // [al, and, at, he, in, ing, it, le, on, ou, re, se, th, the, to, ve](16)2376,0
        // ...

        // For minMccFrequency=0.1, maxConfusionDelta=0.1, rankIncreasePercent=0.01%
        // took 40 sec on home Mac
        // maxSavings=3200 mccLists.size=279 duplicateBranchesCount=421

        // For minMccFrequency=0.1, maxConfusionDelta=0.1, rankIncreasePercent=0.01%
        // took 22 sec on Alex Mac
        // maxSavings=3592 mccLists.size=100 duplicateBranchesCount=63
        // [an, and, ar, as, at, ch, de, ge, he, in, ing, ing , is, is , it, le, ll, ma, me, of,
        // on, on , or, or , ot, ou, ra, re, ro, se, st, ta, th, the, the , to, to , ve](38)3592,98;114709350
        // [an, and, ar, as, at, ch, de, ge, he, in, ing, ing , is, is , it, le, ll, ma, me, of,
        // on, on , or, or , ot, ou, ra, re, se, st, ta, th, the, the , to, to , ve](37)3568,83;-24,-15;114061500
        // [al, an, and, ar, as, at, ch, de, ge, he, in, ing, ing , is, is , it, le, me, of,
        // on, on , or, or , ot, ou, re, ro, se, st, th, the, the , to, to , ve](35)3558,59;-10,-24;113942298
        // ...
    }

    @Test
    public void test_alice_half_smallConfusion() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String fileName = "aliceinwonderland_half.txt";

        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccFrequency = (int)(in.length() / 100 * 0.1);
        int maxConfusionDelta = (int)(in.length() / 100 * 0.01);
        double rankIncreasePercent = 1.00001; // 0.001%
        MccListCreator mccListCreator =
                new MccListCreator(in, minMccFrequency, maxConfusionDelta, rankIncreasePercent);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0.95, in.length());

        // For minMccFrequency=0.2, maxConfusionDelta=0.01, rankIncreasePercent=0.1%
        // took > 55 min on work Mac
        // mccList(26) [the, in, and, er, ou, th, it, at, as, on, al, ing,
        // ed, en, ar, or, an, of, is, es, ch, st, il, om, ot, et]
        // maxSavings=14534 savings=14534, confusions=0

        // For minMccFrequency=0.1, maxConfusionDelta=0.01, rankIncreasePercent=0.001%
        // took 31 sec on work Mac
        // mccList(29) [the, in, and, er, ou, th, it, at, on, as, al, ing,
        // ed, en, ar, or, an, of, is, es, ch, st, il, om, ot, et, to, ra, ro]
        //
        // maxSavings=15302 mccLists.size=49 duplicateBranchesCount=20
        // [al, an, and, ar, as, at, ch, ed, en, er, es, et, il, in, ing,
        // is, it, of, om, on, or, ot, ou, ra, ro, st, th, the, to](29)15302,197
        // [al, an, and, ar, as, at, ch, ed, en, er, es, et, il, in, ing,
        // is, it, of, om, on, or, ot, ou, ra, st, th, the, to](28)15182,118;-120,-79
        // ...

        // For minMccFrequency=0.1, maxConfusionDelta=0.01, rankIncreasePercent=0.005%
        // took 3 min 6 sec on work Mac
        // maxSavings=16273 mccLists.size=348 duplicateBranchesCount=332
        // [al, an, and, ar, as, at, ch, de, ge, he, il, in, ing, is, it, le, me,
        // of, on, or, ot, ou, ra, re, ri, ro, se, st, th, the, to, ve](32)16273,475
        // [al, an, and, ar, as, at, ch, de, et, he, il, in, ing, is, it, le, me,
        // of, on, or, ot, ou, ra, re, ri, ro, se, st, th, the, to, ve](32)16214,542
        // ...

        // For minMccFrequency=0.1, maxConfusionDelta=0.01, rankIncreasePercent=0.001%
        // took 1 min 4 sec on Alex Mac
        // maxSavings=16266 mccLists.size=51 duplicateBranchesCount=12
        // [al, an, and, ar, as, ch, ed, ed , el, en, er, er , es, il, in, ing, ing , is, is , of, om,
        // on, on , or, or , ou, st, th, the, the , to ](31)16266,0;2649693048
        // [al, an, and, ar, as, ch, ed , el, en, er, er , es, il, in, ing, ing , is, is , of, om,
        // on, on , or, or , ou, st, th, the, the , to ](30)16136,0;-130,0;2628578332
        // [al, an, and, ar, as, ch, ed, ed , el, en, er, er , es, il, in, ing, ing , is, of, om,
        // on, on , or, or , ou, st, th, the, the , to ](30)16124,0;2626621756
        // [al, an, and, ar, as, ch, ed , el, en, er, er , es, il, in, ing, ing , is, is , of, om,
        // on, or, or , ou, st, th, the, the , to ](29)15996,0;-140,0;2605833136
        // [al, an, and, ar, as, ch, ed , el, en, er, er , es, il, in, ing, ing , is, of, om,
        // on, on , or, or , ou, st, th, the, the , to ](29)15994,0;2605507040
        // [al, an, and, ar, as, ch, ed , el, en, er, er , es, il, in, ing, ing , is, of, om,
        // on, or, or , ou, st, th, the, the , to ](28)15854,0;-142,0;2582761844
        // [al, an, and, ar, as, ch, ed , el, en, er, er , es, il, in, ing , is, of, om,
        // on, on , or, or , ou, st, th, the, the , to ](28)15835,0;2579663932
        // [al, an, and, ar, as, ch, ed , el, en, er, er , es, il, in, ing , is, of, om,
        // on, or, or , ou, st, th, the, the , to ](27)15695,0;-159,0;2556918736
        // [al, an, and, ar, as, ch, ed , el, en, er, er , es, il, in, ing , is, of, om,
        // on, on , or, ou, st, th, the, the , to ](27)15663,0;2551701200
        // [al, an, and, ar, as, ch, ed , el, en, er, er , es, il, in, ing , is, of, om,
        // on, or, ou, st, th, the, the , to ](26)15523,0;-172,0;2528956004
        // [al, an, and, ar, as, ch, ed , el, en, er, er , es, il, in, ing , is, of,
        // on, on , or, ou, st, th, the, the , to ](26)15488,0;2523249324
    }

    @Test
    public void test_alice() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String fileName = "aliceinwonderland.txt";

        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccFrequency = (int) (in.length() / 100 * 0.1);
        int maxConfusionDelta = (int) (in.length() / 100 * 0.2);
        double rankIncreasePercent = 1.00005; // 0.005
        MccListCreator mccListCreator =
                new MccListCreator(in, minMccFrequency, maxConfusionDelta, rankIncreasePercent);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0.9, in.length());

        // For minMccFrequency=0.1, maxConfusionDelta=0.2, rankIncreasePercent=0.005%
        // took 5 min 1 sec on work Mac
        // maxSavings=33186 mccLists.size=288 duplicateBranchesCount=361
        // [ac, al, an, and, ar, as, at, ch, co, ed, en, er, es, et, il, in, ing, ion, is, it, le,
        // me, of, on, or, ot, ou, ra, ri, ro, se, st, th, the, to](35)33186,1572
        // [ac, al, an, and, ar, as, at, ch, ed, en, er, es, et, il, in, ing, ion, is, it, le, ne,
        // of, om, on, or, ot, ou, ra, ri, ro, se, st, th, the, to](35)33055,1528
        // [ac, al, an, and, ar, as, at, ch, ed, en, er, es, et, il, in, ing, ion, is, it, le, of,
        // om, on, or, ot, ou, ra, ri, ro, se, st, th, the, to](34)32822,1179;-364,-393
        // [ac, al, an, and, ar, as, at, ch, co, ed, en, er, es, et, il, in, ing, ion, is, it, me,
        // of, on, or, ot, ou, ra, ri, ro, se, st, th, the, to](34)32637,1312
        // [ac, al, an, and, ar, as, at, ch, ed, en, er, es, et, il, in, ing, ion, is, it, le, of,
        // om, on, or, ot, ou, ra, ro, se, st, th, the, to](33)32573,1007;-249,-172

        // For minMccFrequency=0.1, maxConfusionDelta=0.2, rankIncreasePercent=0.01%
        // took 38 min 57 sec on home Mac
        // maxSavings=33186 mccLists.size=776 duplicateBranchesCount=1344
        // [ac, al, an, and, ar, as, at, ch, co, ed, en, er, es, et, il, in, ing, ion, is, it, le,
        // me, of, on, or, ot, ou, ra, ri, ro, se, st, th, the, to](35)33186,1572

        // For minMccFrequency=0.1, maxConfusionDelta=0.2, rankIncreasePercent=0.005%
        // took 14 min 34 sec on home Mac
        //maxSavings=33186 mccLists.size=776 duplicateBranchesCount=1344
        // [ac, al, an, and, ar, as, at, ch, co, ed, en, er, es, et, il, in, ing, ion, is, it, le,
        // me, of, on, or, ot, ou, ra, ri, ro, se, st, th, the, to](35)33186,1572
        // [ac, al, an, and, ar, as, at, ch, ed, en, er, es, et, il, in, ing, ion, is, it, le, me,
        // of, om, on, or, ot, ou, ra, ri, ro, se, st, th, the, to](35)33124,1510
        // [ac, al, an, and, ar, as, at, ch, ed, en, er, es, et, il, in, ing, ion, is, it, le, ne,
        // of, om, on, or, ot, ou, ra, ri, ro, se, st, th, the, to](35)33055,1528
        // [ac, al, an, and, ar, as, at, ch, ed, en, er, es, et, il, in, ing, ion, is, it, le, ma,
        // of, om, on, or, ot, ou, ra, ri, ro, se, st, th, the, to](35)32997,1389
        // [ac, al, an, and, ar, as, at, ch, ed, en, er, es, et, il, in, ing, ion, is, it, le, ne,
        // of, om, on, or, ot, ou, ri, ro, se, st, th, the, to](34)32871,1351;-315,-221
    }

    @Test
    public void testBigFile() throws Exception {
        String fileName = "812_notes.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccFrequency = (int)(in.length() / 100 * 0.1);
        int maxConfusionDelta = (int)(in.length() / 100 * 0.1);
        double rankIncreasePercent = 1.0001; // 0.01
        MccListCreator mccListCreator =
                new MccListCreator(in, minMccFrequency, maxConfusionDelta, rankIncreasePercent);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0.9, in.length());

        // For minMccFrequency=0.1, maxConfusionDelta=0.1, rankIncreasePercent=0.001%
        // took 16 min 41 sec on work Mac
        // maxSavings=423871 mccLists.size=42 duplicateBranchesCount=14
        // [ac, al, an, and, ar, as, at, ch, ed, en, er, es, et, il, in, ing, ion, is, it,
        // of, om, on, or, ot, ou, st, th, the](28)423871,0
        // [ac, al, an, and, ar, as, at, ch, ed, en, er, es, et, il, in, ing, ion, is, it,
        // om, on, or, ot, ou, st, th, the](27)417185,0;-6686,0

        // For minMccFrequency=0.1, maxConfusionDelta=0.1, rankIncreasePercent=0.01%
        // took 1 hour 22 min on Alex Mac
    }

    @Test
    public void testBigFileFinalList() throws Exception {
        String fileName = "812_notes.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccFrequency = (int)(in.length() / 100 * 0.1);
        int maxConfusionDelta = (int)(in.length() / 100 * 0.1);
        double rankIncreasePercent = 1.0001; // 0.01%
        MccListCreator mccListCreator =
                new MccListCreator(in, minMccFrequency, maxConfusionDelta, rankIncreasePercent);

        mccListCreator.createMccList(new ArrayList<>(MccLists.finalList));

        outputResults(0.85, in.length());

        // frequencies(28)={er=26629, in=23698, or=22353, at=17823, en=17442, ed=16582, st=16204,
        // on=16020, es=15620, al=15152, ar=14993, ou=13853, the=13635, om=13128, ing=12973,
        // an=12590, th=12413, it=11910, il=10472, is=10346, ch=9541, as=9053, and=8992, ion=8892,
        // et=8247, ac=7246, ot=6886, of=6686}
        // confusions(0)={}
        // newSavingsTotal=423871, newConfusionTotal=0
        // typedChars(172)= < >=347701 e=135528 t=85914 s=81786 o=79894 i=75056 <Enter>=71298
        // l=70504 a=66798 r=65372 c=63149 p=59434 d=58935 m=54066 .=47977 u=45566 -=42060
        // b=40602 g=36326 /=35619 w=35482 n=34107 f=32983 y=30455 0=30296 1=28275 h=26985 < >?=25875
        // v=24601 k=22358 2=22056 :=20820 ,=14137 x=14072 4=12515 3=12268 6=10630 5=10187 "=9998
        // 8=8099 q=7894 )=7821 9=7781 (=7772 7=7269 _=7022 j=5956 '=5955 +=4583 ==4519 z=3899
        // \=3260 $=2193 <TAB>=2068 ?=1964 >=1724 @=1594 ]=1348 [=1335 %=1237 ;=1173 *=1138  =1137
        // {=1065 &=1052 }=1008 #=984 <=939 |=763 ~=726 •=490 !=447 ’=435 ”=309 “=289 `=284 .. ^=111
    }
}