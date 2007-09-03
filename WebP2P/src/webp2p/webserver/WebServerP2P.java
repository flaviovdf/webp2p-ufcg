package webp2p.webserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.log4j.Logger;

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
		
		this.replicator = new Replicator(WebServerP2PConfig.getInstance().getReplicaValidity());
		this.replicator.loadAdjacents(ADJACENTS_FILENAME);
	}
	
	/**
	 * Used only by tests.
	 */
	WebServerP2P(DataManager dataManager, Replicator replicator) {
		this.dataManager = dataManager;
		this.replicator = replicator;
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

	public boolean overheadDetected(List<String> files) {
		LOG.debug("Notification of overhead for the following files: "+files);
		replicator.replicateContent(files.toArray(new String[files.size()]));
		return true;
	}
}