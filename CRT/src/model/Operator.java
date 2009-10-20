package model;

import java.util.ArrayList;
import model.Global.OpType;

public class Operator {

	private Global.OpType	      type;
	private ArrayList<Constraint>	associatedConstraints;

	public Operator(OpType type) {

		this.type = type;
	}

	public OpType getType() {

		return type;
	}

	public ArrayList<Constraint> getAssociatedConstraints() {

		return associatedConstraints;
	}

	public void addConstraint(Constraint constraint) {

		associatedConstraints.add(constraint);
	}
	
	public boolean equals(Object obj){
		return (obj instanceof Operator && this.type == ((Operator) obj).type);
	}
}
