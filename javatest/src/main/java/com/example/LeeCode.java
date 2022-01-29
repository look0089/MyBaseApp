package com.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jzs on 2021/10/29.
 */
class LeeCode {

    public static void main(String[] args) {
        int[] a = {7, 11, 4, 5, 3, 15};
        System.out.println(Arrays.toString(Bubble(a)));
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
}
