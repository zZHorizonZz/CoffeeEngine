package com.horizon.game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BullshitTest {

    public int searchInsert(int[] nums, int target) {
        for(int i = 0; i < nums.length; i++)
            if(nums[i] == target)
                return i;

        int[] testNums = new int[nums.length + 1];
        boolean isAdded = false;

        for(int i = 0; i < nums.length; i++){
            if(target < nums[i]) {
                testNums[i] = target;
                isAdded = true;
            } else {
                testNums[i] = nums[isAdded ? i - 1 : i];
            }
        }

        return testSearch(testNums, target);
    }

    public int testSearch(int[] nums, int target){
        for(int i = 0; i < nums.length; i++) {
            if (nums[i] == target)
                return i;
        }

        return 0;
    }

    private int segmentation = 10;
    private int versions;
    private int[] offsetHistory = new int[0];

    public int firstBadVersion(int n) {
        int lastSegment = 1;
        int segmentSize = 1;
        this.versions = n;

        if(n > segmentation) {
            segmentSize = segmentate(segmentSize, n);
            int badVersion = check(lastSegment, versions, segmentSize);

            if(badVersion == 0) {
                return checkFromOffSet(calculateOffset(segmentSize, versions)) - 2;
            } else {
                return badVersion;
            }
        } else {
            return checkSegment(lastSegment, n);
        }
    }

    public int check(int lastSegment, int endOfSegment, int segmentSize) {
        try{
            for(int i = lastSegment; i < endOfSegment; i += segmentSize) {

                if(isBadVersion(i)) {
                    System.out.println("Check -> " + i);
                    if(segmentSize <= 10) {
                        return checkSegment(i - segmentSize, i);
                    } else {
                        return check(i - segmentSize, i, segmentSize / 10);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    public int checkFromOffSet(int offset) {
        int segmentSize = segmentate(1, offset);
        int badVersion = check(versions - addtionalCalculation() + 1, offset, segmentSize);

        if(badVersion == 0) {
            return checkFromOffSet(calculateOffset(segmentSize, offset));
        } else {
            return badVersion;
        }
    }

    public int addtionalCalculation(){
        int addtionalNumber = offsetHistory[0];

        if(offsetHistory.length > 1) {
            for(int i = 1; i < offsetHistory.length; i++){
                addtionalNumber -= offsetHistory[offsetHistory.length - i];
            }
        }

        return addtionalNumber;
    }

    public int calculateOffset(int segmentSize, int segment) {
        int offset = segment;

        for(int i = segmentSize; i <= segment; i += segmentSize) {
            offset -= segmentSize;
        }

        int[] tempOffsetHistory = offsetHistory;
        offsetHistory = new int[offsetHistory.length + 1];

        for(int i = 0; i < offsetHistory.length; i++){
            offsetHistory[i] = i == tempOffsetHistory.length ? offset : tempOffsetHistory[i];
        }

        return offset;
    }

    public int segmentate(int n, int versions) {
        if(n * segmentation > versions || n == 1000000000)
            return n / segmentation;
        else
            return segmentate(n * segmentation, versions);
    }

    public int checkSegment(int start, int end) {
        for(int i = start; i <= end; i++)
            if(isBadVersion(i))
                return i;

        return 0;
    }

    private boolean isBadVersion(int i) {
        return i >= 1150769282;
    }

    public int climbStairs(int n) {
        int distincts = 0;
        boolean stillPossible = true;
        List<Integer> steps = new LinkedList<Integer>();

        while(stillPossible){
            steps.add(steps.size() <= distincts ? steps.size() == 0 && distincts == 0 ? 1 : 2 : 1);

            if(calculate(intToArray(steps)) == n){
                distincts++;
                if(check(steps) == -1)
                    stillPossible = false;
                if(check(steps) == 1)
                    stillPossible = false;
                steps.clear();
            }
        }

        return distincts;
    }

    public int check(List<Integer> steps){
        int index = 1;
        for(int i : steps){
            if(steps.size() == index && i == 1) {
                return 1;
            }if(i == 1)
                return 0;

            index++;
        }

        return -1;
    }

    public int[] intToArray(List<Integer> integers){
        int[] ints = new int[integers.size()];
        for(int i = 0; i < integers.size(); i++){
            ints[i] = integers.get(i);
        }

        return ints;
    }

    public boolean isPossible(int[] steps, int top){
        int testSteps = 0;

        for(int index = 0; index < steps.length; index++){
            testSteps += steps[index];
        }

        if(testSteps >= top)
            return false;
        else
            return true;
    }

    public int calculate(int[] steps){
        int testSteps = 0;

        for (int step : steps) {
            testSteps += step;
        }

        return testSteps;
    }
}
