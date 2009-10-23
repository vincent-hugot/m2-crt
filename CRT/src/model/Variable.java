package model;

import java.util.ArrayList;
import java.util.Collections;

public class Variable {

	protected String				name;
	protected boolean				artificial;
	
	/* Fomula: domain = baseDomain - excludedDomain */
	protected ArrayList<Integer>	baseDomain; /* Original domain */
	protected ArrayList<Integer>	domain; /* Domain restricted over time */
	protected ArrayList<Integer>	excludedDomain; /* Exclusion domain */
	
	
	protected ArrayList<Constraint> associatedConstraints;
	protected ArrayList<Substitution> associatedExpressions;
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
		baseDomain = new ArrayList<Integer>();
		if (min <= max) {
			for (int i = min; i <= max; i++)
				baseDomain.add(i);
		}

		construct(name, baseDomain, artificial);
	}

	public Variable(String name, int min, int max) {
		baseDomain = new ArrayList<Integer>();
		if (min <= max) {
			for (int i = min; i <= max; i++)
				baseDomain.add(i);
		}

		construct(name, baseDomain, false);
	}

	public void construct(String name, ArrayList<Integer> domain, boolean artificial) {
		this.name = name;
		this.artificial = artificial;
		
		this.baseDomain = domain;
		this.domain = new ArrayList<Integer>(domain);
		this.excludedDomain = new ArrayList<Integer>();
		
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
	
	
	
	public ArrayList<Integer> getBaseDomain() {
		return baseDomain;
	}
	public ArrayList<Integer> getDomain() {
		return domain;
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
		if (domain.contains(value) ||
				(baseDomain.contains(value) && !excludedDomain.contains(value))) {
			domain.remove(new Integer(value));
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
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Variable)) return false;
		Variable v = (Variable) obj;
		
		return (name.equals(v.name) && artificial == v.artificial
				&& baseDomain.equals(v.baseDomain) && domain.equals(v.domain)
				&& excludedDomain.equals(v.excludedDomain));
	}
	
	public boolean isValidValue(int val){
		return (domain.contains(new Integer(val)) ||
			(baseDomain.contains(new Integer(val)) && !excludedDomain.contains(new Integer(val))));
	}
	
	
	
	void resetDomain() {
		domain.clear();
		excludedDomain.clear();
	}
}
