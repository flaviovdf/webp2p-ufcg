package webp2p.webserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;

import webp2p.discoveryservice.DiscoveryServiceStub;
import webp2p.util.LineReader;
import webp2p.webserver.config.WebServerP2PConfig;

public class WebServerP2P {
	
	private static final Logger LOG = Logger.getLogger(WebServerP2P.class);
	
	private static final String SHAREDFILES_FILENAME = "webserverp2p.sharedfiles";
	private static final String ADJACENTS_FILENAME = "webserverp2p.adjacents";
	
	private DataManager dataManager;
	private Replicator replicator;
	
	public WebServerP2P() {
		this.dataManager = new DataManager();
		this.dataManager.loadLocalSharedFiles(SHAREDFILES_FILENAME);
		
		this.replicator = new Replicator();
		this.replicator.loadAdjacents(ADJACENTS_FILENAME);
	}
	
	/**
	 * Used only by tests.
	 */
	WebServerP2P(DataManager dataManager, Replicator replicator) {
		this.dataManager = dataManager;
		this.replicator = replicator;
	}
	
	static void init() {
		String dsAddr = WebServerP2PConfig.getInstance().getDiscoveryServiceAddress();
		int dsPort = WebServerP2PConfig.getInstance().getDiscoveryServicePort();
		DiscoveryServiceStub discoveryService = new DiscoveryServiceStub(dsAddr, dsPort);
		
		String wsAddr = WebServerP2PConfig.getInstance().getWebServerP2PAddress();
		int wsPort = WebServerP2PConfig.getInstance().getWebServerP2PPort();
		String wsFullAddr = "http://" + wsAddr + ":" + wsPort;
		LOG.info("Initializing the WebServerP2P: " + wsFullAddr);
		LOG.info("Using the DiscoveryService " + dsAddr + ":" + dsPort);
		
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

	public byte[] getContent(String url) {
		LOG.info("The content " + url + " was requested");
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream(256);
			URL site = new URL(url);
			URLConnection connection = site.openConnection();

			for (String key : connection.getHeaderFields().keySet()) {
				if (key != null) {
					out.write((key + ": " + connection.getHeaderFields().get(key).get(0) + "\n").getBytes());
				}
			}
			
			out.write("\n".getBytes());
			
			out.write(this.dataManager.getData(url));
			
			return out.toByteArray();
		} catch (MalformedURLException e) {
			LOG.error("The url " + url + " is not well formed", e);
			return e.toString().getBytes();
		} catch (IOException e) {
			LOG.error("Error on accessing the url " + url + " stream", e);
			return e.toString().getBytes();
		}
	}

	public boolean storeReplica(String url) {
		LOG.info("Storing replica of url " + url);
		return this.dataManager.storeRemoteData(url);
	}
	
}