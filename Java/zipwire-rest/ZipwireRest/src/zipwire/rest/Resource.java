package zipwire.rest;

import java.util.*;

import zipwire.exception.ZipwireException;
import zipwire.xml.*;

public class Resource {
	private int status;
	private String response;
	private XmlNamingRule xmlNamingRule;
	public Resource(XmlNamingRule rule, int status, String response){
		this.xmlNamingRule = rule;
		this.status = status;
		this.response = response;
	}
	public String asString() {
		return response;
	}
	public int getStatus() {
		return status;
	}
	public Xml asXml() {
		try {
		  return new Xml(asString());
		}
		catch(Exception e){
			throw new ZipwireException(status + " " + asString(), e);
		}
	}
	public <T>T as(Class<T> interfaceToUse) {
		return new XmlClump(asXml(), xmlNamingRule).as(interfaceToUse);
	}
	public List asList(Class interfaceToUse, String elementName) {
		return new XmlClump(asXml(), xmlNamingRule).asList(interfaceToUse, elementName);
	}
}