package models;

public abstract class AbstractNode {

    int label;

    public AbstractNode() { }

    public AbstractNode(int label) {
        this.label = label;
    }

    public int getLabel() {
        return this.label;
    }

    @Override
    public String toString() {
        return ""+label;
    }
}
