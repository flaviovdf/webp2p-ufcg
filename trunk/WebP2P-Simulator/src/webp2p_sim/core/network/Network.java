package webp2p_sim.core.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import webp2p_sim.core.entity.ApplicationMessage;
import webp2p_sim.core.entity.NetworkEntity;
import webp2p_sim.core.entity.TimedEntity;

public class Network implements TimedEntity {

	private static Logger LOG = Logger.getLogger(Network.class);
	
	private final Map<Address, NetworkEntity> entities;
	private final Map<SenderToReceiver, Connection> connections;
	private final EndToEndDelay endToEndDelay;

	private static Network instance = null;

	private Network() {
		this.endToEndDelay = new SimpleDistanceDelay();
		this.entities = new HashMap<Address, NetworkEntity>();
		this.connections = new HashMap<SenderToReceiver, Connection>();
		
		LOG.info("Underlying Network Created");
	}
	
	public static Network getInstance() {
		if (instance == null) {
			instance = new Network();
		}
		
		return instance;
	}
	
	public void bind(Host host, NetworkEntity entity) throws NetworkException {
		Address address = host.getAddress();
		if (entities.containsKey(address)) {
			LOG.info("Unable to bind < " + entity + " >, address < " + address + " > already in use");
			throw new NetworkException("Name already in use " + address);
		}
		
		LOG.info("Sucessfully bound < " + entity + " > to address < " + address + " >");
		entities.put(address, entity);
	}

	public void unbind(Host host) throws NetworkException {
		Address address = host.getAddress();
		if (!entities.containsKey(address)) {
			LOG.info("Unable to unbind < " + address + " >, not bound");
			throw new NetworkException("Name already in use " + address);
		}
		
		LOG.info("Sucessfully unbound < " + address + " >");
		entities.remove(address);
	}
	
	//FIXME Since entities wil ignore errors, no notification is sent in case one occurs
	public void sendMessage(Host sender, Host receiver, ApplicationMessage message) {
		NetworkEntity sendere = entities.get(sender.getAddress());
		NetworkEntity receivere = entities.get(receiver.getAddress());
		
		if (sendere == null || receivere == null) {
			if (sendere == null) {
				LOG.info("Unable to send message < " + message + " >, sender not bound < " + sender + " >");
			}
			else if (receivere == null) {
				LOG.info("Unable to send message < " + message + " >, receiver not bound < " + receiver + " >");				
			}
		}
		else {
			Connection connection = getConnection(sender, receiver);
			
			if (connection == null) {
				LOG.info("Connection < " + connection + " > stablished");
				connection = new Connection(sender, receiver);
				connections.put(new SenderToReceiver(sender, receiver), connection);
			}
			
			LOG.info("Transmitting message < " + message + " > through connection < " + connection + " >");
			connection.transmitMessage(endToEndDelay, new NetworkMessage(message));
		}
	}
	
	//FIXME Since entities will ignore errors, no notification is sent in case one occurs
	public void tickOcurred() {
		Iterator<Entry<SenderToReceiver, Connection>> it = connections.entrySet().iterator();
		while(it.hasNext()) {
			Connection con = it.next().getValue();
			
			NetworkEntity sender = entities.get(con.getSenderAddress());
			NetworkEntity receiver = entities.get(con.getReceiverAddress());
			
			if (sender == null || receiver == null) {
				if (sender == null) {
					LOG.info("Unable to send messages, sender not bound < " + sender + " >");
				}
				else if (receiver == null) {
					LOG.info("Unable to send messages, receiver not bound < " + receiver + " >");				
				}
				
				//remove connection
				it.remove();
			}
			else {
				//flush data for one tick.
				List<NetworkMessage> messagesDone = con.flushData();
				
				for (NetworkMessage nm : messagesDone) {
					receiver.sendMessage(nm.getApplicationMessage());
				}
				
				if (con.noMoreMessages()) {
					it.remove();
				}
			}
		}
	}

	public static void reset() {
		LOG.info("Underlying Network is going down");
		Network.instance = null;
	}

	protected Connection getConnection(Host sender, Host receiver) {
		return connections.get(new SenderToReceiver(sender, receiver));
	}
	
	protected EndToEndDelay getDelay() {
		return endToEndDelay;
	}
	
	private class SenderToReceiver {

		private final Host sender;
		private final Host receiver;

		public SenderToReceiver(Host sender, Host receiver) {
			this.sender = sender;
			this.receiver = receiver;
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
			final SenderToReceiver other = (SenderToReceiver) obj;
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
}
