package server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import proxy.HereIsContentMessage;
import proxy.ProxyImpl;

import common.InfinitTimeToLive;
import common.TimeToLive;

import core.entity.SimpleQueuedEntity;
import ds.DiscoveryService;
import ds.PutFileRequest;
import edu.uah.math.distributions.Distribution;

public class WebServer extends SimpleQueuedEntity {
	
	private static final int DEFAULT_THRESHOLD = 20;
	private static final int DEFAULT_THRESHOLD_TTL = 200;
	private static final int DEFAULT_REPLICATION_TTL = 2000;
	
	private DiscoveryService discoveryService;
	private List<WebServer> adj;

	private Map<String, TimeToLive> files;
	private Map<String, ReplicationInfo> replicationMap;
	
	public WebServer(Distribution distribution, DiscoveryService discoveryService) {
		super(distribution);
		this.discoveryService = discoveryService;
		this.adj = new LinkedList<WebServer>();
		this.replicationMap = new HashMap<String, ReplicationInfo>();
		this.files = new HashMap<String, TimeToLive>();
	}

	void loadFile(String url, int size) {
		this.files.put(url, new InfinitTimeToLive());
		this.replicationMap.put(url, new ReplicationInfo(DEFAULT_THRESHOLD, DEFAULT_THRESHOLD_TTL));
		this.discoveryService.sendMessage(new PutFileRequest(url, this));
	}
	
	Set<String> getFiles() {
		return this.files.keySet();
	}
	
	void addAdj(WebServer server) {
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
	
	void getContent(long request, String url, ProxyImpl proxy) {
		int result;
		if (this.files.containsKey(url)) {
			result = 0;
			
			ReplicationInfo info = this.replicationMap.get(url);
			info.urlRequested();
			if (info.mustReplicate()) {
				for (WebServer server : this.adj) {
					if (!info.hasReplica(server)) {
						info.resetGets();
						server.sendMessage(new CreateReplicaOfUrlMessage(url, this));
						break;
					}
				}
			}
		} else {
			result = -1;
		}
		
		proxy.sendMessage(new HereIsContentMessage(request, result));
	}
	
	//called by other servers
	
	void createReplicaOfUrl(String url, WebServer server) {
		server.sendMessage(new GetContentForReplicationMessage(url, this));
	}

	void getContentForReplication(String url, WebServer server) {
		ReplicationInfo info = this.replicationMap.get(url);
		if (info != null) {
			info.replicationRequested(server, DEFAULT_REPLICATION_TTL);
			server.sendMessage(new HereIsReplicaOfContent(url, DEFAULT_REPLICATION_TTL));
		}
	}

	void hereIsReplicaOfUrl(String url, int replicationTTL) {
		loadFile(url, 0);
	}
}
