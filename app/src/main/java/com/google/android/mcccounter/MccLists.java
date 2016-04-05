package com.google.android.mcccounter;

import java.util.Arrays;
import java.util.List;

/** Created by alex on 4/4/16. */

public class MccLists {
//    public static List<String> shortMccList = Arrays.asList(
//            "re", "in", "or", "an", "th", "al", "at", "ma", "ar", "es",
//            "co", "il", "ac", "ch", "no", "de", "se", "ed", "of", "le",
//            "ro", "ou", "ti", "is", "it", "en", "er", "on", "to", "st"); // Full list

    // Removed: re, ti, le, co, se, ma, st
    // Removed: es, ti, re, co, le, se, ma*/
    // Removed: es, ti, le, se, ma
    // Removed: ti, es, le, se, ma
    // To full list added: as, ea, ha, he, io, li, ll, me, nd, ne, nt, te, us, ve // More than 9k
    // To full list added: as, io, li, me, ne, nt, te, us, ve
    // Removed: io, se, nt, ne, es, ti, ed, en, ma, li, il, us, ac, as, no
    // Removed: 8 MCCs (es, ti, ed, en, se, ma, il, ac, no) and added 3 MCCs (me, te, ve)
    // Removed: 11 MCCs (ac, ed, es, is, le, ma, no, ou, se, th, ti) and added "me"
    public static List<String> shortMccList = Arrays.asList(
        "re", "in", "or", "an", "al", "at", "ar", "co", "ch", "de",
        "of", "ro", "it", "er", "on", "to", "st", "me", "il", "en");

    public static List<String> longMccList = Arrays.asList("ing", "and", "the");
}
