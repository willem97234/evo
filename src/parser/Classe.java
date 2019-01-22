package parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

public class Classe {
	

	public  String Package;
	public  ArrayList<SimpleName> attributes;	
	public  ArrayList<Methode> methodes;	
	public  ArrayList<SimpleName> fields;
	public int nbMethodes;
	public String definition;
	public String name;
	public HashMap<String,Integer> Classe_Pairing;
	public HashMap<String,Float> Global_Classe_Pairing;
	
	public HashMap<String, Float> getGlobal_Classe_Pairing() {
		return Global_Classe_Pairing;
	}

	public void setGlobal_Classe_Pairing(HashMap<String, Float> global_Classe_Pairing) {
		Global_Classe_Pairing = global_Classe_Pairing;
	}

	public HashMap<String,Float> Local_Metric;

	public int nombreDeLignes;
	public int nombreAppelTotal;

	public int getNombreAppelTotal() {
		return nombreAppelTotal;
	}
	
	public void Metric() {
		for(String key : Classe_Pairing.keySet()) {
			float metric = (float)Classe_Pairing.get(key)/(float)getNombreAppelTotal();
			Local_Metric.put(key,metric);
		}
	}

	public void setNombreAppelTotal() {
		Integer nombreAppelTotal = 0;
		for (Integer i : Classe_Pairing.values()) {
			nombreAppelTotal += i;
		}
		this.nombreAppelTotal = nombreAppelTotal;
	}

	public Map<String, Integer> getClasse_Pairing() {
		return Classe_Pairing;
	}

	public void setClasse_Pairing(HashMap<String, Integer> classe_Pairing) {
		Classe_Pairing = classe_Pairing;
		setNombreAppelTotal();
		Metric();
	}
	
	
	public Classe() {
		super();
		this.nombreDeLignes = 0;
		this.nbMethodes = 0;
		this.attributes = new ArrayList<SimpleName>() ;	
		this.methodes = new ArrayList<Methode>();	
		this.fields = new ArrayList<SimpleName>();
		this.Classe_Pairing  = new HashMap<>();
		this.Local_Metric  = new HashMap<>();
		this.Global_Classe_Pairing  = new HashMap<>();


	}
	
	public int getNbMethodes() {
		return nbMethodes;
	}

	public void setNbMethodes(int nbMethodes) {
		this.nbMethodes = nbMethodes;
	}
		
	public void AddMethodes(Methode methodes) {
		this.methodes.add(methodes);
		nbMethodes++;
	}
	
	public void setMethodes(ArrayList<Methode> methodes) {
		this.methodes = methodes;
		this.nbMethodes = this.methodes.size();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackage() {
		return Package;
	}
	public void setPackage(String package1) {
		Package = package1;
	}
	public ArrayList<SimpleName> getAttributes() {
		return attributes;
	}
	public void setAttributes(ArrayList<SimpleName> attributes) {
		this.attributes = attributes;
	}
	
	public int getNombreDeLignes() {
		return nombreDeLignes;
	}
	
	public int getNombreDeLignes(String parse) {
		String[] Tokens = parse.split("\n");
		nombreDeLignes += Tokens.length;
		return nombreDeLignes;
	}
	public void setNombreDeLignes(int nbLines) {		
		this.nombreDeLignes = nbLines;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public ArrayList<SimpleName> getFields() {
		return fields;
	}
	public void setFields(ArrayList<SimpleName> fields) {
		this.fields = fields;
	}
	
	
	public HashMap<String, Float> getLocal_Metric() {
		return Local_Metric;
	}

	public void setLocal_Metric(HashMap<String, Float> local_Metric) {
		Local_Metric = local_Metric;
	}

	public ArrayList<Methode> getMethodes() {
		return methodes;
	}

	public void setNombreAppelTotal(int nombreAppelTotal) {
		this.nombreAppelTotal = nombreAppelTotal;
	}

	@Override
	public String toString() {
		return String.format(
				"Classe [Package=%s,\n attributes=%s,\n methodes=%s,\n fields=%s,\n nbMethodes=%s,\n name=%s,\n Classe_Pairing=%s,\n Global_Classe_Pairing=%s,\n Local_Metric=%s,\n nombreDeLignes=%s,\n nombreAppelTotal=%s\n]\n\n\n",
				Package, attributes, methodes, fields, nbMethodes, name, Classe_Pairing, Global_Classe_Pairing,
				Local_Metric, nombreDeLignes, nombreAppelTotal);
	}

	
	
	/*@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		if (Package != null)
			builder.append("\n\nPackage", Package);
		if (attributes != null)
			builder.append("\nattributes", attributes);
		if (methodes != null)
			builder.append("\nmethodes", methodes);
		if (fields != null)
			builder.append("\nfields", fields);
		builder.append("\nnbMethodes", nbMethodes);
		if (name != null)
			builder.append("\nname", name);
		if (Classe_Pairing != null)
			builder.append("\nClasse_Pairing", Classe_Pairing);
		if (Local_Metric != null)
			builder.append("\nLocal_Metric", Local_Metric);
		builder.append("\nnombreDeLignes", nombreDeLignes);
		builder.append("\nnombreAppelTotal", nombreAppelTotal);
		return builder.toString();
	}*/


	
	
	

}
