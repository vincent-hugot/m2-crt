package valuation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

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
	int [] valuation; //liste des valeurs instanciées jusqu'a i
	int iindex;

	public FullLookAhead(Model model) {
		this.model = model;
		this.X = model.getVariables();//normalement on a pas à redefinir X mais X.variables
		this.queue = new LinkedList<Couple>();
		valuation = new int[X.size()];
		System.out.println(model);
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
		Variable [] varRestors = new Variable [X.size()]; // tableau des variables à restaurer apres application de l'algo fla
		i=0;
		Integer xi;

		for (indice=0; indice<X.size(); indice++){
			varRestors[indice] = model.backup(X.get(indice));
			/*
			 * sauvegarde des domaines de chaque variable
			 * en fait on va sauvegarder les variables 
			 * (un niveau plus haut pour + de commodités)
			 * tout sauvegarder puis tout restaurer
			 * la valuation modifie pas le modele
			 */
		}

		HashMap<Integer, Variable>[] varRestorsMap = new HashMap[X.size()];
		for (indice =0; indice<X.size();indice++){
			varRestorsMap[indice] = new HashMap<Integer, Variable>();
		}

		while (i>=0 && i<X.size()){

			iindex =i;
			/*
				if (i==11){
				}
			 */
			varRestorsMap[i].clear();
			varRestorsMap[i] =  model.backupVariables(i+1);//c'est i+1
			/*
			System.out.println(" choice "+i+" "+X.get(i)+ " "+X.get(13)+" "+X.get(12));
			System.out.println("save "+i+X.get(i).getName()+" "+varRestorsMap[i]);
			 */
			//Il y a 2 cas, si on a plus de 3 variables restantes, et s'il y en a qu'une
			//System.out.println("i("+i+")= "+X.get(0));
			if (X.size()-i > 2){
				xi = selectValueFla(i);

			}else{
				xi = selectValueFlaLess3Vars(i);
			}

			System.out.println("i("+i+")= "+xi+ " "+X.get(0));
			//System.out.println(model);

			if (xi == null){
				/*
				i <-  i-1 (backtrack)
				reset each D'k , k>i, to its value before xi was last instanciated
				 */
				//System.out.println("backtrack"+ X.get(i).getName());
				String back="";
				for (indice=0;indice<i;indice++){
					back+=" "+valuation[indice];
				}
				System.out.println(back + " "+X.get(0));
				i--;

				if (i!=-1){
					model.restoreHashMap(varRestorsMap[i]);
				}
			}
			else{
				//updateSubstitutions()?; a est enlevé
				valuation[i]=xi;
				//System.out.println("advance("+X.get(i).getName()+" "+i+")= "+xi);
				// +" "+X.get(12));
				//System.out.println(model);

				if (!X.get(i).getAssociatedSubstitutions().isEmpty()){
					System.out.println("model avant subs et xi"+X.get(0)+" "+ X);
					//On rajoute a avant de lancer l'update des substitutions
					X.get(i).getDomain().add(xi);
					updateSubstitutionsWithoutModifLessEquals(i);//sinon on a des trucs faux
					X.get(i).getDomain().remove(xi);
					//System.out.println(model);
					System.out.println("model apres subs et xi"+X.get(0)+" "+ X);
				}
				i++;
			}
		}

		if (i == X.size() ){
			System.out.println("sol trouvee");
			resVars = new HashSet<Variable>();
			//On cree un HashSet avec singleton pour donner une solution
			for (i = 0;i<X.size();i++){
				//System.out.println(valuation[i]);
				Variable variableValuee = model.backup(X.get(i));
				if (!variableValuee.isArtificial()){
					variableValuee.getDomain().clear();
					variableValuee.getDomain().add(valuation[i]);
					resVars.add(variableValuee);
				}
			}
			System.out.println("sol "+resVars);
		}
		else{
			System.out.println("sol pas trouvee"+i);
		}

		for (indice=0; indice<X.size(); indice++){
			model.restore(varRestors[indice]);
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
	 */

	public Integer selectValueFla(int i){
		//System.out.println("normal i");

		Domain domIterationA = (Domain) X.get(i).getDomain().clone();
		// clone sinon acces concurent quand on retire a de Dom(i)
		int indice;
		Iterator<Integer> iteratorDomI = domIterationA.iterator();
		Integer a;
		boolean consistent;
		HashMap<Integer, Variable> intToVar;

		/* 
		 * (iteratorDomI.hasNext()) <=>
		 * while (!X.get(i).getDomain().isEmpty())
		 */
		while (iteratorDomI.hasNext())//on utilise un retour pour quitter la boucle
		{
			a = iteratorDomI.next();

			X.get(i).getDomain().remove(a); //on supprime a du domaine i
			/*if (i == 12){
				System.out.println(100+" "+X);
			}
			 */

			//System.out.println(model);

			//svg à partir de i+1 ici avant de faire des modifs
			//on stocke lensemble des variables qui seront certainement modifiées 
			intToVar = model.backupVariables(i+1);//ok avant ou apres remove de a peu importe

			boolean futurEmpty = false;

			for (int j=i+1; j<model.getVariables().size();j++){
				for (int k=i+1; k<model.getVariables().size();k++){
					if (k!=j){
						if (!X.get(j).getDomain().isEmpty()&& !futurEmpty)//sait on jamais...
						{
							Domain domIterationJ = (Domain)model.getVariables().get(j).getDomain().clone();

							Iterator<Integer> iteratorb = domIterationJ.iterator();

							Domain vjToRemove = new Domain();

							while(iteratorb.hasNext()){ //test pour chaque b si on a c qui a une val possible
								Integer b = iteratorb.next();
								/*

								if (i==12){
									System.out.println(X.get(12));
								}
								 */
								/*
								HashMap<Integer, Variable > g=model.backupVariables(0);
								System.out.println("AVANT subs"+X);
								 */
								consistent = consistent(i,a,j,b,k);
								/*
								System.out.println(X.get(i).getName()+"= "+a+ " "+X.get(j).getName()+" = " + b+" "+X.get(k).getName()+" res= "+consistent);
								System.out.println("apres subs"+X);
								HashMap<Integer, Variable > g1=model.backupVariables(0);
								if (!g.equals(g1)){
									while (true)//marche pas egalite constante
									System.out.println("prob x"+X);


								}
								*/
								 

								/*
								if (i==12){
									//System.out.println(X);
								}
								 */
								//System.out.println(model);

								if (!consistent){
									vjToRemove.add(b);
									System.out.println(X.get(i).getName()+"= "+a+ " "+X.get(j).getName()+" = " + b+" "+X.get(k).getName()+" res= "+consistent+ " X "+X);
								}
								//System.out.println(X.get(i).getName()+"= "+a+ " "+X.get(j).getName()+" = " + b+" "+X.get(k).getName()+" res= "+consistent);
								//System.out.println("a("+i+")= "+a+ " b("+j+")= " + b+" k= "+k+" res= "+consistent +" "+ X);
							}

							//if (i==12){
							//System.out.println(X.get(12));
							//}
							if (!vjToRemove.isEmpty()){
								//System.out.println("to remove: "+X.get(j).getName()+" "+vjToRemove);

								X.get(j).getDomain().removeAll(vjToRemove);

								if (!X.get(j).getAssociatedSubstitutions().isEmpty()){

									X.get(i).getDomain().add(a);
									System.out.println("before subst with a because removed value"+X);
									updateSubstitutionsWithoutModifLessEquals(j);
									X.get(i).getDomain().remove(a);
									//System.out.println(model);
									System.out.println("after subst because removed value"+X);

								}

								futurEmpty = futureEmpty();
							}
						}
						else{

							//System.out.println("prob");
						}
					}
				}
			}
			boolean futurDomainEmpty=false;
			indice=i+1;//i+1; en fait car si la variable est singleton et qu'on si on l'enleve
			/*
				if any future domain is empty
				reset each D'j , i < j <= n, to value before a was selected
				else
				return a
			 */
			while (indice<X.size() && !futurDomainEmpty ){
				futurDomainEmpty = model.getVariables().get(indice).getDomain().isEmpty();
				if (futurDomainEmpty){
					System.out.println(X.get(indice).getName()+ " empty");
				}
				indice++;
			}

			if (futurDomainEmpty){
				model.restoreHashMap(intToVar);//On restaure
				intToVar.clear();
			}
			else{



				return a;
			}
		}



		return null;
	}

	/**
	 * 
	 * @param i
	 * @return
	 * Un select-value special si il reste 2 ou 1 variable
	 * Par exemple si on a 3 var A,B et C et que l'on a B!=C, le select value avec 3 variables prendra la 1ere valeur qui viendra pour les 2 dernieres variables
	 */
	public Integer selectValueFlaLess3Vars(int i){

		//System.out.println("i2varou1");
		//System.out.println(model);

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
		//Il reste 0 variable a instancier apres celle en cours
		while ( X.size()-i-1 == 0 && iteratorDomI.hasNext())
		{// si nombre de var ...< 3 donc on passe pas dans le double for

			boolean valid = true;

			a = iteratorDomI.next();
			//X.get(i).getDomain().remove(a); //on supprime a du domaine i
			//On supprime a apres

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

		//Si le nombre de variable 1 variable à instancier apres celle en cours
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
			indice=i+1;//i+1 car si le domaine de la variable est un singleton et qu'on l'enleve
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
					model.restore(varRestors [j],i+1);
				}		//on rend un modele restaure
				System.out.println("backtrack a"+X);
			}
			else{
				return a;
			}

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
	 * il faut utiliser AC3 car on verifie la consistance
	 * 
	 *  l'algo select value enleve 
	 *  la valeur a du domaine i
	 *  donc pour utiliser 
	 *  la fonction
	 *  il faut la remettre dans le domaine de i 
	 *  
	 *  
	 */
	public boolean consistent(int i, int a, int j, int b, int k){
		int indice;

		HashMap<Integer, Variable> intToVar = new HashMap<Integer, Variable>();
		intToVar.put(i, model.backup(X.get(i)));//TODO dja?
		intToVar.put(j, model.backup(X.get(j)));
		intToVar.put(k, model.backup(X.get(k)));
		//On svgarde Var(i),Var(j) et Var(k)

		//On svgarde les substitutions associee à Var(i),Var(j) et Var(k)
		AddbackupSubstitutionsToHash(intToVar, X.get(i));
		AddbackupSubstitutionsToHash(intToVar, X.get(j));
		AddbackupSubstitutionsToHash(intToVar, X.get(k));

		//HashSet<Variable> vs =new HashSet<Variable>();
		if (!X.get(i).getAssociatedSubstitutions().isEmpty()){
			for (Substitution s : X.get(i).getAssociatedSubstitutions()){
				if (model.getKey(s.getLeft())<iindex){
					s.getLeft().getDomain().clear();
					s.getLeft().getDomain().add(valuation[model.getKey(s.getLeft())]);
				}
				if (model.getKey(s.getRight())<iindex){
					s.getRight().getDomain().clear();
					s.getRight().getDomain().add(valuation[model.getKey(s.getRight())]);
				}
				if (model.getKey(s.getSubstitutionVariable())<iindex){
					s.getSubstitutionVariable().getDomain().clear();
					s.getSubstitutionVariable().getDomain().add(valuation[model.getKey(s.getSubstitutionVariable())]);
				}
			}
		}
		if (!X.get(j).getAssociatedSubstitutions().isEmpty()){
			for (Substitution s : X.get(j).getAssociatedSubstitutions()){
				if (model.getKey(s.getLeft())<iindex){
					s.getLeft().getDomain().clear();
					s.getLeft().getDomain().add(valuation[model.getKey(s.getLeft())]);
				}
				if (model.getKey(s.getRight())<iindex){
					s.getRight().getDomain().clear();
					s.getRight().getDomain().add(valuation[model.getKey(s.getRight())]);
				}
				if (model.getKey(s.getSubstitutionVariable())<iindex){
					s.getSubstitutionVariable().getDomain().clear();
					s.getSubstitutionVariable().getDomain().add(valuation[model.getKey(s.getSubstitutionVariable())]);
				}
			}
		}
		if (!X.get(k).getAssociatedSubstitutions().isEmpty()){
			for (Substitution s : X.get(k).getAssociatedSubstitutions()){
				if (model.getKey(s.getLeft())<iindex){
					s.getLeft().getDomain().clear();
					s.getLeft().getDomain().add(valuation[model.getKey(s.getLeft())]);
				}
				if (model.getKey(s.getRight())<iindex){
					s.getRight().getDomain().clear();
					s.getRight().getDomain().add(valuation[model.getKey(s.getRight())]);
				}
				if (model.getKey(s.getSubstitutionVariable())<iindex){
					s.getSubstitutionVariable().getDomain().clear();
					s.getSubstitutionVariable().getDomain().add(valuation[model.getKey(s.getSubstitutionVariable())]);
				}
			}
		}
		if ((j==X.size()-1 || k==X.size()-1) && a==0)
		System.out.println(intToVar);


		X.get(i).getDomain().clear();
		X.get(i).getDomain().add(a);
		X.get(j).getDomain().clear();
		X.get(j).getDomain().add(b);

		/*		TODO faire ca la
		if (model.getKey(sub.getLeft()) < iindex){
			X.get(isubleft).getDomain().clear();
			X.get(isubleft).getDomain().add(valuation[iindex]);

		}
		if (model.getKey(sub.getRight()) < iindex){
			X.get(isubright).getDomain().clear();
			X.get(isubright).getDomain().add(valuation[iindex]);

		}
		if (model.getKey(sub.getSubstitutionVariable()) < iindex){
			X.get(isubvar).getDomain().clear();
			X.get(isubvar).getDomain().add(valuation[iindex]);

		}*/

		/*
		if (X.get(3)==X.get(k))
			System.out.println(model+"sans update");
		updateSubstitutions();
		if (X.get(3)==X.get(k))
			System.out.println(model+"avec update");
		 */
		//System.out.println(model);
		//System.out.println("consistent? "+X);

		queue = initQueue(X.get(i), X.get(j), X.get(k));

		//System.out.println(queue + " "+queue.size()+" "+X);
		/*
		if (i==1 && a ==5 && j==3){
			System.out.println(model);
		}
		 */
		while (!queue.isEmpty()) {

			Couple c = queue.poll();

			// Applying REVISE, then checking reduce
			if (revise(c.getXi(), c.getXj())) {

				/*
				if (i==1 && a ==5){
					System.out.println(c.getXi()+" "+c.getXj()+" "+model.getConstraintConcerningVariables(c.getXi(), c.getXj()));
					//System.out.println(X);
				}
				 */

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

		boolean consistent=true;
		//indice=0;//i+1; i+1 en fait now car si la variable est singleton et qu'on le vire
		indice=i;// car le ac3 custom peut virer le singleton de dom(i)
		while (indice<X.size() && consistent ){
			consistent = consistent && !model.getVariables().get(indice).getDomain().isEmpty();
			if (!consistent){
				//System.out.println(model.getVariables().get(indice).getName()+" is empty");
			}
			indice++;
		}
		/*
		if (i==1 && a ==5 && j==3){
			System.out.println(model);
		}
		 */
		//System.out.println(model);
		
		
		model.restoreHashMap(intToVar);
		
		if ((j==X.size()-1 || k==X.size()-1 )&& a==0)
			System.out.println(" rest "+X);
		
		/*
		 * restaurer les substitution effet de bord
		 */
		//System.out.println("restore");
		//System.out.println(model);
		//System.out.println("a("+i+")= "+a+ " b("+j+")= " + b+" k= "+k+" res= ");
		return consistent;
	}

	/**
	 * à partir de la variable globale iindex,
	 * on cherche si à partir de l'indice iindex+1, il y a un domaine vide
	 * @return
	 */
	public boolean futureEmpty(){
		boolean allOk=true;
		int indice=iindex+1;// car le ac3 custom peut virer le singleton de dom(i)
		while (indice<X.size() && allOk ){
			allOk = allOk && !model.getVariables().get(indice).getDomain().isEmpty();
			if (!allOk){
				//System.out.println(model.getVariables().get(indice).getName()+" is empty");
			}
			indice++;
		}
		return !allOk;
	}

	/**
	 * 
	 * @param hashmapOfBackupVar
	 * @param v
	 * à partir d'une hashmap donnée en parametre et d'une variable v,
	 * on ajoute un backup des variables qui utilisent v en commun dans leur substitutions
	 */
	public void AddbackupSubstitutionsToHash(HashMap<Integer, Variable> hashmapOfBackupVar, Variable v) {

		if (!v.getAssociatedSubstitutions().isEmpty()){

			for (Substitution sub : v.getAssociatedSubstitutions()) {

				int isubleft = model.getKey(sub.getLeft());
				int isubright = model.getKey(sub.getRight());
				int isubvar = model.getKey(sub.getSubstitutionVariable());

				hashmapOfBackupVar.put(model.getKey(sub.getLeft()), model.backup(sub.getLeft()));
				hashmapOfBackupVar.put(model.getKey(sub.getRight()), model.backup(sub.getRight()));
				hashmapOfBackupVar.put(model.getKey(sub.getSubstitutionVariable()), model.backup(sub.getSubstitutionVariable()));
				//System.out.println(" X"+X);


			}
		}

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
				//System.out.println(""+xi+xj+crt+ valid + X + " "+ai + " "+ aj);
			}
			//System.out.println("res "+xi+xj+ valid+" "+ai+" "+aj);

			// Every Cij passed for (Ai,Aj), an Aj was found.
			if (valid == true) return true;
		}

		// No valid Aj was found, good bye Ai.
		return false;
	}

	/**
	 * Queue initialisation (every couple)
	 * modified / AC3 only an arc between the 3 variables involved in the function consistent
	 * can be added
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

		//System.out.println("avant revise sub----------------"+model);

		//if ( !xi.getAssociatedSubstitutions().isEmpty()) updateSubstitutions();

		boolean reduce = false;
		Domain toRemove = new Domain();

		//System.out.println("avant revise----------------"+model);

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
					//if (!xi.getAssociatedSubstitutions().isEmpty()) updateSubstitutions();//C get xi index

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

				//System.out.println("revise kill ai= "+ ai + model.getConstraintConcerningVariables(xi, xj));

				reduce = true; // There was a reduction

			}
		}

		// Removing what was reduced
		xi.getDomain().removeAll(toRemove);
		//System.out.println(toRemove);

		// if Xi is reduced and belongs to a substitution
		//System.out.println("avant sub----------------"+model);
		//if (reduce && !xi.getAssociatedSubstitutions().isEmpty()) updateSubstitutions();
		//System.out.println("apres sub----------------"+model);

		//if (reduce && !xi.getAssociatedSubstitutions().isEmpty()) updateSubstitutions(X.indexOf(xi));
		if (reduce && !xi.getAssociatedSubstitutions().isEmpty()) updateSubstitutions(model.getKey(xi));
		return reduce;
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
	 * 
	 * @param iinitial
	 * modifie / AC3
	 * si iinitial < iindex alors on ajoute la valeur valuation[i] à la variable impliquee dans la substitution
	 */
	public void updateSubstitutions(int iinitial) {
		ArrayList<Substitution> listeSub = X.get(iinitial).getAssociatedSubstitutions();
		Domain toRemove = new Domain();

		//System.out.println("update "+iinitial +" "+X.get(iinitial).getName());


		for ( Substitution sub: listeSub){
			/*
			int isubleft = model.getKey(sub.getLeft());
			int isubright = model.getKey(sub.getRight());
			int isubvar = model.getKey(sub.getSubstitutionVariable());
			 */

			Domain newDomain = sub.getLeft().getDomain().arithmeticOperation(
					sub.getSubstitutionOperator(), sub.getRight().getDomain());
			//			if (newDomain == X.get(iinitial).getDomain()){


			//if (isubvar>=iindex){
			//System.out.println(sub.getLeft().getName()+sub.getLeft().getDomain()+sub.getRight().getName()+sub.getRight().getDomain()+ " subs var"+sub.getSubstitutionVariable());
			if (sub.getSubstitutionVariable().getDomain().restrict(newDomain)){
				//	System.out.println("new dom" + newDomain +sub.getSubstitutionVariable().getName());
			}
			//}

			//}

			else{
				//System.out.println("subst var < iini");
			}



			toRemove.clear();
			for (Integer aa : sub.getLeft().getDomain()) {

				if (!findSubstitutionVals(sub, aa, sub.getLeft())) { // reduce on every (xk,A)
					toRemove.add(aa);
					//System.out.println(aa + ""+sub.getLeft().getDomain()+sub.getLeft().getName());
					// reduce(sub.getSubstitutionVariable(),sub.getSubstitutionVariable());
					//reduce(sub.getLeft(), sub.getLeft());
				}
			}
			sub.getLeft().getDomain().removeAll(toRemove);
			//System.out.println(toRemove);


			// Step 3: restraining B
			toRemove.clear();
			for (Integer ab : sub.getRight().getDomain()) {

				if (!findSubstitutionVals(sub, ab, sub.getRight())) { // reduce on every (xk,B)
					toRemove.add(ab);
					// reduce(sub.getSubstitutionVariable(),sub.getSubstitutionVariable());
					//reduce(sub.getRight(), sub.getRight());
				}
			}
			sub.getRight().getDomain().removeAll(toRemove);
			//System.out.println(toRemove +" to remove from right"+ toRemove.isEmpty());

		}




	}

	/**
	 * 
	 * @param iinitial
	 * modifie / AC3
	 * si iinitial < iindex alors on ajoute la valeur valuation[i] à la variable impliquee dans la substitution
	 * sauf qu'on restaure les variables dont l'indice est inferieur à iindex apres avoir màj les substitution
	 */

	public void updateSubstitutionsWithoutModifLessEquals(int iinitial) {

		ArrayList<Substitution> listeSub = X.get(iinitial).getAssociatedSubstitutions();
		Domain toRemove = new Domain();

		boolean reduce;

		HashMap<Integer, Variable> intToVar = new HashMap<Integer, Variable>();
		TreeSet<Integer> indiceVariablesToUpdate = new TreeSet<Integer>();

		for ( Substitution sub: listeSub){

			int isubleft = model.getKey(sub.getLeft());
			int isubright = model.getKey(sub.getRight());
			int isubvar = model.getKey(sub.getSubstitutionVariable());

			if (isubleft < iindex && !intToVar.containsKey(isubleft)){
				intToVar.put(isubleft, model.backup(sub.getLeft()));
				sub.getLeft().getDomain().clear();
				sub.getLeft().getDomain().add(valuation[isubleft]);
				System.out.println("l "+valuation[X.indexOf(sub.getLeft())]+X.indexOf(sub.getLeft()));
			}
			//< car on ajoute a à i deja

			if (isubright < iindex && !intToVar.containsKey(isubright)){
				intToVar.put(isubright, model.backup(sub.getRight()));
				sub.getRight().getDomain().clear();
				sub.getRight().getDomain().add(valuation[isubright]);
				System.out.println("r "+valuation[X.indexOf(sub.getRight())]+X.indexOf(sub.getRight()));
			}

			if (isubvar < iindex && !intToVar.containsKey(isubvar)){
				intToVar.put(isubvar, model.backup(sub.getSubstitutionVariable()));
				sub.getSubstitutionVariable().getDomain().clear();
				sub.getSubstitutionVariable().getDomain().add(valuation[isubvar]);
				System.out.println("r "+valuation[X.indexOf(sub.getRight())]+X.indexOf(sub.getRight()));
			}

			Domain newDomain = sub.getLeft().getDomain().arithmeticOperation(
					sub.getSubstitutionOperator(), sub.getRight().getDomain());
			//			if (newDomain == X.get(iinitial).getDomain()){


			//reduce = false;
			if (isubvar>iindex){
				//System.out.println(sub.getLeft().getName()+sub.getLeft().getDomain()+sub.getRight().getName()+sub.getRight().getDomain()+ " subs var"+sub.getSubstitutionVariable());
				if (sub.getSubstitutionVariable().getDomain().restrict(newDomain)){
					//reduce = true;
					System.out.println("new dom" + newDomain +sub.getSubstitutionVariable().getName());
					if (iinitial != isubvar)
						indiceVariablesToUpdate.add(isubvar);
				}
			}
			//}
			else{
				//System.out.println("subst var < iini");
			}


			toRemove.clear();
			for (Integer aa : sub.getLeft().getDomain()) {

				if (!findSubstitutionVals(sub, aa, sub.getLeft())) { // reduce on every (xk,A)
					toRemove.add(aa);

					/**
					 * prob TODO vire D
					 */

					System.out.println("sub = "+ sub+" aa = "+ aa + " sub left " + sub.getLeft().getName()+ " = "+sub.getLeft().getDomain());
					// reduce(sub.getSubstitutionVariable(),sub.getSubstitutionVariable());
					//reduce(sub.getLeft(), sub.getLeft());
					System.out.println(iinitial+" <-iinitial isubleft-> "+isubleft);
				}
			}


			/**
			 * prob si inf ex: 0 D dans send more
			 * > parce que si 0
			 */
			if (isubleft > iindex)
				if (sub.getLeft().getDomain().removeAll(toRemove)){
					System.out.println(iindex+" <-iindex isubleft-> "+isubleft);
					if (iinitial != isubleft)
						indiceVariablesToUpdate.add(isubleft);
				}

			//System.out.println(toRemove);


			// Step 3: restraining B
			toRemove.clear();
			for (Integer ab : sub.getRight().getDomain()) {

				if (!findSubstitutionVals(sub, ab, sub.getRight())) { // reduce on every (xk,B)
					toRemove.add(ab);
					reduce = true;
					// reduce(sub.getSubstitutionVariable(),sub.getSubstitutionVariable());
					//reduce(sub.getRight(), sub.getRight());
				}
			}

			if (isubright > iindex)
				if (sub.getRight().getDomain().removeAll(toRemove))
					if (iinitial != isubright)
						indiceVariablesToUpdate.add(isubright);


			//System.out.println(toRemove +" to remove from right"+ toRemove.isEmpty());

			//restoreSubstitutions(variablesUpdated, iindex);
			//variablesUpdated.clear();

		}
		model.restoreHashMap(intToVar);
		intToVar.clear();
		System.out.println(" red "+ indiceVariablesToUpdate );
		/**
		 * Si iinitial = centre,gauche ou droite,
		 * on update sur les indices <> iinitial
		 */
		for (Integer indice: indiceVariablesToUpdate){
			updateSubstitutionsWithoutModifLessEquals(indice);
			System.out.println("reupdate "+X.get(indice).getName());
		}
		//if (reduce)
		//System.out.println(reduce +" "+ X);

	}
}