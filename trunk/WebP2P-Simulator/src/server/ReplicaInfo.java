package server;

import core.entity.TimedEntity;

public class ReplicaInfo implements TimedEntity {

	private final int replicaTTL;
	private final int thresholdTTL;
	private final int threshold;
	private int numGets;
	private int ticks;

	public ReplicaInfo(int replicaTTL, int threshold, int thresholdTTL) {
		this.replicaTTL = replicaTTL;
		this.threshold = threshold;
		this.thresholdTTL = thresholdTTL;
		this.numGets = 0;
		this.ticks = 0;
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
	}
}
