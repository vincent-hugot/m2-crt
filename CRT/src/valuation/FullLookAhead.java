package valuation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import model.Constraint;
import model.Domain;
import model.Model;
import model.Operator;
import model.Substitution;
import model.Variable;
import ac.Couple;

/**
chaque instance est propagée sur les variables restant à instancier
réduction des domaines des variables futures
lorsqu’un domaine d’une variable future devient vide,
l’instanciation courante est rejetée
mettent en oeuvre des algorithmes de consistance d’arcs

Chaque instance est propagée dans le graphe par un algorithme
AC1
Tout le graphe est impacté jusqu’à ce que le point fixe soit atteind
Méthode qui élimine le plus de valeurs inconsistantes

Full-look-ahead : ne fait qu’une seule passe sur le graphe pour
propager une réduction
 *
 */
public class FullLookAhead {

	private Model model;// le modele
	private ArrayList<Variable> X;
	private LinkedList<Couple> queue;
	LinkedList<Variable>[] file; // Liste de Liste de variable

	public FullLookAhead(Model model) {
		this.model = model;
		this.X = model.getVariables();
		this.queue = new LinkedList<Couple>();
		file = new LinkedList [X.size()];
		for (int i=0; i<X.size();i++){
			file[i] = new LinkedList<Variable>();
		}
	}

	/**
	 * Applying fla
D'i <-  Di for 1 <= i <= n
i <-  1
while ( 1 <= i <= n )
	xi <- select-value-xxx
	if (xi is null) (no value was returned)
		i <-  i-1 (backtrack)
		reset each D'k , k>i, to its value before xi was last instanciated
	else
		i <-  i+1
if ( i=0 )
	return "inconsistent"
else
	return instantiated values of {x1, . . . , xn}
	 */
	public void run() { 
		/*
		sauvegarde des domaines de chaque variable
		en fait on va sauvegarder les variables 
		(un niveau plus haut pour + de commodités)
		 */

		int i,indice;

		i=0;
		Integer xi;
		/*
		if (X.size()>=1)
			file[0].addFirst(model.backup(model.getVariables().get(0)));
		 */
		while (i>=0 && i<X.size()){

			//for (indice=i+1; indice<X.size(); indice++){
			for (indice=i; indice<X.size(); indice++){
				file[indice].addFirst(model.backup(model.getVariables().get(indice)));
				//sauvegarde variable indice
			}

//			xi = selectValueFla(model.getVariables().get(i), i);
			xi = selectValueFla(i);

			if (xi == null){
				i--;
				for (indice=i+1; indice<X.size(); indice++){
					model.restore(file[indice].pollFirst());//pop
					/*
					 * càd i du while-1
					i <-  i-1 (backtrack)
					reset each D'k , k>i, to its value before xi was last instanciated
					 */
				}
			}
			else{
				/*
				for (indice=i+1; indice<X.size(); indice++){
					model.restore(file[indice].getFirst());//pop 
				}
				 */
				i++;

				if (i == X.size()){
					//TODO afficher tte sol
					//i--; //no
					System.out.println("-la sol en bas-");
					System.out.println(model);
					System.out.println("-ci dessus-");
					System.out.println(model.getVariables());
					System.out.println("-la sol en haut-");
				}
			}
		}

		if (i<0){
			System.out.println("non");
		}
		else
		{
			System.out.println("peut etre");
		}
	}

	/**
	 * 
	 * @param v
	 * @param i
	 * @return
	 * 
	 * On choisit a de domaine de i
	 * on svg le dom de i
	 * On reduit le domaine de i à a
	 * Pour tout les dom de j tq j > i on test teste que chaque val b de dom j
	 * il faut qu'avec les valeur a et b, le systeme soit consistant 
	 * 
	if there is no value c : D'k such that consistent(a1, ... ai-1; xi = a; xj = b; xk = c)
		remove b from D'j
		removed-value <- true
	 * 
	 */

	public Integer selectValueFla(int i){
		//i+1 sera restauré dans la methode run
		Domain domIterationA = model.getVariables().get(i).getDomain();
		int indice;
		
		Iterator<Integer> iteratorDomI = domIterationA.iterator();
		Integer a;
		
		//on svg tout
		Variable [] vBackups = new Variable [X.size()];
		for (indice=0; indice<X.size();indice++){
			vBackups[indice] = model.backup(model.getVariables().get(indice));
		}

		//while (!model.getVariables().get(i).getDomain().isEmpty())
		while (iteratorDomI.hasNext())//retour pour quitter la boucle
		{
			a = iteratorDomI.next();
			model.getVariables().get(i).getDomain().clear();
			model.getVariables().get(i).getDomain().add(a);

			

			//removedValue = false;
			for (int j=i+1; j<model.getVariables().size();j++){
				for (int k=i+1; k<model.getVariables().size();k++){
					if (k!=j){
						if (!model.getVariables().get(j).getDomain().isEmpty())//sait on jamais...
						{
							Domain domIterationJ = (Domain)model.getVariables().get(j).getDomain().clone();

							Iterator<Integer> iteratorb = domIterationJ.iterator();

							Domain vjToRemove = new Domain();

							boolean consistent = false;

							while(iteratorb.hasNext()){//test pour chaque b si on a c qui a une val possible
								Integer b = iteratorb.next();
								
								consistent = consistent(i,a,j,b,k);//i);

								if (!consistent){
									vjToRemove.add(b);
								}
							}
							model.getVariables().get(j).getDomain().removeAll(vjToRemove);
						}
					}
				}
			}
			boolean futurDomainEmpty=false;
			indice=0;//i+1;
			while (indice<X.size() && !futurDomainEmpty ){
				futurDomainEmpty = model.getVariables().get(indice).getDomain().isEmpty();
				indice++;
			}

			if (futurDomainEmpty){
				
				vBackups[i].getDomain().remove(a);//on enleve a
				for (indice=0; indice<X.size();indice++){
					model.restore(vBackups[indice]);
				}		//on rend un modele restaure
			}
			else{
				return a;
			}


		}
		/*
		if any future domain is empty
		reset each D'j , i < j <= n, to value before a was selected
		else
		return a
		 */
		return null;
	}

	public boolean consistent(int i, int a, int j, int b, int k){//int i){
		System.out.println("consistance appel: i="+i+ " a="+ a+ " j=" +j+ " b="+ b+ "k=" + k);
		int indice;
		boolean continu = true;
		boolean futurDomainEmpty = true;

		//on svg tout
		Variable [] vBackups = new Variable [X.size()];
		for (indice=0; indice<X.size();indice++){
			vBackups[indice] = model.backup(model.getVariables().get(indice));
		}

		Domain domIterationC = (Domain) X.get(k).getDomain().clone();
		Iterator<Integer> iteratorDomC = domIterationC.iterator();
		Integer integerC;

		while( iteratorDomC.hasNext() && continu ){

			X = model.getVariables();//necessaire pour AC3
			//on lui redonne l'addresse de l'array sait on jamais

			initQueueAfterInstanciation(X.get(i), X.get(j), X.get(k));
			System.out.println(" consistence:"+X.get(i)+ X.get(j)+ X.get(k));
			//on initialise la queue

			X.get(i).getDomain().clear();
			X.get(i).getDomain().add(a);
			X.get(j).getDomain().clear();
			X.get(j).getDomain().add(b);
			
			integerC = iteratorDomC.next();
			X.get(k).getDomain().clear();
			X.get(k).getDomain().add(integerC);

			while (!queue.isEmpty()) {

				Couple c = queue.poll();

				// Applying REVISE, then checking reduce
				if (revise(c.getXi(), c.getXj())) {

					// There was a reduction, propagation to any (xk,xi) with k!=i
					// && k!=j
					reduce(c.getXi(), c.getXj());
				}
			}

			futurDomainEmpty=false;
			indice=0;
			while (indice<X.size() && !futurDomainEmpty ){
				futurDomainEmpty = model.getVariables().get(indice).getDomain().isEmpty();
				indice++;
				if (futurDomainEmpty){
					continu = false;
				}
			}

			for (indice=0; indice<X.size();indice++){
				model.restore(vBackups[indice]);
			}		//on rend un modele restaure

		}

		return !futurDomainEmpty;


	}

	/**
	 * Queue initialisation (every couple)
	 * 
	 * @return
	 */
	public LinkedList<Couple> initQueue() {
		LinkedList<Couple> queue = new LinkedList<Couple>();

		for (Variable xi : X) {
			for (Variable xj : X) {
				// if (!xi.equals(xj)) { // For the first time, we allow them
				if (!model.getConstraintConcerningVariables(xi, xj).isEmpty()) {
					Couple C = new Couple(xi, xj);
					queue.push(C);
				}
			}
		}
		return queue;
	}

	public LinkedList<Couple> initQueueAfterInstanciation(Variable va, Variable vb, Variable vc) {
		LinkedList<Couple> queue = new LinkedList<Couple>();

		for (Variable xi : X) {
			for (Variable xj : X) {
				// if (!xi.equals(xj)) { // For the first time, we allow them

				if (xi == va || xj == vb || xi == vb || xj == va || xi == vc || xj == vc){//On choisit les arcs a reviser

					if (!model.getConstraintConcerningVariables(xi, xj).isEmpty()) {
						Couple C = new Couple(xi, xj);
						queue.push(C);
						System.out.println("salllllllllllllllllut");
					}
				}
			}
		}
		return queue;
	}



	/**
	 * Revise part of the algorithm
	 * 
	 * @param xi
	 * @param xj
	 * @return
	 */
	public Boolean revise(Variable xi, Variable xj) {
		boolean reduce = false;
		Domain toRemove = new Domain();

		// No constraints, no reduce.
		if (model.getConstraintConcerningVariables(xi, xj).isEmpty()) return false;


		/*
		 * Special case: Xi [op] Xi. If every [op] is =, <= or >=, domain is
		 * left. If any [op] is !=, < or >, domain is emptied. No other
		 * constraint study is done
		 */
		if (xi == xj) {
			for (Constraint crt : model.getConstraintConcerningVariables(xi, xj)) {

				// If one Cij is not an equal-type, emptying and reduction
				if (crt.getOp() == Operator.Constraint.NOT_EQUAL
						|| crt.getOp() == Operator.Constraint.GREATER
						|| crt.getOp() == Operator.Constraint.LOWER) {

					xi.getDomain().clear();

					// Xi is reduced and belongs to a substitution
					if (!xi.getAssociatedSubstitutions().isEmpty()) updateSubstitutions();

					return true; // There was a reduction
				}
			}
		}


		// Note: not removing directly, since iteration AND removal is
		// prohibited
		for (Integer ai : xi.getDomain()) {

			// Trying to find Aj | (Ai,Aj) respects every Cij constraint
			if (!findAj(xi, xj, ai)) {

				// No Aj valid: Ai makes like a tree, and leaves. (HURR DURR
				// sorry.)
				// xi.getDomain().remove(ai);
				toRemove.add(ai);

				reduce = true; // There was a reduction

			}
		}

		// Removing what was reduced
		xi.getDomain().removeAll(toRemove);

		// if Xi is reduced and belongs to a substitution
		if (reduce && !xi.getAssociatedSubstitutions().isEmpty()) updateSubstitutions();

		return reduce;
	}


	/**
	 * Given Ai in D(Xi), trying to find Aj | (Ai,Aj) respects every Cij
	 * constraint
	 * 
	 * @param xj
	 * @param xi
	 * @param ai
	 * @return true if there was a compatible aj, false otherwise
	 */
	public boolean findAj(Variable xi, Variable xj, Integer ai) {
		boolean valid = true;

		// For each value of D(Xj)
		for (Integer aj : xj.getDomain()) {

			valid = true;

			// For each Cij
			for (Constraint crt : model.getConstraintConcerningVariables(xi, xj)) {

				// Only 1 non-respected constraint => Aj gets away.
				if (!crt.areValidValues(xi, xj, ai, aj)) valid = false;
			}

			// Every Cij passed for (Ai,Aj), an Aj was found.
			if (valid == true) return true;
		}

		// No valid Aj was found, good bye Ai.
		return false;
	}



	/**
	 * Method used when reacting to a positive "reduce" value in AC3's REVISE
	 * call: Add any (xk,xi) couple to "queue", so that xk != xi && xk != xj,
	 * and there is a relation/constraint between Xk and Xi
	 * 
	 * xk != xj is in the algorithm. xk != xi is because we do not need to treat
	 * (Xi,Xi) case anymore (it is a one-time reduce-or-not by adding them in
	 * the initial queue)
	 * 
	 * @param xi
	 * @param xj
	 */
	public void reduce(Variable xi, Variable xj) {

		for (Variable xk : X) {
			if (xk != xi && xk != xj && !model.getConstraintConcerningVariables(xk, xi).isEmpty())
				queue.offer(new Couple(xk, xi));
		}
	}



	/**
	 * General substitutions update, every step is done for every substitution
	 * (see AC3 class description)
	 * 
	 * Note: manual REDUCE calls are done on (Xi,Xi) couple (we wanna make
	 * propagation, but don't have any Xj exclude)
	 * 
	 * Note 2: For dependencies problem, we HAVE to make 2 loops:
	 * - from @1 to @n to propagate substitutions changes (due to sub. dependencies)
	 * - from @n to @1 to re-propagates substitutions changes (due to variable change)
	 * 
	 * Comments are done from example: Z = A + B
	 */
	public void updateSubstitutions() {
		Domain toRemove = new Domain();


		for (int i=0; i<model.getSubstitutions().size(); i++) {

			Substitution sub = model.getSubstitutions().get(i);


			// Step 1: D(Z) reduction from D(A+B)
			Domain newDomain = sub.getLeft().getDomain().arithmeticOperation(
					sub.getSubstitutionOperator(), sub.getRight().getDomain());

			// Restriction + reduce on every (xk,Z)
			if (sub.getSubstitutionVariable().getDomain().restrict(newDomain))
				reduce(sub.getSubstitutionVariable(), sub.getSubstitutionVariable());


			// Step 2: restraining A
			toRemove.clear();
			for (Integer aa : sub.getLeft().getDomain()) {

				if (!findSubstitutionVals(sub, aa, sub.getLeft())) { // reduce on every (xk,A)
					toRemove.add(aa);
					// reduce(sub.getSubstitutionVariable(),sub.getSubstitutionVariable());
					reduce(sub.getLeft(), sub.getLeft());
				}
			}
			sub.getLeft().getDomain().removeAll(toRemove);


			// Step 3: restraining B
			toRemove.clear();
			for (Integer ab : sub.getRight().getDomain()) {

				if (!findSubstitutionVals(sub, ab, sub.getRight())) { // reduce on every (xk,B)
					toRemove.add(ab);
					// reduce(sub.getSubstitutionVariable(),sub.getSubstitutionVariable());
					reduce(sub.getRight(), sub.getRight());
				}
			}
			sub.getRight().getDomain().removeAll(toRemove);
		}



		// for (Substitution sub : model.getSubstitutions()) {
		for (int i = model.getSubstitutions().size() - 1; i >= 0; i--) {

			Substitution sub = model.getSubstitutions().get(i);


			// Step 1: D(Z) reduction from D(A+B)
			Domain newDomain = sub.getLeft().getDomain().arithmeticOperation(
					sub.getSubstitutionOperator(), sub.getRight().getDomain());

			// Restriction + reduce on every (xk,Z)
			if (sub.getSubstitutionVariable().getDomain().restrict(newDomain))
				reduce(sub.getSubstitutionVariable(), sub.getSubstitutionVariable());


			// Step 2: restraining A
			toRemove.clear();
			for (Integer aa : sub.getLeft().getDomain()) {

				if (!findSubstitutionVals(sub, aa, sub.getLeft())) { // reduce on every (xk,A)
					toRemove.add(aa);
					// reduce(sub.getSubstitutionVariable(),sub.getSubstitutionVariable());
					reduce(sub.getLeft(), sub.getLeft());
				}
			}
			sub.getLeft().getDomain().removeAll(toRemove);


			// Step 3: restraining B
			toRemove.clear();
			for (Integer ab : sub.getRight().getDomain()) {

				if (!findSubstitutionVals(sub, ab, sub.getRight())) { // reduce on every (xk,B)
					toRemove.add(ab);
					// reduce(sub.getSubstitutionVariable(),sub.getSubstitutionVariable());
					reduce(sub.getRight(), sub.getRight());
				}
			}
			sub.getRight().getDomain().removeAll(toRemove);
		}
	}


	/**
	 * (Assuming a Z=A+B substitution example)
	 * 
	 * Special findAj version, for substitution. Checks if a value of A or B
	 * doesn't break the substitution (it breaks if Z was reduced, and val in A
	 * or B can no longer give a single value in Z)
	 * 
	 * We are given: - The substitution - Value of A or B to be tested - Were
	 * (out of A and B) does this value belong
	 * 
	 * If val in A, we tests Z&B values, else we test Z&A ones.
	 * 
	 * Idea is that we must have a triplet of values so that Az = Aa [operator]
	 * Ab
	 * 
	 * @param sub the Substitution to test
	 * @param val the A or B value to check if it doesn't outright the
	 *            substitution
	 * @param reference the Variable that contains val (A or B, aka left or
	 *            right)
	 * @return true if there was a compatible aj, false otherwise
	 */
	public boolean findSubstitutionVals(Substitution sub, int val, Variable reference) {

		/* reference/val is Left (A) */
		if (reference == sub.getLeft()) {

			// For each Az value of D(Z)
			for (Integer az : sub.getSubstitutionVariable().getDomain()) {

				// For each Ab of D(B)
				for (Integer ab : sub.getRight().getDomain()) {

					// YATTA! Az = val + Ab
					if (sub.areValidValues(az, val, ab)) return true;
				}
			}
		}

		/* reference/val is Right (B) */
		if (reference == sub.getRight()) {

			// For each Az value of D(Z)
			for (Integer az : sub.getSubstitutionVariable().getDomain()) {

				// For each Aa of D(A)
				for (Integer aa : sub.getLeft().getDomain()) {

					// YATTA! Az = Aa + val
					if (sub.areValidValues(az, aa, val)) return true;
				}
			}
		}

		// If no valid value, or reference/val is not left nor right, no value
		// found
		return false;
	}


	/**
	 * @return the x
	 */
	public ArrayList<Variable> getX() {
		return X;
	}
}

