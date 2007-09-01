package webp2p_sim.proxy;

import java.util.Set;

import webp2p_sim.core.entity.AbstractApplicationMessage;
import webp2p_sim.core.network.Host;

public class GetResponse extends AbstractApplicationMessage {

	private final Set<Host> servers;
	private final long requestID;

	public GetResponse(Set<Host> servers, long requestID) {
		this.servers = servers;
		this.requestID = requestID;
	}

	public void realProcess() {
		((Proxy) receiverEntity).hereAreServers(requestID, servers);
	}

	public boolean equals(Object other) {
		if (!(other instanceof GetResponse)) return false; 
		GetResponse parameterEntity = (GetResponse) other;
		return parameterEntity.requestID == this.requestID && this.servers.equals(parameterEntity.servers);
	}
	
}
