package model;

import java.util.ArrayList;

public class Model {

	private ArrayList<Variable>	  variables;
	private ArrayList<Constraint>	constraints;

	public Model() {

		variables = new ArrayList<Variable>();
		constraints = new ArrayList<Constraint>();
	}

	public void newVariable(String nom, boolean artificial, int minBoundary, int maxBoundary) {
		// TODO fill in
	}

	public void newVariable(String nom, int minBoundary, int maxBoundary) {
		newVariable(nom, false, minBoundary, maxBoundary);
	}

	public void newConstant(int value) {
		// TODO fill in
	}

	public void newExpression(Variable operand1, Variable operand2, Operator operator) {
		// TODO fill in
	}

	public void newConstraint(Variable operand1, Variable operand2, Operator operator, Variable equal) {
		// TODO fill in
	}

	public void newOperator(Global.opType type) {
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
