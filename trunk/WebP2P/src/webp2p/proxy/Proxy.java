package webp2p.proxy;

import org.apache.xmlrpc.XmlRpcException;

import webp2p.discoveryservice.DiscoveryServiceStub;
import webp2p.proxy.arbitrator.WebServerArbitrator;
import webp2p.webserver.WebServerStub;

public class Proxy {
	
	private static Proxy uniqueInstance = null;
	
	private DiscoveryServiceStub discoveryService;
	private WebServerArbitrator webserverArbitrator;

	private Proxy() {
		this.discoveryService = null;
		this.webserverArbitrator = null;
	}
	
	public static Proxy getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Proxy();
		}
		return uniqueInstance;
	}
	
	public void config(DiscoveryServiceStub discoveryService, WebServerArbitrator arbitrator) {
		this.discoveryService = discoveryService;
		this.webserverArbitrator = arbitrator;
	}
	
	public byte[] getContent(String url) {
		if (this.discoveryService == null || this.webserverArbitrator == null) {
			throw new IllegalStateException("You first need to call the config() method");
		}
		try {
			String[] servers = this.discoveryService.get(url);
			String wsAddr = this.webserverArbitrator.chooseWebServer(servers);
			if (wsAddr == null) return null;
			WebServerStub wsStub = new WebServerStub(wsAddr);
			return wsStub.getContent(url);
		} catch (XmlRpcException e) {
			return null;
		}
	}

}
