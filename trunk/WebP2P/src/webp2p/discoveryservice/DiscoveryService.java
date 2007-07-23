package webp2p.discoveryservice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DiscoveryService {

	private Map<String, Set<String>> table;

	public DiscoveryService() {
		this.table = new HashMap<String, Set<String>>();
	}

	public void put(String wsAddr, String file) {
		if (! this.table.containsKey(file)) {
			this.table.put(file, new HashSet<String>());
		}
		
		Set<String> webservers = this.table.get(file);
		webservers.add(wsAddr);
	}
	
	public List<String> get(String file) {
		Set<String> webservers = this.table.get(file);
		
		if (webservers == null) return new LinkedList<String>();
		
		return new LinkedList<String>(webservers);
	}

	public void delete(String wsAddr, String file) {
		Set<String> webservers = this.table.get(file);
		
		if (webservers != null) {
			webservers.remove(wsAddr);
			
			if (webservers.isEmpty()) this.table.remove(file);
		}
	}
}
