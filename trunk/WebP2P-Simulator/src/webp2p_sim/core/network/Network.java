package webp2p_sim.core.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import webp2p_sim.core.entity.NetworkEntity;
import webp2p_sim.core.entity.TimedEntity;

public class Network implements TimedEntity {

	private static Logger LOG = Logger.getLogger(Network.class);
	
	public enum Type {TEST, TCP}
	
	private final Map<Address, NetworkEntity> entities;
	private final Map<SenderToReceiver, Connection> connections;
	private final EndToEndDelay endToEndDelay;

	private final Type type;

	public Network(Type type) {
		this.type = type;
		this.endToEndDelay = new BandwidthOnlyDelay();
		this.entities = new HashMap<Address, NetworkEntity>();
		this.connections = new HashMap<SenderToReceiver, Connection>();
		
		LOG.debug("Underlying Network Created");
	}
	
	public void bind(Host host, NetworkEntity entity) throws NetworkException {
		Address address = host.getAddress();
		if (entities.containsKey(address)) {
			LOG.debug("Unable to bind < " + entity + " >, address < " + address + " > already in use");
			throw new NetworkException("Name already in use " + address);
		}
		
		LOG.debug("Sucessfully bound < " + entity + " > to address < " + address + " >");
		entities.put(address, entity);
	}

	public void unbind(Host host) throws NetworkException {
		Address address = host.getAddress();
		if (!entities.containsKey(address)) {
			LOG.debug("Unable to unbind < " + address + " >, not bound");
			throw new NetworkException("Name already in use <" + address + ">");
		}
		
		LOG.debug("Sucessfully unbound < " + address + " >");
		entities.remove(address);
	}
	
	//FIXME Since entities will ignore errors, no notification is sent in case one occurs
	public void sendMessage(Host sender, Host receiver, ApplicationMessage message) {
		NetworkEntity sendere = entities.get(sender.getAddress());
		NetworkEntity receivere = entities.get(receiver.getAddress());
		
		if (sendere == null || receivere == null) {
			if (sendere == null) {
				LOG.debug("Unable to send message < " + message + " >, sender not bound < " + sender + " >");
			}
			else if (receivere == null) {
				LOG.debug("Unable to send message < " + message + " >, receiver not bound < " + receiver + " >");				
			}
		}
		else {
			//Setting sender and receiver
			message.setCallerEntity(sendere);
			message.setReceiverEntity(receivere);
			
			Connection connection = getConnection(sender, receiver);
			
			if (connection == null) {
				connection = new Connection(sender, receiver);
				connections.put(new SenderToReceiver(sender, receiver), connection);
				LOG.debug("Connection < " + connection + " > stablished");
			}
			
			LOG.debug("Transmitting message < " + message + " > through connection < " + connection + " >");
			NetworkMessage newMessage = NetworkMessageFactory.newMessage(message, type);
			connection.transmitMessage(endToEndDelay, newMessage);
		}
	}
	
	//FIXME Since entities will ignore errors, no notification is sent in case one occurs
	public void tickOcurred() {
		//A message can be sent while iterating, thats why we clone
		Iterator<Entry<SenderToReceiver, Connection>> it = new HashMap<SenderToReceiver, Connection>(connections).entrySet().iterator();
		
		Set<SenderToReceiver> toDelete = new HashSet<SenderToReceiver>();
		
		while(it.hasNext()) {
			Entry<SenderToReceiver, Connection> next = it.next();
			Connection con = next.getValue();
			
			NetworkEntity sender = entities.get(con.getSenderAddress());
			NetworkEntity receiver = entities.get(con.getReceiverAddress());
			
			if (sender == null || receiver == null) {
				if (sender == null) {
					LOG.debug("Unable to send messages, sender not bound < " + sender + " >");
				}
				else if (receiver == null) {
					LOG.debug("Unable to send messages, receiver not bound < " + receiver + " >");				
				}
				
				//remove connection
				toDelete.add(next.getKey());
//				it.remove();
			}
			else {
				//flush data for one tick.
				List<NetworkMessage> messagesDone = con.flushData();
				
				for (NetworkMessage nm : messagesDone) {
					receiver.receiveMessage(nm.getApplicationMessage());
				}
				
				if (con.noMoreMessages()) {
					toDelete.add(next.getKey());
//					it.remove();
				}
			}
		}
		
		for (SenderToReceiver s : toDelete) {
			connections.remove(s);
		}
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
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((receiver == null) ? 0 : receiver.hashCode());
			result = prime * result
					+ ((sender == null) ? 0 : sender.hashCode());
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

	public boolean isBound(Host host) {
		return entities.containsKey(host.getAddress());
	}
}
