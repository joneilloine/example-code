package zipwire.xml;

import java.lang.reflect.*;

public class XmlInvocationHandler implements InvocationHandler {
	private XmlClump clump;
	public XmlInvocationHandler(XmlClump clump) {
		this.clump = clump;
	}
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
		  if ("toString".equals(method.getName())) return clump.toString();
		  if ("getChildText".equals(method.getName())) return clump.getText();
		  else return clump.get(toKeyName(method.getName()), method.getReturnType());
		}
		catch(Exception e){
			throw new RuntimeException(e.getMessage() + clump.toString());
		}
	}
	private String toKeyName(String methodName) {
		return clump.namingRule().transform(methodName.replace("get", ""));
	}
}
