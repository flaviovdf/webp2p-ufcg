package server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import common.TimeToLive;

import core.entity.TimedEntity;

class ReplicationInfo implements TimedEntity {

	private final int thresholdTTL;
	private final int threshold;
	
	private int numGets;
	private int ticks;
	private Map<WebServer, ReplicationStatus> replicaTTLMap;
	
	public ReplicationInfo(int threshold, int thresholdTTL) {
		this.threshold = threshold;
		this.thresholdTTL = thresholdTTL;
		this.numGets = 0;
		this.ticks = 0;
		this.replicaTTLMap = new HashMap<WebServer, ReplicationStatus>();
	}
	
	
	public void replicationRequested(WebServer server) {
		this.numGets = 0;
		replicaTTLMap.put(server, new ReplicationStatus());
	}
	
	public void replicationDone(WebServer server, int replicationTTL) {
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

	public void tickOcurred() {
		ticks++;
		
		if (ticks % thresholdTTL == 0) {
			numGets = 0;
		}
		
		Iterator<WebServer> servers = replicaTTLMap.keySet().iterator();
		while (servers.hasNext()) {
			WebServer server = servers.next();
			ReplicationStatus status = replicaTTLMap.get(server);
			if (status.isDone() && status.getTTL().decrease() == 0) {
				servers.remove();
				replicaTTLMap.remove(server);
			}
		}
	}


	public boolean hasReplica(WebServer server) {
		return replicaTTLMap.containsKey(server);
	}

}