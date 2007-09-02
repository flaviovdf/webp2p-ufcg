package webp2p.discoveryservice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import webp2p.webserver.WebServerP2P;

/**
 * The <code>DiscoveryService</code> is a catalog which holds information about where the files are.
 */
public class DiscoveryService {
	
	private static final Logger LOG = Logger.getLogger(DiscoveryService.class);

	/**
	 * The list of web servers by a given file.
	 */
	private Map<String, Set<String>> table;

	/**
	 * Creates a new <code>DiscoveryService</code> instance.
	 */
	public DiscoveryService() {
		this.table = new HashMap<String, Set<String>>();
	}

	/**
	 * Associates a WebServer with a file.
	 * 
	 * @param wsAddr The WebServer address (i.e., http://1.2.3.4:8080).
	 * @param file The file identifier (i.e., http://1.2.3.4:8080/file1.in).
	 * @return <code>true</code> if it is a first entry for the specified file, <code>false</code> otherwise.
	 */
	public boolean put(String wsAddr, String file) {
		LOG.info("Adding file " + file + " to WebServerP2P " + wsAddr);
		boolean firstEntryForFile = false;

		if (! this.table.containsKey(file)) {
			this.table.put(file, new HashSet<String>());
			firstEntryForFile = true;
			LOG.debug("First entry for the file " + file + " in " + wsAddr);
		}

		Set<String> webservers = this.table.get(file);
		webservers.add(wsAddr);
		
		return firstEntryForFile;
	}
	
	/**
	 * Returns a list with the WebServer addresses which owns the specified <code>file</code>.
	 * 
	 * @param file The file to be queried.
	 * @return The WebServer addresses list.
	 */
	public List<String> get(String file) {
		LOG.info("File " + file + " requested");
		Set<String> webservers = this.table.get(file);
		
		LOG.debug("Answer: " + webservers);
		if (webservers == null) return new LinkedList<String>();
		
		return new LinkedList<String>(webservers);
	}

	/**
	 * Deletes an entry from the <code>DiscoveryService</code>.
	 * 
	 * @param wsAddr The WebServer address (i.e., http://1.2.3.4:8080).
	 * @param file The file identifier (i.e., http://1.2.3.4:8080/file1.in).
	 * @return <code>true</code> if the operation is successfully performed, <code>false</code> otherwise.
	 */
	public boolean delete(String wsAddr, String file) {
		LOG.info("Removing file " + file + " from WebServerP2P " + wsAddr);
		boolean result = false;
		
		Set<String> webservers = this.table.get(file);
		
		if (webservers == null) {
			LOG.debug("No entries were found for file " + file);
		} else {
			result = webservers.remove(wsAddr);
			
			if (webservers.isEmpty()) {
				this.table.remove(file);
				LOG.debug("No more entries for file " + file);
			}
		}
		
		return result;
	}
}
