package parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;



public class Parser  {

	public static final String projectPath = "\\\\WILL-HP\\Users\\Will\\eclipse-workspace\\reponseXml\\ProjectToParse";
	public static final String projectSourcePath = projectPath + "\\src";
	public static final String jrePath = "C:\\Program Files (x86)\\Java\\jdk1.8.0_192\\jre";

	public static ArrayList<Classe> ClassesList = new ArrayList<Classe>();
	public static ArrayList<String> SrcPackage = new ArrayList<String>();



	public static void main(String[] args) throws IOException {

		//CtClass<?> l = Launcher.parseClass("class A { void m() { System.out.println(\"yeah\");} }");
        
		// read java files
		System.out.println("Analyse en cours.");

		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);


		for (File fileEntry : javaFiles) {
			Classe newClasse = new Classe();

			String className = fileEntry.getName();
			className = className.replace(".java", "");
			newClasse.setName(className);
			
			String Package = fileEntry.getPath();
			Package = Package.replace(folder.getAbsolutePath()+"\\","");
			Package = Package.replace("\\"+fileEntry.getName(),"");
			newClasse.setPackage(Package);
			if(!SrcPackage.contains(Package)) {
				SrcPackage.add(Package);
			}

			String content = FileUtils.readFileToString(fileEntry);
			CompilationUnit parse = parse(content.toCharArray());
			newClasse.setDefinition(parse.toString());
			newClasse.setNombreDeLignes(newClasse.getNombreDeLignes(parse.toString()));
			newClasse.setMethodes(MethodPairing(parse));
			newClasse.setClasse_Pairing(ClassPairing(parse));
			newClasse.setAttributes(printVariables (parse));
			newClasse.setFields(fieldacces(parse));
			//System.out.println(newClasse.toString());
			
			ClassesList.add(newClasse); 
		}


		//PrintTPInfo();
		GlobalCouplageAnalyse();
		LogWriter(ClassesList);
		javaToDotMethodes(ClassesList);
		javaToDotClasses(ClassesList);
		javaToDotClassesPairing(ClassesList);
		
		System.out.println("Analyse terminé avec succes,veuillez consulter le fichier AnalyseOutput.txt !");

	}

	public static void PrintTPInfo() {
		System.out.println("Nombre de classes : " + ClassesList.size());

		System.out.println("Nombre de Packages: " + SrcPackage.size());
		System.out.println(SrcPackage.toString() + "\n");

		System.out.println("Nombre de lignes: " + getnbLines());

		System.out.println("Nombre de methodes: " + getnbMethods());

		float nbClassMethodAvg = (float)getnbMethods() / (float)ClassesList.size();
		System.out.println("Nombre moyen de methodes par classe : " + nbClassMethodAvg );

		float nbLineMethodAvg = (float)getnbLines() / (float)getnbMethods();
		System.out.println("Nombre moyen de methodes par classe : " + nbLineMethodAvg );

		float nbAtributesClassAvg = (float)getnbAttributs() / (float)ClassesList.size();
		System.out.println("Nombre moyen d'atributs par classe : " + nbAtributesClassAvg + " avec " + getnbAttributs() + " atributes " );
		System.out.println("Nombre maximal de paremetre : " + getMaxParametre());


		System.out.println("10 percent des classes ayant + methodes : \n" + getMethodPercent(50) + "\n");
		System.out.println("10 percent des classes ayant + Attributs : \n" + getPercentAttribut(50) + "\n");
		System.out.println("10 percent des classes ayant + methodes & Attributs : \n" + getpercentAttributMethod(50) + "\n" );
		System.out.println("Classes ayant + de X methodes : " + getLimitMethod(50) + "\n");
		System.out.println("10 percent Classes ayant + de lignes de code : \n" + getpercentCodeLine(50) + "\n");


	}

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		parser.setBindingsRecovery(true);

		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);

		parser.setUnitName("");

		String[] sources = { projectSourcePath }; 
		String[] classpath = {jrePath};

		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(classSource);

		return (CompilationUnit) parser.createAST(null); // create and parse
	}


	// navigate variable information
	public static ArrayList<SimpleName> printVariables (CompilationUnit parse) {

		ArrayList<SimpleName> attributes = new ArrayList<SimpleName>();
		VariableDeclarationFragmentVisitor visitor = new VariableDeclarationFragmentVisitor();
		parse.accept(visitor);

		for (VariableDeclarationFragment variable : visitor.getVariables()) {
			if(!attributes.contains(variable.getName()))
				attributes.add(variable.getName());

		}
		return attributes;
	}

	public static ArrayList<SimpleName> fieldacces (CompilationUnit parse) {
		ArrayList<SimpleName> fields = new ArrayList<SimpleName>();
		FieldAccessVisitor visitor = new FieldAccessVisitor ();
		parse.accept(visitor);
		for (SimpleName  variable : visitor.getFields()) {
			fields.add(variable);
		}

		return fields;
	}

	// navigate typeDeclaration information
	public static void printTypeDeclaration (CompilationUnit parse) {

		TypeDeclarationVisitor visitor = new TypeDeclarationVisitor();
		parse.accept(visitor);

		for (TypeDeclaration Types : visitor.getTypes()) {
		}
	}

	

	public static HashMap<String,Integer> ClassPairing(CompilationUnit parse) {
		
		HashMap<String,Integer> CP = new HashMap<String,Integer>();
		VariableDeclarationFragmentVisitor visitor = new VariableDeclarationFragmentVisitor();
		parse.accept(visitor);

		for (VariableDeclarationFragment variable : visitor.getVariables()) {
			if(variable.getInitializer() != null) {
				String dependance = variable.getInitializer().toString();
				
				dependance = dependance.replaceAll("(\\((.*?)\\))", "");
				dependance = dependance.replace("new","");
				dependance = dependance.replace(" ","");

				if(CP.containsKey(dependance)) {
					int incr = CP.get(dependance) + 1;
					CP.put(dependance, incr );
				} else {
					CP.put(dependance, 1 );
				}
			}
		}

		MethodDeclarationVisitor visitor2 = new MethodDeclarationVisitor(); 
		parse.accept(visitor2);

		for (MethodDeclaration method : visitor2.getMethods()) {


			MethodInvocationVisitor visitor3 = new MethodInvocationVisitor(); 
			method.accept(visitor3);
			for (MethodInvocation methodInvocation : visitor3.getMethods()) {

				ITypeBinding typeBinding = methodInvocation.getExpression().resolveTypeBinding();

				if(CP.containsKey(typeBinding.getName())) {

					int incr = CP.get(typeBinding.getName()) + 1;
					CP.put(typeBinding.getName(), incr );
				} else {
					CP.put(typeBinding.getName(), 1 );
				}
			}


		}


		return CP;
	}
	
	

	public static ArrayList<Methode> MethodPairing(CompilationUnit parse) {

		ArrayList<Methode> Methodes = new ArrayList<Methode>();
		MethodDeclarationVisitor visitor = new MethodDeclarationVisitor(); 
		parse.accept(visitor);

		for (MethodDeclaration method : visitor.getMethods()) {

			Methode newMethod = new Methode(); 
			newMethod.setName(method.getName().toString());
			MethodInvocationVisitor visitor3 = new MethodInvocationVisitor(); method.accept(visitor3);

			for (MethodInvocation methodInvocation : visitor3.getMethods()) {

				if(newMethod.method_Invocation.containsKey(methodInvocation.getName().toString())) {

					int incr = newMethod.method_Invocation.get(methodInvocation.getName().toString()) + 1;
					newMethod.method_Invocation.put(methodInvocation.getName().toString(), incr );

				} else {

					newMethod.method_Invocation.put(methodInvocation.getName().toString(), 1);

				}
			}
			Methodes.add(newMethod);
		}
		

		return Methodes;
	}

	
	public Classe getClasseFromString(String name) {
		
		for (Classe c : ClassesList) {
			if(c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}
	
	public float getCouplage(String a, String b) {
     
		Classe classeA = getClasseFromString(a);
		Classe classeB = getClasseFromString(b);
		float metrique = -1;
		if((classeA != null) && (classeB != null)) {
			 metrique = classeA.getClasse_Pairing().get(classeB.getName()) + classeB.getClasse_Pairing().get(classeA.getName());
			metrique = metrique / getGlobalRelation();
		}

		return metrique;
	}
	
	
	public static void GlobalCouplageAnalyse() {
		
		float globalRelation;

		for (Classe c : ClassesList) {
			
			globalRelation = 0;

			for (Classe d : ClassesList) {

				if(!d.equals(c)) {

					if( c.getClasse_Pairing().keySet().contains(d.getName()) ) {

						globalRelation += c.getClasse_Pairing().get(d.getName());

					}
					
					if(d.getClasse_Pairing().keySet().contains(c.getName())) {
						
						globalRelation +=  d.getClasse_Pairing().get(c.getName());
					}
					
					globalRelation = globalRelation / getGlobalRelation();

					c.getGlobal_Classe_Pairing().put(d.getName(), globalRelation); 

				}


			}			


		}

	}
	
	
	public static float getGlobalRelation() {
		float globalRelation = 0;
		for (Classe c : ClassesList) {
			for(String key : c.getClasse_Pairing().keySet()) {
				globalRelation += c.getClasse_Pairing().get(key);
			}
		}

		return globalRelation;
	}
	
	
	public static float getnbLines() {
		float lines = 0;
		for (Classe c : ClassesList) {
			lines += c.getNombreDeLignes();
		}
		return lines;
	}

	public static float getnbMethods() {
		float methodes = 0;
		for (Classe c : ClassesList) {
			methodes += c.methodes.size();
		}
		return methodes;
	}

	public static float getnbAttributs() {

		float Attributs = 0;
		for (Classe c : ClassesList) {
			Attributs += c.attributes.size();
		}
		return Attributs;
	}

	public static ArrayList<Classe> getMethodPercent(int percent) {

		Collections.sort(ClassesList, new Comparator<Classe>() {
			@Override
			public int compare(Classe o1, Classe o2) {
				return  o2.getNbMethodes() - o1.getNbMethodes();
			}
		});

		ArrayList<Classe> list = new ArrayList<Classe> ();
		int limitClass = (ClassesList.size()*percent)/100 ;	
		for (int i = 0; i < limitClass; i++) {
			list.add(ClassesList.get(i));
		}	
		return list;	
	}

	public static ArrayList<Classe> getPercentAttribut(int percent) {

		Collections.sort(ClassesList, new Comparator<Classe>() {
			@Override
			public int compare(Classe o1, Classe o2) {
				return  o2.getAttributes().size() - o1.getAttributes().size();
			}
		});

		ArrayList<Classe> list = new ArrayList<Classe> ();
		int limitClass = (ClassesList.size()*percent)/100 ;	
		for (int i = 0; i < limitClass; i++) {
			list.add(ClassesList.get(i));
		}	
		return list;	
	}

	public static ArrayList<Classe> getpercentAttributMethod(int percent) {

		Collections.sort(ClassesList, new Comparator<Classe>() {
			@Override
			public int compare(Classe o1, Classe o2) {
				return  (o2.getAttributes().size() + o2.getMethodes().size()) - (o1.getAttributes().size() + o1.getMethodes().size());
			}
		});

		ArrayList<Classe> list = new ArrayList<Classe> ();
		int limitClass = (ClassesList.size()*percent)/100 ;	
		for (int i = 0; i < limitClass; i++) {
			list.add(ClassesList.get(i));
		}	
		return list;	
	}

	public static ArrayList<Classe> getLimitMethod(int limit) {

		ArrayList<Classe> list = new ArrayList<Classe> ();
		for (Classe c : ClassesList) {
			if(c.getMethodes().size() >= limit) {
				list.add(c);
			}
		}

		return list;	
	}

	public static ArrayList<Classe> getpercentCodeLine(int percent) {

		Collections.sort(ClassesList, new Comparator<Classe>() {
			@Override
			public int compare(Classe o1, Classe o2) {
				return  o2.getNombreDeLignes() - o1.getNombreDeLignes();
			}
		});

		ArrayList<Classe> list = new ArrayList<Classe> ();
		int limitClass = (ClassesList.size()*percent)/100 ;	
		for (int i = 0; i < limitClass; i++) {
			list.add(ClassesList.get(i));
		}	
		return list;	
	}

	public static int getMaxParametre() {

		Collections.sort(ClassesList, new Comparator<Classe>() {
			@Override
			public int compare(Classe o1, Classe o2) {
				return  o2.getFields().size() - o1.getFields().size();
			}
		});


		return ClassesList.get(0).getFields().size();	
	}

	public static void javaToDotMethodes(ArrayList<Classe> ClassList) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter("DOTFileMethodPairing.gv"));
		writer.write("graph out { ");
		for (Classe c : ClassList) {
			ArrayList<Methode> Methodes = c.getMethodes();
			for (Methode m : Methodes) {

				Set<?> cles = m.getMethod_Invocation().keySet();
				Iterator<?> it = cles.iterator();
				while (it.hasNext()){
					String line = m.getName() + " -- ";
					String cle = (String)it.next(); 
					int valeur = m.getMethod_Invocation().get(cle); 
					line += cle + " [label=" + valeur + "];\n";
					line = line.replace("(", "_");
					line = line.replace(")", "_");
					line = line.replace(".", "_");
					writer.write(line);
				}

			}
		}
		writer.write("}");
		writer.close();

	}

	public static void javaToDotClasses(ArrayList<Classe> ClassList) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter("DOTFileCallPairing.gv"));
		writer.write("graph out { ");
		for (Classe c : ClassList) {
			Set<?> keyClass = c.getClasse_Pairing().keySet();
			Iterator<?> itClass = keyClass.iterator();
			while (itClass.hasNext()){
					String line = c.getName() + " -- ";
					String cle = (String)itClass.next(); 
					int valeur = c.getClasse_Pairing().get(cle); 
					line += cle + " [label=" + valeur + "];\n"; 
					line = line.replace("(", "_");
					line = line.replace(")", "_");
					line = line.replace(".", "_");
					writer.write(line);
				}

			}

	writer.write("}");
	writer.close();

}
	
	
	public static void javaToDotClassesPairing(ArrayList<Classe> ClassList) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter("DOTFileClassPairing.gv"));
		writer.write("graph PairingOut { ");
		for (Classe c : ClassList) {
			Set<?> keyClass = c.getGlobal_Classe_Pairing().keySet();
			Iterator<?> itClass = keyClass.iterator();
			while (itClass.hasNext()){
					String cle = (String)itClass.next();
					String line = c.getName() + " -- ";
					line += cle;
					float valeur = c.getGlobal_Classe_Pairing().get(cle); 
					line += " [label=" + valeur + "];\n"; 
					writer.write(line);
				}

			}

	writer.write("}");
	writer.close();

}
	
	public static void LogWriter(ArrayList<Classe> ClassList) throws IOException {

		Date date = new Date();
		BufferedWriter writer = new BufferedWriter(new FileWriter("AnalyseOutput.txt"));
		writer.write("*************** Questions TP ***************\n\n");
		writer.write("-Nombre de classes : " + ClassesList.size() + "\n");
		writer.write("-Nombre de Packages: " + SrcPackage.size()+ "\n");
		writer.write("-Nombre de lignes: " + getnbLines()+ "\n");
		writer.write("-Nombre de methodes: " + getnbMethods()+ "\n");
		
		float nbClassMethodAvg = (float)getnbMethods() / (float)ClassesList.size();
		writer.write("-Nombre moyen de methodes par classe : " + nbClassMethodAvg+ "\n");
		
		float nbLineMethodAvg = (float)getnbLines() / (float)getnbMethods();
		writer.write("-Nombre moyen de methodes par classe : " + nbLineMethodAvg+ "\n");


		float nbAtributesClassAvg = (float)getnbAttributs() / (float)ClassesList.size();
		writer.write("-Nombre moyen d'attributs par classe : " + nbAtributesClassAvg + " sur " + getnbAttributs() + "attributs."+ "\n");

		writer.write("-Nombre total de parametres : " + getMaxParametre()+ "\n");
		writer.write("-10% des classes ayant le plus de methodes : " + getMethodPercent(10)+ "\n");
		writer.write("-10% des classes ayant le plus d'attributs : " + getPercentAttribut(10)+ "\n");
		writer.write("-10% des classes ayant le plus de methodes et d'attributs : " + getpercentAttributMethod(10)+ "\n");
		writer.write("-10% des classes ayant le plus de ligne de code : " + getpercentCodeLine(10)+ "\n");
		int value = 50;
		writer.write("-Classes ayant le plus de " + value + " methodes : " + getLimitMethod(value) + "\n");

	writer.write("*************** Fin Questions ***************\n\n");
	
	writer.write("*************** informations ***************\n\n");
	writer.write(ClassList.toString());
	writer.write("*************** Fin informations ***************\n\n");

	writer.close();

}

}
