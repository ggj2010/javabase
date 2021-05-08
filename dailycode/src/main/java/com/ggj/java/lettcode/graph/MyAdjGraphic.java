package com.ggj.java.lettcode.graph;

import java.util.ArrayList;

/**
 * @author gaoguangjin
 */
public class MyAdjGraphic {
    static final int maxWeight = -1; //如果两个结点之间没有边，权值为-1；
    ArrayList vertices = new ArrayList();//存放结点的集合
    int[][] edges; //邻接矩阵的二维数组
    int numOfEdges; //边的数量

    public MyAdjGraphic(int n) {
        edges = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) //对角线上的元素为0
                {
                    edges[i][j] = 0;
                } else {
                    edges[i][j] = maxWeight;
                }
            }
        }
        numOfEdges = 0;
    }

    //返回边的数量
    public int getNumOfEdges() {
        return this.numOfEdges;
    }

    //返回结点的数量
    public int getNumOfVertice() {
        return this.vertices.size();
    }

    //返回结点的值
    public Object getValueOfVertice(int index) {
        return this.vertices.get(index);
    }

    //获得某条边的权值
    public int getWeightOfEdges(int v1, int v2) throws Exception {
        if ((v1 < 0 || v1 >= vertices.size()) || (v2 < 0 || v2 >= vertices.size())) {
            throw new Exception("v1或者v2参数越界错误！");
        }
        return this.edges[v1][v2];

    }

    //插入结点
    public void insertVertice(Object obj) {
        this.vertices.add(obj);
    }

    //插入带权值的边
    public void insertEdges(int v1, int v2, int weight) throws Exception {
        if ((v1 < 0 || v1 >= vertices.size()) || (v2 < 0 || v2 >= vertices.size())) {
            throw new Exception("v1或者v2参数越界错误！");
        }

        this.edges[v1][v2] = weight;
        this.numOfEdges++;
    }

    //删除某条边
    public void deleteEdges(int v1, int v2) throws Exception {
        if ((v1 < 0 || v1 >= vertices.size()) || (v2 < 0 || v2 >= vertices.size())) {
            throw new Exception("v1或者v2参数越界错误！");
        }
        if (v1 == v2 || this.edges[v1][v2] == maxWeight)//自己到自己的边或者边不存在则不用删除。
        {
            throw new Exception("边不存在！");
        }

        this.edges[v1][v2] = maxWeight;
        this.numOfEdges--;
    }

    //打印邻接矩阵
    public void print() {
        for (int i = 0; i < this.edges.length; i++) {
            for (int j = 0; j < this.edges[i].length; j++) {
                System.out.print(edges[i][j] + " ");
            }
            System.out.println();
        }
    }


    //插入的边的类
    public static class Weight {

        int row;  //起点
        int col;  //终点
        int weight; //权值

        Weight(int row, int col, int weight) {
            this.row = row;
            this.col = col;
            this.weight = weight;
        }

        public static void createAdjGraphic(MyAdjGraphic g, Object[] vertices, int n, Weight[] weight, int e)
                throws Exception {
            //初始化结点
            for (int i = 0; i < n; i++) {
                g.insertVertice(vertices[i]);
            }
            //初始化所有的边
            for (int i = 0; i < e; i++) {
                g.insertEdges(weight[i].row, weight[i].col, weight[i].weight);
            }
        }
    }


    public static void main(String[] args) {

        int n = 5; //5个结点
        int e = 5; //5条边

        MyAdjGraphic g = new MyAdjGraphic(n);
        Object[] vertices = new Object[]{new Character('A'), new Character('B'), new Character('C'), new Character('D'), new Character('E')};
        Weight[] weights = new Weight[]{new Weight(0, 1, 10), new Weight(0, 4, 20), new Weight(2, 1, 40), new Weight(1, 3, 30), new Weight(3, 2, 50)};
        try {
            g.print();
            Weight.createAdjGraphic(g, vertices, n, weights, e);
            System.out.println("--------该临街矩阵如下---------");
            g.print();
            System.out.println("结点的个数：" + g.getNumOfVertice());
            System.out.println("边的个数：" + g.getNumOfEdges());
            g.deleteEdges(0, 4);
            System.out.println("--------删除之后---------");
            g.print();
            System.out.println("结点的个数：" + g.getNumOfVertice());
            System.out.println("边的个数：" + g.getNumOfEdges());
        } catch (Exception ex) {

        }
    }
}
/*--------该临街矩阵如下---------
10 -1 -1 20
-1 0 -1 30 -1
-1 40 0 -1 -1
-1 -1 50 0 -1
-1 -1 -1 -1 0
结点的个数：5
边的个数：5
--------删除之后---------
10 -1 -1 -1
-1 0 -1 30 -1
-1 40 0 -1 -1
-1 -1 50 0 -1
-1 -1 -1 -1 0
结点的个数：5
边的个数：4*/
