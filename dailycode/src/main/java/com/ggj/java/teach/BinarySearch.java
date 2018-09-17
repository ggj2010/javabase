package com.ggj.java.teach;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;

/**
 * @author:gaoguangjin
 * @date:2018/8/22
 */
@Slf4j
public class BinarySearch {
    public static void main(String[] args) {
        int[] arrays=new int[11];
        Random random=new Random();
        int data=123;
        for (int i=0;i<10;i++) {
            arrays[i]=(random.nextInt(200));
        }
        arrays[10]=data;
        Arrays.sort(arrays);
        for (int array : arrays) {
            log.info(array+"");
        }
        int seq=search(arrays,data,0,arrays.length-1);
        System.out.println("数组位置："+seq);
    }

    private static int search(int[] arrays,int data,int left,int right) {
        if(data < arrays[left] || data > arrays[right] || left > right){
            return -1;
        }
        int middleSeq=(left+right)/2;
        int middleData=arrays[middleSeq];
        if(data>middleData){
            return search(arrays,data,middleSeq+1,right);
        }else  if(data<middleData){
            return search(arrays,data,left,middleSeq-1);
        }else  if(data==middleData){
            return middleSeq;
        }
       return 0;
    }
}
