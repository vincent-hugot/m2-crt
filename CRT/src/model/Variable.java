package model;

import java.util.ArrayList;

public class Variable {

	protected String				name;
	protected boolean				artificial;
	
	protected Domain 				domain;
	
	protected ArrayList<Constraint> 	associatedConstraints;
	protected ArrayList<Substitution> 	associatedExpressions;
	protected ArrayList<Variable>		neighbors;

	
	/**
	 * @param name
	 */
	public Variable(String name, Domain domain2, boolean artificial) {
		construct(name, domain2, artificial);
	}

	public Variable(String name, Domain domain2) {
		construct(name, domain2, false);
	}

	public Variable(String name, int min, int max, boolean artificial) {
		construct(name, new Domain(min, max), artificial);
	}

	public Variable(String name, int min, int max) {
		construct(name, new Domain(min, max), false);
	}

	public void construct(String name, Domain domain2, boolean artificial) {
		this.name = name;
		this.artificial = artificial;
		
		this.domain = domain2;
		
		this.associatedConstraints = new ArrayList<Constraint>();
		this.associatedExpressions = new ArrayList<Substitution>();
		this.neighbors = new ArrayList<Variable>();
	}

	public String getName() {
		return name;
	}
	
	public boolean isArtificial() {
		return artificial;
	}
	
	
	public Domain getDomain() {
		return domain;
	}
	
	
	public ArrayList<Constraint> getAssociatedConstraints() {
		return associatedConstraints;
	}
	
	public void addConstraint(Constraint c){
		associatedConstraints.add(c);
	}
	
	public ArrayList<Substitution> getAssociatedExpressions() {
		return associatedExpressions;
	}
	
	public void addExpression(Substitution e){
		associatedExpressions.add(e);
	}
	
	public ArrayList<Variable> getNeighbors() {
		return neighbors;
	}
	public void addNeighbor(Variable v){
		neighbors.add(v);
	}
	
	/*
	public boolean equals(Object obj) {
		if (!(obj instanceof Variable)) return false;
		Variable v = (Variable) obj;
		
		return (name.equals(v.name) && artificial == v.artificial
				&& baseDomain.equals(v.baseDomain) && domain.equals(v.domain)
				&& excludedDomain.equals(v.excludedDomain));
	}
	*/
	
}
