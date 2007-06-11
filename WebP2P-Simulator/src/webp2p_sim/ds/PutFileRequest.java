package webp2p_sim.ds;

import webp2p_sim.core.entity.AbstractApplicationMessage;
import webp2p_sim.server.WebServer;

public class PutFileRequest extends AbstractApplicationMessage {

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
