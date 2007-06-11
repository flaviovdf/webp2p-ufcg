package webp2p_sim.core.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Connection {

	private final Host sender;
	private final Host receiver;
	private final List<NetworkMessage> messages;
	
	public Connection(Host sender, Host receiver) {
		this.sender = sender;
		this.receiver = receiver;
		this.messages = new ArrayList<NetworkMessage>();
	}
	
	public Address getSenderAddress() {
		return sender.getAddress();
	}
	
	public Address getReceiverAddress() {
		return receiver.getAddress();
	}
	
	public void transmitMessage(NetworkMessage message) {
		messages.add(message);
		sender.getBandwidth().allocateUpBand(this);
		receiver.getBandwidth().allocateDownBand(this);
	}
	
	public float getAllocatedSenderUploadBandwidth() {
		return sender.getBandwidth().getAllocatedUpBand(this);
	}
	
	public float getAllocatedReceiverDownloadBandwidth() {
		return receiver.getBandwidth().getAllocatedDownBand(this);
	}
	
	public long getAmountDataBeingTransfered() {
		long counter = 0;
		for (NetworkMessage message : messages) {
			counter += message.dataLeft();
		}
		
		return counter;
	}

	public List<NetworkMessage> flushData(EndToEndDelay endToEndDelay, long timeInterval) {
		List<NetworkMessage> returnValue = new ArrayList<NetworkMessage>();
		
		//delay is for the whole data, we must find how much data this interval
		double delayBetween = endToEndDelay.getDelayBetweenConnection(this);
		
		long totalData = getAmountDataBeingTransfered();
		double dataOnInterval = (totalData * timeInterval) / delayBetween; 
		
		//now we decrease this data per message equally
		long dataToFlushPerMessage = Math.round(dataOnInterval / messages.size());
		
		//this variable is used to maintain extra data not used by messages
		long extraDataToFlush;
		do {
			extraDataToFlush = 0;
			Iterator<NetworkMessage> it = messages.iterator();
			while (it.hasNext()) {
				NetworkMessage message = it.next();
				long dataLeft = message.dataLeft();
				
				if (dataLeft >= dataToFlushPerMessage) {
					message.dataTransfered(dataToFlushPerMessage);
				}
				else {
					message.dataTransfered(dataLeft);
					extraDataToFlush += dataToFlushPerMessage - dataLeft;
				}
				
				if (message.dataLeft() == 0) {
					returnValue.add(message);
					it.remove();
				}
			}
			
			//not leaving loop
			if (extraDataToFlush > 0) {
				dataToFlushPerMessage = Math.round(extraDataToFlush / messages.size());
			}
			
		} while(extraDataToFlush > 0 && !messages.isEmpty());
		
		if (messages.isEmpty()) {
			sender.getBandwidth().deallocateUpBand(this);
			receiver.getBandwidth().deallocateDownBand(this);
		}
		
		return returnValue;
	}

	public boolean noMoreMessages() {
		return messages.isEmpty();
	}
	
	@Override
	public String toString() {
		return "Client: " + sender.toString() + " - Server:" + receiver.toString();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((receiver == null) ? 0 : receiver.hashCode());
		result = PRIME * result + ((sender == null) ? 0 : sender.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Connection other = (Connection) obj;
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		return true;
	}
}