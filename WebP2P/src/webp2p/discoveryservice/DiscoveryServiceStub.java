package webp2p.discoveryservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class DiscoveryServiceStub {

	private XmlRpcClient client;

	public DiscoveryServiceStub(String dsAddr, int port) {
		XmlRpcClientConfigImpl pConfig = new XmlRpcClientConfigImpl();
		try {
			pConfig.setServerURL(new URL(dsAddr + ":" + port));
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid address " + dsAddr + ":" + port, e);
		}
		
		this.client = new XmlRpcClient();
		this.client.setConfig(pConfig);
	}

	public void put(String wsAddr, String file) throws XmlRpcException {
		System.out.println(this.client.execute("ds.put", new Object[] { wsAddr, file }));
	}
	
	public List<String> get(String file) throws XmlRpcException {
		Object result = this.client.execute("ds.get", new Object[] { file });
		System.out.println(result);
		return null;
	}

}
