package model;

import java.util.ArrayList;

public class Model {

	private ArrayList<Variable>		variables;
	private ArrayList<Constraint>	constraints;
	private ArrayList<Substitution>	substitutions;

	public Model() {
		variables = new ArrayList<Variable>();
		constraints = new ArrayList<Constraint>();
		substitutions = new ArrayList<Substitution>();
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

	public void newSubstitution(Variable operand1, Variable operand2, Operator.Arithmetic operator, Variable equal) {
		Substitution e = new Substitution(operand1, operator, operand2, equal);
		operand1.addSubstitution(e);
		operand2.addSubstitution(e);
		equal.addConstraint(e);
		substitutions.add(e);
	}

	public void newConstraint(Variable operand1, Variable operand2, Operator.Constraint operator) {
		Constraint c = new Constraint(operand1, operator, operand2);
		operand1.addConstraint(c);
		operand2.addConstraint(c);
		
		constraints.add(c);
	}

	public ArrayList<Variable> getVariables() {
		return variables;
	}

	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}

	public ArrayList<Substitution> getSubstitutions() {
		return substitutions;
	}

	public boolean equals(Object obj) {
		return (obj instanceof Model && this.variables.equals(((Model) obj).variables) && this.constraints
				.equals(((Model) obj).constraints) && this.substitutions.equals(((Model) obj).substitutions));
	}

	public ArrayList<Constraint> getConstraintConcerningVariables(Variable v1, Variable v2) {
		ArrayList<Constraint> res, al1, al2;
		res = new ArrayList<Constraint>();
		al1 = v1.getAssociatedConstraints();
		al2 = v2.getAssociatedConstraints();
		
		for (Constraint constraint : al1) {
			if (al2.contains(constraint))
				res.add(constraint);
		}
		return res;
	}

	public Variable newVariable(String name, Domain newDomain, boolean artificial) {
		Variable v = new Variable(name, newDomain, artificial);
		variables.add(v);

		return v;
	}

}
