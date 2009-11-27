/**
 * 
 */
package fitnesse;

import java.util.ArrayList;
import java.util.Iterator;

import edu.emory.mathcs.backport.java.util.Collections;

import model.Model;
import model.Variable;
import translator.Translator;
import ac.AC6;

/**
 * 
 *
 *l'objectif de la classe AC3Fitnesse est de tester l'algorithme AC3 
 *à partir d'une seule classe.
 *
 */

public class AC6Fitnesse {
	
	private Translator translator; //contient le parseur
	public Model model;//contient le modele initial, on pourra l'afficher si besoin
	private ArrayList<Variable> listeVariables;//contient toutes les variables (avec leur domaine) apres application de l'algo ac3 
	
	
	/**
	 * @param fileContent
	 * Constructeur qui prend en argument le contenu d'un fichier
	 * puis applique AC3, il définit également les variables model et X
	 */
	public AC6Fitnesse(String fileContent){
		translator = new Translator("Test Fitnesse", fileContent);
		model = translator.translate();
		AC6 ac6=new AC6(model);
		ac6.run();
		listeVariables = model.getVariables();
		/*en fait le model qu'on a fournit au constructeur d'AC6Fitnesse est modifie 
		 * mais on garde le pointeur sur ce modele
		 * meme s'il devient un attribut private
		 */
		
	}
	/**
	 * @param val
	 * @param variableName
	 * @return
	 * retourne true si la valeur appartient au domaine de la variable 
	 * et que la variable existe
	 * retourne faux dans tous les autres cas
	 */
	public boolean ValeurAppartientAuDomaineDe(int val, String variableName){
		Iterator<Variable> i = listeVariables.iterator();
		Variable variable = null;
		boolean variableTrouvee = false;
		
		while (i.hasNext() && !variableTrouvee){
			variable = ((Variable)i.next());
			if (variable.getName().equals(variableName)){
				variableTrouvee = true;
			}
		}
		
		if (variableTrouvee){
			return variable.getDomain().contains(val);
		}
		else
			return false;
		
	}
	

	
	/**
	 * @param val
	 * @param variableName
	 * @return
	 * retourne true si toutes les valeurs de la liste (un tableau d'entier)
	 * appartiennent au domaine de la variable 
	 * et que la variable existe
	 * retourne faux dans tous les autres cas
	 * 
	 * sert à tester une assert true 
	 */
	public boolean ListeValeurAppartientAuDomaineDe(Integer[] val, String variableName){
		Iterator<Variable> i = listeVariables.iterator();
		Variable variable = null;
		boolean variableTrouvee = false;
		
		//on met le contenu du tableau dans une collection (arrayList)
		//pour pouvoir utiliser la fonction containsAll...
		ArrayList<Integer> listeVal = new ArrayList<Integer>();
		/*for (int j=0; j<val.length; j++){
			listeVal.add(val[j]);
		}*/
		
		Collections.addAll(listeVal, val);
		
		while (i.hasNext() && !variableTrouvee){
			variable = ((Variable)i.next());
			if (variable.getName().equals(variableName)){
				variableTrouvee = true;
			}
		}
		if (variableTrouvee){
			return variable.getDomain().containsAll(listeVal);
		}
		else
			return false;
		
	}
	
	/**
	 * @param val
	 * @param variableName
	 * @return
	 * retourne true si toutes les valeurs de la liste (un tableau d'entier)
	 * n' appartiennent pas au domaine de la variable 
	 * et que la variable existe
	 * retourne faux dans tous les autres cas
	 * 
	 * sert à tester une assert true
	 */
	public boolean ListeValeurHorsDuDomaineDe(Integer[] val, String variableName){
		Iterator<Variable> i = listeVariables.iterator();
		Variable variable = null;
		boolean variableTrouvee = false;
		boolean toutesVariablesHorsDomaine = true;
		int k=0;
		
		while (i.hasNext() && !variableTrouvee){
			variable = ((Variable)i.next());
			if (variable.getName().equals(variableName)){
				variableTrouvee = true;
			}
		}
		if (variableTrouvee){
			while ( k < val.length && toutesVariablesHorsDomaine ){
				if (variable.getDomain().contains(val[k])){
					toutesVariablesHorsDomaine = false;
				}
				k++;
			}
			return toutesVariablesHorsDomaine;
		}
		else
			return false;
		// si la variable n'existe pas il vaudrait mieux retourner une exception
	}
	
	public String toString(){// en fait pas de modif visuelle du model
		return listeVariables.toString();
	}

	public static void main (String [] args){
		String content="DOMAINS: X : [0,...,10], Y : [0,...,6], Z : [5,...,10]; CONSTRAINTS: X = Y, X<>3, X<>Z;";
		AC6Fitnesse fitnesseTestAc6 = new AC6Fitnesse(content);
		System.out.println(fitnesseTestAc6.model);
	}
	
	
}
