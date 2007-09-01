package webp2p_sim.core.network;

import webp2p_sim.core.entity.ApplicationMessage;



public class NetworkMessage {

	private static final long DEFAULT_SIZE = 0;
	private final ApplicationMessage applicationMessage;
	private final long dataSize;
	private double dataTransfered;

	public NetworkMessage(ApplicationMessage applicationMessage) {
		this.applicationMessage = applicationMessage;
		this.dataSize = applicationMessage.size() + DEFAULT_SIZE;
		this.dataTransfered = 0l;
	}
	
	public ApplicationMessage getApplicationMessage() {
		return applicationMessage;
	}
	
	public long size() {
		return dataSize;
	}

	public void dataTransfered(double dataToFlushPerMessage) {
		dataTransfered += dataToFlushPerMessage;
	}
	
	public double dataLeft() {
		return Math.max(0, dataSize - dataTransfered);
	}
}
