package server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import proxy.HereIsContentMessage;
import proxy.ProxyImpl;

import common.TimeToLive;

import core.entity.SimpleQueuedEntity;
import ds.DiscoveryService;
import ds.PutFileRequest;
import edu.uah.math.distributions.Distribution;

public class WebServer extends SimpleQueuedEntity {
	
	private static final Logger LOG = Logger.getLogger( WebServer.class );
	
	private static final int DEFAULT_THRESHOLD = 20;
	private static final int DEFAULT_THRESHOLD_TTL = 50;
	private static final int DEFAULT_REPLICATION_TTL = 100;
	
	private String url;
	private DiscoveryService discoveryService;
	private List<WebServer> adj;

	private Map<String, TimeToLive> files;
	private Map<String, ReplicationInfo> replicationMap;
	
	public WebServer(Distribution distribution, String url, DiscoveryService discoveryService) {
		super(distribution);
		this.url = url;
		this.discoveryService = discoveryService;
		this.adj = new LinkedList<WebServer>();
		this.replicationMap = new HashMap<String, ReplicationInfo>();
		this.files = new HashMap<String, TimeToLive>();
	}

	void loadFile(String url, int size, TimeToLive ttl) {
		LOG.debug( "Loading file " + url + " in server " + this.url );
		
		TimeToLive olderTTL = this.files.get(url);
		
		if (olderTTL == null || olderTTL.remaining() < ttl.remaining()) {
			this.files.put(url, ttl);
			this.replicationMap.put(url, new ReplicationInfo(DEFAULT_THRESHOLD, DEFAULT_THRESHOLD_TTL));
			this.discoveryService.sendMessage(new PutFileRequest(url, this));
		}
	}
	
	Set<String> getFiles() {
		return this.files.keySet();
	}
	
	void addAdj(WebServer server) {
		LOG.debug( "Connecting server " + this.toString() + " to " + server );
		
		this.adj.add(server);
	}
	
	List<WebServer> getAdj() {
		return this.adj;
	}
	
	@Override
	public void tickOcurred() {
		super.tickOcurred();
		
		Iterator<String> urls = files.keySet().iterator();
		while (urls.hasNext()) {
			String url = urls.next();
			TimeToLive ttl = files.get(url);
			if (ttl.decrease() == 0) {
				LOG.debug( "Removing page " + url + " from server " + this.url );
				
				urls.remove();
				files.remove(url);
				replicationMap.remove(url);
			}
		}
		
		Collection<ReplicationInfo> replicationInfos = replicationMap.values();
		for (ReplicationInfo ri : replicationInfos) {
			ri.tickOcurred();
		}
	}

	//called by proxies
	
	void getContent(long request, String url, ProxyImpl callback) {
		LOG.debug( "Content for " + url + " requested by " + callback + " request number " + request );
		
		int result;
		if (this.files.containsKey(url)) {
			result = 0;
			
			ReplicationInfo info = this.replicationMap.get(url);
			info.urlRequested();
			if (info.mustReplicate()) {
				for (WebServer server : this.adj) {
					if (!info.hasReplica(server)) {
						LOG.debug( "Replication for " + url + " requested to " + server );
						info.replicationRequested(server);
						server.sendMessage(new CreateReplicaOfUrlMessage(url, this));
						break;
					}
				}
			}
		} else {
			result = -1;
		}
		
		LOG.debug( "Sending content to " + callback + " request number " + request);
		callback.sendMessage(new HereIsContentMessage(request, result));
	}
	
	//called by other servers
	
	void createReplicaOfUrl(String url, WebServer server) {
		LOG.debug( "Replica creation requested from " + server + " , content url " + url);
		server.sendMessage(new GetContentForReplicationMessage(url, this));
	}

	void getContentForReplication(String url, WebServer server) {
		ReplicationInfo info = this.replicationMap.get(url);
		if (info != null) {
			LOG.debug( "Sending replica for url " + url + " to " + server + " ttl " + DEFAULT_REPLICATION_TTL);
			info.replicationDone(server, DEFAULT_REPLICATION_TTL);
			//FIXME size = 1?!
			server.sendMessage(new HereIsReplicaOfContent(url, DEFAULT_REPLICATION_TTL, 1));
		}
	}

	void hereIsReplicaOfUrl(String url, int replicationTTL, int size) {
		LOG.debug( "Replica for url " + url + " recevied  ttl " + DEFAULT_REPLICATION_TTL);
		loadFile(url, size, new TimeToLive(replicationTTL));
	}
	
	public String toString() {
		return this.url;
	}

}
