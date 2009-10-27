package ac;

import model.Variable;

public class Couple {
	private Variable xi;
	private Variable xj;
	
	
	public Couple() {
		this.xi = null;
		this.xj = null;
	}
	
	public Couple(Variable xi, Variable xj) {
		this.xi = xi;
		this.xj = xj;
	}
	
	
	public Variable getXi() {
		return xi;
	}
	public void setXi(Variable xi) {
		this.xi = xi;
	}
	public Variable getXj() {
		return xj;
	}
	public void setXj(Variable xj) {
		this.xj = xj;
	}
	
	public boolean equals(Couple c) {
		return (this.xi == c.xi) && (this.xj == c.xj);
	}
	public boolean equals(Object c) {
		return (c instanceof Couple)
			&& (this.xi == ((Couple)c).xi)
			&& (this.xj == ((Couple)c).xj);
	}
	
	public String toString() {
		return "(" + xi.getName() + "," + xj.getName() + ")";
	}
}
