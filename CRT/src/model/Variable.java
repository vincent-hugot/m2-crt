package model;

import java.util.ArrayList;
import java.util.HashSet;

import ac.Value;

public class Variable {

	protected String				name;
	protected boolean				artificial;
	
	protected Domain 				domain;
	
	protected ArrayList<Constraint> 	associatedConstraints;
	protected ArrayList<Substitution> 	associatedSubstitutions;

	
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
		this.associatedSubstitutions = new ArrayList<Substitution>();
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
	
	public ArrayList<Substitution> getAssociatedSubstitutions() {
		return associatedSubstitutions;
	}
	
	public void addSubstitution(Substitution e){
		associatedSubstitutions.add(e);
	}
	
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Variable)) return false;
		Variable v = (Variable) obj;
		
		return (name.equals(v.name) && artificial == v.artificial
				&& domain.equals(v.domain));
		//return this == obj;
	}
	
	
	public String toString() {
		return name + " : " + domain;
	}
	
	public HashSet<Value> getValues()
	{
		HashSet<Value> res = new HashSet<Value>();
		
		for (Integer val : domain) {
			res.add(new Value(this, val));
		}
		
		return res;
	}
}
