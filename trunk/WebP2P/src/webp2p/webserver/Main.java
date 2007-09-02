package webp2p.webserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;

import webp2p.discoveryservice.DiscoveryServiceStub;
import webp2p.loadmeter.LoadListener;
import webp2p.loadmeter.LoadMeter;
import webp2p.loadmeter.Metric;
import webp2p.loadmeter.Pinger;
import webp2p.loadmeter.ProxyLoadListener;
import webp2p.loadmeter.exception.InconsistenWebServerFilesListException;
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
			
			ProxyLoadListener loadListener = new ProxyLoadListener(wsFullAddr);
			
			LOG.info("Using the DiscoveryService " + dsAddr + ":" + dsPort);
			init(discoveryService, wsFullAddr);
			
			XMLRPCSkeleton webserverp2pSkel = new XMLRPCSkeleton("ws", WebServerP2P.class);
			webserverp2pSkel.start(port);
			
			// passar o webp2p
			initLoadMeter(loadListener);
			
			System.out.println("Successfully started!");
		} catch (NumberFormatException e) {
			System.err.println("Invalid port number");
			System.exit(2);
		} catch (FileNotFoundException e) {
			System.err.println("webserverp2p.sharedfiles not found.");
			System.exit(2);
		} catch (InconsistenWebServerFilesListException e) {
			System.err.println("Files in webserverp2p.sharedfiles are not from the same web server.");
			System.exit(2);
		}
	}
	
	/**
	 * Initializes the WebServerP2P.
	 * 
	 * @param discoveryService The <code>DiscoveryService</code>.
	 * @param wsFullAddr The <code>WebServerP2P</code> external address.
	 */
	private static void init(DiscoveryServiceStub discoveryService, String wsFullAddr) {
		LOG.info("Initializing the WebServerP2P: " + wsFullAddr);
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
	
	/**
	 * Initializes teh Load Meter and bind it with the WebServerP2P.
	 * @param wsP2P The <code>WebServerP2P</code> object that will be the listener of the loadMeter.
	 * @throws FileNotFoundException
	 * @throws InconsistenWebServerFilesListException
	 */
	private static void initLoadMeter(LoadListener listener) throws FileNotFoundException, InconsistenWebServerFilesListException {
		// default values
		// 1KB and 30s
		int bufferSize = 1024;
		int pingInterval = 30000;
		try {
			Properties props = new Properties();
			props.load(new BufferedInputStream(new FileInputStream("loadmeter.properties")));
			bufferSize = Integer.parseInt(props.getProperty("buffer"));
			pingInterval = Integer.parseInt(props.getProperty("ping.interval"));
		} catch (IOException e) {
			System.out.println("loadmeter.properties could not be readed. Using the default value 1024 for buffer's size and 30 seconds for ping interval.");
		}
		
		LoadMeter loadMeter = new LoadMeter(bufferSize);
		List<String> files = LineReader.readFile(new File(SHAREDFILES_FILENAME), "#");
		Metric metric = new Metric(files,WebServerP2PConfig.getInstance().getMinimumDownloadRateForFile()); 
		loadMeter.addListener(listener, metric);
		
		Pinger pinger = new Pinger(loadMeter);
		Timer time = new Timer();
		time.schedule(pinger,10000,pingInterval);
	}
}