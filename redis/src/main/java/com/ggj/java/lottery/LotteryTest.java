package com.ggj.java.lottery;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * 抽奖
 * <p>
 * 就是把0到1的区间分块，而分块的依据就是物品占整个的比重，再根据随机数种子来产生0-1中间的某个数，来判断这个数是落在哪个区间上，而对应的就是抽到了那个物品。
 * 随机数理论上是概率均等的，产生的每个数理论上也应该概率均等，那么相应的区间所含数的多少就体现了抽奖物品概率的不同。
 *
 * @author:gaoguangjin
 * @date 2017/2/10 17:09
 */
@Slf4j
public class LotteryTest {

    public static void main(String[] args) {
        //
        Gift gift1 = new Gift("1", "一等奖", "0.0001", 2);
        Gift gift2 = new Gift("2", "二等奖", "0.001", 8);
        Gift gift3 = new Gift("3", "三等奖", "0.01", 90);
        Map<Integer, Gift> giftMap = new ConcurrentHashMap<>();
        List<Gift> sizeList = new ArrayList<>();
        sizeList.add(gift1);
        sizeList.add(gift2);
        sizeList.add(gift3);
        //假设有1万人参加抽奖
        int size = 10 * 10000;
        for (int i = 0; i < sizeList.size(); i++) {
            Gift gift = sizeList.get(i);
            BigDecimal bigDecimal=new BigDecimal(gift.getProbability());
            double number = bigDecimal.multiply(new BigDecimal(size)).doubleValue();
            gift.setNumberRange(number);
            giftMap.put(i + 1, gift);
        }
        for (int i = 0; i <= size; i++) {
            int randomNumber = new Random().nextInt(size);
            Integer key=0;
            for (int j = 0; j < sizeList.size(); j++) {
                double startRange = 0;
                double endRange = 0;
                if (j > 0) {
                    Gift giftPre = sizeList.get(j - 1);
                    startRange = giftPre.getNumberRange();
                }
                Gift gift = sizeList.get(j);
                endRange = gift.getNumberRange() + startRange;
                if(startRange<randomNumber&&randomNumber<endRange){
                    key=j+1;
                    break;
                }
            }
            if(key==0){
                //log.info("{}：未中奖",randomNumber);
            }else{
                log.info("{}： {}中奖,",randomNumber,key);
            }
        }
    }
}
