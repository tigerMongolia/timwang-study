package com.timwang.leetcode.core.stack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangjun
 * @date 2020-03-17
 */
public class MergeNumbers {

    public static void main(String[] args) {
        InputStreamReader ir = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(ir);
        List<String> inputStrs = new ArrayList<>();
        int numbers = 0;
        try {
            String s = in.readLine();
            inputStrs.add(s);
            numbers++;
            while ((s = in.readLine()) != null) {
                numbers++;
                inputStrs.add(s);
                if (numbers == 2) {
                    numbers = 0;
                    int[] numberArrOne = getNumbers(inputStrs.get(0));
                    int[] numberArrTwo = getNumbers(inputStrs.get(1));
                    int[] merge = merge(numberArrOne, numberArrOne.length, numberArrTwo, numberArrTwo.length);
                    System.out.println(changeResult(merge));
                    inputStrs = new ArrayList<>();
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String changeResult(int[] merge) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < merge.length; i++) {
            if (i == merge.length - 1) {
                stringBuilder.append(merge[i]);
                continue;
            }
            stringBuilder.append(merge[i]).append(" ");
        }
        return stringBuilder.toString();
    }

    private static int[] getNumbers(String string) {
        String[] strArr = string.split(" ");
        int[] one = new int[strArr.length];
        List<Integer> result = new ArrayList<>(strArr.length);
        for (String s : strArr) {
            result.add(Integer.valueOf(s));
        }
        int[] idList = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            idList[i] = result.get(i);
        }
        return idList;


    }

    private static int[] merge(int[] firstArr, int firstArrLen, int[] secondArr, int secondArrLen) {
        int[] result = new int[firstArrLen + secondArrLen];
        int i = 0, j = 0, k = 0;
        while (i < firstArrLen && j < secondArrLen) {
            if (firstArr[i] < secondArr[j]) {
                result[k++] = firstArr[i++];
            } else {
                result[k++] = secondArr[j++];
            }
        }
        if (i != firstArrLen) {
            while (i < firstArrLen) {
                result[k++] = firstArr[i++];
            }
        }
        if (j != secondArrLen) {
            while (j < secondArrLen) {
                result[k++] = secondArr[j++];
            }
        }
        k = 0;
        for (i = 0; i < firstArr.length; i++) {
            firstArr[i] = result[k++];
        }
        return result;
    }


}
