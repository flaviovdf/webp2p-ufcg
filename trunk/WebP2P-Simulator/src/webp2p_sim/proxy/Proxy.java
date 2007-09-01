package webp2p_sim.proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import webp2p_sim.common.ContentIF;
import webp2p_sim.common.RequestCallBack;
import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.core.network.Host;
import webp2p_sim.core.network.Network;
import webp2p_sim.ds.GetServersForURLRequest;
import webp2p_sim.server.GetContentRequest;
import webp2p_sim.util.RandomLongGenerator;
import edu.uah.math.distributions.Distribution;

public class Proxy extends SimpleQueuedEntity implements RequestCallBack, ContentIF {

	private static Logger LOG = Logger.getLogger( Proxy.class );
	
	private final Host discoveryService;
	private Map<Long, RequestData> requests; 
		
	public Proxy(Host host, Distribution distribution, Network network, Host discoveryService, RandomLongGenerator requestIDGenerator) {
		super(host, distribution, network);
		this.requests = new HashMap<Long, RequestData>();
		this.discoveryService = discoveryService;
	}
	
	void hereAreServers(long request, Set<Host> servers) {
		RequestData requestData = requests.get(request);
		if (requestData != null) {
			LOG.info( "Server list " + servers + " received for url "+requestData.getUrl()+" with request " + request );
			if (servers != null && !servers.isEmpty()) {
				Host server = servers.iterator().next();
				if (server != null) {
					LOG.info( "Asking file " + requestData.getUrl() + " to server " + server + ". Request: " + request );
					sendMessage(server, new GetContentRequest(request, requestData.getUrl(), this.getHost()));
				}
			} else {
				sendMessage(requestData.entity, new HereIsContentMessage(request, -1)); // empty result
			}
		} else {
			LOG.info( "Request " + request + " not found on the proxy map" );
		}
	}

	public void hereIsContent(long request, int result) {
		LOG.info( "Request " + request + " completed with result " + result );
		RequestData requestData = this.requests.get(request);
		if (requestData != null) {
			sendMessage(requestData.entity, new HereIsContentMessage(request,result));
		}
	}
	
	//just for test
	Map<Long, RequestData> getRequestsMap() {
		return this.requests;
	}

	public void getContent(Request request) {
		LOG.info( "Asking for file " + request.getUrl() + ". Request: " + request.getId() );
		
		requests.put(request.getId(), new RequestData(request.getCallBack(),request.getUrl()));
		sendMessage(discoveryService, new GetServersForURLRequest(new Request(request.getId(), request.getUrl(), this.getHost())));
	}
	
	protected class RequestData {
		private Host entity;
		private String url;
		
		public RequestData(Host host, String url) {
			this.entity = host;
			this.url = url;
		}
		
		public Host getBrowser() {
			return entity;
		}
		
		public String getUrl() {
			return url;
		}
	}
	
}