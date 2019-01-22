package parser;

public class Attribut {

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInitialiazer() {
		return initialiazer;
	}
	public void setInitialiazer(String initialiazer) {
		this.initialiazer = initialiazer;
	}

	public Attribut() {
		super();
	}

	@Override
	public String toString() {
		return String.format("[name=%s, initialiazer=%s]", name, initialiazer);
	}

	public  String initialiazer;
	public  String name;


}
