package proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import server.GetContentRequest;
import server.WebServer;
import core.entity.SimpleQueuedEntity;
import ds.DiscoveryService;
import ds.GetServersForURLRequest;
import edu.uah.math.distributions.Distribution;

public class ProxyImpl extends SimpleQueuedEntity implements Proxy {

	private static Logger LOG = Logger.getLogger( ProxyImpl.class );
	
	private final DiscoveryService discoveryService;
	private Map<Long, String> requests; 
		
	public ProxyImpl(Distribution distribution, DiscoveryService discoveryService) {
		super(distribution);
		this.requests = new HashMap<Long, String>();
		this.discoveryService = discoveryService;
	}
	
	public void makeRequest(String url) {
		long generatedRequestID = generateRequestID();
		LOG.debug( "Asking for file " + url + ". Request: " + generatedRequestID );
		requests.put(generatedRequestID, url);
		discoveryService.sendMessage(new GetServersForURLRequest(generatedRequestID, url, this));
	}

	private long generateRequestID() {
		return (long) (Math.random() * Long.MAX_VALUE);
	}

	void hereAreServers(long request, Set<WebServer> servers) {
		String url = requests.get(request);
		LOG.debug( "Server list " + servers + " received for url "+url+" with request " + request );
		if (url != null) {
			WebServer server = servers.iterator().next();
			if (server != null) {
				LOG.debug( "Asking file " + url + " to server " + server + ". Request: " + request );
				server.sendMessage(new GetContentRequest(request, url, this));
			}
		}
	}

	void hereIsContent(long request, int result) {
		LOG.debug( "Request " + request + " completed with result " + result );
	}
	
	public String toString() {
		return "Proxy";
	}
}