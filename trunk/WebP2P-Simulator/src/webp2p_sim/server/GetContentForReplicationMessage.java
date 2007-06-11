package webp2p_sim.server;

import webp2p_sim.core.entity.AbstractApplicationMessage;

public class GetContentForReplicationMessage extends AbstractApplicationMessage {

	private final String url;
	private final WebServer server;

	public GetContentForReplicationMessage(String url, WebServer server) {
		this.url = url;
		this.server = server;
	}

	public void process() {
		((WebServer) entity).getContentForReplication(url, server);
	}

}
