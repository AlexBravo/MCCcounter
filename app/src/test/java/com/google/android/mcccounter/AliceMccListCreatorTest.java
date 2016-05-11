package com.google.android.mcccounter;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;

// Created by alex on 5/11/2016

public class AliceMccListCreatorTest extends MccListCreatorTest {
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
}
