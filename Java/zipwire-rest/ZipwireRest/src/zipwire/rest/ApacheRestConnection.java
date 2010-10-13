package zipwire.rest;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.*;

import zipwire.exception.ZipwireException;
import zipwire.xml.XmlNamingRule;

public class ApacheRestConnection implements RestConnection {
	private String protocol, host, username, password;
	private XmlNamingRule xmlNamingRule;
	public ApacheRestConnection(String host, String username, String password, XmlNamingRule xmlNamingRule) {
		if (host == null) throw new ZipwireException("Need to specify a host, username, and password");
		this.protocol = "http://";
		this.host = host;
		this.username = username;
		this.password = password;
		this.xmlNamingRule = xmlNamingRule;
	}
	public Resource get(String uri) {
        return execute(new GetMethod(buildUri(uri)));
	}
	public Resource post(String uri, String xml){
		return execute(add(new PostMethod(buildUri(uri)), xml));
	}
	public Resource put(String uri, String xml) {
		return execute(add(new PutMethod(buildUri(uri)), xml));
	}
	public Resource delete(String uri) {
		return execute(new DeleteMethod(buildUri(uri)));
	}
	private Resource execute(HttpMethod m) {
		try {
	        m.setDoAuthentication(true);
	        int status = connect().executeMethod(m);
	        return new Resource(xmlNamingRule, status, m.getResponseBodyAsString());
		}
		catch(Exception e){
			throw new ZipwireException("Couldn't execute request:", e);
		}
	}
	private HttpMethod add(EntityEnclosingMethod m, String xml) {
		try {
			m.setRequestEntity(new StringRequestEntity(xml, "application/xml", "ISO-8859-1"));
			return m;
		}
		catch(Exception e){
			throw new ZipwireException(e);
		}
	}
	private HttpClient connect() {
		HttpClient client = new HttpClient();
        client.getState().setCredentials(
            new AuthScope(host, 80),
            new UsernamePasswordCredentials(username, password)
        );
		return client;
	}
	private String buildUri(String path) {
		return protocol + host + "/" + path;
	}
}