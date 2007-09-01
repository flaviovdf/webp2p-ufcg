package webp2p_sim.server;

import webp2p_sim.util.TimeToLive;

class ReplicationStatus {
	
	private TimeToLive ttl;
	
	public ReplicationStatus() {
	}
	
	public void setAsDone(TimeToLive ttl) {
		this.ttl = ttl;
	}

	public TimeToLive getTTL() {
		return ttl;
	}
	
	public boolean isDone() {
		return this.ttl != null;
	}
}
