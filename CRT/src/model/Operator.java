package model;


import model.Global.OpType;

public class Operator {

	private Global.OpType	      type;
	
	public Operator(OpType type) {

		this.type = type;
	}

	public OpType getType() {

		return type;
	}

	public boolean equals(Object obj){
		return (obj instanceof Operator && this.type == ((Operator) obj).type);
	}
	
	public boolean isArithmetical(){
		return (type == OpType.ADD || type == OpType.MUL || type == OpType.SUB || type == OpType.DIV);
	}
}
