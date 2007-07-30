package webp2p.util;

import java.io.IOException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcStreamServer;
import org.apache.xmlrpc.webserver.WebServer;

import webp2p.util.StatefulProcessorFactoryFactory;

public class XMLRPCSkeleton {
	
	private String name;
	private Class clazz;

	public XMLRPCSkeleton(String name, Class clazz) {
		this.name = name;
		this.clazz = clazz;
	}
	
	public void start(int port) {
		PropertyHandlerMapping pMapping = new PropertyHandlerMapping();
		pMapping.setRequestProcessorFactoryFactory(new StatefulProcessorFactoryFactory());
		try {
			pMapping.addHandler(this.name, this.clazz);
		} catch (XmlRpcException e) {
			throw new RuntimeException("Could not add handler for class " + this.clazz, e);
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
