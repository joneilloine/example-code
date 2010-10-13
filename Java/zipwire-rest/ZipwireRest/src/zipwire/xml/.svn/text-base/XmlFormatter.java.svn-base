package zipwire.xml;

import java.util.Iterator;
import java.util.Map;

public abstract class XmlFormatter {
	protected Xml xml;
	protected XmlTags tags() {
		return new XmlTags();
	}
	public XmlFormatter(Xml xml) {
		this.xml = xml;
	}
	public String toString(){
		if (xml.isElement()) return formatElementAsString();
		if (xml.isText()) return formatTextAsString();
		if (xml.isDocument()) return formatDocumentAsString();
		return "";	
	}
	protected String asString(Xml[] xmls) {
		String text = "";
		for (Xml xml : xmls) text += xml.asString(getClass());
		return text;
	}
    protected String asString(Map<String, String> attributes) {
    	String text = "";
    	for (Iterator<String> i = attributes.keySet().iterator(); i.hasNext(); ){
    		String key = i.next();
    		String value = attributes.get(key);
    		text += formatAttributeAsString(key, value);
    	}
		return text;
	}
	protected abstract String formatDocumentAsString();
	protected abstract String formatTextAsString();
	protected abstract String formatElementAsString();
	protected abstract String formatAttributeAsString(String key, String value);
}