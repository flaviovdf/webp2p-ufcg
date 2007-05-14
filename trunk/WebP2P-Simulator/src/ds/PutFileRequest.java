package ds;

import server.WebServer;
import core.entity.AbstractMessage;

public class PutFileRequest extends AbstractMessage {

	private String url;
	private WebServer server;

	public PutFileRequest(String url, WebServer server) {
		this.url = url;
		this.server = server;
	}

	public void process() {
		((DiscoveryService) entity).putRequest(this.url, this.server);
	}
}
