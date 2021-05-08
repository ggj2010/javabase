package com.ggj.java.teach;

/**
 * fn=f(n-1)+f(n-2)
 * 0 1 2 3 4 5 6  7  8
 * 0 1 1 2 3 5 8 13 21
 *
 * @author gaoguangjin
 */
public class Fibonacci {


    public static void main(String[] args) {

        //testOne();
        testTwo();

    }

    private static void testOne() {
        System.out.println(getSum(1));
        System.out.println(getSum(2));
        System.out.println(getSum(3));
        System.out.println(getSum(4));
        System.out.println(getSum(5));
        System.out.println(getSum(6));
        System.out.println(getSum(7));
        System.out.println(getSum(8));
        System.out.println(getSum(9));

    }

    private static void testTwo() {
        System.out.println(demoTwo(1));
        System.out.println(demoTwo(2));
        System.out.println(demoTwo(3));
        System.out.println(demoTwo(4));
        System.out.println(demoTwo(5));
        System.out.println(demoTwo(6));
        System.out.println(demoTwo(7));
        System.out.println(demoTwo(8));
        System.out.println(demoTwo(9));
    }

    /**
     * 递归
     *
     * @param i
     * @return
     */
    private static int getSum(int i) {
        if (i < 1) {
            return 0;
        }
        if (i <= 2) {
            return 1;
        }
        return getSum(i - 1) + getSum(i - 2);
    }

    /**
     * 动态规划
     * @param i
     * @return
     */
    private static int demoTwo(int i) {
        if (i < 1) {
            return 0;
        }
        if (i <= 2) {
            return 1;
        }
        int tempNext = 1;
        int tempNextNext = 1;
        int temp = 0;
        for (int j = 2; j < i; j++) {
            temp = tempNext + tempNextNext;
            tempNext = tempNextNext;
            tempNextNext = temp;

        }
        return temp;
    }


}
