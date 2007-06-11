package webp2p_sim.core.network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import webp2p_sim.core.entity.NetworkEntity;
import webp2p_sim.core.entity.TimedEntity;

public class Network implements TimedEntity {

	private static Logger LOG = Logger.getLogger(Network.class);
	
	private final Map<Address, NetworkEntity> entities;
	private final Set<Connection> connections;
	private final EndToEndDelay endToEndDelay;

	private static Network instance = null;

	private Network() {
		this.endToEndDelay = new SimpleDistanceDelay();
		this.entities = new HashMap<Address, NetworkEntity>();
		this.connections = new HashSet<Connection>();
		
		LOG.info("Underlying Network Created");
	}
	
	public static Network getInstance() {
		if (instance == null) {
			instance = new Network();
		}
		
		return instance;
	}
	
	public void bind(Address address, NetworkEntity entity) throws NetworkException {
		if (entities.containsKey(address)) {
			LOG.info("Unable to bind " + entity + ", address " + address + " already in use");
			throw new NetworkException("Name already in use " + address);
		}
		
		LOG.info("Sucessfully bound " + entity + " to address" + address);
		entities.put(address, entity);
	}

	public void unbind(Address address) throws NetworkException {
		if (!entities.containsKey(address)) {
			LOG.info("Unable to unbind " + address + ", not bound");
			throw new NetworkException("Name already in use " + address);
		}
		
		LOG.info("Sucessfully unbound " + address);
		entities.remove(address);
	}
	
	public void sendMessage(Host sender, Host receiver, ApplicationMessage message) {
		NetworkEntity sendere = entities.get(sender.getAddress());
		NetworkEntity receivere = entities.get(receiver.getAddress());
		
		if (sendere == null || receivere == null) {
			if (sendere == null) {
				LOG.info("Unable to send message " + message + ", sender not bound" + sender);
			}
			else if (receivere == null) {
				LOG.info("Unable to send message " + message + ", receiver not bound" + receiver);				
			}
		}
		else {
			Connection connection = new Connection(receiver, sender);
			
			if (!connections.contains(connection)) {
				connections.add(connection);
			}
			
			connection.transmitMessage(new NetworkMessage(message));
		}
	}
	
	public void tickOcurred() {
		Iterator<Connection> it = connections.iterator();
		while(it.hasNext()) {
			Connection con = it.next();
			
			NetworkEntity sender = entities.get(con.getSenderAddress());
			NetworkEntity receiver = entities.get(con.getReceiverAddress());
			
			if (sender == null || receiver == null) {
				if (sender == null) {
					LOG.info("Unable to send messages, sender not bound " + sender);
				}
				else if (receiver == null) {
					LOG.info("Unable to send messages, receiver not bound " + receiver);				
				}
			}
			
			else {
				//flush data for one tick.
				List<NetworkMessage> messagesDone = con.flushData(endToEndDelay, 1);
				
				if (con.noMoreMessages()) {
					it.remove();
					
					for (NetworkMessage nm : messagesDone) {
//						FIXME 
//						receiver.sendMessage(nm.getApplicationMessage());
					}
				}
			}
		}
	}

	public static void reset() {
		Network.instance = null;
	}
}