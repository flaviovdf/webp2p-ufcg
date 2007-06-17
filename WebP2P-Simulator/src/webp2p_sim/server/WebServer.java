package webp2p_sim.server;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.ds.PutFileRequest;
import webp2p_sim.proxy.HereIsContentMessage;
import webp2p_sim.proxy.Request;
import webp2p_sim.util.TimeToLive;
import edu.uah.math.distributions.Distribution;

public class WebServer extends SimpleQueuedEntity {
	
	private static final Logger LOG = Logger.getLogger( WebServer.class );
	
	private static final int DEFAULT_THRESHOLD = 20;
	private static final int DEFAULT_THRESHOLD_TTL = 50;
	private static final int DEFAULT_REPLICATION_TTL = 100;
	
	private String name;
	private DiscoveryService discoveryService;
	private List<WebServer> adj;

	private Map<String, TimeToLive> files;
	private Map<String, ReplicationInfo> replicationMap;
	private ReplicationStrategy strategyOfReplication;
	
	public WebServer(String name, Distribution distribution, DiscoveryService discoveryService) {
		super(name, distribution);
		this.name = name;
		this.discoveryService = discoveryService;
		this.adj = new LinkedList<WebServer>();
		this.replicationMap = new HashMap<String, ReplicationInfo>();
		this.files = new HashMap<String, TimeToLive>();
		this.strategyOfReplication = new DefaultReplicationStrategy();
	}

	void loadFile(String url, int size, TimeToLive ttl) {
		LOG.debug( "Loading file " + url + " in server " + this.name );
		
		TimeToLive olderTTL = this.files.get(url);
		
		if (olderTTL == null || olderTTL.remaining() < ttl.remaining()) {
			this.files.put(url, ttl);
			this.replicationMap.put(url, new ReplicationInfo(DEFAULT_THRESHOLD, DEFAULT_THRESHOLD_TTL,url));
			this.discoveryService.sendMessage(new PutFileRequest(url, this));
		}
	}
	
	public void setStrategyOfReplication(ReplicationStrategy strategyOfReplication) {
		this.strategyOfReplication = strategyOfReplication;
	}
	
	public Set<String> getFiles() {
		return Collections.unmodifiableSet(this.files.keySet());
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
				LOG.debug( "Removing page " + url + " from server " + this.name );
				
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
	
	void getContent(Request request) {
		LOG.debug( "Content for " + request.getUrl() + " requested by " + request.getCallBack() + " request number " + request.getId() );
		
		int result;
		if (this.files.containsKey(request.getUrl())) {
			result = 0;
			
			ReplicationInfo info = this.replicationMap.get(request.getUrl());
			info.urlRequested();
			if (info.mustReplicate()) {
				this.strategyOfReplication.performReplication(this, info);
			}
		} else {
			result = -1;
		}
		
		LOG.debug( "Sending content to " + request.getCallBack() + " request number " + request.getId());
		request.getCallBack().sendMessage(new HereIsContentMessage(request.getId(), result));
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
}