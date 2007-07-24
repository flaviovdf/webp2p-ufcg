package webp2p.discoveryservice;

import java.io.IOException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcStreamServer;
import org.apache.xmlrpc.webserver.WebServer;

import webp2p.util.StatefulProcessorFactoryFactory;

public class DiscoveryServiceSkel {
	
	public void start(int port) {
		PropertyHandlerMapping pMapping = new PropertyHandlerMapping();
		pMapping.setRequestProcessorFactoryFactory(new StatefulProcessorFactoryFactory());
		try {
			pMapping.addHandler("ds", DiscoveryService.class);
		} catch (XmlRpcException e) {
			throw new RuntimeException("Could not add handler for class " + this.getClass(), e);
		}
		
		WebServer webServer = new WebServer(port);
		XmlRpcStreamServer server = webServer.getXmlRpcServer();
		server.setHandlerMapping(pMapping);
		try {
			webServer.start();
		} catch (IOException e) {
			throw new RuntimeException("Could not bind the server on port " + port, e);
		}
	}

}
