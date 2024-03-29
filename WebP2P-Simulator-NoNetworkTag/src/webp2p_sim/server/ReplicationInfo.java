package webp2p_sim.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import webp2p_sim.core.entity.NetworkEntity;
import webp2p_sim.core.entity.TimedEntity;
import webp2p_sim.util.TimeToLive;

class ReplicationInfo implements TimedEntity {

	private final int thresholdTTL;
	private final int threshold;
	
	private int numGets;
	private int ticks;
	private Map<NetworkEntity, ReplicationStatus> replicaTTLMap;
	private String url;
	
	public ReplicationInfo(int threshold, int thresholdTTL, String url) {
		this.threshold = threshold;
		this.thresholdTTL = thresholdTTL;
		this.numGets = 0;
		this.ticks = 0;
		this.replicaTTLMap = new HashMap<NetworkEntity, ReplicationStatus>();
		this.url = url;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public void replicationRequested(NetworkEntity server) {
		this.numGets = 0;
		replicaTTLMap.put(server, new ReplicationStatus());
	}
	
	public void replicationDone(NetworkEntity server, int replicationTTL) {
		ReplicationStatus status = replicaTTLMap.get(server);
		if (status != null) {
			status.setAsDone(new TimeToLive(replicationTTL));
		}
	}
	
	public void urlRequested() {
		numGets++;
	}
	
	public boolean mustReplicate() {
		return numGets >= threshold;
	}
	
	public int getRequestsInThisWindow() {
		return this.numGets;
	}

	public void tickOcurred() {
		ticks++;
		
		if (ticks % thresholdTTL == 0) {
			numGets = 0;
		}
		
		Iterator<NetworkEntity> servers = replicaTTLMap.keySet().iterator();
		while (servers.hasNext()) {
			NetworkEntity server = servers.next();
			ReplicationStatus status = replicaTTLMap.get(server);
			if (status.isDone() && status.getTTL().decrease() == 0) {
				servers.remove();
			}
		}
	}


	public boolean hasReplica(NetworkEntity server) {
		return replicaTTLMap.containsKey(server);
	}

}