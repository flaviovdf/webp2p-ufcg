package webp2p.webserver;

import webp2p.discoveryservice.DiscoveryServiceStub;
import webp2p.util.XMLRPCSkeleton;
import webp2p.webserver.config.WebServerP2PConfig;


public class Main {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("* Usage: java -cp [classpath] " + Main.class.getName() + " <port>");
			System.exit(1);
		}
		
		try {
			System.out.println("Starting WebServerP2P...");
			int port = Integer.parseInt(args[0]);
			
			String dsAddr = WebServerP2PConfig.getInstance().getDiscoveryServiceAddress();
			int dsPort = WebServerP2PConfig.getInstance().getDiscoveryServicePort();
			DiscoveryServiceStub discoveryService = new DiscoveryServiceStub(dsAddr, dsPort);
			
			String wsAddr = WebServerP2PConfig.getInstance().getWebServerP2PAddress();
			int wsPort = WebServerP2PConfig.getInstance().getWebServerP2PPort();
			String wsFullAddr = "http://" + wsAddr + ":" + wsPort;
			
			WebServerP2P.init(discoveryService, wsFullAddr);
			
			XMLRPCSkeleton webserverp2pSkel = new XMLRPCSkeleton("ws", WebServerP2P.class);
			webserverp2pSkel.start(port);
			
			System.out.println("Successfully started!");
		} catch (NumberFormatException e) {
			System.err.println("Invalid port number");
			System.exit(2);
		}
	}

}
