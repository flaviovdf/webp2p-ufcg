package webp2p_sim.util;

import webp2p_sim.server.WebServer;

public class UrlToWebServer {
	
	private WebServer server;
	private String url;
	
	public UrlToWebServer(WebServer server, String url) {
		super();
		this.server = server;
		this.url = url;
	}

	public WebServer getServer() {
		return server;
	}

	public String getUrl() {
		return url;
	}
}
