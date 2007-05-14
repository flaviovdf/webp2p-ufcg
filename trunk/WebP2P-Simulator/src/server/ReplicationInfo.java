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
	private Map<WebServer, TimeToLive> replicaTTLMap;
	
	public ReplicationInfo(int threshold, int thresholdTTL) {
		this.threshold = threshold;
		this.thresholdTTL = thresholdTTL;
		this.numGets = 0;
		this.ticks = 0;
		this.replicaTTLMap = new HashMap<WebServer, TimeToLive>();
	}
	
	
	public void replicationRequested(WebServer server, int replicationTTL) {
		replicaTTLMap.put(server, new TimeToLive(replicationTTL));
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
			TimeToLive ttl = replicaTTLMap.get(server);
			if (ttl.decrease() == 0) {
				servers.remove();
				replicaTTLMap.remove(server);
			}
		}
	}


	public boolean hasReplica(WebServer server) {
		return replicaTTLMap.containsKey(server);
	}


	public void resetGets() {
		this.numGets = 0;
	}
}
