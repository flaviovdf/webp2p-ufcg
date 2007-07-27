package webp2p.webserver;

import org.apache.xmlrpc.XmlRpcException;

public class WebServerStub {
	
	public WebServerStub(String wsAddr) {
		
	}

	public byte[] getContent(String url) throws XmlRpcException {
		return ("file content: " + url).getBytes();
	}

}
