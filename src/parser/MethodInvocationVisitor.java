package parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

public class MethodInvocationVisitor extends ASTVisitor {
	List<MethodInvocation> methods = new ArrayList<MethodInvocation>();
	List<ConstructorInvocation> cons = new ArrayList<ConstructorInvocation>();

	List<SuperMethodInvocation> superMethods = new ArrayList<SuperMethodInvocation>();
	
	public boolean visit(MethodInvocation node) {
		methods.add(node);
		return super.visit(node);
	}
	
	
	public boolean visit(ConstructorInvocation node) {
		cons.add(node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node) {
		superMethods.add(node);
		return super.visit(node);
	}

	
	public List<MethodInvocation> getMethods() {
		return methods;
	}
	
	public List<ConstructorInvocation> getCons() {
		return cons;
	}
	
	public List<SuperMethodInvocation> getSuperMethod() {
		return superMethods;
	}
}
