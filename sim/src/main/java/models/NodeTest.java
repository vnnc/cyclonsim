package models;

public class NodeTest extends AbstractNode {

    int age = 0;
    public NodeTest(int label)
    {
        this.label = label;
    }

    public int getAge() {
        return age;
    }
}
