package model;

import java.util.ArrayList;
import main.Tools;

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

	public Substitution newSubstitution(Variable operand1, Variable operand2, Operator.Arithmetic operator, Variable equal) {
		Substitution e = new Substitution(operand1, operator, operand2, equal);
		operand1.addSubstitution(e);
		if (operand1 != operand2) operand2.addSubstitution(e);
		
		equal.addSubstitution(e);
		substitutions.add(e);
		return e;
	}

	public Constraint newConstraint(Variable operand1, Variable operand2, Operator.Constraint operator) {
		Constraint c = new Constraint(operand1, operator, operand2);
		operand1.addConstraint(c);
		if (operand1 != operand2) operand2.addConstraint(c);
		
		constraints.add(c);
		return c;
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

	
	/**
	 * Calculate and return the list of constraints between v1 and v2.
	 * (List of Cij between Xi and Xj)
	 * @param xi
	 * @param xj
	 * @return
	 */
	public ArrayList<Constraint> getConstraintConcerningVariables(Variable xi, Variable xj) {
		ArrayList<Constraint> res = new ArrayList<Constraint>();
		
		// Getting every Xi constraint (Cik)
		res.addAll(xi.getAssociatedConstraints());
		
		// Retaining only those into Xj (Cij)
		res.retainAll(xj.getAssociatedConstraints());
		
		return res;
	}

	public Variable newVariable(String name, Domain newDomain, boolean artificial) {
		Variable v = new Variable(name, newDomain, artificial);
		variables.add(v);

		return v;
	}
	
	
	public String toString() {
		String str = "";
		
		str += "DOMAINS:\n  ";
		str += Tools.implode(variables, "\n  ");
		str += "\nCONSTRAINTS:\n  ";
		str += Tools.implode(constraints, "\n  ");
		str += "\nSUBSTITUTIONS:\n  ";
		str += Tools.implode(substitutions, "\n  ");
		
		return str;
	}
}
