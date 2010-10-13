package zipwire.text;

import junit.framework.TestCase;

public class TestCamelCase extends TestCase {
	public void testSeparate(){
		assertEquals("Hello_World", new CamelCase("HelloWorld").separateWith("_"));
		assertEquals("Split-Multiple-Words", new CamelCase("SplitMultipleWords").separateWith("-"));
	}
}
