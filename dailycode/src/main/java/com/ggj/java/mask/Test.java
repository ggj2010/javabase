package com.ggj.java.mask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author gaoguangjin
 */
public class Test {
    public static void main(String[] args) {
        add();
        update();

    }

    private static void add() {
        String addJson = "[\n" +
                "    {\n" +
                "        \"parentId\":-1,\n" +
                "        \"chapterName\":\"父章节名\",\n" +
                "        \"chapterId\":0,\n" +
                "        \"sort\":0,\n" +
                "        \"children\":[\n" +
                "            {\n" +
                "                \"parentId\":0,\n" +
                "                \"chapterName\":\"章节名1\",\n" +
                "                \"chapterId\":0,\n" +
                "                \"sort\":0\n" +
                "            },\n" +
                "            {\n" +
                "                \"parentId\":0,\n" +
                "                \"chapterName\":\"章节名2\",\n" +
                "                \"chapterId\":0,\n" +
                "                \"sort\":1\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";
        List<Node> addNodeList = JSONObject.parseArray(addJson, Node.class);
        //铺平 去除子节点
        List<Node> addNodeAndremoveChildList = new ArrayList<>();
        for (Node node : addNodeList) {
            flatNew(node, null, addNodeAndremoveChildList, null);
        }
        System.out.println(JSON.toJSON(addNodeAndremoveChildList));
    }


    private static void update() {
        String oldJson = "[\n" +
                "    {\n" +
                "        \"parentId\":-1,\n" +
                "        \"chapterName\":\"父章节名\",\n" +
                "        \"chapterId\":1,\n" +
                "        \"sort\":0,\n" +
                "        \"children\":[\n" +
                "            {\n" +
                "                \"parentId\":1,\n" +
                "                \"chapterName\":\"章节名1\",\n" +
                "                \"chapterId\":11,\n" +
                "                \"sort\":0\n" +
                "            },\n" +
                "            {\n" +
                "                \"parentId\":1,\n" +
                "                \"chapterName\":\"章节名2\",\n" +
                "                \"chapterId\":12,\n" +
                "                \"sort\":1\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";


        String updateJson = "[\n" +
                "    {\n" +
                "        \"parentId\":-1,\n" +
                "        \"chapterName\":\"父章节名\",\n" +
                "        \"chapterId\":1,\n" +
                "        \"sort\":0,\n" +
                "        \"children\":[\n" +
                "            {\n" +
                "                \"parentId\":1,\n" +
                "                \"chapterName\":\"章节名0\",\n" +
                "                \"chapterId\":0,\n" +
                "                \"sort\":0,\n" +
                "                \"children\":[\n" +
                "                    {\n" +
                "                        \"parentId\":0,\n" +
                "                        \"chapterName\":\"子章节名00\",\n" +
                "                        \"chapterId\":0,\n" +
                "                        \"sort\":0\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"parentId\":0,\n" +
                "                        \"chapterName\":\"子章节名01\",\n" +
                "                        \"chapterId\":0,\n" +
                "                        \"sort\":1\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"parentId\":1,\n" +
                "                \"chapterName\":\"章节名2\",\n" +
                "                \"chapterId\":12,\n" +
                "                \"sort\":1\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";

        List<Node> oldNodeList = JSONObject.parseArray(oldJson, Node.class);
        List<Node> newNodeList = JSONObject.parseArray(updateJson, Node.class);

        Map<Integer, Node> oldMap = new HashMap<>();
        Map<Integer, Node> updateMap = new HashMap<>();
        /**
         * 更新list
         */
        List<Node> updateNodeList = new ArrayList<>();

        for (Node node : oldNodeList) {
            flatOld(node, oldMap);
        }

        for (Node node : newNodeList) {
            flatNew(node, updateMap, updateNodeList, null);
        }

        for (Map.Entry<Integer, Node> oldNodeEntry : oldMap.entrySet()) {
            Integer key = oldNodeEntry.getKey();
            Node oldNode = oldNodeEntry.getValue();
            //不存在,删除
            if (!updateMap.containsKey(key)) {
                oldNode.setOperate(3);
                updateNodeList.add(oldNode);
            } else {
                Node updateNode = updateMap.get(key);
                if (!updateNode.equals(oldNode)) {
                    updateNodeList.add(updateNode);
                }
            }
        }

        System.out.println(JSONObject.toJSONString(updateNodeList));
    }

    private static void flatNew(Node node, Map<Integer, Node> updateMap, List<Node> updateNodeList, String virtureParentId) {
        //当前ID
        Integer currentId = node.getChapterId();
        Integer currentParentId = node.getParentId();
        if (currentParentId == 0) {
            if (virtureParentId == null) {
                virtureParentId = "xkt" + UUID.randomUUID().toString();
            }
            node.setVirtureParentId(virtureParentId);
        }
        if (currentId == 0) {
            node.setVirtureId("xkt" + UUID.randomUUID().toString());
        }
        //当前章节为新增
        if (StringUtils.isNotEmpty(node.getVirtureId())) {
            node.setOperate(1);
            updateNodeList.add(node);
        } else {
            //修改
            node.setOperate(2);
            updateMap.put(node.getChapterId(), node);
        }
        List<Node> nodeChildren = node.getChildren();
        node.setChildren(null);
        if (CollectionUtils.isNotEmpty(nodeChildren)) {
            node.setMetaleaf(true);
            for (Node childNode : nodeChildren) {
                flatNew(childNode, updateMap, updateNodeList, node.getVirtureId());
            }
        }
    }


    private static void flatOld(Node node, Map<Integer, Node> oldMap) {
        oldMap.put(node.getChapterId(), node);
        List<Node> nodeChildren = node.getChildren();
        node.setChildren(null);
        if (CollectionUtils.isNotEmpty(nodeChildren)) {
            node.setMetaleaf(true);
            for (Node childNode : nodeChildren) {
                flatOld(childNode, oldMap);
            }
        }
    }

    @Data
    public static class Node {
        private int parentId;
        private String virtureParentId;
        private String virtureId;
        private int operate;
        private String chapterName;
        private int chapterId;
        private boolean metaleaf;
        private int sort;
        private List<Node> children;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return parentId == node.parentId &&
                    chapterId == node.chapterId &&
                    metaleaf == node.metaleaf &&
                    sort == node.sort &&
                    Objects.equals(chapterName, node.chapterName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(parentId, chapterName, chapterId, sort, metaleaf);
        }
    }
}
