package zipwire.xml;

import java.util.List;

import zipwire.text.CamelCase;
import junit.framework.TestCase;

public class TestXmlClump extends TestCase {
	public void testToString(){
		assertEquals(simple, new XmlClump(new Xml(simple), xmlNamingRule).toString());
	}
	
	public void testHandleBasic(){
		Project project = new XmlClump(new Xml(simple), xmlNamingRule).as(Project.class);
		assertEquals("1234", project.getId());
		assertEquals("Zipwire", project.getProjectName());
	}
	
	public void testHandleLists(){
		List<Weight> weights = new XmlClump(new Xml(basicList), xmlNamingRule)
									.asList(Weight.class, "weight");
		assertEquals("<weight>23</weight>", weights.get(0).toString());
		assertEquals("23", weights.get(0).getChildText());
		assertEquals("24", weights.get(1).getChildText());
		assertEquals("25", weights.get(2).getChildText());
	}
	public void testHandleComplexStructures(){
		ContactDetails details = new XmlClump(new Xml(complex), xmlNamingRule).as(ContactDetails.class);
		assertEquals(2, details.getEmailAddresses().asList().size());
		assertEquals("craig@example.com", details.getEmailAddresses().asList().get(0).as(EmailAddress.class).getEmail());
		assertEquals("baz@example.com", details.getEmailAddresses().asList().get(1).as(EmailAddress.class).getEmail());
	}

	String simple = 
		"<?xml version=\"1.0\"?>" +
		"<project>" +
		"<id>1234</id>" +
		"<project-name>Zipwire</project-name>" +
		"</project>";		
	
	String basicList = 
		"<?xml version=\"1.0\"?>" +
		"<weights>" +
		"<weight>23</weight><weight>24</weight><weight>25</weight>" +
		"</weights>"; 
	
	String complex = 
		"<?xml version=\"1.0\"?>" +
		"<contact-details>" +
		"<email-addresses>" +
		"<email-address><email>craig@example.com</email></email-address>" +
		"<email-address><email>baz@example.com</email></email-address>" +
		"</email-addresses>" +
		"</contact-details>"; 
	
	XmlNamingRule xmlNamingRule = new XmlNamingRule(){
		public String transform(String method) {
			return new CamelCase(method).separateWith("-").toLowerCase();
		}
	};
	
	interface Project {String getProjectName(); String getId(); XmlClump contactDetails();}
	interface ContactDetails {XmlClump getEmailAddresses();}
	interface EmailAddress {String getEmail();}
	interface Weight {String getChildText();}
}