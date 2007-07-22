package webp2p.discoveryservice.ui;

import java.io.IOException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcStreamServer;
import org.apache.xmlrpc.webserver.WebServer;

public class Main {

	public static void main(String[] args) throws IOException, XmlRpcException {
		PropertyHandlerMapping pMapping = new PropertyHandlerMapping();
		pMapping.addHandler("test", Main.class);
		
		WebServer webServer = new WebServer(9090);
		XmlRpcStreamServer server = webServer.getXmlRpcServer();
		server.setHandlerMapping(pMapping);
		webServer.start();
	}

	public boolean isEven(int n) {
		return n % 2 == 0;
	}

}
