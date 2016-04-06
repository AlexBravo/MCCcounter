package com.google.android.mcccounter;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/** * Created by wendy on 3/20/2016. */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateMccList {

//    @Before
//    public void setUp() throws Exception {
//    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void createMccList() throws Exception {
        InputStream is = getInstrumentation().getContext().getResources()
          .openRawResource(com.google.android.mcccounter.test.R.raw.aliceinwonderland);
        //    .openRawResource(com.google.android.mcccounter.test.R.raw.my812_notes);

        String in = Utility.toString(is);

        MccCalculator fullMccCalculator = new MccCalculator(MccLists.fullShortMccList, MccLists.fullLongMccList);
        HashMap<String, Integer> frequencies = fullMccCalculator.calculateFrequencies(in);
        Log.i("MCC frequencies", frequencies.toString());

        Set<String> allSortedMCCs = frequencies.keySet();
        Log.i("MCC allSortedMCCs", allSortedMCCs.toString());

        List<String> newList = new ArrayList<>();


        while (!allSortedMCCs.isEmpty()) {
            HashMap<String, Integer> candidateConfusions = new HashMap<>();
            for (String candidate : allSortedMCCs) {
                newList.add(candidate);
                MccCalculator mccCalculator = new MccCalculator(newList, MccLists.fullLongMccList);
                newList.remove(candidate);

                HashMap<String, Integer> confusions = mccCalculator.calculateConfusions(in);

                //Log.i("MCC confusions", "Adding " + candidate + " resulted in " + confusions.size() + " confusions " + confusions.toString());

                // Calculate the sum of all confusions
                int confusionTotal = Utility.calculateTotal(confusions);
                // Calculate rank so that less confusing and more frequent MCCs have a higher rank
                // Add 1 to avoid 0
                int confusionRank = frequencies.get(candidate) * 10000 / (confusionTotal + 1) ;
                candidateConfusions.put(candidate, confusionRank);
            }
            LinkedHashMap<String, Integer> sortedCandidateConfusions = Utility.sortMap(candidateConfusions);
            //Log.i("MCC confusions", sortedCandidateConfusions.toString());


            Map.Entry<String, Integer> entry = sortedCandidateConfusions.entrySet().iterator().next();
            String mccToAdd = entry.getKey();
            Log.i("MCC ranks", "'" + mccToAdd + "' has rank " + entry.getValue());

            allSortedMCCs.remove(mccToAdd);
            newList.add(mccToAdd);
        }

        Log.i("MCC newList", newList.toString());
    }
}
