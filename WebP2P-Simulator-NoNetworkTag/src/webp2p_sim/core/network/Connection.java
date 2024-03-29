package webp2p_sim.core.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Connection {

	private final Host sender;
	private final Host receiver;
	private final List<NetworkMessage> messages;
	private double dataPerTick;
	
	public Connection(Host sender, Host receiver) {
		this.sender = sender;
		this.receiver = receiver;
		this.messages = new ArrayList<NetworkMessage>();
		this.dataPerTick = 0;
	}
	
	public Address getSenderAddress() {
		return sender.getAddress();
	}
	
	public Address getReceiverAddress() {
		return receiver.getAddress();
	}
	
	public void transmitMessage(EndToEndDelay endToEnd, NetworkMessage message) {
		messages.add(message);
		sender.getBandwidth().allocateUpBand(this);
		receiver.getBandwidth().allocateDownBand(this);
		
		//calculating amount of data being sent through this connection
		double delay = endToEnd.getDelayBetweenConnection(this);
		double totalData = getAmountDataBeingTransfered();
		this.dataPerTick = (totalData) / delay;
	}
	
	public float getAllocatedSenderUploadBandwidth() {
		return sender.getBandwidth().getAllocatedUpBand(this);
	}
	
	public float getAllocatedReceiverDownloadBandwidth() {
		return receiver.getBandwidth().getAllocatedDownBand(this);
	}
	
	public double getAmountDataBeingTransfered() {
		double counter = 0;
		for (NetworkMessage message : messages) {
			counter += message.dataLeft();
		}
		
		return counter;
	}

	public List<NetworkMessage> flushData() {
		List<NetworkMessage> returnValue = new ArrayList<NetworkMessage>();
		
		//now we decrease this data per message equally
		double dataToFlushPerMessage = dataPerTick / messages.size();
		
		//this variable is used to maintain extra data not used by messages
		long extraDataToFlush;
		do {
			extraDataToFlush = 0;
			Iterator<NetworkMessage> it = messages.iterator();
			while (it.hasNext()) {
				NetworkMessage message = it.next();
				double dataLeft = message.dataLeft();
				
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
				dataToFlushPerMessage = extraDataToFlush / messages.size();
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
	
	public int getAmountMessagesBeingTransfered() {
		return messages.size();
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