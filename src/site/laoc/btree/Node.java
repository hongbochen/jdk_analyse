package site.laoc.btree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node implements Comparable<Node>{

    private final List<Entry> entrys;
    private final List<Node> childNodes;
    private Node parentNode;

    public Node(){
        entrys = new ArrayList<>();
        childNodes = new ArrayList<>();
    }

    public Node add(Entry entry){
        entrys.add(entry);
        Collections.sort(entrys);

        return this;
    }

    public Node addChild(Node node){
        childNodes.add(node);
        Collections.sort(childNodes);

        return this;
    }

    public List<Entry> getEntrys() {
        return entrys;
    }

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(entrys.get(0).getKey(),o.getEntrys().get(0).getKey());
    }
}
