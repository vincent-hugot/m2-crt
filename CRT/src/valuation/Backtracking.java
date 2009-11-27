package valuation;

import java.util.ArrayList;
import java.util.HashMap;
import translator.TranslationVisitor;
import main.Tools;
import model.Constant;
import model.Constraint;
import model.Domain;
import model.Model;
import model.Substitution;
import model.Variable;


public class Backtracking {
	
	Model model;
	ArrayList<Variable> vars;
	HashMap<Variable,Integer> values;
	HashMap<Variable,Domain> excludedDomains;
	
	
	public Backtracking(Model model) {
		this.model = model;
		vars = getRealVariables();
		
		values = new HashMap<Variable,Integer>();
		excludedDomains = new HashMap<Variable,Domain>();
		
		for (Variable v : model.getVariables()) {
			values.put(v, null);
			excludedDomains.put(v, new Domain());
		}
	}
	
	
	public String run() {
		String res = "lol what";
		
		//Variable var = vars.get(0);
		
		/*System.out.println("[[" + var + "]] " + excludedDomains);
		instanciate(var, 2);
		System.out.println("[[" + var + "]] " + excludedDomains);
		restore(var);
		System.out.println("[[" + var + "]] " + excludedDomains);
		System.out.println(getValue(var));*/
		
		
		System.out.println(excludedDomains);
		boolean win = instanciateR(0);
		System.out.println(excludedDomains);
		
		if (!win) return "No solution";
		
		ArrayList<String> solutions = new ArrayList<String>();
		for (Variable var : vars) {
			solutions.add(var.getName() + "=" + values.get(var));
		}
		
		res = Tools.implode(solutions, ", ");
		
		return res;
	}
	
	
	/**
	 * Returns variables that are not constants nor substitutions
	 * @return
	 */
	public ArrayList<Variable> getRealVariables() {
		
		ArrayList<Variable> vars = new ArrayList<Variable>();
		for (Variable var : model.getVariables()) {
			
			//System.out.println(var.getName() + " : " + (var instanceof Constant)
			//		+ " / " + var.getName().startsWith(TranslationVisitor.SUBSTITUTE));
			
			
			if (!((var instanceof Constant) || var.getName().startsWith(TranslationVisitor.SUBSTITUTE)))
				vars.add(var);
			
		}
		
		return vars;
		
	}
	
	
	
	public boolean instanciateR(int varIndice) {
		
		// End of list reached
		if (varIndice >= vars.size()) {
			
			// Checking if there was a solution, and storing it
			for (Variable var : vars) {
				if (getValue(var) == null) return false;
				
				values.put(var, getValue(var));
			}
			
			// There is a solution!
			return true;
		}
		
		
		// Recursion part
		
		// Trying instanciation of "this" variable
		Variable var = vars.get(varIndice);
		Domain dom = var.getDomain();
		
		ArrayList<Integer> domList = new ArrayList<Integer>(dom);
		
		for (Integer val : domList) {
		//for (int i=0; i<dom.size(); i++) {	
			
			//Integer val = dom.
			
			instanciate(var, val);
			updateSubstitutions(); // Updating substitutions
			
			//if (!validModel()) {
				//System.err.println("!validModel() at " + var.getName() + ":" + val);
			//	return false;
			//}
			//else {
				//System.err.println("validModel() at " + var.getName() + ":" + val);
				//System.out.println(model);
			//}
			
			// Instanciation is valid (for Model AND till the end)
			if (validModel() && instanciateR(varIndice+1)) {
				values.put(var, val);
				return true;
			}
			
			// Instanciation invalid, restoring
			else {
				restore(var);
				restoreSubstitutions();
			}
		}
		
		return false;
	}
	
	
	
	
	public void instanciate(Variable var, int value) {
		ArrayList<Integer> toExclude = new ArrayList<Integer>();
		
		for (Integer exclude : var.getDomain()) {
			if (exclude != value)
				toExclude.add(exclude);
		}
		
		for (Integer exclude : toExclude) {
			reduce(var,exclude);
		}
	}
	
	
	
	public void reduce(Variable var, int value) {
		
		// Remembering removed value
		excludedDomains.get(var).add(value);
		
		// Removing it
		var.getDomain().remove(value);
	}
	
	
	
	public void restore(Variable var) {
		
		// Re-adding values
		for (Integer val : excludedDomains.get(var)) {
			var.getDomain().add(val);
		}
		
		// Clearing what we remembered
		excludedDomains.get(var).clear();
	}
	
	
	public Integer getValue(Variable var) {
		
		Domain excluded = excludedDomains.get(var);
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		for (Integer i : var.getDomain()) {
			if (!excluded.contains(i)) values.add(i);
		}
		
		// If 0 values or more than 1, no solution possible
		if (values.size() != 1)
			return null;
		
		return values.get(0);
	}
	
	
	
	/**
	 * Checks if the current model is valid
	 * @return
	 */
	public boolean validModel() {
		
		System.err.println("---");
		
		// Looking for empty domains
		for (Variable var : model.getVariables()) {
			if (var.getDomain().isEmpty()) {
				System.err.println("FAIL OF THE FAIL");
				return false;
			}
				
		}
		
		
		// Looking through constraints
		for (Constraint crt : model.getConstraints()) {
			
			System.err.println("Constraint : " + crt + " (Vars : " + model.getVariables() + ")");
			
			Variable xi = crt.getLeft();
			Variable xj = crt.getRight();
			
			
			// \forall Ai in Xi, \exists Aj in Xj | Ai and Aj compatible
			for (Integer ai : xi.getDomain()) {
				
				//System.err.println("   Xi:" + xi.getName() + "/Ai:" + ai);
				
				boolean found = false;
				for (Integer aj : xj.getDomain()) {
					
					System.err.println("      (Xi) " + xi.getName() + ":" + ai + "   (Xj) " + xj.getName() + ":" + aj);
					
					if (crt.areValidValues(xi, xj, ai, aj)) {
						found = true;
						System.err.println("        FOUND!");
					}
				}
				
				
				if (!found) {
					System.err.println("(crt) FAAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIL.");
					return false;
				}
			}
			
		}
		
		// Looking through substitutions
		for (Substitution sub : model.getSubstitutions()) {
			
			System.err.println("Substitution : " + sub + " (Vars : " + model.getVariables() + ")");
			
			
			Variable xs = sub.getEqual();
			Variable xi = sub.getLeft();
			Variable xj = sub.getRight();
			
			
			// \forall As in Xs, \exists Ai in Xi & \exists Aj in Xj | (As,Ai,Aj) compatible
			for (Integer as : xs.getDomain()) {
				
				System.err.println("   (Xs) " + xs.getName() + ":" + as);
				
				boolean found = false;
				
				for (Integer ai : xi.getDomain()) {
					for (Integer aj : xj.getDomain()) {
						System.err.println("      (Xi) " + xi.getName() + ":" + ai + "   (Xj) " + xj.getName() + ":" + aj);
						
						if (sub.areValidValues(as, ai, aj)) {
							found = true;
							System.err.println("        FOUND!");
						}
					}
				}
				
				if (!found) {
					System.err.println("(sub) FAAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIL.");
					return false;
				}
			}
			
		}
		
		
		// No return false ? returning true.
		return true;
	}
	
	
	
	
	
	
	
	/**
	 * Substitution update, BACKTRACKING/VALUATION version
	 * We only do Step 1 : for Z=A+B, updating Z as A or B may have changed
	 * (the reason is Backtracking only "change" by instanciating non-substitution variables)
	 * 
	 * The domain restriction is memorized as with Backtracking.reduce()
	 */
	public void updateSubstitutions() {
		
		System.err.println("---\nUpdating... (Vars : " + model.getVariables() + ")");
		
		for (int i=0; i<model.getSubstitutions().size(); i++) {
			
			Substitution sub = model.getSubstitutions().get(i);
			System.err.println("DomainRight : " + sub.getRight());
			
			// Step 1: D(Z) reduction from D(A+B)
			Domain newDomain = sub.getLeft().getDomain().arithmeticOperation(
					sub.getSubstitutionOperator(), sub.getRight().getDomain());
			
			System.err.println("{{{{"+newDomain+"}}}} " + sub);
			
			ArrayList<Integer> toExclude = new ArrayList<Integer>();
			for (Integer val : sub.getSubstitutionVariable().getDomain()) {
				if (!newDomain.contains(val))
					toExclude.add(val);
			}
			//sub.getSubstitutionVariable().getDomain().restrict(newDomain);
			
			for (Integer val : toExclude) {
				reduce(sub.getSubstitutionVariable(), val);
			}
			
		}
		

		// for (Substitution sub : model.getSubstitutions()) {
		for (int i = model.getSubstitutions().size() - 1; i >= 0; i--) {
			
			Substitution sub = model.getSubstitutions().get(i);
			
			
			// Step 1: D(Z) reduction from D(A+B)
			Domain newDomain = sub.getLeft().getDomain().arithmeticOperation(
					sub.getSubstitutionOperator(), sub.getRight().getDomain());
			
			System.err.println("(((("+newDomain+")))) " + sub);
			
			ArrayList<Integer> toExclude = new ArrayList<Integer>();
			for (Integer val : sub.getSubstitutionVariable().getDomain()) {
				if (!newDomain.contains(val))
					toExclude.add(val);
			}
			//sub.getSubstitutionVariable().getDomain().restrict(newDomain);
			
			for (Integer val : toExclude) {
				reduce(sub.getSubstitutionVariable(), val);
			}
		}
	}
	
	
	public void restoreSubstitutions() {
		

		System.err.println("---\nRestoring... (Vars : " + model.getVariables() + ")");
		
		for (Substitution sub : model.getSubstitutions()) {
			
			Variable var = sub.getSubstitutionVariable();
			
			// Re-adding values
			for (Integer val : excludedDomains.get(var)) {
				var.getDomain().add(val);
			}
			
			// Clearing what we remembered
			excludedDomains.get(var).clear();
		}
	}
}
