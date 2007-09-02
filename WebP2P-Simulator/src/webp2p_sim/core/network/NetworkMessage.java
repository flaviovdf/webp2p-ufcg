package webp2p_sim.core.network;


public class NetworkMessage {

	//128 bits
	public static final long DEFAULT_SIZE = 192;
	
	private final ApplicationMessage applicationMessage;
	private final long dataSize;
	private double dataTransfered;

	public NetworkMessage(ApplicationMessage applicationMessage) {
		this.applicationMessage = applicationMessage;
		this.dataSize = applicationMessage.size() + DEFAULT_SIZE;
		this.dataTransfered = 0l;
	}
	
	public NetworkMessage(ApplicationMessage applicationMessage, long size) {
		this.applicationMessage = applicationMessage;
		this.dataSize = applicationMessage.size() + size;
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
