package webp2p_sim.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import webp2p_sim.common.ContentIF;
import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.core.network.Host;
import webp2p_sim.core.network.Network;
import webp2p_sim.ds.PutFileRequest;
import webp2p_sim.proxy.HereIsContentMessage;
import webp2p_sim.proxy.Request;
import webp2p_sim.util.TimeToLive;
import edu.uah.math.distributions.Distribution;

public class WebServer extends SimpleQueuedEntity implements ContentIF {
	
	private static final Logger LOG = Logger.getLogger( WebServer.class );
	
	private final int threshhold;
	private final int thresholdTTL;
	private final int replicationTTL;
	
	private Host discoveryService;
	private List<Host> adj;

	private Map<String, FileInfo> files;
	private Map<String, ReplicationInfo> replicationMap;
	private ReplicationStrategy strategyOfReplication;

	
	public WebServer(Host host, Distribution distribution, Network network, Host discoveryService, boolean bindSelf, int threshhold, int thresholdTTL, int replicationTTL) {
		super(host, distribution, network, bindSelf);
		this.discoveryService = discoveryService;
		this.threshhold = threshhold;
		this.thresholdTTL = thresholdTTL;
		this.replicationTTL = replicationTTL;
		this.adj = new LinkedList<Host>();
		this.replicationMap = new HashMap<String, ReplicationInfo>();
		this.files = new HashMap<String, FileInfo>();
		this.strategyOfReplication = new DefaultReplicationStrategy();
	}

	public void loadFile(String url, int size, TimeToLive ttl) {
		LOG.info( "Loading file " + url + " in server " + this );
		
		FileInfo fileInfo = this.files.get(url);
		
		if (fileInfo == null || fileInfo.getTTL().remaining() < ttl.remaining()) {
			this.files.put(url, new FileInfo(ttl, size));
			this.replicationMap.put(url, new ReplicationInfo(threshhold, thresholdTTL,url));
			sendMessage(discoveryService, new PutFileRequest(url, this.getHost()));
		}
	}
	
	public void setStrategyOfReplication(ReplicationStrategy strategyOfReplication) {
		this.strategyOfReplication = strategyOfReplication;
	}
	
	public Set<String> getFiles() {
		return Collections.unmodifiableSet(this.files.keySet());
	}
	
	public void addAdj(Host server) {
		LOG.info( "Connecting server " + this.toString() + " to " + server );
		this.adj.add(server);
	}
	
	public List<Host> getAdj() {
		return new ArrayList<Host>(this.adj);
	}
	
	@Override
	public void tickOcurred() {
		super.tickOcurred();
		
		Iterator<String> urls = files.keySet().iterator();
		while (urls.hasNext()) {
			String url = urls.next();
			FileInfo fileInfo = files.get(url);
			if (fileInfo.getTTL().decrease() == 0) {
				LOG.info( "Removing page " + url + " from server " + this );
				
				urls.remove();
				replicationMap.remove(url);
			}
		}
		
		Collection<ReplicationInfo> replicationInfos = replicationMap.values();
		for (ReplicationInfo ri : replicationInfos) {
			ri.tickOcurred();
		}
	}

	//called by proxies
	
	public void getContent(Request request) {
		LOG.info( "Content for " + request.getUrl() + " requested by " + request.getCallBack() + " request number " + request.getId() );
		
		int result;
		FileInfo fileInfo = this.files.get(request.getUrl());
		if (fileInfo != null) {
			result = 0;
			
			ReplicationInfo info = this.replicationMap.get(request.getUrl());
			info.urlRequested();
			if (info.mustReplicate()) {
				this.strategyOfReplication.performReplication(this, info);
			}
			
			sendMessage(request.getCallBack(), new HereIsContentMessage(request.getId(), result, fileInfo.getSize()));
		} else {
			result = -1;
			sendMessage(request.getCallBack(), new HereIsContentMessage(request.getId(), result, 0));
		}
		
		LOG.info( "Sending content to " + request.getCallBack() + " request number " + request.getId());
	}
	
	//called by other servers
	
	public void createReplicaOfUrl(String url, Host server) {
		LOG.info( "Replica creation requested from " + server + " , content url " + url);
		sendMessage(server, new GetContentForReplicationMessage(url, this.getHost()));
	}

	public void getContentForReplication(String url, Host server) {
		ReplicationInfo info = this.replicationMap.get(url);
		if (info == null) {
			LOG.info( "Trying to replicate an inexistent url " + url + " to " + server);
		} else {
			LOG.info( "Sending replica for url " + url + " to " + server + " ttl " + replicationTTL);
			info.replicationDone(server, replicationTTL);
			sendMessage(server, new HereIsReplicaOfContent(url, replicationTTL, this.files.get( url ).getSize()));
		}
	}

	public void hereIsReplicaOfUrl(String url, int replicationTTL, int size) {
		LOG.info( "Replica for url " + url + " recevied  ttl " + replicationTTL);
		loadFile(url, size, new TimeToLive(replicationTTL));
	}
	
	private class FileInfo {
		private TimeToLive ttl;
		private int size;

		public FileInfo(TimeToLive ttl, int size) {
			this.ttl = ttl;
			this.size = size;
		}
		
		public int getSize() {
			return size;
		}
		
		public TimeToLive getTTL() {
			return ttl;
		}
	}

	protected void staSendMessage(Host receiver, CreateReplicaOfUrlMessage createReplicaOfUrlMessage) {
		sendMessage(receiver, createReplicaOfUrlMessage);
	}

}
