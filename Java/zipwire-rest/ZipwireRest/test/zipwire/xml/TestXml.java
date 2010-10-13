package zipwire.xml;

import zipwire.xml.Xml;
import junit.framework.TestCase;

public class TestXml extends TestCase {
	Xml xml;
	
	String sampleXml = 
		"<?xml version=\"1.0\"?>" + 
		"<Customer id=\"123\">"+  
		"<Name first=\"mr\" last=\"craig\" title=\"davidson\"/>" + 
		"<AccountLimit>" +
		"<Money>" +
		"<Currency>USD</Currency>" +
		"<Value>500</Value>" +
		"</Money>" +
		"</AccountLimit>" + 
		"<PrimaryAddress><Address><Line1>4 Acacia Ave</Line1><Line2>Nuttytown</Line2><Line3/></Address></PrimaryAddress>" + 
		"<BillingAddress><Address><Line1>4 Acacia Ave</Line1><Line2>Nuttytown</Line2><Line3/></Address></BillingAddress>" + 
		"</Customer>";

	String sampleXmlWithSpaces = 
		"<?xml version=\"1.0\"?>" + 
		"<Customer id=\"123\">"+ 
		"  " + 
		"<Name first=\"mr\" last=\"craig\" title=\"davidson\"/>" + 
		"<AccountLimit>" +
		"<Money>" +
		"<Currency>USD</Currency>" +
		"<Value>500</Value>" +
		"</Money>" +
		"</AccountLimit>" + 
		"<PrimaryAddress><Address><Line1>4 Acacia Ave</Line1><Line2>Nuttytown</Line2><Line3/></Address></PrimaryAddress>" + 
		"<BillingAddress><Address><Line1>4 Acacia Ave</Line1><Line2>Nuttytown</Line2><Line3/></Address></BillingAddress>" + 
		"</Customer>";
	
	public void testCleanWhiteSpace(){
		String x = sampleXmlWithSpaces;
		//while (x.indexOf("> ") != -1) 
			x = x.replaceAll("> +<", "><");
		assertEquals(sampleXml, x);
	}
	
	public void setUp() {
		xml= new Xml(sampleXml);
		assertTrue(xml.isDocument());
	}
	
	public void testFirstChild() {
		Xml customer = xml.firstChild();
		assertNotNull(customer);
		assertTrue(customer.isElement());
		assertEquals("Customer", customer.getName());
		assertTrue(customer.isElement());		
	}
	
	public void testGetAttributes(){
		Xml customer = xml.firstChild();
		assertEquals("123", customer.getAttribute("id"));
		assertEquals("123", customer.getAttributes().get("id"));
	}
	
	public void testHasAttributes(){
		Xml customer = xml.firstChild();
		assertTrue(customer.hasAttributes());
		assertTrue(customer.children("Name")[0].hasAttributes());
		assertFalse(customer.children("Money")[0].hasAttributes());
	}
	
	public void testChildren() {
		assertEquals(xml.firstChild(), xml.children()[0]);
	}
	
	public void testNamedChildren(){
		assertEquals(xml.firstChild(), xml.children("Customer")[0]);
		assertEquals(xml.firstChild().firstChild(), xml.firstChild().children("Name")[0]);
		assertEquals(2, xml.children("Address").length);
	}
	
	public void testHasChildren() {
		Xml customer = xml.firstChild();
		assertTrue(customer.hasChildren());
		assertFalse(customer.children("Name")[0].hasChildren());
		assertFalse(customer.children("Currency")[0].hasChildren());
	}
	
	public void testText() {
		Xml customer = xml.firstChild();
		assertEquals("USD", customer.children("Currency")[0].getText());
		assertEquals("500", customer.children("Value")[0].getText());
		assertEquals("4 Acacia Ave", customer.children("Line1")[0].getText());
		assertNull("Expected null but was: " + customer.getText(), customer.getText());
	}
	
	public void testHasText() {
		Xml customer = xml.firstChild();
		assertTrue(customer.children("Currency")[0].hasText());
		assertTrue(customer.children("Value")[0].hasText());
		assertFalse(customer.hasText());
	}
}