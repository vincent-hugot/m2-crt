package model;

import java.util.ArrayList;

public class Operator {

	private Global.opType	      type;
	private ArrayList<Constraint>	associatedConstraints;

	public Operator(Global.opType type) {

		this.type = type;
	}

	public Global.opType getType() {

		return type;
	}

	public ArrayList<Constraint> getAssociatedConstraints() {

		return associatedConstraints;
	}

	public void addConstraint(Constraint constraint) {

		associatedConstraints.add(constraint);
	}
}
