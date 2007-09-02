package webp2p_sim.core.entity;

import webp2p_sim.core.network.ApplicationMessage;

public interface NetworkEntity {

	public void receiveMessage(ApplicationMessage applicationMessage);
	
}
