package zipwire.xml;

import org.w3c.dom.DOMException;

public class DefaultXmlFormatter extends XmlFormatter {
	public DefaultXmlFormatter(Xml xml) {
		super(xml);
	}
	protected String formatDocumentAsString() {
		return tags().xmlVersion() + asString(xml.children());
	}
	protected String formatElementAsString() {
		if (!xml.hasChildren() && !xml.hasText())
			return tags().elementOpenTagStart() + xml.getName() + asString(xml.getAttributes()) + tags().tagCloseElementClose();
		else
			return tags().elementOpenTagStart() + xml.getName() + asString(xml.getAttributes()) + tags().tagClose() + 
					asString(xml.children()) + 
					tags().elementCloseTagStart() + xml.getName() + tags().tagClose();
	}
	protected String formatTextAsString() throws DOMException {
		return xml.node.getNodeValue();
	}
	protected String formatAttributeAsString(String key, String value) {
		return " " + key + tags().attributeStart() + value + tags().attributeEnd();
	}
}