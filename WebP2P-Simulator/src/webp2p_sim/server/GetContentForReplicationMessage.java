package webp2p_sim.server;

import webp2p_sim.core.entity.AbstractApplicationMessage;
import webp2p_sim.core.network.Host;

public class GetContentForReplicationMessage extends AbstractApplicationMessage {

	private final String url;
	private final Host server;

	public GetContentForReplicationMessage(String url, Host server) {
		this.url = url;
		this.server = server;
	}

	public void realProcess() {
		((WebServer) receiverEntity).getContentForReplication(url, server);
	}

	public boolean equals(Object o) {
		if (!(o instanceof GetContentForReplicationMessage)) return false;
		
		GetContentForReplicationMessage other = (GetContentForReplicationMessage) o;
		return this.url.equals( other.url ) && this.server.equals( other.server );
	}
}
