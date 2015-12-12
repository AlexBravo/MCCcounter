package com.google.android.mcccounter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Created by Kids on 12/12/2015.

public class MccCounter {
    public Map<String, Integer> calculateMCCs(String in){
        Map<String, Integer> mccs = new HashMap<>();
        if(!in.isEmpty()){
            mccs.put("ing", 1);
        }
        return mccs;
    }
}
