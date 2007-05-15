package server;

import common.TimeToLive;

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
