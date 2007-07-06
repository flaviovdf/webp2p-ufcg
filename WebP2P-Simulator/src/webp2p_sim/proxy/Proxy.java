package webp2p_sim.proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.ds.GetServersForURLRequest;
import webp2p_sim.server.ContentIF;
import webp2p_sim.server.GetContentRequest;
import webp2p_sim.server.WebServer;
import webp2p_sim.util.RandomLongGenerator;
import edu.uah.math.distributions.Distribution;

public class Proxy extends SimpleQueuedEntity implements RequestCallBack, ContentIF {

	private static Logger LOG = Logger.getLogger( Proxy.class );
	
	private final DiscoveryService discoveryService;
	private Map<Long, String> requests; 
	private RandomLongGenerator requestIDGenerator;
		
	public Proxy(String name, Distribution distribution, DiscoveryService discoveryService, RandomLongGenerator requestIDGenerator) {
		super(name, distribution);
		this.requests = new HashMap<Long, String>();
		this.discoveryService = discoveryService;
		this.requestIDGenerator = requestIDGenerator;
	}
	
	void hereAreServers(long request, Set<WebServer> servers) {
		String url = requests.get(request);
		LOG.debug( "Server list " + servers + " received for url "+url+" with request " + request );
		if (url != null) {
			if (servers != null && !servers.isEmpty()) {
				WebServer server = servers.iterator().next();
				if (server != null) {
					LOG.debug( "Asking file " + url + " to server " + server + ". Request: " + request );
					server.sendMessage(new GetContentRequest(request, url, this));
				}
			}
		}
	}

	public void hereIsContent(long request, int result) {
		LOG.debug( "Request " + request + " completed with result " + result );
	}
	
	//just for test
	Map<Long, String> getRequestsMap() {
		return this.requests;
	}

	public void getContent(Request request) {
		LOG.debug( "Asking for file " + request.getUrl() + ". Request: " + request.getId() );
		requests.put(request.getId(), request.getUrl());
		discoveryService.sendMessage(new GetServersForURLRequest(new Request(request.getId(), request.getUrl(), this)));
	}
	
}