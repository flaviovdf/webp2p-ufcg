package webp2p.proxy;

import org.apache.xmlrpc.XmlRpcException;

import webp2p.discoveryservice.DiscoveryServiceStub;
import webp2p.webserver.WebServerStub;

public class Proxy {
	
	private DiscoveryServiceStub discoveryService;

	public Proxy(DiscoveryServiceStub discoveryService) {
		this.discoveryService = discoveryService;
	}
	
	public byte[] getContent(String url) {
		try {
			String[] servers = this.discoveryService.get(url);

			for (String wsAddr : servers) {
				WebServerStub wsStub = new WebServerStub(wsAddr);
//				byte[] file = wsStub.getContent(url);
				
			}
			
			return null;
		} catch (XmlRpcException e) {
			return null;
		}
	}
	
	public void hereIsContent() {
		
	}
	
	public void hereAreServers() {
		
	}

}
