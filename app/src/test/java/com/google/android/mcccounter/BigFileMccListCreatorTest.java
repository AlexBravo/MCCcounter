package com.google.android.mcccounter;

import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;

// Created by alex on 5/11/2016

public class BigFileMccListCreatorTest extends MccListCreatorTest {
    @Test
    public void testBigFile() throws Exception {
        String fileName = "812_notes.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        // Length of 812_notes is 2896k, so 0.1% = 2896
        int minMccFrequency = 6000; // (int)(in.length() / 100 * 0.1);
        int maxConfusionDelta = (int)(minMccFrequency * 0.5); // (int)(in.length() / 100 * 0.1);
        double rankIncreasePercent = 1.00001; // 0.001
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

        // For minMccFrequency=0.1, maxConfusionDelta=0.1, rankIncreasePercent=0.001%
        // took 33 min on Alex Mac
        // maxSavings=503929 mccLists.size=44 duplicateBranchesCount=8
        // [ac, al, an, and, ar, as, at, ch, ed, ed , el, en, er, er , es, et, il, in, ing, ing ,
        // ion, is, is , it, of, om, on, on , or, or , ot, ou, st, th, the , to ](36)503929,65;2918797511043
        // [ac, al, an, and, ar, as, at, ch, ed, ed , el, en, er, er , es, et, il, in, ing, ing ,
        // ion, is , it, of, om, on, on , or, or , ot, ou, st, th, the , to ](35)500333,65;-3596,0;2897971361112
        // [ac, al, an, and, ar, as, at, ch, ed, ed , el, en, er, er , es, et, il, in, ing, ing ,
        // ion, is, is , it, of, om, on, on , or, or , ou, st, th, the , to ](35)497091,41;2879216642420
        // [ac, al, an, and, ar, as, at, ch, ed, ed , el, en, er, er , es, et, il, in, ing, ing ,
        // ion, is , it, of, om, on, on , or, or , ou, st, th, the , to ](34)493495,41;-6838,-24;2858390319905
        // [ac, al, an, and, ar, as, ch, ed, ed , el, en, er, er , es, et, il, in, ing, ing ,
        // ion, is, is , it, of, om, on, on , or, or , ou, st, th, the , to ](34)479310,20;2776246249182
        // [ac, al, an, and, ar, as, ch, ed, ed , el, en, er, er , es, et, il, in, ing, ing ,
        // ion, is , it, of, om, on, on , or, or , ou, st, th, the , to ](33)475714,20;-17781,-21;2755419775656
        // [ac, al, an, and, ar, as, ch, ed, ed , el, en, er, er , es, et, il, in, ing, ing ,

        // For minMccFrequency=6k, maxConfusionDelta=3k, rankIncreasePercent=0.001%
        // took 28 min on Alex Mac
        // maxSavings=487147 mccLists.size=32 duplicateBranchesCount=0
        // [ac, al, an, and, ar, as, at, ch, ed , el, en, er, er , es, et, il, in, ing , ion, is ,
        // it, of, om, on, or, or , ot, ou, st, th, the , to ](32)487147,65;2821603019083
        // [ac, al, an, and, ar, as, at, ch, ed , el, en, er, er , es, et, il, in, ing , ion, is ,
        // it, of, om, on, or, or , ou, st, th, the , to ](31)480309,41;-6838,-24;2782021345020
        // [ac, al, an, and, ar, as, ch, ed , el, en, er, er , es, et, il, in, ing , ion, is ,
        // it, of, om, on, or, or , ou, st, th, the , to ](30)462528,20;-17781,-21;2679050247022
        // [ac, al, an, and, ar, as, ch, ed , el, en, er, er , es, et, il, in, ing , ion, is ,
        // it, of, om, on, or, or , ou, th, the , to ](29)450649,10;-11879,-10;2610254568920
    }

    @Test
    public void testBigFileFinalList() throws Exception {
        String fileName = "812_notes.txt";
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        final String in = Utility.toString(is);

        // Length of 812_notes is 2896k, so 0.1% = 2896
        int minMccFrequency = 6000; // (int)(in.length() / 100 * 0.1);
        int maxConfusionDelta = (int)(minMccFrequency * 0.5); // (int)(in.length() / 100 * 0.1);
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
