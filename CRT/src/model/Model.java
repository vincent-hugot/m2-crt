package model;

import java.util.ArrayList;

public class Model {

	private ArrayList<Variable>		variables;
	private ArrayList<Constraint>	constraints;
	private ArrayList<Expression>	expressions;

	public Model() {
		variables = new ArrayList<Variable>();
		constraints = new ArrayList<Constraint>();
		expressions = new ArrayList<Expression>();
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

	public void newExpression(Variable operand1, Variable operand2, Operator.Arithmetic operator, Variable equal) {
		Expression e = new Expression(operand1, operator, operand2, equal);
		operand1.addExpression(e);
		operand2.addExpression(e);
		equal.addConstraint(e);
		expressions.add(e);
	}

	public void newConstraint(Variable operand1, Variable operand2, Operator.Constraint operator) {
		Constraint c = new Constraint(operand1, operator, operand2);
		operand1.addConstraint(c);
		operand2.addConstraint(c);
		
		operand1.addNeighbor(operand2);
		operand2.addNeighbor(operand1);
		constraints.add(c);
	}

	public ArrayList<Variable> getVariables() {
		return variables;
	}

	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}

	public ArrayList<Expression> getExpressions() {
		return expressions;
	}

	public boolean equals(Object obj) {
		return (obj instanceof Model && this.variables.equals(((Model) obj).variables) && this.constraints
				.equals(((Model) obj).constraints));
	}

	public ArrayList<Constraint> getConstraintConcerningVariables(Variable v1, Variable v2) {
		ArrayList<Constraint> res, al1, al2;
		java.util.ListIterator<Constraint> it;
		Constraint cons;
		res = new ArrayList<Constraint>();
		al1 = v1.getAssociatedConstraints();
		al2 = v2.getAssociatedConstraints();

		it = al1.listIterator();

		while (it.hasNext()) {
			cons = it.next();
			if (cons instanceof Constraint && al2.contains(cons)) {
				res.add(cons);
			}
		}
		return res;
	}

}
