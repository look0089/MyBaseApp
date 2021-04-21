package com.example;

/**
 * @author jzs created 2017/5/15
 * 计算1出现过几次
 */
public class NumberOf1Between1AndN {

    public static void main(String arg[]) {
        System.out.println("" + NumberOf1Between1AndN_Solution(12));
    }

    public static int NumberOf1Between1AndN_Solution(int n) {
        int count = 0;
        for (int i = 1; i <= n; i++) {
            count += getNumOf1(i);
        }

        return count;
    }

    private static int getNumOf1(int i) {
        int count = 0;
        while (i > 0) {
            if (i % 10 == 1) {
                System.out.println("i=" + i);
                count++;
            }
            i = i / 10;
        }
        return count;
    }
}
