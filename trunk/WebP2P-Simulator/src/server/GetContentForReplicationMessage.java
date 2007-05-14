package server;

import core.entity.AbstractMessage;

public class GetContentForReplicationMessage extends AbstractMessage {

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
