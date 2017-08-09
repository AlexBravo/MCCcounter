package com.google.android.mcccounter;

import org.junit.Test;

import java.util.ArrayList;

// Created by alex on 5/11/2016

public class BasicMccListCreatorTest extends MccListCreatorTest {
    @Test
    public void test_the_1() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String in = "the";

        MccListCreator mccListCreator = new MccListCreator(in, 1, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        Results actual = getResults(0, in.length());
        Results expected = new Results(
                /*maxSavings*/ 2, /*mccListsSize*/ 3, /*duplicateBranchesCount*/ 0);

        compareResults(expected, actual);
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

        getResults(0, in.length());

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

        getResults(0, in.length());
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

        getResults(0, in.length());

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

        getResults(0, in.length());

        // Expected results
        // maxSavings=4 mccLists.size=3 duplicateBranchesCount=0
        // [the](1)4,0;56 [th](1)2,0;28 [he](1)2,0;28
    }

    @Test
    public void test_the_the__2() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String in = "the the ";


        MccListCreator mccListCreator = new MccListCreator(in, 2, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        getResults(0, in.length());

        // Expected results
        // maxSavings=6 mccLists.size=4 duplicateBranchesCount=0
        // [the ](1)6,0;108 [the](1)4,0;72 [th](1)2,0;36 [he](1)2,0;36
    }

    @Test
    public void test_thethehe_3() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String in = "thethehe";

        MccListCreator mccListCreator = new MccListCreator(in, 3, 1, 100);
        mccListCreator.createMccList(new ArrayList<String>());

        getResults(0, in.length());

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

        getResults(0, in.length());

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

        //getResults(0.95);
        getResults(0, in.length());

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
}
