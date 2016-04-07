package com.google.android.mcccounter;

import java.util.Arrays;
import java.util.List;

/** Created by alex on 4/4/16. */

public class MccLists {
//    public static List<String> shortList = Arrays.asList(
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

    // list of 59 bigrams that have frequencies higher than 8,000 + "of":
    public static List<String> fullShortMccList = Arrays.asList(
            "in", "er", "re", "th", "on", "or", "an", "le", "te", "es",
            "he", "at", "to", "en", "co", "ro", "ed", "ti", "st", "de",
            "ng", "al", "it", "se", "ar", "nt", "nd", "ou", "om", "ma",
            "me", "li", "ne", "is", "il", "ve", "as", "io", "ra", "ta",
            "ll", "no", "ch", "ea", "et", "us", "ce", "ha", "ec", "fo",
            "ic", "ot", "ge", "ac", "ri", "el", "la", "ct", "ca", "of");
    public static List<String> fullLongMccList = Arrays.asList("ing", "ion", "and", "the");
}
