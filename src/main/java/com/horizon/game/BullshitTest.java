package com.horizon.game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BullshitTest {

    public int[] twoSum(int[] nums, int target) {
        int attempts = 0;
        int solution = 0;
        int[] numSolution = new int[]{0, 0};

        while(attempts < 5){

            //int position
            for(int i : sumarize(nums, numSolution, target)){
                solution += i;
            }

            if(target == solution)
                break;
            attempts++;
        }

        return numSolution;
    }

    public int[] sumarize(int[] numbers, int[] currentIndexes, int target){
        List<Integer> indexes = new LinkedList<Integer>();

        for(int number : numbers){
            int currentTarget = calculateCurrentTarget(numbers, currentIndexes);

            currentTarget += number;
            if(currentTarget > target)
                continue;
            else if(currentTarget < target)
                sumarize(numbers, currentIndexes, target);
            else if(currentTarget == target)
                return null;
        }

        return null;
    }

    private int calculateCurrentTarget(int[] numbers, int[] currentIndexes){
        int currentTarget = 0;
        for(int index : currentIndexes){
            currentTarget += numbers[index];
        }

        return currentTarget;
    }
}
