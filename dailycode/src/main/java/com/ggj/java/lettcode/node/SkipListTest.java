package com.ggj.java.lettcode.node;

import lombok.Data;

import java.util.Random;

/**
 * https://www.cnblogs.com/cofjus/p/13602564.html
 * https://www.jianshu.com/p/168423ad0ab9
 * 跳跃表
 * 一个跳跃表应该有若干个层（Level）链表组成；
 * 1. 跳跃表中最底层的链表包含所有数据； 每一层链表中的数据都是有序的；
 * 2. 如果一个元素 X 出现在第i层，那么编号比 i 小的层都包含元素 X；
 * 3. 第 i 层的元素通过一个指针指向下一层拥有相同值的元素；
 * 4. 在每一层中，-∞ 和 +∞ 两个元素都出现(分别表示 INT_MIN 和 INT_MAX)；
 * 5. 头指针（head）指向最高一层的第一个元素；
 *
 * @author gaoguangjin
 */
public class SkipListTest<T> {
    // 节点数量
    public int n;
    //节点最大层次
    public int h;
    //第一个节点
    public SkipListEntry head;
    //最后一个节点
    public SkipListEntry tail;
    //随机数，决定新添加的节点是否能够向更高一层的链表攀升
    public Random r;

    public static void main(String[] args) {

    }

    public SkipListTest() {
        //初始化，创建空节点

        this.head = new SkipListEntry(Integer.MIN_VALUE, null);
        this.tail = new SkipListEntry(Integer.MAX_VALUE, null);

        // head->min->max
        this.head.right = tail;
        // tail->max
        this.tail.left = head;

        this.h = 0;
        this.n = 0;
        this.r = new Random();
    }

    /**
     * 1、如果传入的 key 值在跳跃表中存在，则 findEntry 返回该对象的底层节点；
     * 2、如果传入的 key 值在跳跃表中不存在，则 findEntry 返回跳跃表中 key 值小于 key，并且 key 值相差最小的底层节点
     *
     * @param key
     * @return
     */
    public SkipListEntry findEntry(Integer key) {
        SkipListEntry p = head;
        while (true) {
            //从左向右，直到右边节点key值大于要查找的key值
            while (p.right.key <= key) {
                p = p.right;
            }

            // 如果有更低层的节点，则向低层移动
            if (p.down != null) {
                p = p.down;
            } else {
                break;
            }
        }
        return p;
    }

    public Object get(Integer key) {
        SkipListEntry entry = findEntry(key);
        if (entry.getKey().equals(key)) {
            return entry.value;
        } else {
            return null;
        }
    }

    /**
     * 如果 put 的 key 值在跳跃表中存在，则进行修改操作；
     * 如果 put 的 key 值在跳跃表中不存在，则需要进行新增节点的操作，并且需要由 random 随机数决定新加入的节点的高度（最大level）；
     * 当新添加的节点高度达到跳跃表的最大 level，需要添加一个空白层（除了-oo 和 +oo 没有别的节点）
     * 1、第一步，查找适合插入的位子
     * 2、第二步，在查找到的p节点后面插入新增的节点q
     * 3、第三步，重复下面的操作，使用随机数决定新增节点的高度
     * 从p节点开始，向左移动，直到找到含有更高level节点的节点；
     * 将p指针向上移动一个level；
     * 创建一个和q节点data一样的节点，插入位子在跳跃表中p的右方和q的上方；
     * 直到随机数不满足向上攀升的条件为止
     */
    public Object put(Integer key, Object value) {
        SkipListEntry p, q;
        int i = 0;

        // 查找适合插入的位子
        p = findEntry(key);

        if (p.key.equals(key)) {
            Object oldValue = p.value;
            p.value = value;
            return oldValue;
        }

        //如果跳跃表中不存在含有key值的节点，则进行新增操作
        q = new SkipListEntry(key, value);

        q.right = p.right;
        q.left = p;
        p.right.left = q;
        p.right = q;


        //  再使用随机数决定是否要向更高level攀升

        while (r.nextDouble() < 0.5) {
            // 如果新元素的级别已经达到跳跃表的最大高度，则新建空白层
            if (i >= h) {
                addEmptyLevel();
            }

            //找到最左边第一个有up的节点，然后再根据概率判断是否要升
            //从p向左扫描含有高层节点的节点
            while (p.up == null) {
                p = p.left;
            }
            p = p.up;

            //新增和q指针指向的节点含有相同key值的节点对象
            SkipListEntry z = new SkipListEntry(key, null);

            //插入z，插入位子在跳跃表中p的右方
            z.left = p;
            z.right = p.right;
            p.right.left = z;
            p.right = z;

            //q的上方
            z.down = q;
            q.up = z;
            // 继续向上爬
            q = z;
            i = i + 1;
        }
        n = n + 1;
        return null;
    }

    public void addEmptyLevel() {
        SkipListEntry<T> p1 = new SkipListEntry<T>(Integer.MIN_VALUE, null);
        SkipListEntry<T> p2 = new SkipListEntry<T>(Integer.MAX_VALUE, null);
        p1.right = p2;
        p1.down = head;
        p2.left = p1;
        p2.down = tail;
        head.up = p1;
        tail.up = p2;
        head = p1;
        tail = p2;
        h++;
    }


    public SkipListEntry findEntryOne(Integer key) {
        SkipListEntry entry = head;
        while (true) {
            SkipListEntry currentEntry = entry;
            if (key > entry.key) {
                //向右边查询
                entry = entry.right;
                if (entry != tail) {
                    continue;
                } else {
                    entry = currentEntry.down;
                }
            } else if (key < entry.key) {
                //向左边查询
                entry = entry.left;
                if (entry != head) {
                    continue;
                } else {
                    entry = currentEntry.down;
                }
            } else {
                return entry;
            }
        }
    }

    @Data
    static class SkipListEntry<T> {
        public Integer key;
        public T value;

        public SkipListEntry up;
        public SkipListEntry down;
        public SkipListEntry left;
        public SkipListEntry right;

        public SkipListEntry(Integer key, T value) {
            this.key = key;
            this.value = value;
        }
    }
}
