package site.laoc.btree;

import java.util.Collections;
import java.util.List;

public class BTree {
    private final int m; // B树的阶
    private final int min; // 元素最小值
    private Node root; // 根节点

    public BTree(int m){
        this.m = m;
        this.min = (int)Math.ceil(m / 2) - 1;
    }

    public Node getRoot(){
        return root;
    }

    // 搜索
    public Entry searchEntry(int key){
        return searchEntry(root,key);
    }

    private Entry searchEntry(Node node,int key) {
        if (node == null) {
            return null;
        }

        int index = Collections.binarySearch(node.getEntrys(), new Entry(key, null));

        if (index >= 0) {
            return node.getEntrys().get(index);
        } else {
            if (node.getChildNodes().size() == 0) {
                return null;
            }

            return searchEntry(node.getChildNodes().get(-index - 1), key);
        }
    }

    public Node searchNode(int key){
        return new Node();
    }

    private Node searchNode(Node node,int key){
        if(node == null){
            return null;
        }

        // 使用二分查找定位小标
        int index = Collections.binarySearch(node.getEntrys(),new Entry(key,null));
        if(index >= 0) {
            return node;
        }else{
            if(node.getChildNodes().size() == 0){
                return null;
            }

            return searchNode(node.getChildNodes().get(-index-1),key);
        }
    }

    // 添加元素
    public void add(Entry e){
        if(root == null){
            Node node = new Node();
            node.add(e);
            root = node;
            return;
        }

        add(root,e);
    }

    private void add(Node node,Entry entry){
        // 如果当前为叶子节点
        if(node.getChildNodes().size() == 0){

            // 如果当前节点的元素未满，字何解添加元素
            if(node.getEntrys().size() < m - 1){
                node.getEntrys().add(entry);
                return;
            }

            // 如果当前节点的元素已满，则分裂当前节点
            node.getEntrys().add(entry);
            split(node);
            return;
        }

        int index = Collections.binarySearch(node.getEntrys(),entry);
        if(index < 0){
            add(node.getChildNodes().get(-index - 1),entry);
        }
    }

    /**
     * 分离当前节点
     * @param node
     */
    private void split(Node node){
        int mid = node.getEntrys().size() / 2;

        // 分隔值
        Entry seperateEntry = node.getEntrys().get(mid);
        // 分离后的左节点
        Node leftNode = new Node();
        leftNode.getEntrys().addAll(node.getEntrys().subList(0,mid));
        // 分离后的右节点
        Node rightNode = new Node();
        rightNode.getEntrys().addAll(node.getEntrys().subList(mid + 1,node.getEntrys().size()));

        // 分离子节点
        if(node.getChildNodes().size() > 0){
            List<Node> leftChildNode = node.getChildNodes().subList(0,mid + 1);
            for(Node tmp : leftChildNode){
                leftNode.setParentNode(leftNode);
            }
            leftNode.getChildNodes().addAll(leftChildNode);

            List<Node> rightChildNode = node.getChildNodes().subList(mid + 1, node.getEntrys().size() + 1);
            for(Node tmp : rightChildNode){
                tmp.setParentNode(rightNode);
            }
            rightNode.getChildNodes().addAll(rightChildNode);
        }

        // 当前节点为根节点
        if(node.getParentNode() == null){
            Node newNode = new Node();
            newNode.add(seperateEntry);
            root = newNode;
            leftNode.setParentNode(newNode);
            rightNode.setParentNode(newNode);

            root.addChild(leftNode).addChild(rightNode);
        }else{
            node.getParentNode().add(seperateEntry);
            leftNode.setParentNode(node.getParentNode());
            rightNode.setParentNode(node.getParentNode());
            node.getParentNode().addChild(leftNode).addChild(rightNode);
            node.getParentNode().getChildNodes().remove(node);
            // 若其父节点溢出，继续分裂
            if (node.getParentNode().getEntrys().size() > m - 1) {
                split(node.getParentNode());
            }
        }
    }
}
