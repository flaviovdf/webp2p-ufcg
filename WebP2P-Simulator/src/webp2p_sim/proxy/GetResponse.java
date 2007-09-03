package webp2p_sim.proxy;

import java.util.List;

import webp2p_sim.core.network.AbstractApplicationMessage;
import webp2p_sim.core.network.Host;

public class GetResponse extends AbstractApplicationMessage {

	private final List<Host> servers;
	private final long requestID;

	public GetResponse(List<Host> servers, long requestID) {
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
