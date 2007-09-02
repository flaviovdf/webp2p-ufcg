package webp2p.webserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;

import webp2p.discoveryservice.DiscoveryServiceStub;
import webp2p.util.LineReader;
import webp2p.util.XMLRPCSkeleton;
import webp2p.webserver.config.WebServerP2PConfig;


public class Main {
	
	private static final String SHAREDFILES_FILENAME = "webserverp2p.sharedfiles";
	
	private static final Logger LOG = Logger.getLogger(Main.class);

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
			
			init(discoveryService, wsFullAddr);
			
			XMLRPCSkeleton webserverp2pSkel = new XMLRPCSkeleton("ws", WebServerP2P.class);
			webserverp2pSkel.start(port);
			
			System.out.println("Successfully started!");
		} catch (NumberFormatException e) {
			System.err.println("Invalid port number");
			System.exit(2);
		}
	}
	
	/**
	 * Initialize the WebServerP2P.
	 * 
	 * @param discoveryService The <code>DiscoveryService</code>.
	 * @param wsFullAddr The <code>WebServerP2P</code> external address.
	 */
	private static void init(DiscoveryServiceStub discoveryService, String wsFullAddr) {
		LOG.info("Initializing the WebServerP2P: " + wsFullAddr);
		LOG.info("Using the DiscoveryService " + wsFullAddr);
		
		try {
			List<String> sharedFiles = LineReader.readFile(new File(SHAREDFILES_FILENAME), "#");

			for (String file : sharedFiles) {
				try {
					LOG.debug("Publishing file: " + file);
					discoveryService.put(wsFullAddr, file);
				} catch (XmlRpcException e) {
					// the file could not be published
					LOG.error("Could not publish the file " + file, e);
				}
			}
		} catch (FileNotFoundException e) {
			// the shared file could not be read
			LOG.error("Could not find the shared file " + SHAREDFILES_FILENAME, e);
		}
	}

}
