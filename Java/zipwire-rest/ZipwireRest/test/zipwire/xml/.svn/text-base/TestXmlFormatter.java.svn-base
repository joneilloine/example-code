package zipwire.xml;

import junit.framework.TestCase;

public class TestXmlFormatter extends TestCase {
	public void testAsString(){
		assertEquals(sampleXml, 
				new Xml(sampleXml).asString());
	}
	
	public void testAsStringWithSpaces(){
		assertEquals(sampleXml,
				new Xml(sampleXmlWithSpaces).asString());
	}
	
	public void testAsStringWithText(){
		assertEquals(textHeavyXml,
				new Xml(textHeavyXml)
					.asString());
	}
	
	String textHeavyXml = 
		"<?xml version=\"1.0\"?>" + 
		"<p>"+ 
		"Hello" +
		"<a href=\"requestHandler?command=link\">Link</a>" + 
		"World" + 
		"<a href=\"requestHandler?command=link\">Link</a>" +
		"Wide" + 
		"</p>";
		
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
		"     \n" +
		"     \n" + 
		"<Name first=\"mr\" last=\"craig\" title=\"davidson\"/>" + 
		"<AccountLimit>" +
		"<Money>" +
		"<Currency>USD</Currency>" +
		"<Value>500</Value>" +
		"</Money>" +
		"</AccountLimit>" + 
		"<PrimaryAddress><Address><Line1>4 Acacia Ave</Line1><Line2>Nuttytown</Line2><Line3/></Address></PrimaryAddress>" + 
		"<BillingAddress><Address><Line1>4 Acacia Ave</Line1><Line2>Nuttytown</Line2><Line3/></Address></BillingAddress>" + 
		"     " + 
		"</Customer>";
}