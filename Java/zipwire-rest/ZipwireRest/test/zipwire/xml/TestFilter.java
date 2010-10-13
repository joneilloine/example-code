package zipwire.xml;

import junit.framework.TestCase;

public class TestFilter extends TestCase {

	public void testStripWhitespace(){
		String actual = sampleXmlWithSpaces;
		actual = actual.replaceAll("\n", "").replaceAll("> +", ">");
		assertEquals(sampleXml, actual);
	}
	
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
		"                \n" +
		"     \n" + 
		"                \n" +
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
