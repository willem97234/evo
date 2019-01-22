package parser;


public class Classe {
    public java.lang.String Package;

    public java.util.ArrayList<org.eclipse.jdt.core.dom.SimpleName> attributes;

    public java.util.ArrayList<parser.Methode> methodes;

    public java.util.ArrayList<org.eclipse.jdt.core.dom.SimpleName> fields;

    public int nbMethodes;

    public java.lang.String definition;

    public java.lang.String name;

    public java.util.HashMap<java.lang.String, java.lang.Integer> Classe_Pairing;

    public java.util.HashMap<java.lang.String, java.lang.Float> Global_Classe_Pairing;

    public java.util.HashMap<java.lang.String, java.lang.Float> getGlobal_Classe_Pairing() {
        return Global_Classe_Pairing;
    }

    public void setGlobal_Classe_Pairing(java.util.HashMap<java.lang.String, java.lang.Float> global_Classe_Pairing) {
        Global_Classe_Pairing = global_Classe_Pairing;
    }

    public java.util.HashMap<java.lang.String, java.lang.Float> Local_Metric;

    public int nombreDeLignes;

    public int nombreAppelTotal;

    public int getNombreAppelTotal() {
        return nombreAppelTotal;
    }

    public void Metric() {
        for (java.lang.String key : Classe_Pairing.keySet()) {
            float metric = ((float) (Classe_Pairing.get(key))) / ((float) (getNombreAppelTotal()));
            Local_Metric.put(key, metric);
        }
    }

    public void setNombreAppelTotal() {
        java.lang.Integer nombreAppelTotal = 0;
        for (java.lang.Integer i : Classe_Pairing.values()) {
            nombreAppelTotal += i;
        }
        this.nombreAppelTotal = nombreAppelTotal;
    }

    public java.util.Map<java.lang.String, java.lang.Integer> getClasse_Pairing() {
        return Classe_Pairing;
    }

    public void setClasse_Pairing(java.util.HashMap<java.lang.String, java.lang.Integer> classe_Pairing) {
        Classe_Pairing = classe_Pairing;
        setNombreAppelTotal();
        Metric();
    }

    public Classe() {
        super();
        this.nombreDeLignes = 0;
        this.nbMethodes = 0;
        this.attributes = new java.util.ArrayList<org.eclipse.jdt.core.dom.SimpleName>();
        this.methodes = new java.util.ArrayList<parser.Methode>();
        this.fields = new java.util.ArrayList<org.eclipse.jdt.core.dom.SimpleName>();
        this.Classe_Pairing = new java.util.HashMap<>();
        this.Local_Metric = new java.util.HashMap<>();
        this.Global_Classe_Pairing = new java.util.HashMap<>();
    }

    public int getNbMethodes() {
        return nbMethodes;
    }

    public void setNbMethodes(int nbMethodes) {
        this.nbMethodes = nbMethodes;
    }

    public void AddMethodes(parser.Methode methodes) {
        this.methodes.add(methodes);
        (nbMethodes)++;
    }

    public void setMethodes(java.util.ArrayList<parser.Methode> methodes) {
        this.methodes = methodes;
        this.nbMethodes = this.methodes.size();
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getPackage() {
        return Package;
    }

    public void setPackage(java.lang.String package1) {
        Package = package1;
    }

    public java.util.ArrayList<org.eclipse.jdt.core.dom.SimpleName> getAttributes() {
        return attributes;
    }

    public void setAttributes(java.util.ArrayList<org.eclipse.jdt.core.dom.SimpleName> attributes) {
        this.attributes = attributes;
    }

    public int getNombreDeLignes() {
        return nombreDeLignes;
    }

    public int getNombreDeLignes(java.lang.String parse) {
        java.lang.String[] Tokens = parse.split("\n");
        nombreDeLignes += Tokens.length;
        return nombreDeLignes;
    }

    public void setNombreDeLignes(int nbLines) {
        this.nombreDeLignes = nbLines;
    }

    public java.lang.String getDefinition() {
        return definition;
    }

    public void setDefinition(java.lang.String definition) {
        this.definition = definition;
    }

    public java.util.ArrayList<org.eclipse.jdt.core.dom.SimpleName> getFields() {
        return fields;
    }

    public void setFields(java.util.ArrayList<org.eclipse.jdt.core.dom.SimpleName> fields) {
        this.fields = fields;
    }

    public java.util.HashMap<java.lang.String, java.lang.Float> getLocal_Metric() {
        return Local_Metric;
    }

    public void setLocal_Metric(java.util.HashMap<java.lang.String, java.lang.Float> local_Metric) {
        Local_Metric = local_Metric;
    }

    public java.util.ArrayList<parser.Methode> getMethodes() {
        return methodes;
    }

    public void setNombreAppelTotal(int nombreAppelTotal) {
        this.nombreAppelTotal = nombreAppelTotal;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("Classe [Package=%s,\n attributes=%s,\n methodes=%s,\n fields=%s,\n nbMethodes=%s,\n name=%s,\n Classe_Pairing=%s,\n Global_Classe_Pairing=%s,\n Local_Metric=%s,\n nombreDeLignes=%s,\n nombreAppelTotal=%s\n]\n\n\n", Package, attributes, methodes, fields, nbMethodes, name, Classe_Pairing, Global_Classe_Pairing, Local_Metric, nombreDeLignes, nombreAppelTotal);
    }
}

