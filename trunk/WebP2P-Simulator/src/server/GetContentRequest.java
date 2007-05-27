package server;

import proxy.Proxy;
import core.entity.AbstractMessage;

public class GetContentRequest extends AbstractMessage {

	private long request;
	private String url;
	private Proxy callback;

	public GetContentRequest(long request, String url, Proxy callback) {
		this.request = request;
		this.url = url;
		this.callback = callback;
	}

	public void process() {
		((WebServer) entity).getContent(this.request, this.url, this.callback);
	}

}
