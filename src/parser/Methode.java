package parser;

import java.util.HashMap;
import java.util.Map;

public class Methode {

	public String name;
 	@Override
	public String toString() {
		return "[name=" + name + ", method_Invocation=" + method_Invocation + ", returnType=" + returnType+"]";
	}

	public Map<String,Integer> method_Invocation = new HashMap<>();
	public String returnType;

	public Methode() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Integer> getMethod_Invocation() {
		return method_Invocation;
	}

	public void setMethod_Invocation(Map<String, Integer> method_Invocation) {
		this.method_Invocation = method_Invocation;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	

}
