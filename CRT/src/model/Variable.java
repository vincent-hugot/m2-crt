package model;

import java.util.ArrayList;

public class Variable {

	private String	           name;
	private boolean	           artificial;
	private ArrayList<Integer>	domain;
	private ArrayList<Integer>	excludedDomain;

	// TODO : Add constraints list

	/**
	 * @param name
	 */
	public Variable(String name, ArrayList<Integer> domain, boolean artificial) {

		super();
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

	// TODO : add exclude()-like method
	// TODO : add constraints accesses (add one, get list)
}
