package analz;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: tna2
 * Date: 4/9/14
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class SyntaxNode {
    private String value;
    private LinkedList<SyntaxNode> children;
    private SyntaxNode parent;

    public SyntaxNode() {
        this.children = new LinkedList<SyntaxNode>();
        this.parent = null;
    }

    public SyntaxNode(String value) {
        this();
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public LinkedList<SyntaxNode> getChildren() {
        return this.children;
    }

    public SyntaxNode getParent() {
        return this.parent;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setParent(SyntaxNode parent) {
        this.parent = parent;
    }

    public void addChild(SyntaxNode child) {
        this.children.add(child);
    }

    public LinkedList<SyntaxNode> getChilren() {
        return this.children;
    }

    public boolean isLeaf() {
        return (this.children.isEmpty());
    }

    public boolean isRoot() {
        return (this.parent == null);
    }
}
