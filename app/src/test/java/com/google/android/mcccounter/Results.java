package com.google.android.mcccounter;

/**
 * @author Alex  on 6/5/2016.
 */
public class Results {
    private long maxSavings;
    private int mccListsSize;
    private int duplicateBranchesCount;

    public Results(long maxSavings, int mccListsSize, int duplicateBranchesCount) {
        this.maxSavings = maxSavings;
        this.mccListsSize = mccListsSize;
        this.duplicateBranchesCount = duplicateBranchesCount;
    }

    public long getMaxSavings(){
        return maxSavings;
    }
    public int getMccListsSize(){
        return mccListsSize;
    }
    public int getDuplicateBranchesCount(){
        return duplicateBranchesCount;
    }
}
