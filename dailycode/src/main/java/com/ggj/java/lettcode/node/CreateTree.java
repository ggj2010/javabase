package com.ggj.java.lettcode.node;

/**
 * 根据前序遍历创建树
 * @author gaoguangjin
 */
public class CreateTree {
    private static int count=0;
    private static char[] array;
    public static void main(String[] args) {
        String sortNode="AB#D##C##";
         array = sortNode.toCharArray();

        Node root = createNode();
        System.out.println("args = [" + root + "]");
    }

    /**
     *
     * @return
     */
    private static Node createNode() {
        Node root=null;
        //如果是#代表根结点为空
        if(count>=array.length||array[count]=='#' ){
            root=null;
            count++;
        }else{
            //根、左、右 count-1 是因为上面的+1
            root=new Node(array[count]);
            count++;
            root.left=createNode();
            root.right=createNode();
        }
        return root;
    }


}
