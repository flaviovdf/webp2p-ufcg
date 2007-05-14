package proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import server.GetContentRequest;
import server.WebServer;
import core.entity.SimpleQueuedEntity;
import ds.DiscoveryService;
import ds.GetServersForURLRequest;
import edu.uah.math.distributions.Distribution;

public class ProxyImpl extends SimpleQueuedEntity implements Proxy {

	private final DiscoveryService discoveryService;
	private Map<Long, String> requests; 
		
	public ProxyImpl(Distribution distribution, DiscoveryService discoveryService) {
		super(distribution);
		this.requests = new HashMap<Long, String>();
		this.discoveryService = discoveryService;
	}
	
	public void makeRequest(String url) {
		long generatedRequestID = generateRequestID();
		requests.put(generatedRequestID, url);
		discoveryService.sendMessage(new GetServersForURLRequest(generatedRequestID, url, this));
	}

	private long generateRequestID() {
		return (long) (Math.random() * Long.MAX_VALUE);
	}

	void hereAreServers(long request, Set<WebServer> servers) {
		String url = requests.get(request);
		if (url != null) {
			WebServer server = servers.iterator().next();
			if (server != null) {
				//TODO logar que um request foi feito
				server.sendMessage(new GetContentRequest(request, url, this));
			}
		}
	}

	void hereIsContent(long request, int result) {
		
	}
	
}