package model;

import java.util.ArrayList;
import java.util.Collections;

public class Variable {

	protected String				name;
	protected boolean				artificial;
	protected ArrayList<Integer>	domain;
	protected ArrayList<Integer>	excludedDomain;
	protected ArrayList<Constraint> associatedConstraints;
	protected ArrayList<Expression> associatedExpressions;
	protected ArrayList<Variable>	neighbors;

	
	/**
	 * @param name
	 */
	public Variable(String name, ArrayList<Integer> domain, boolean artificial) {
		construct(name, domain, artificial);
	}

	public Variable(String name, ArrayList<Integer> domain) {
		construct(name, domain, false);
	}

	public Variable(String name, int min, int max, boolean artificial) {
		domain = new ArrayList<Integer>();
		if (min <= max) {
			for (int i = min; i <= max; i++)
				domain.add(i);
		}

		construct(name, domain, artificial);
	}

	public Variable(String name, int min, int max) {
		domain = new ArrayList<Integer>();
		if (min <= max) {
			for (int i = min; i <= max; i++)
				domain.add(i);
		}

		construct(name, domain, false);
	}

	public void construct(String name, ArrayList<Integer> domain, boolean artificial) {
		this.name = name;
		this.domain = domain;
		this.artificial = artificial;
		this.excludedDomain = new ArrayList<Integer>();
		this.associatedConstraints = new ArrayList<Constraint>();
		this.associatedExpressions = new ArrayList<Expression>();
		this.neighbors = new ArrayList<Variable>();
	}

	public String getName() {

		return name;
	}

	public ArrayList<Integer> getDomain() {

		return domain;
	}

	public boolean isArtificial() {

		return artificial;
	}

	public ArrayList<Integer> getExcludedDomain() {

		return excludedDomain;
	}

	public int minBound() {
		if (!domain.isEmpty())
			return domain.get(0);
		return 0;
	}

	public int maxBound() {
		if (!domain.isEmpty())
			return domain.get(domain.size() - 1);
		return 0;
	}

	public void exclude(int value) {
		if (domain.contains(value) && !excludedDomain.contains(value)) {
			excludedDomain.add(value);
			Collections.sort(excludedDomain);
		}
	}

	public ArrayList<Constraint> getAssociatedConstraints() {
		return associatedConstraints;
	}
	
	public void addConstraint(Constraint c){
		associatedConstraints.add(c);
	}
	
	public ArrayList<Expression> getAssociatedExpressions() {
		return associatedExpressions;
	}

	public void addExpression(Expression e){
		associatedExpressions.add(e);
	}
	
	public ArrayList<Variable> getNeighbors() {
		return neighbors;
	}
	public void addNeighbor(Variable v){
		neighbors.add(v);
	}
	
	public boolean equals(Object obj) {
		boolean res;

		res = (obj instanceof Variable && this.name.equals(((Variable) obj).name)
				&& this.artificial == ((Variable) obj).artificial && this.domain.equals(((Variable) obj).domain) && this.excludedDomain
				.equals(((Variable) obj).excludedDomain));
		return res;
	}
	
	public boolean isValidValue(int val){
		return (domain.contains(new Integer(val)) && !excludedDomain.contains(new Integer(val)));
	}
	
	
	
	/* Value getters for algorithm */
	public ArrayList<Integer> getRemainingDomain() {
		ArrayList<Integer> remaining = new ArrayList<Integer>();
		
		for (int i = 0; i < remaining.size(); i++) {
			if (excludedDomain.contains(i))
				remaining.add(domain.get(i));
		}
		
		return remaining;
	}
	
	void resetExcludedDomain(){
		excludedDomain.clear();
	}
}
