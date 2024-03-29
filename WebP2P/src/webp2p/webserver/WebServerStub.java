package webp2p.webserver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.AsyncCallback;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class WebServerStub {
	
	private XmlRpcClient client;
	private static final Logger LOG = Logger.getLogger(WebServerStub.class);
	
	public WebServerStub(String wsAddr) {
		XmlRpcClientConfigImpl pConfig = new XmlRpcClientConfigImpl();
		try {
			pConfig.setServerURL(new URL(wsAddr));
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid URL '" + wsAddr + "'", e);
		}
		
		this.client = new XmlRpcClient();
		this.client.setConfig(pConfig);
	}

	public byte[] getContent(String url) throws XmlRpcException {
		return (byte[]) this.client.execute("ws.getContent", new Object[] { url });
	}
	
	public void storeReplica(String url, AsyncCallback callback) throws XmlRpcException {
		this.client.executeAsync("ws.storeReplica", new Object[] { url }, callback);
	}
	
	public void overheadDetected(List<String> files, AsyncCallback callback) throws XmlRpcException {
		this.client.executeAsync("ws.overheadDetected", new Object[] { files }, callback);
	}
}
