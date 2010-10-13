package zipwire.xml;

import java.util.*;

import zipwire.core.*;

public class XmlClump {
	private Xml xml;
	private XmlNamingRule xmlNamingRule;
	public XmlClump(Xml xml, XmlNamingRule xmlNamingRule) {
		this.xml = xml;
		this.xmlNamingRule = xmlNamingRule;
	}
	public Object get(String name, Class<?> returnType) {
		Xml[] children = xml.children(name);
		Xml child = children[0];
		if (child.firstChild().isText()) return child.getText();
		else return new XmlClump(child, this.namingRule());
	}
	public <T>T as(Class<T> interfaceToUse) {
		return (T)new ProxyBuilder(new XmlInvocationHandler(this), interfaceToUse).build();
	}
	public XmlNamingRule namingRule() {
		return xmlNamingRule;
	}
	public String toString(){
		return xml.asString();
	}
	public String getText(){
		return xml.getText();
	}
	public List<XmlClump> asList() {
		  List results = new ArrayList();
		  Xml[] children = xml.children();
		  for (Xml child : children)
			results.add(new XmlClump(child, this.namingRule()));
		  return results;
	}
	public List asList(Class interfaceToUse, String elementName) {
		Xml [] xmls = xml.children(elementName);
		List list = new ArrayList();
		for (Xml xml : xmls)
			list.add(new XmlClump(xml, xmlNamingRule).as(interfaceToUse));
		return list;
	}
}