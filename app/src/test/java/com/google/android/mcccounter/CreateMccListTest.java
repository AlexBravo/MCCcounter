package com.google.android.mcccounter;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

//Created by alex on 4/6/2016
public class CreateMccListTest {

//    private MccListCreator mccListCreator;
//    @Before
//    public void setUp() throws Exception {
//        mccListCreator = new MccListCreator();
//    }

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
            long listScore = (listSavings / numberOfMccs) * (length - listConfusions + 1) ;

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
        // [the](1)2,0 [th](1)1,0 [he](1)1,0
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
        // [re, the](2)3,0 [re, th](2)2,0 [he, re](2)2,0 [er, th](2)2,0 [the](1)2,0
        // [re](1)1,0 [th](1)1,0 [er](1)1,0 [he](1)1,0
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
        // [the](1)4,0 [th](1)2,0 [he](1)2,0 [et](1)1,0

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
        // [the](1)4,0 [th](1)2,0 [he](1)2,0
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
        //[he](1)3,0
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
        // [ar, in, the, to](4)8,0 [ar, et, in, the](4)8,0 [in, re, the, to](4)8,0
        // [ar, in, the](3)7,0;-1,0 [in, re, the](3)7,0 [et, in, the](3)7,0 [in, the, to](3)7,0
        // [in, re, th, to](4)6,0;-1,0 [ar, in, th, to](4)6,0 [he, in, re, to](4)6,0
        // [ar, et, the](3)6,0;0,0 [re, the, to](3)6,0 [ar, et, he, in](4)6,1;0,1
        // [ar, he, in, to](4)6,0 [ar, et, in, th](4)6,0 [in, the](2)6,0;0,-1 [ar, the, to](3)6,0
        // [ar, he, in](3)5,0 [he, in, re](3)5,0 [in, th, to](3)5,0 [in, re, th](3)5,0
        // [he, in, to](3)5,0 [re, the](2)5,0;-1,0 [ar, in, th](3)5,0 [et, in, re, to](4)5,1;0,1
        // [et, the](2)5,0;0,-1 [et, in, th](3)5,0 [the, to](2)5,0;0,0 [et, he, in](3)5,1
        // [ar, the](2)5,0;0,-1 [ar, et, in](3)5,0 [in, re, to](3)4,0 [ar, in, to](3)4,0
        // [et, in](2)4,0;-1,0 [in, th](2)4,0 [he, re, to](3)4,0 [he, in](2)4,0;0,0
        // [ar, th, to](3)4,0 [et, in, re](3)4,1 [ar, et, th](3)4,0 [ar, et, he](3)4,1
        // [the](1)4,0;0,0 [ar, he, to](3)4,0 [re, th, to](3)4,0 [re, th](2)3,0;-1,0
        // [ar, et](2)3,0 [ar, he](2)3,0 [et, re, to](3)3,1 [he, re](2)3,0;0,-1 [in, to](2)3,0
        // [et, th](2)3,0 [et, he](2)3,1 [th, to](2)3,0 [ar, in](2)3,0 [he, to](2)3,0
        // [ar, th](2)3,0 [in, re](2)3,0 [th](1)2,0 [re, to](2)2,0 [in](1)2,0 [et, re](2)2,1
        // [ar, to](2)2,0 [he](1)2,0 [et](1)2,0 [to](1)1,0 [ar](1)1,0 [re](1)1,0

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
        // [al, and, at, he, in, ing, it, le, on, or, ou, se, th, the, to, ve](16)2368,1
        // [al, an, as, at, he, in, ing, it, le, on, ou, re, th, the, to, ve](16)2354,0
        // [al, an, as, at, he, in, ing, it, le, on, or, ou, th, the, to, ve](16)2346,1
        // [al, an, at, he, in, ing, it, le, on, ou, re, se, th, the, to, ve](16)2327,0
        // [al, and, as, at, he, in, ing, it, le, on, ou, re, th, the, to](15)2323,0;-80,0
        // ...

        // For minMccFrequency=0.1, maxConfusionDelta=0.1, rankIncreasePercent=0.01%
        // took 40 sec on home Mac
        // maxSavings=3200 mccLists.size=279 duplicateBranchesCount=421
    }

    @Test
    public void test_alice_half_smallConfusion() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String fileName = "aliceinwonderland_half.txt";

        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccFrequency = (int)(in.length() / 100 * 0.1);
        int maxConfusionDelta = (int)(in.length() / 100 * 0.01);
        double rankIncreasePercent = 1.00005; // 0.005%
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
        // [al, an, and, ar, as, at, ch, ed, en, er, es, et, il, in, ing,
        // is, it, of, om, on, or, ot, ou, st, th, the, to](27)15081,52;-101,-66

        // For minMccFrequency=0.1, maxConfusionDelta=0.01, rankIncreasePercent=0.005%
        // took 3 min 6 sec on work Mac
        // maxSavings=16273 mccLists.size=348 duplicateBranchesCount=332
        // [al, an, and, ar, as, at, ch, de, ge, he, il, in, ing, is, it, le, me,
        // of, on, or, ot, ou, ra, re, ri, ro, se, st, th, the, to, ve](32)16273,475
        // [al, an, and, ar, as, at, ch, de, et, he, il, in, ing, is, it, le, me,
        // of, on, or, ot, ou, ra, re, ri, ro, se, st, th, the, to, ve](32)16214,542
        // ...
    }

    @Test
    public void test_alice_smallConfusion() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String fileName = "aliceinwonderland.txt";

        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        int minMccFrequency = (int)(in.length() / 100 * 0.2);
        int maxConfusionDelta = (int)(in.length() / 100 * 0.1);
        double rankIncreasePercent = 1.00001; // 0.001%
        MccListCreator mccListCreator =
                new MccListCreator(in, minMccFrequency, maxConfusionDelta, rankIncreasePercent);
        mccListCreator.createMccList(new ArrayList<String>());

        outputResults(0.95, in.length());
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