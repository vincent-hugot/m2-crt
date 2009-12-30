package valuation;

import java.util.ArrayList;
import java.util.HashSet;
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
TODO ce n'est pas vraiment ce qu'on a fait...?

ressources: 
http://www-isl.ece.arizona.edu/ece566/projects/Constraints/AlgorithmAC.pdf
www.cs.kuleuven.ac.be/~dannyd/AI_Chap91_Constr2.ppt
CoursIAGL-Complet.pdf
http://www.ics.uci.edu/~dechter/courses/ics-275a/spring-2009/slides/chapter5-09.ppt
 *
 */
public class FullLookAhead {

	private Model model;// le modele
	private ArrayList<Variable> X;
	private LinkedList<Couple> queue;
	LinkedList<Variable>[] file; // Liste de Liste de variable
	//une file d'attente de "supermarché" 

	public FullLookAhead(Model model) {
		this.model = model;
		this.X = model.getVariables();//normalement on a pas à redefinir X mais X.variables
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
	public HashSet<Variable>  run() { 

		int i,indice;
		HashSet<Variable> resVars = null;
		int [] valuation = new int[X.size()];

		i=0;
		Integer xi;

		for (indice=0; indice<X.size(); indice++){
			file[indice].addFirst(model.backup(model.getVariables().get(indice)));
			/*
			 * sauvegarde des domaines de chaque variable
			 * en fait on va sauvegarder les variables 
			 * (un niveau plus haut pour + de commodités)
			 * tout sauvegarder puis tout restaurer
			 * la valuation modifie pas le modele
			 */
		}

		while (i>=0 && i<X.size()){

			for (indice=i; indice<X.size(); indice++){
				file[indice].addFirst(model.backup(model.getVariables().get(indice)));
				//sauvegarde variable indice
			}

			if (X.size()-i > 2){
				xi = selectValueFla(i);
			}else{
				xi = selectValueFlaLess3Vars(i);
			}
			

			//System.out.println("i("+i+")= "+xi);
			//System.out.println(model);

			if (xi == null){
				System.out.println("backtrack");
				i--;
				for (indice=i+1; indice<X.size(); indice++){
					model.restore(file[indice].pollFirst());//pop
					/*
					i <-  i-1 (backtrack)
					reset each D'k , k>i, to its value before xi was last instanciated
					 */
				}
			}
			else{
				valuation[i]=xi;
				System.out.println("advance("+i+")= "+xi);
				i++;
			}
		}

		if (i == X.size() ){
			System.out.println("sol trouvee");
			resVars = new HashSet<Variable>();
			//On cree un HashSet avec singleton pour donner une solution
			for (i = 0;i<X.size();i++){
				System.out.println(valuation[i]);
				Variable variableValuee = model.backup(X.get(i));
				variableValuee.getDomain().clear();
				variableValuee.getDomain().add(valuation[i]);
				resVars.add(variableValuee);
			}
		}
		else{
			System.out.println("sol pas trouvee"+i);
		}

		for (indice=0; indice<X.size(); indice++){
			model.restore(file[indice].pollFirst());//pop
			//On rend le modele qu'on avait avant de lancer l'algo
		}

		return resVars;

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
	 * TODO Ca ne passe pas si on a A<>A avec strictement moins de 3 variables
	 */

	public Integer selectValueFla(int i){
		System.out.println("normal i");

		Domain domIterationA = (Domain) X.get(i).getDomain().clone();
		// clone sinon acces concurent quand on retire a de Dom(i)
		int indice;
		Iterator<Integer> iteratorDomI = domIterationA.iterator();
		Integer a;
		boolean consistent;

		Variable [] varRestors = new Variable [X.size()-i-1]; // tableau des variables à restaurer


		/* 
		 * (iteratorDomI.hasNext()) <=>
		 * while (!X.get(i).getDomain().isEmpty())
		 */
		while (iteratorDomI.hasNext())//retour pour quitter la boucle
		{
			a = iteratorDomI.next();

			model.getVariables().get(i).getDomain().remove(a); //on supprime a du domaine i 

			//svg à partir de i+1 ici avant de faire des modifs
			//on stocke lensemble des variables modifiées
			int indiceTab = 0;
			for (indice=i+1; indice<X.size();indice++){
				varRestors[indiceTab] = model.backup(X.get(indice));
				indiceTab++;
			}

			for (int j=i+1; j<model.getVariables().size();j++){
				for (int k=i+1; k<model.getVariables().size();k++){
					if (k!=j){
						if (!X.get(j).getDomain().isEmpty())//sait on jamais...
						{
							Domain domIterationJ = (Domain)model.getVariables().get(j).getDomain().clone();

							Iterator<Integer> iteratorb = domIterationJ.iterator();

							Domain vjToRemove = new Domain();

							while(iteratorb.hasNext()){ //test pour chaque b si on a c qui a une val possible
								Integer b = iteratorb.next();

								consistent = consistent(i,a,j,b,k);

								if (!consistent){
									vjToRemove.add(b);
								}
								//System.out.println("a("+i+")= "+a+ " b("+j+")= " + b+" k= "+k+" res= "+consistent);
							}
							//System.out.println("to remove: "+vjToRemove);

							X.get(j).getDomain().removeAll(vjToRemove);
						}
					}
				}
			}
			boolean futurDomainEmpty=false;
			indice=i+1;//i+1; en fait now car si la variable est singleton et qu'on le vire
			/*
				if any future domain is empty
				reset each D'j , i < j <= n, to value before a was selected
				else
				return a
			 */
			while (indice<X.size() && !futurDomainEmpty ){
				futurDomainEmpty = model.getVariables().get(indice).getDomain().isEmpty();
				indice++;
			}

			if (futurDomainEmpty){

				for (int j=0; j<X.size()-i-1;j++){
					model.restore(varRestors [j]);
				}		//on rend un modele restaure
			}
			else{
				return a;
			}
		}



		return null;
	}


	public Integer selectValueFlaLess3Vars(int i){
		
		System.out.println("i2varou1");
		
		Variable [] varRestors = new Variable [X.size()-i-1];
		Integer a;
		Domain domIterationA = (Domain) X.get(i).getDomain().clone();
		// clone sinon acces concurent quand on retire a de Dom(i)
		Iterator<Integer> iteratorDomI = domIterationA.iterator();
		int indice;
		int indiceTab=0;
		boolean consistent;

		if (X.size()-i-1 > 2)
		{// si nombre de var ...< 3 donc on passe pas dans le double for
			return null;//ou exception
		}
		
		Variable xi = X.get(i);
		while ( X.size()-i-1 == 0 && iteratorDomI.hasNext())
		{// si nombre de var ...< 3 donc on passe pas dans le double for
			
			boolean valid = true;
			
			a = iteratorDomI.next();
			//X.get(i).getDomain().remove(a); //on supprime a du domaine i
			
			for (Constraint crt : model.getConstraintConcerningVariables(xi, xi)) {

				// Only 1 non-respected constraint => Aj gets away.
				if (!crt.areValidValues(xi, xi, a, a)) valid = false;

			}

			// Every Cij passed for (Ai,Aj), an Aj was found.

			X.get(i).getDomain().remove(a); //on supprime a du domaine i

			if (valid == true) {
				return a;
			}
		}

		//Si le nombre de variable est < 3

		while (X.size()-i-1 == 1 && iteratorDomI.hasNext())//retour pour quitter la boucle
		{
			a = iteratorDomI.next();
			X.get(i).getDomain().remove(a); //on supprime a du domaine i

			for (indice=i+1; indice<X.size();indice++){
				varRestors[indiceTab] = model.backup(X.get(indice));
				indiceTab++;
			}

			for (int j=i+1; j<X.size();j++){
				if (!X.get(j).getDomain().isEmpty())//sait on jamais...
				{
					Domain domIterationJ = (Domain)model.getVariables().get(j).getDomain().clone();

					Iterator<Integer> iteratorb = domIterationJ.iterator();

					Domain vjToRemove = new Domain();

					while(iteratorb.hasNext()){ //test pour chaque b si on a c qui a une val possible
						Integer b = iteratorb.next();

						consistent = consistent(i,a,j,b, j);//TODO bof bof

						if (!consistent){
							vjToRemove.add(b);
						}
						//System.out.println("a("+i+")= "+a+ " b("+j+")= " + b+" k= "+"k"+" res= "+consistent);
					}
					//System.out.println("to remove: "+vjToRemove);

					X.get(j).getDomain().removeAll(vjToRemove);
				}

			}

			boolean futurDomainEmpty=false;
			indice=i+1;//i+1; en fait now car si la variable est singleton et qu'on le vire
			/*
				if any future domain is empty
				reset each D'j , i < j <= n, to value before a was selected
				else
				return a
			 */
			while (indice<X.size() && !futurDomainEmpty ){
				futurDomainEmpty = model.getVariables().get(indice).getDomain().isEmpty();
				indice++;
			}

			if (futurDomainEmpty){

				for (int j=0; j<X.size()-i-1;j++){
					model.restore(varRestors [j]);
				}		//on rend un modele restaure
			}
			else{
				return a;
			}










			/*
			boolean valid = true;
			Variable xi=X.get(i);
			//Variable xj=X.get(i+1);

			//si c'est la seconde var, ou si elle est unique
			if (X.size() == 1 || i == 1){
				for (Constraint crt : model.getConstraintConcerningVariables(xi, xi)) {

					// Only 1 non-respected constraint => Aj gets away.
					if (!crt.areValidValues(xi, xi, a, a)) valid = false;

				}

				// Every Cij passed for (Ai,Aj), an Aj was found.

				X.get(i).getDomain().remove(a); //on supprime a du domaine i

				if (valid == true) {
					return a;
				}

			}
			else{// X.size()==2
				// et i==0
				varRestors[1] = model.backup(X.get(1));

				// For each value of D(Xj)
				for (Constraint crt : model.getConstraintConcerningVariables(xi, xi)) {

					// Only 1 non-respected constraint => Aj gets away.
					if (!crt.areValidValues(xi, xi, a, a)) valid = false;

				}

				if (valid)
				{

					for (Integer aj : X.get(1).getDomain()) {

						valid = true;

						// For each Cij
						for (Constraint crt : model.getConstraintConcerningVariables(X.get(0), X.get(1))) {

							// Only 1 non-respected constraint => Aj gets away.
							if (!crt.areValidValues(xi, X.get(1), a, aj)) valid = false;
						}

						// Every Cij passed for (Ai,Aj), an Aj was found.
						if (valid == false)
							X.get(1).getDomain().remove(aj);
					}

					// No valid Aj was found, good bye Ai.
				}

				if (X.get(1).getDomain().isEmpty()){
					model.restore(varRestors[1]);
				}

			}
			 */

		}
		return null;

	}

	/**
	 * consistent(a1, ... ai-1; xi = a; xj = b; xk = c)
	 * @param i
	 * @param a
	 * @param j
	 * @param b
	 * @param k
	 * @return
	 * On teste l'arc Di -> Dj -> Dk -> Di soit 3 arcs
	 * dans ce sens là car dans la boucle
	 * il faut utiliser AC3 car on verifie la consistance
	 * 
	 *  l'algo select value enleve 
	 *  la valeur a du domaine i
	 *  donc pour utiliser 
	 *  la fction
	 *  il faut la remettre soit ici 
	 *  soit dans select value
	 *  
	 *  
	 */
	public boolean consistent(int i, int a, int j, int b, int k){
		int indice;

		Variable [] varRestors = new Variable [3]; // tableau des variables à restaurer
		varRestors[0] = model.backup(X.get(i));
		varRestors[1] = model.backup(X.get(j));
		varRestors[2] = model.backup(X.get(k));

		X.get(i).getDomain().clear();
		X.get(i).getDomain().add(a);
		X.get(j).getDomain().clear();
		X.get(j).getDomain().add(b);

		//System.out.println(model);

		queue = initQueue(X.get(i), X.get(j), X.get(k));

		//System.out.println(queue + ""+queue.size());

		while (!queue.isEmpty()) {

			Couple c = queue.poll();

			// Applying REVISE, then checking reduce
			if (revise(c.getXi(), c.getXj())) {

				// There was a reduction, propagation to any (xk,xi) with k!=i
				// && k!=j
				//reduce(c.getXi(), c.getXj());
				for (Variable xk : X) {
					if ( (xk.equals(X.get(k))||xk.equals(X.get(i))||xk.equals(X.get(j)) )
							//(xk==(X.get(k))||xk==(X.get(i))||xk==(X.get(j)) )
							&& xk != c.getXi() && xk != c.getXj()
							&& !model.getConstraintConcerningVariables(xk, c.getXi()).isEmpty()){
						//System.out.println(model.getConstraintConcerningVariables(xk, c.getXi()));
						Couple cc = new Couple(xk, c.getXi());
						queue.offer(cc);
						//System.out.println(cc);
					}
				}
			}
		}

		boolean futurDomainEmpty=false;
		//indice=0;//i+1; i+1 en fait now car si la variable est singleton et qu'on le vire
		indice=i;// car le ac3 custom peut virer le singleton de dom(i)
		while (indice<X.size() && !futurDomainEmpty ){
			futurDomainEmpty = model.getVariables().get(indice).getDomain().isEmpty();
			if (futurDomainEmpty){
				//System.out.println(model.getVariables().get(indice).getName());
			}
			indice++;
		}

		//System.out.println(model);

		model.restore(varRestors [0]);
		model.restore(varRestors [1]);
		model.restore(varRestors [2]);

		//System.out.println(model);

		//System.out.println("a("+i+")= "+a+ " b("+j+")= " + b+" k= "+k+" res= "+!futurDomainEmpty);

		//System.out.println(!futurDomainEmpty);
		return !futurDomainEmpty;
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
	 * Queue initialisation (every couple)
	 * modified / AC3 for only the 3 variables involved in the function consistent
	 * @return
	 */
	public LinkedList<Couple> initQueue(Variable vi, Variable vj, Variable vk) {
		LinkedList<Couple> queue = new LinkedList<Couple>();

		//System.out.println(X.size());

		for (Variable xi : X) {
			for (Variable xj : X) {
				// if (!xi.equals(xj)) { // For the first time, we allow them
				if ( (xi.equals(vi) || xi.equals(vj) || xi.equals(vk)) &&
						(xj.equals(vi) || xj.equals(vj) || xj.equals(vk))
				)
				{
					if (!model.getConstraintConcerningVariables(xi, xj).isEmpty()) {
						Couple C = new Couple(xi, xj);
						queue.push(C);
						//System.out.println(C);
					}
					else {
						//System.out.println(xi+" "+xj);
					}
				}
				else {
					//System.out.println(xi+" "+xj);
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

}

