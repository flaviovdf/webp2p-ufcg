package webp2p_sim.core.network;

public class BandwidthOnlyDelay implements EndToEndDelay {

	@Override
	public double getDelayBetweenConnection(Connection connection) {
		double size = connection.getAmountDataBeingTransfered();
		
		double senderUp = size / connection.getAllocatedSenderUploadBandwidth();
		double receiverDown = size / connection.getAllocatedReceiverDownloadBandwidth();
		
		return Math.max(senderUp, receiverDown);
	}

}
