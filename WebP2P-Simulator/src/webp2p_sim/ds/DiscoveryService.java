package webp2p_sim.ds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import webp2p_sim.core.entity.NetworkEntity;
import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.proxy.GetResponse;
import webp2p_sim.server.WebServer;
import edu.uah.math.distributions.Distribution;

public class DiscoveryService extends SimpleQueuedEntity {
	
	private static final Logger LOG = Logger.getLogger( DiscoveryService.class );

	private Map<String, Set<WebServer>> url2servers;
	
	public DiscoveryService(String name, Distribution distribution) {
		super(name, distribution);
		this.url2servers = new HashMap<String, Set<WebServer>>();
	}
	
	void getRequest(long requestID, String url, NetworkEntity callback) {
		LOG.debug( "Request " + requestID + " asking for file " + url );
		
		Set<WebServer> servers = url2servers.get(url);
		LOG.debug( "Sending WebServer list " + servers + " to request " + requestID );
		callback.sendMessage(new GetResponse(new HashSet<WebServer>(servers), requestID));
	}
	
	void putRequest(String url, WebServer webServer) {
		LOG.debug( "WebServer " + webServer + " published file " + url );
		
		Set<WebServer> servers = url2servers.get(url);
		if (servers == null) {
			servers = new HashSet<WebServer>();
			url2servers.put(url, servers);
		}
		
		servers.add(webServer);
	}

}
