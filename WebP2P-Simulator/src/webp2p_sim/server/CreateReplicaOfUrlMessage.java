package webp2p_sim.server;

import webp2p_sim.core.entity.AbstractApplicationMessage;
import webp2p_sim.core.network.Host;

public class CreateReplicaOfUrlMessage extends AbstractApplicationMessage {

	private final String url;
	private final Host server;

	public CreateReplicaOfUrlMessage(String url, Host server) {
		this.url = url;
		this.server = server;
	}

	public void realProcess() {
		((WebServer) receiverEntity).createReplicaOfUrl(url, server);
	}

}
