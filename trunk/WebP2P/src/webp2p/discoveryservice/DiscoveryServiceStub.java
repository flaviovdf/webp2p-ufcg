package webp2p.discoveryservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class DiscoveryServiceStub {

	private XmlRpcClient client;

	public DiscoveryServiceStub(String dsAddr, int dsPort) {
		XmlRpcClientConfigImpl pConfig = new XmlRpcClientConfigImpl();
		try {
			pConfig.setServerURL(new URL("http://" + dsAddr + ":" + dsPort));
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid URL 'http://" + dsAddr + ":" + dsPort + "'", e);
		}
		
		this.client = new XmlRpcClient();
		this.client.setConfig(pConfig);
	}

	public void put(String wsAddr, String file) throws XmlRpcException {
		this.client.execute("ds.put", new Object[] { wsAddr, file });
	}
	
	public String[] get(String file) throws XmlRpcException {
		Object[] result = (Object[]) this.client.execute("ds.get", new Object[] { file });
		return Arrays.asList(result).toArray(new String[0]);
	}
	
	public void delete(String wsAddr, String file) throws XmlRpcException {
		this.client.execute("ds.delete", new Object[] { wsAddr, file });
	}

}
