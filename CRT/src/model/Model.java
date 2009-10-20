package model;

import java.util.ArrayList;

import model.Global.OpType;

public class Model {

	private ArrayList<Variable>	  variables;
	private ArrayList<Constraint>	constraints;

	public Model() {

		variables = new ArrayList<Variable>();
		constraints = new ArrayList<Constraint>();
	}

	public Variable newVariable(String name, int minBoundary, int maxBoundary, boolean artificial) {
		Variable v = new Variable(name, minBoundary, maxBoundary, artificial);
		variables.add(v);
		
		return v;
	}

	public Variable newVariable(String name, int minBoundary, int maxBoundary) {
		return newVariable(name, minBoundary, maxBoundary, false);
	}

	public Constant newConstant(int value) {
		Constant c = new Constant(value);		
		variables.add(c);
		return c;
	}

	public void newExpression(Variable operand1, Variable operand2, Global.OpType operator, Variable equal) {
		Expression e = new Expression(operand1, operator, operand2, equal);
		constraints.add(e);
	}

	public void newConstraint(Variable operand1, Variable operand2, OpType operator) {
		Constraint c = new Constraint(operand1, operator, operand2);
		constraints.add(c);
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
