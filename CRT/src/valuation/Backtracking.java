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
	
	HashMap<Integer,Variable> modelBackup; // General backup
	HashMap[] modelBackups;
	
	
	
	public Backtracking(Model model) {
		this.model = model;
		vars = getRealVariables();
		
		modelBackups = new HashMap[vars.size()];
	}
	
	
	
	public String run() {
		String res = "";
		
		modelBackup = model.backupVariables(0);
		
		
		boolean win = instanciateR(0);
		
		
		if (win) {
			
			// Fetching solutions
			ArrayList<String> solutions = new ArrayList<String>();
			for (Variable var : vars) {
				solutions.add(var.getName() + "=" + var.getDomain().first());//values.get(var));
			}
			res = Tools.implode(solutions, ", ");
		}
		
		// Restoring model
		model.restoreHashMap(modelBackup);
		
		
		return res;
	}
	
	
	/**
	 * Returns variables that are not constants nor substitutions
	 * @return
	 */
	public ArrayList<Variable> getRealVariables() {
		
		ArrayList<Variable> vars = new ArrayList<Variable>();
		for (Variable var : model.getVariables()) {
			
			
			if (!((var instanceof Constant)
					|| var.getName().startsWith(TranslationVisitor.SUBSTITUTE)))
				vars.add(var);
			
		}
		
		return vars;
		
	}
	
	
	
	public boolean instanciateR(int varIndice) {
		
		// End of list reached
		if (varIndice >= vars.size()) {
			
			// Checking if there was a solution, and storing it
			for (Variable var : vars) {
				//if (getValue(var) == null) return false;
				if (var.getDomain().size() != 1) return false;
				
				//values.put(var, getValue(var));
			}
			
			// There is a solution!
			return true;
		}
		
		
		// Recursion part
		
		// Trying instanciation of "this" (current) variable
		Variable var = vars.get(varIndice);
		Domain dom = var.getDomain();
		
		ArrayList<Integer> domList = new ArrayList<Integer>(dom);
		
		for (Integer val : domList) {
			
			instanciate(varIndice, val);
			updateSubstitutions(); // Updating substitutions
			
			
			// Instanciation is valid (for Model AND till the end)
			if (validModel() && instanciateR(varIndice+1)) {
				return true;
			}
			
			// Instanciation invalid, restoring
			else {
				restore(varIndice);
				//restoreSubstitutions();
			}
		}
		
		return false;
	}
	
	
	
	
	public void instanciate(int varIndice, int value) {
		Variable var = vars.get(varIndice);
		
		// Backuping
		modelBackups[varIndice] = model.backupVariables(0);
		//modelBackups[varIndice] = model.backupVariables(varIndice);
		
		// Instantiation = applying only one value
		var.getDomain().clear();
		var.getDomain().add(value);
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public void restore(Integer varIndice) {
		model.restoreHashMap((HashMap<Integer,Variable>) modelBackups[varIndice]);
	}
	
	
	
	
	/**
	 * Checks if the current model is valid
	 * @return
	 */
	public boolean validModel() {
		
		//System.err.println("---");
		
		// Looking for empty domains
		for (Variable var : model.getVariables()) {
			if (var.getDomain().isEmpty()) {
				//System.err.println("FAIL OF THE FAIL");
				return false;
			}
				
		}
		
		
		// Looking through constraints
		for (Constraint crt : model.getConstraints()) {
			
			//System.err.println("Constraint : " + crt + " (Vars : " + model.getVariables() + ")");
			
			Variable xi = crt.getLeft();
			Variable xj = crt.getRight();
			
			
			// \forall Ai in Xi, \exists Aj in Xj | Ai and Aj compatible
			boolean found = false;
			for (Integer ai : xi.getDomain()) {
				
				//System.err.println("   Xi:" + xi.getName() + "/Ai:" + ai);
				
				
				for (Integer aj : xj.getDomain()) {
					
					//System.err.println("      (Xi) " + xi.getName() + ":" + ai + "   (Xj) " + xj.getName() + ":" + aj);
					
					if (crt.areValidValues(xi, xj, ai, aj)) {
						found = true;
						//System.err.println("        FOUND!");
					}
				}
				
				
			}
			
			if (!found) {
				//System.err.println("(crt) FAAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIL.");
				return false;
			}
			
		}
		
		// Looking through substitutions
		for (Substitution sub : model.getSubstitutions()) {
			
			//System.err.println("Substitution : " + sub + " (Vars : " + model.getVariables() + ")");
			
			
			Variable xs = sub.getEqual();
			Variable xi = sub.getLeft();
			Variable xj = sub.getRight();
			
			
			// \forall As in Xs, \exists Ai in Xi & \exists Aj in Xj | (As,Ai,Aj) compatible
			boolean found = false;
			for (Integer as : xs.getDomain()) {
				
				//System.err.println("   (Xs) " + xs.getName() + ":" + as);
				
				
				for (Integer ai : xi.getDomain()) {
					for (Integer aj : xj.getDomain()) {
						//System.err.println("      (Xi) " + xi.getName() + ":" + ai + "   (Xj) " + xj.getName() + ":" + aj);
						
						if (sub.areValidValues(as, ai, aj)) {
							found = true;
							//System.err.println("        FOUND!");
						}
					}
				}
				
				
			}if (!found) {
				//System.err.println("(sub) FAAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIL.");
				return false;
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
		
		//System.err.println("---\nUpdating... (Vars : " + model.getVariables() + ")");
		
		for (int i=0; i<model.getSubstitutions().size(); i++) {
			
			Substitution sub = model.getSubstitutions().get(i);
			//System.err.println("DomainRight : " + sub.getRight());
			
			// Step 1: D(Z) reduction from D(A+B)
			Domain newDomain = sub.getLeft().getDomain().arithmeticOperation(
					sub.getSubstitutionOperator(), sub.getRight().getDomain());
			
			//System.err.println("{{{{"+newDomain+"}}}} " + sub);
			
			ArrayList<Integer> toExclude = new ArrayList<Integer>();
			for (Integer val : sub.getSubstitutionVariable().getDomain()) {
				if (!newDomain.contains(val))
					toExclude.add(val);
			}
			//sub.getSubstitutionVariable().getDomain().restrict(newDomain);
			
			for (Integer val : toExclude) {
				sub.getSubstitutionVariable().getDomain().remove(val);
				//reduce(sub.getSubstitutionVariable(), val);
			}
			
		}
		

		// for (Substitution sub : model.getSubstitutions()) {
		for (int i = model.getSubstitutions().size() - 1; i >= 0; i--) {
			
			Substitution sub = model.getSubstitutions().get(i);
			
			
			// Step 1: D(Z) reduction from D(A+B)
			Domain newDomain = sub.getLeft().getDomain().arithmeticOperation(
					sub.getSubstitutionOperator(), sub.getRight().getDomain());
			
			//System.err.println("(((("+newDomain+")))) " + sub);
			
			ArrayList<Integer> toExclude = new ArrayList<Integer>();
			for (Integer val : sub.getSubstitutionVariable().getDomain()) {
				if (!newDomain.contains(val))
					toExclude.add(val);
			}
			//sub.getSubstitutionVariable().getDomain().restrict(newDomain);
			
			for (Integer val : toExclude) {
				sub.getSubstitutionVariable().getDomain().remove(val);
				//reduce(sub.getSubstitutionVariable(), val);
			}
		}
	}
}
