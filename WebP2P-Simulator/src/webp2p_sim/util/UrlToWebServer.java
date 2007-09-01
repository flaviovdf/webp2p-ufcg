package webp2p_sim.util;

import webp2p_sim.core.network.Host;

public class UrlToWebServer {
	
	private Host server;
	private String url;
	
	public UrlToWebServer(Host server, String url) {
		super();
		this.server = server;
		this.url = url;
	}

	public Host getServer() {
		return server;
	}

	public String getUrl() {
		return url;
	}
}
