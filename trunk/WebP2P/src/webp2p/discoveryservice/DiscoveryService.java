package webp2p.discoveryservice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The <code>DiscoveryService</code> is a catalog which holds information about where the files are.
 */
public class DiscoveryService {

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
System.out.println("DiscoveryService.put() " + wsAddr + " -> "+file);
		boolean firstEntryForFile = false;

		if (! this.table.containsKey(file)) {
			this.table.put(file, new HashSet<String>());
			firstEntryForFile = true;
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
		Set<String> webservers = this.table.get(file);
		
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
		boolean result = false;
		
		Set<String> webservers = this.table.get(file);
		
		if (webservers != null) {
			result = webservers.remove(wsAddr);
			
			if (webservers.isEmpty()) this.table.remove(file);
		}
		
		return result;
	}
}
