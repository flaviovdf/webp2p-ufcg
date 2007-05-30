package webp2p_sim.server;

import webp2p_sim.core.entity.AbstractMessage;

public class CreateReplicaOfUrlMessage extends AbstractMessage {

	private final String url;
	private final WebServer server;

	public CreateReplicaOfUrlMessage(String url, WebServer server) {
		this.url = url;
		this.server = server;
	}

	public void process() {
		((WebServer) entity).createReplicaOfUrl(url, server);
	}

}
