package webp2p_sim.proxy;

import java.util.Set;

import webp2p_sim.core.entity.AbstractApplicationMessage;
import webp2p_sim.core.entity.NetworkEntity;

public class GetResponse extends AbstractApplicationMessage {

	private final Set<NetworkEntity> servers;
	private final long requestID;

	public GetResponse(Set<NetworkEntity> servers, long requestID) {
		this.servers = servers;
		this.requestID = requestID;
	}

	public void process() {
		((Proxy) entity).hereAreServers(requestID, servers);
	}

	public boolean equals(Object other) {
		if (!(other instanceof GetResponse)) return false; 
		GetResponse parameterEntity = (GetResponse) other;
		return parameterEntity.requestID == this.requestID && this.servers.equals(parameterEntity.servers);
	}
	
}
