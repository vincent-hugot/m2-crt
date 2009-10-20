package model;

import java.util.ArrayList;
import java.util.Collections;

public class Variable {

	private String				name;
	private boolean				artificial;
	private ArrayList<Integer>	domain;
	private ArrayList<Integer>	excludedDomain;

	// TODO : Add constraints list

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

	public boolean equals(Object o) {
		boolean res;

		res = (o instanceof Variable && this.name.equals(((Variable) o).name)
				&& this.artificial == ((Variable) o).artificial && this.domain.equals(((Variable) o).domain) && this.excludedDomain
				.equals(((Variable) o).excludedDomain));
		return res;
	}

	// TODO : add exclude()-like method
	// TODO : add constraints accesses (add one, get list)
}
