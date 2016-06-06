package com.google.android.mcccounter;

/**
 * Created by wendy on 6/5/2016.
 */
public class Results {
    private long maxSavings;
    private int mccListsSize;
    private int duplicateBranchesCount;

    public void setMaxSavings(long val){
        maxSavings = val;
    }
    public long getMaxSavings(){
        return maxSavings;
    }
    public void setMccListsSize(int val){
        mccListsSize = val;
    }
    public int getMccListsSize(){
        return mccListsSize;
    }
    public void setDuplicateBranchesCount(int val){
        duplicateBranchesCount = val;
    }
    public int getDuplicateBranchesCount(){
        return duplicateBranchesCount;
    }
}
