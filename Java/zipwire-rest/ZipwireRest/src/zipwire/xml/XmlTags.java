package zipwire.xml;

public class XmlTags {
	public String xmlVersion() {
		return "<?xml version=\"1.0\"?>";
	}
	public String elementCloseTagStart(){
		return "</";
	}
	public String elementOpenTagStart() {
		return "<";
	}
	public String tagClose() {
		return ">";
	}
	public String tagCloseElementClose() {
		return "/>";
	}
	public String attributeStart() {
		return "=\"";
	}
	public String attributeEnd() {
		return "\"";
	}
	public String textStart() {
		return "";
	}
	public String textEnd() {
		return "";
	}
}