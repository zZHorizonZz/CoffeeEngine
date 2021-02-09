package com.horizon.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestGame {

    public static double d1 = Double.MAX_VALUE;
    public static float test = 5.0f;
    public static String testName = "test";
    public static int testId = 5;
    public static long testDuration = System.currentTimeMillis();
    public static char aChar = 'e';

    public static int[] testData = new int[] {5,5,5,5,5,5,5};
    public static List<String> testDataList = new LinkedList<>();

    public static Map<Integer, String> integerStringMap = new HashMap<>();

    public static void main(String[] args) {
        TestClass game = new TestClass();

        game.getTest1();
        testData[0] = 1;
        integerStringMap.get(4);
    }
}
