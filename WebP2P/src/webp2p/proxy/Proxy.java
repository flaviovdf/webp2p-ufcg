package webp2p.proxy;

import org.apache.xmlrpc.XmlRpcException;

import webp2p.discoveryservice.DiscoveryServiceStub;
import webp2p.webserver.WebServerStub;

public class Proxy {
	
	private DiscoveryServiceStub discoveryService;
	private WebServerArbitrator webserverArbitrator;

	public Proxy(DiscoveryServiceStub discoveryService, WebServerArbitrator arbitrator) {
		this.discoveryService = discoveryService;
		this.webserverArbitrator = arbitrator;
	}
	
	public byte[] getContent(String url) {
		try {
			String[] servers = this.discoveryService.get(url);
			String wsAddr = this.webserverArbitrator.chooseWebServer(servers);
			WebServerStub wsStub = new WebServerStub(wsAddr);
			return wsStub.getContent(url);
		} catch (XmlRpcException e) {
			return null;
		}
	}

}
