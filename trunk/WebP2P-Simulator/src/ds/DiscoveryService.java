package ds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import proxy.GetResponse;
import server.WebServer;
import core.entity.QueuedEntity;
import core.entity.SimpleQueuedEntity;
import edu.uah.math.distributions.Distribution;

public class DiscoveryService extends SimpleQueuedEntity {

	private Map<String, Set<WebServer>> url2servers;
	
	private Set<WebServer> allServers;

	public DiscoveryService(Distribution distribution) {
		super(distribution);
		this.url2servers = new HashMap<String, Set<WebServer>>();
		this.allServers = new HashSet<WebServer>();
	}
	
	void getRequest(long requestID, String url, QueuedEntity callback) {
		Set<WebServer> servers = url2servers.get(url);
		callback.sendMessage(new GetResponse(new HashSet<WebServer>(servers), requestID));
	}
	
	void putRequest(String url, WebServer webServer) {
		Set<WebServer> servers = url2servers.get(url);
		if (servers == null) {
			servers = new HashSet<WebServer>();
			url2servers.put(url, servers);
		}
		
		allServers.add(webServer);
		servers.add(webServer);
	}

}
