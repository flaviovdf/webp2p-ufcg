package webp2p_sim.core.network;


/*
 * Based on the Simple underlying network in:
 * 
 * Ingmar Baumgart, Bernhard Heep, Stephan Krause, 
 * OverSim: A Flexible Overlay Network Simulation Framework, 
 * Proceedings of 10th IEEE Global Internet Symposium (GI '07) 
 * in conjunction with IEEE INFOCOM 2007, Anchorage, AK, USA, May 2007
 * 
 * But here we assume d_n=0 and use a different distance approach.
 */ 
public class SimpleDistanceDelay implements EndToEndDelay {

	private final double TIME_FACTOR;
	
	public SimpleDistanceDelay() {
		this(0.5);
	}
	
	public SimpleDistanceDelay(double time_factor) {
		TIME_FACTOR = time_factor;
	}
	
	public double getDelayBetweenConnection(Connection connection) {
		Address senderAddress = connection.getSenderAddress();
		Address receiveAddress = connection.getReceiverAddress();
		
		if (senderAddress.getNumericHost() == receiveAddress.getNumericHost()) {
			return 0;
		}
		
		double size = connection.getAmountDataBeingTransfered();
		
		double senderUp = size / connection.getAllocatedSenderUploadBandwidth();
		double receiverDown = size / connection.getAllocatedReceiverDownloadBandwidth();
		
		return Math.max(senderUp, receiverDown) + TIME_FACTOR * distanceBeetweenHosts(senderAddress, receiveAddress);
	}
	
	private int distanceBeetweenHosts(Address hosta, Address hostb) {
		long numericHostA = hosta.getNumericHost();
		long numericHostB = hostb.getNumericHost();
		
		long xorResult = numericHostA ^ numericHostB;
		
		return Long.bitCount(xorResult);
	}

}
