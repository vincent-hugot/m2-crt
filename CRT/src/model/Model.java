package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import ac.ValuedVariable;

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

	public Substitution newSubstitution(Variable operand1, Variable operand2, Operator.Arithmetic operator,
			Variable equal) {
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
		return (obj instanceof Model && this.variables.equals(((Model) obj).variables)
				&& this.constraints.equals(((Model) obj).constraints) && this.substitutions
				.equals(((Model) obj).substitutions));
	}

	/**
	 * Calculate and return the list of constraints between v1 and v2. (List of Cij between Xi and Xj)
	 * 
	 * @param xi
	 * @param xj
	 * @return
	 */
	public ArrayList<Constraint> getConstraintConcerningVariables(Variable xi, Variable xj) {
		ArrayList<Constraint> res = new ArrayList<Constraint>();
				/*
				// Getting every Xi constraint (Cik)
				res.addAll(xi.getAssociatedConstraints());

				// Retaining only those into Xj (Cij)
				res.retainAll(xj.getAssociatedConstraints());
				*/
				
				for (Constraint constraint : constraints) {
					if (constraint.isCij(xi, xj)) res.add(constraint);
				}

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

	/** LATEX output: 
	 * Returns a nice PS-Tricks LaTeX graph 
	 * representing the model.
	 * 
	 * Variables are represented on a virtual circle, 
	 * whose radius is computed in such a way that variables
	 * are separated by at least a fixed distance.
	 * 
	 * Same thing with domains, circling the variables.
	 * 
	 * The different kinds of constraints are represented by 
	 * different styles of lines.
	 * 
	 * It is still a work in progress, contact me if you 
	 * have any question and please don't touch this method
	 * until I'm done with it :)
	 *  
	 * @author Vincent HUGOT
	 * 
	 * @param distanceVars minimum distance between two variables.
	 * 	The radius of the circle of variables is computed from this data, and
	 *  also depends on the number of variables in the model, of course.
	 *  This is used rather than giving the radius directly because it is a 
	 *  much better indicator of visual quality.
	 * @param distanceDoms minimum distance between two domains
	 * @param warpVars rotation of the circle of variables, in "slices" 
	 * @param warpDoms rotation of the circle of domains, in "slices" 
	 * @return a LaTeX PSTricks figure
	 */
	public String toLaTeX(double distanceVars, double distanceDoms, double warpVars, double warpDoms)
	{
		StringBuilder sb = new StringBuilder();
		
		/* Some trigonometry... */
		int counter = 0;
		double alpha = 2 * Math.PI / variables.size();
		double radiusVars = Math.max(distanceVars, distanceVars / Math.sin(alpha));
		double radiusDoms = Math.max(distanceDoms, distanceDoms / Math.sin(alpha));
		double totalRadius = Math.max(radiusVars, radiusDoms) + 0.6;
		double betaV = warpVars * alpha;
		double betaD = warpDoms * alpha;
		
		/* preamble */
		sb.append("\\pspicture(-"+totalRadius+",-"+totalRadius+")("
				+totalRadius+","+totalRadius+")\n");
		
		sb.append("%%CRT to LaTeX: " + "; " + distanceVars + "; " +  distanceDoms + "; " 
				+  warpVars + "; " +  warpDoms + "\n");
		
		for (Variable var : variables) {
			
			String displayedName = var.getName();
			if (var.isArtificial())
			{
				for (Substitution sub : substitutions) {
					if (sub.substitutionVariable == var) {
						switch (sub.substitutionOperator) {
						case ADD:
							displayedName += "$+$";
							break;
						case DIV:
							displayedName += "$\\div$";
							break;
						case MUL:
							displayedName += "$\\times$";
							break;
						case SUB:
							displayedName += "$-$";
							break;
						}
						break;
					}
				}
			}
			
			double varX = radiusVars * Math.cos(counter * alpha + betaV);
			double varY = radiusVars * Math.sin(counter * alpha + betaV);
			sb.append("%%VAR: " + var + "\n");
			sb.append("\\rput("+varX+","+varY+"){\\rnode{" + var.getName() + 
					"}{\\psshadowbox{\\bf " + displayedName + "}}}\n");
			
			double domX = radiusDoms * Math.cos(counter * alpha + betaD);
			double domY = radiusDoms * Math.sin(counter * alpha + betaD);
			sb.append("\\rput("+domX+","+domY+"){\\rnode{@@DOM" + var.getName() + 
					"}{\\psframebox[framearc=.4]{\\scriptsize " + var.getDomain().toLaTeX() + "}}}\n");
			
			sb.append("\\ncline[linestyle=solid]{*-*}{"+
					var.getName() +
					"}{@@DOM"+ var.getName() + "}\n");
			counter++;
		}
		
		sb.append("\\psset{arrowscale=2}\n");
		
		for (Constraint con : constraints) {
			sb.append("%%CON: " + con + "\n");
			String linestyle = "";
			String type = "-";
			switch (con.op) {
			case  EQUAL:
				linestyle = "doubleline=true,linestyle=dashed";
				type = "-";
				break;
			case GREATER_OR_EQUAL:
				linestyle = "doubleline=true,linestyle=solid,arrowscale=1";
				type = "->";
				break;
			case GREATER:
				linestyle = "doubleline=false,linestyle=solid";
				type = "->";
				break;
			case LOWER_OR_EQUAL:
				linestyle = "doubleline=true,linestyle=solid,arrowscale=1";
				type = "<-";
				break;
			case LOWER:
				linestyle = "doubleline=false,linestyle=solid";
				type = "<-";
				break;
			case NOT_EQUAL:
				linestyle = "doubleline=true,linestyle=dashed,arrowscale=0.8";
				type = "|-|";
			default:
				break;
			}
			sb.append("\\ncarc[" + linestyle + "]{" + type + "}{" + con.left.getName() + "}{" +
					con.right.getName() + "}\n");
		}
		
		for (Substitution sub : substitutions) {
			sb.append("%%SUB: " + sub + "\n");
			sb.append("\\ncline[linestyle=dotted,arrowscale=1.35]{*-}{"+
					sub.substitutionVariable.getName() +
					"}{"+ sub.left.getName() + "}\n");
			sb.append("\\ncline[linestyle=dotted,arrowscale=1.35]{o-}{"+
					sub.substitutionVariable.getName() +
					"}{"+ sub.right.getName() + "}\n");
		}
		
		/* postamble */
		sb.append("\\endpspicture");
		
		return sb.toString();
	}

	public HashSet<ValuedVariable> getValues() {
		HashSet<ValuedVariable> res = new HashSet<ValuedVariable>();
		
		for (Variable var : this.variables) {
			res.addAll(var.getValues());
		}
		return res;
	}
	
	public Variable backup(Variable v){
		
	    Variable vc = new Variable(v.getName(),(Domain) v.getDomain().clone(),v.isArtificial());  
		vc.associatedConstraints=v.getAssociatedConstraints(); 
		vc.associatedSubstitutions=v.getAssociatedSubstitutions();
	

		return vc;
	}
	
	/**
	 * @param v de type Variable, on remplace la variable dans l'ArrayList variables par v si elles ont le meme nom de variable
	 * en fait il n'y a que la reference sur la variable qui change
	 * et la reference sur le domaine de la variable
	 */
	public void restore(Variable v){
		Iterator<Variable> iVar = variables.iterator();
		Variable variable = null;//variable temporaire qui sert pour iterer
		boolean variableTrouvee = false;//a t-on trouve la variable qui a le meme nom que v dans l'ArrayList variables
		int i=0;//indice dans array list
		
		while (iVar.hasNext() && !variableTrouvee){
			variable = ((Variable)iVar.next());
			if (variable.getName().equals(v.getName())){
				variableTrouvee = true;
			}
			i++;
		}
		
		if (variableTrouvee){
			//variables.set(i-1, v);//i-1
			//non car il faut 1 point de restauration a usage multiple
			
			//variables.set(i-1, backup(v));
			//non car sinon si on fait un model.getConstraintsConcerningVariables, les 2 pointeurs sont differents
			//et en plus les substitutions surement
			
			//trop complique on va juste changer le domaine...
			variable.setDomain((Domain) v.getDomain().clone());
			
		}
	}
}
