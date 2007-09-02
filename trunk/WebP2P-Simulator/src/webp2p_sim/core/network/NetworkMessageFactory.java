package webp2p_sim.core.network;

import webp2p_sim.core.network.Network.Type;

public class NetworkMessageFactory {

	public static NetworkMessage newMessage(ApplicationMessage message, Type type) {
		switch (type) {
		case TEST:
			return new NetworkMessage(message, 0);
		case TCP:
			return new NetworkMessage(message);
		default:
			return new NetworkMessage(message);
		}
	}

}
