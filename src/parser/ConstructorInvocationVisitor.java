package parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
public class ConstructorInvocationVisitor extends ASTVisitor{


	List<ConstructorInvocation> cons = new ArrayList<ConstructorInvocation>();


	public boolean visit(ConstructorInvocation node) {
		cons.add(node);
		return super.visit(node);
	}


	public List<ConstructorInvocation> getCons() {
		return cons;
	}


}


