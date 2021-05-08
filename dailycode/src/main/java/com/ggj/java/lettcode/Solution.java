package com.ggj.java.lettcode;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode-cn.com/problems/two-sum/
 * 给定 nums = [2, 7, 11, 15], target = 9
 * <p>
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 *
 * @author gaoguangjin
 */
public class Solution {
    public static void main(String[] args) {
        int nums[] = {1, 10, 3, 9, 2};
        // twoSum(nums,19);
        twoSum3(nums, 19);

        maxLength(nums);
    }

    //时间复杂度：O(n^2)O(n
    //空间复杂度：O(1)O(1)。
    public static int[] twoSum(int[] nums, int target) {
        int restult[] = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    restult[0] = i;
                    restult[1] = j;
                    System.out.println(String.format("find result index %S and %S", i, j));
                    return restult;
                }
            }
        }
        return null;
    }

    public static int[] twoSum2(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    System.out.println(String.format("find result index %S and %S", i, j));
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public static int[] twoSum3(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        // 1,10,3,9,2
        // 19
        // 1 18
        // 10 9
        // 3 16
        //9 ,10
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(nums[i])) {
                System.out.println(String.format("find result index %S and %S", i, map.get(nums[i])));
                return new int[]{i, map.get(nums[i])};
            }
            map.put(complement, i);
        }
        return null;
    }

    public static int maxLength(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int max = 0;
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        //定义两个变量 j 和 i
        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            //遇到重复
            if (map.containsKey(arr[i])) {
                j = Math.max(j, map.get(arr[i] + 1));
            }
            map.put(arr[i], i);
            max = Math.max(max, i - j + 1);
        }

        return max;

        // write code here
    }


}
