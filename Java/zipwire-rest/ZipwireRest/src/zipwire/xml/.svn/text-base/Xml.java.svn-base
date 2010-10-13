package zipwire.xml;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.lang.reflect.Constructor;

import zipwire.exception.ZipwireException;

public class Xml {
	private int depth = 0;
	protected Node node;
	public Xml(int depth, Node n) {
		this.depth = depth;
		node = n;
	}
	public Xml(String xmlString){
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			node = builder.parse(asStream(stripSpaceTags(xmlString)));}
		catch(Exception e){throw new RuntimeException(e.getMessage() + "Couldn't parse:" + xmlString);}
	}
	private String stripSpaceTags(String xmlString) {
		return xmlString.replaceAll("\n", "").replaceAll("\r", "").replaceAll("> *", ">");
	}
	public int depth(){
		return depth;
	}
	private InputStream asStream(String s) {
		return new ByteArrayInputStream(s.getBytes());
	}
	public Xml firstChild() {
		return new Xml(depth+1, node.getFirstChild());
	}
	public String getName() {
		return node.getNodeName();
	}
	public String getAttribute(String attributeName) {
		try {return node.getAttributes().getNamedItem(attributeName).getNodeValue();}
		catch(Exception e){return null;}
	}
	public Map<String, String> getAttributes() {
		NamedNodeMap aNodes = node.getAttributes();
		Map<String, String> attributes = new TreeMap<String, String>();
		if (aNodes == null) return attributes;
		for (int i=0; i<aNodes.getLength(); i++) 
			attributes.put(aNodes.item(i).getNodeName(), aNodes.item(i).getNodeValue());
		return attributes;
	}
	public Xml[] children() {
		return asXml(node.getChildNodes());
	}
	private Xml[] asXml(NodeList children) {
		ArrayList<Xml> xmls = new ArrayList<Xml>();
		for (int i=0; i<children.getLength(); i++){ 
			Xml xml = new Xml(depth+1, children.item(i));
			if (xml.isElement() || xml.isText()) xmls.add(xml);
		}
		return xmls.toArray(new Xml[xmls.size()]);
	}
	public boolean isText() {
		return node.getNodeType() == Node.TEXT_NODE;
	}
	public Xml[] children(String name) {
		if (isElement()) return asXml(((Element)node).getElementsByTagName(name));
		if (isDocument()) return asXml (((Document)node).getElementsByTagName(name));
		return new Xml[0];
	}
	public boolean isDocument() {
		return node.getNodeType() == Node.DOCUMENT_NODE;
	}
	public boolean isElement() {
		return node.getNodeType() == Node.ELEMENT_NODE;
	}
	public boolean equals(Object o){
		return o != null && o instanceof Xml && node.equals(((Xml)o).node); 
	}
	public boolean hasAttributes() {
		return getAttributes().size() > 0;
	}
	public boolean hasChildren() {
		return children().length > 0 && hasElement(children());
	}
	private boolean hasElement(Xml[] xmls) {
		for (Xml xml : xmls) if (xml.isElement()) return true;
		return false;
	}
	public String getText() {
		try {return children()[0].node.getNodeValue();}
		catch(Exception e){return null;}
	}
	public boolean hasText() {
		return 
		getText() != null && 
		!((getText().replace("\n", "").replace("\t", "").trim()).equals(""));
	}
	private XmlFormatter createFormatterInstance(Class<? extends XmlFormatter> formatter){
		try {
			Constructor<? extends XmlFormatter> sole = 
				(Constructor<? extends XmlFormatter>)formatter.getConstructors()[0];
			return sole.newInstance(new Object[]{this});
		}
		catch(Exception e){
			throw new ZipwireException("Couldn't instantiate formatter", e);
		}
	}
	public String asString() {
		return asString(DefaultXmlFormatter.class);
	}
	public String asString(Class<? extends XmlFormatter> formatter) {
		return createFormatterInstance(formatter).toString();
	}
}