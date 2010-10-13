package zipwire.rest;

import java.util.List;

import zipwire.text.CamelCase;
import zipwire.xml.XmlNamingRule;
import junit.framework.TestCase;

public class TestRestServiceIntegration extends TestCase {
	String host;
	String username;
	String password;
	RestConnection connection = new ApacheRestConnection(
			host, username, password, 
			new XmlNamingRule(){
				public String transform(String method) {
					return new CamelCase(method).separateWith("-").toLowerCase();
			}}
	){};
		
	interface Project {
		String getName();
	}
	
	public void testSingleEntity(){
		Project project = connection.get("projects/2782118.xml").as(Project.class);
		assertNotNull(project);
		assertEquals("REST Interface", project.getName());
	}
	
	public void testListOfEntities(){
		List<Project> projects = connection.get("projects.xml").asList(Project.class, "project");
		assertEquals(1, projects.size());
		assertEquals("REST Interface", projects.get(0).getName());
	}
}