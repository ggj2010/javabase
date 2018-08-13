
package com.ggj.java.teach;

import java.util.Arrays;

/**
 * @author:gaoguangjin
 * @date:2018/4/9
 */

public class Itertor {
    public static void main(String[] args) {

        int[] array={5,7,2,3,4};
        Arrays.sort(array);
        sort(array);
        for (int i : array) {
            System.out.println(""+i);
        }


        // String[] arrayString={"1","a"};
        //
        // for (String s : arrayString) {
        // System.out.println("args = [" + s + "]");
        // }
        //
        // for (int i : array) {
        // System.out.println("args = [" + i + "]");
        // }
        //
        // for (int i = 0; i <array.length ; i++) {
        // if(i==1)
        // continue;
        // System.out.println("args = [" + array[i] + "]");
        // }
        //
        // int a=5;
        // int i=0;
        //
        // while(i<5){
        // i=i+1;
        // System.out.println("------");
        // }

        int[] array1 = {
                1, 2, 3, 4
        };

        int[] array2 = new int[7];

        // System.arraycopy(array1,0,array2,0,array1.length);
        // arrayCopy(array1,0,array2,0,array1.length);
        // for (int i : array2) {
        // System.out.println("args = [" + i + "]");
        // }
        //
        // test1();
        //
        // test2();
        //
        // int c = testReturnInt("a", 1);
        //
        // String d = testReturnString("a");
        //
        //
        // int e=testGetSum(1,1);
        // System.out.println("args = [" + e + "]");
        testSum(1, 1);
    }

    /**
     * 排序
     * @param array
     */
    private static void sort(int[] array) {

    }

    private static void testSum(int a, int i1) {
        // 逻辑
        System.out.println("sum:" + (a + i1));
    }

    private static int testGetSum(int i, int i1) {
        return i + i1;
    }

    private static int testReturnInt(String a, int i) {
        // 执行一段逻辑
        return 112;
    }

    private static String testReturnString(String a) {
        return "dd";
    }

    private static void test2() {
        // 学怎么用 System.arraycopy
    }

    private static void test1() {
        // 怎么学 for循环
    }

    private static void arrayCopy(int[] array1, int i, int[] array2, int i1, int length) {

    }
}
