package models;

public class SimpleNode extends AbstractNode{
	public SimpleNode(int label){
		super(label);
	}

	public SimpleNode(Object label) {
		super((Integer) label);
	}
}
