package webp2p_sim.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.core.network.Host;
import webp2p_sim.core.network.Network;
import webp2p_sim.proxy.GetResponse;
import webp2p_sim.proxy.Request;
import edu.uah.math.distributions.Distribution;

public class DiscoveryService extends SimpleQueuedEntity {
	
	private static final Logger LOG = Logger.getLogger( DiscoveryService.class );

	private Map<String, List<Host>> url2servers;
	
	public DiscoveryService(Host host, Distribution distribution, Network network, boolean bindSelf) {
		super(host, distribution, network, bindSelf);
		this.url2servers = new HashMap<String, List<Host>>();
	}
	
	private void getRequest(long requestID, String url, Host callback) {
		LOG.info( "Request " + requestID + " asking for file " + url );
		
		List<Host> servers = url2servers.get(url);
		ArrayList<Host> response = new ArrayList<Host>();
		
		if (servers != null) {
			response.addAll(servers);
		}
		
		LOG.info( "Sending WebServer list " + response + " to request " + requestID );
		sendMessage(callback, new GetResponse(response, requestID));
	}
	
	void putRequest(String url, Host webServer) {
		LOG.info( "WebServer " + webServer + " published file " + url );
		
		List<Host> servers = url2servers.get(url);
		if (servers == null) {
			servers = new ArrayList<Host>();
			url2servers.put(url, servers);
		}
		
		servers.add(webServer);
	}

	void getRequest(Request request) {
		this.getRequest(request.getId(), request.getUrl(), request.getCallBack());
	}

}
