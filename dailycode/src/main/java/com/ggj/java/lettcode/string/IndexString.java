package com.ggj.java.lettcode.string;


/**
 * 假设有数组 a字符串goodgao 判断gao在字符串a的那个index
 * https://www.cnblogs.com/yjiyjige/p/3263858.html
 *
 * @author gaoguangjin
 */
public class IndexString {
    public static void main(String[] args) {
        //jnduwslil
        char[] parentAarry = "acabad".toCharArray();
        char[] needMatchAarry = "aba".toCharArray();
        normal(parentAarry, needMatchAarry);

        int index = bf(parentAarry, needMatchAarry);
        System.out.println("find bf = [" + index + "]");
        int[] dd = getNext(needMatchAarry);

        int index2 = kmp(parentAarry, needMatchAarry);
        System.out.println("find kmp = [" + index2 + "]");

    }

    /**
     * 朴素的模式匹配
     *
     * @param t
     * @param p
     */
    private static int bf(char[] t, char[] p) {
        // 主串的位置
        int i = 0;
        // 模式串的位置
        int j = 0;
        while (i < t.length && j < p.length) {
            // 当两个字符相同，就比较下一个
            if (t[i] == p[j]) {
                i++;
                j++;
            } else {
                // 一旦不匹配，i后退
                i = i - j + 1;
                // j归0
                j = 0;
            }
        }
        //找到
        if (j == p.length) {
            return i - j;
        } else {
            return -1;
        }
    }

    /**
     * 朴素算法
     *
     * @param parentAarry
     * @param needMatchAarry
     */
    private static void normal(char[] parentAarry, char[] needMatchAarry) {
        int j = 0;
        for (int i = 0; i < parentAarry.length; i++) {
            char parentStr = parentAarry[i];
            for (; j < needMatchAarry.length; ) {
                if (needMatchAarry[j] == parentStr) {
                    if (j == needMatchAarry.length - 1) {
                        System.out.println("find:" + (i + 1 - needMatchAarry.length));
                    }
                    j++;
                    break;
                } else {
                    //回溯i要回溯
                    i = i - j;
                    j = 0;
                    break;
                }
            }
        }

    }

    /**
     * 当匹配失败时，j要移动的下一个位置k，最前面的k个字符和j之前的最后k个字符是一样的
     * 因为在P的每一个位置都可能发生不匹配，也就是说我们要计算每一个位置j对应的k，所以用一个数组next来保存
     * 1<k<j41w1saew`awQ
     * p[0~ k-1]==p[j-k,j-1]
     * <p>
     * <p>
     * 我们发现一个规律：
     * 当P[k] == P[j]时，
     * 有next[j+1] == next[j] + 1
     * 其实这个是可以证明的：
     * 因为在P[j]之前已经有P[0 ~ k-1] == p[j-k ~ j-1]。（next[j] == k）
     * 这时候现有P[k] == P[j]，我们是不是可以得到P[0 ~ k-1] + P[k] == p[j-k ~ j-1] + P[j]
     * 即：P[0 ~ k] == P[j-k ~ j]，
     * 即next[j+1] == k + 1 == next[j] + 1
     * <p>
     * <p>
     * 有next[j+1] == next[j] + 1
     *
     * @param p
     * @return
     */
    public static int[] getNext(char[] p) {
        //next数组
        int[] next = new int[p.length];
        //初始化等于0
        next[0] = 0;
        //后缀
        int j = 1;
        //前缀
        int k = 0;
        while (j < p.length - 1) {
            // //next[j]表示的是当T[i]和P[j]不同是，j要回到哪个位置
            if (k == 0 || p[j] == p[k]) {
                ++j;
                ++k;
                /**
                 * next[j+1] == k + 1 == next[j] + 1
                 */
                next[j] = k;
            } else {
                //不匹配 k值回溯 模式串的自我匹配 找到p[k]对应的next[k]
                //例如
                /**
                 *  j 1 2 3 4 5 6 7 8 9
                 *
                 *    a b a b a a a b a
                 *
                 *  k 0 1 2 2 3 4 2 2 3
                 *
                 *  当就j=6  k=4的时候 出现不匹配
                 *  p[j]=a p[4]=b
                 *  这个时候就要回溯长度更短的相同前缀后缀
                 *  需要将k的值回溯
                 *  p[k(4)]=b 对应的p[next[k](2)]=b
                 *  k=next[k] ==> k=next[4]=2
                 *   p[k]=p[2]=b  还不相等 继续回溯
                 *   k=next[2]=1
                 *   p[k]=p[2]=a 与p[j]=a相等
                 *   next[7]=2
                 *
                 *  如果直接用k=k-1会有问题
                 *
                 */

                //next[k]表示当前节点前面有相同前缀的最大数量， 找到长度更短的相同前缀后缀！！！！
                k = next[k];

                /**
                 * next[k]表示p[k]前面有相同前缀尾缀的长度(这个长度是按最大算的)，
                 * p[next[k]]表示相同前缀的最后一个字符，如果p[next[k]]==p[j]，
                 * 则可以肯定next[k+1]=next[k]+1,如果p[next[k]]!=p[j]，
                 * 既然next[k]长度的前缀尾缀都不能匹配了，是不是应该缩短这个匹配的长度，
                 * 到底缩短多少呢，next[next[k]]表示p[next[k]]前面有相同前缀尾缀的长度(这个长度是按最大算的)，
                 * 所以一直递推就可以了，因为这个缩小后的长度都是按最大的算的
                 */
            }
        }
        return next;
    }

    private static int kmp(char[] t, char[] p) {
        // 主串的位置
        int i = 0;
        // 模式串的位置
        int j = 1;
        int[] next =getNext(p);
        while (i < t.length && j < p.length) {
            // // 当j为-1时，要移动的是i，当然j也要归0
            if (j == 0 ||t[i] == p[j]) {
                i++;
                j++;
            } else {
                // 一旦不匹配，i后退
               // i = i - j + 1;
                // j归0
                //j = 0;
                j=next[j];
            }
        }
        //找到
        if (j == p.length) {
            return i - j;
        } else {
            return -1;
        }
    }

}
