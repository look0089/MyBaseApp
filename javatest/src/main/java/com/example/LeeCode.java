package com.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jzs on 2021/10/29.
 */
class LeeCode {

    public static void main(String[] args) {
//        int[] a = {7, 11, 4, 5, 3, 15};
//        System.out.println(Arrays.toString(Bubble(a)));
//        String[] a = {"h", "e", "l", "l", "o", "H"};
//        reverseString(a);
//        System.out.println(reverse(2147483647));
        System.out.println(firstUniqChar("loveandpeace"));
    }

    public static int[] Bubble(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 0; j < nums.length - 1 - i; j++) {
                if (nums[j] > nums[j + 1]) {
                    System.out.println(Arrays.toString(nums));
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                    System.out.println(i + "================" + j);
                }
            }
        }
        return nums;
    }

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            System.out.println(m.get(target - nums[i]));
            if (m.get(target - nums[i]) != null) {
                return new int[]{m.get(target - nums[i]), i};
            }
            m.put(nums[i], i);
            System.out.println(m);
        }
        return new int[]{0, 0};
    }

    /**
     * 输入：s = ["h","e","l","l","o"]
     * 输出：["o","l","l","e","h"]
     *
     * @return
     */
    public static void reverseString(String[] s) {
        int length = s.length;
        String temp;
        for (int i = 0; i < length / 2; i++) {
            temp = s[i];
            s[i] = s[length - 1 - i];
            s[length - 1 - i] = temp;
            System.out.println(Arrays.toString(s));
        }
    }

    /**
     * 示例 1：
     * <p>
     * 输入：x = 123
     * 输出：321
     * 示例 2：
     * <p>
     * 输入：x = -123
     * 输出：-321
     * 示例 3：
     * <p>
     * 输入：x = 120
     * 输出：21
     * 示例 4：
     * <p>
     * 输入：x = 0
     * 输出：0
     */
    public static int reverse(int x) {
        int res = 0;
        while (x != 0) {
            System.out.println("x = " + x);
            System.out.println("res = " + x);
            int t = x % 10;
            System.out.println("t = " + t);
            int newRes = res * 10 + t;
            System.out.println("newRes = " + newRes);
            //如果数字溢出，直接返回0
            System.out.println("(newRes - t) / 10 = " + ((newRes - t) / 10 != res));
            if ((newRes - t) / 10 != res) {
                System.out.println("返回0");
                return 0;
            }
            res = newRes;
            x = x / 10;
            System.out.println("=======================");
        }
        return res;
    }

    /**
     * 给定一个字符串 s ，找到 它的第一个不重复的字符，并返回它的索引 。如果不存在，则返回 -1 。
     * 示例 1：
     * <p>
     * 输入: s = "leetcode"
     * 输出: 0
     * 示例 2:
     * <p>
     * 输入: s = "loveleetcode"
     * 输出: 2
     * 示例 3:
     * <p>
     * 输入: s = "aabb"
     * 输出: -1
     */
    public static int firstUniqChar(String x) {
        for (int i = 0; i < x.length(); i++) {
            if (x.lastIndexOf(x.charAt(i)) == x.indexOf(x.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

}
