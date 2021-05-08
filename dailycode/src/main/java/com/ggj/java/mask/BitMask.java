package com.ggj.java.mask;

/**
 * @author gaoguangjin
 */
public class BitMask {
    public static int ADD = 1 << 0; //1*2的0次方 的二进制 0001
    public static int DELETE = 1 << 1; //1*2的1次方 的二进制 0010
    public static int UPDATE = 1 << 2; //1*2的2次方 的二进制 0100
    public static int SELECT = 1 << 3; //1*2的3次方 的二进制 1000
    // 当前状态
    private int currentStatus;

    BitMask(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * 添加某个操作权限 通过或操作实现
     *
     * @param more
     * @return
     */
    private BitMask append(int more) {
        currentStatus = currentStatus | more;
        return this;
    }

    /**
     * 除去某个操作权限 通过非操作 和 与操作共同实现
     *
     * @param more
     * @return
     */
    private BitMask delete(int more) {
        // 如果非操作不好理解，可以理解为 减（－）操作也是可以的
        // currentStatus = currentStatus - more;
        currentStatus &= ~more;
        return this;
    }

    /**
     * 是否拥有某个权限 通过与运算判断
     *
     * @param more
     * @return
     */
    private boolean isPermission(int more) {
        return (currentStatus & more) > 0;
    }

    public static void main(String[] args) {
        BitMask bk = new BitMask(BitMask.DELETE);
        //添加权限
        bk.append(BitMask.ADD).append(BitMask.UPDATE);
        bk.delete(BitMask.ADD);
        // 判断是否有 ADD 操作权限
        System.out.println(bk.isPermission(BitMask.ADD));
        test3();
    }

    /**
     * 测试与
     */
    public static void test1() {
        int a = 5; //0101
        int b = 6; //0110
        System.out.println(a & b); //输出为 0100 为 4
    }

    /**
     * 测试或
     */
    public static void test2() {
        int a = 5; //0101
        int b = 6; //0110
        System.out.println(a | b); // 输出为 0111 为 7
    }

    /**
     * 测试非 可以理解为从 a 中减去 b
     * ~5 取 5的补码，正数的补码 0101 ，为绝对值，其余位补零。
     * 非操作比较绕，可以理解为绝对值＋1 并取负数
     */
    public static void test3() {
        int a = 5; //0101
        // 5的原码取反就是  1010
        // 求反码 1101 求补码 1110
        System.out.println(~a); // 输出为 －6
    }
}
