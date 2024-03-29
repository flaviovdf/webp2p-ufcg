package webp2p_sim.ds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import webp2p_sim.core.entity.NetworkEntity;
import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.proxy.GetResponse;
import webp2p_sim.proxy.Request;
import webp2p_sim.server.WebServer;
import edu.uah.math.distributions.Distribution;

public class DiscoveryService extends SimpleQueuedEntity {
	
	private static final Logger LOG = Logger.getLogger( DiscoveryService.class );

	private Map<String, Set<NetworkEntity>> url2servers;
	
	public DiscoveryService(String name, Distribution distribution) {
		super(name, distribution);
		this.url2servers = new HashMap<String, Set<NetworkEntity>>();
	}
	
	private void getRequest(long requestID, String url, NetworkEntity callback) {
		LOG.debug( "Request " + requestID + " asking for file " + url );
		
		Set<NetworkEntity> servers = url2servers.get(url);
		HashSet<NetworkEntity> response = new HashSet<NetworkEntity>();
		
		if (servers != null) {
			response.addAll(servers);
		}
		LOG.debug( "Sending WebServer list " + response + " to request " + requestID );
		callback.sendMessage(new GetResponse(response, requestID));
		
	}
	
	void putRequest(String url, NetworkEntity webServer) {
		assert webServer instanceof WebServer;
		
		LOG.debug( "WebServer " + webServer + " published file " + url );
		
		Set<NetworkEntity> servers = url2servers.get(url);
		if (servers == null) {
			servers = new HashSet<NetworkEntity>();
			url2servers.put(url, servers);
		}
		
		servers.add(webServer);
	}

	void getRequest(Request request) {
		this.getRequest(request.getId(), request.getUrl(), request.getCallBack());
	}

}
