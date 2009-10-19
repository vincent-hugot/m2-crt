package model;

import java.util.ArrayList;

public class Model {

	private ArrayList<Variable>	  variables;
	private ArrayList<Constraint>	constraints;

	public Model() {

		variables = new ArrayList<Variable>();
		constraints = new ArrayList<Constraint>();
	}

	public Variable newVariable(String name, int minBoundary, int maxBoundary, boolean artificial) {
		// TODO fill in
		
		return null;
	}

	public Variable newVariable(String name, int minBoundary, int maxBoundary) {
		return newVariable(name, minBoundary, maxBoundary, false);
	}

	public Constant newConstant(int value) {
		// TODO fill in
		
		// NOTE : Constant has no domain but its own value
		// Change Constant so that the constructor determines the domain by itself
		
		return null;
	}

	public void newExpression(Variable operand1, Variable operand2, Operator operator, Variable equal) {
		// TODO fill in
	}

	public void newConstraint(Variable operand1, Variable operand2, Operator operator) {
		// TODO fill in
	}

	public ArrayList<Variable> getVariables() {
		return variables;
	}

	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}

	/**
	 * Returns D: list of domains
	 * 
	 * @return
	 */
	/*
	 * public HashMap<Variable, ArrayList<Integer>> getD() { return null; }
	 */
}
