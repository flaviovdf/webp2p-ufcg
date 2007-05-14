package server;

import proxy.ProxyImpl;
import core.entity.AbstractMessage;

public class GetContentRequest extends AbstractMessage {

	private long request;
	private String url;
	private ProxyImpl callback;

	public GetContentRequest(long request, String url, ProxyImpl callback) {
		this.request = request;
		this.url = url;
		this.callback = callback;
	}

	public void process() {
		((WebServer) entity).getContent(this.request, this.url, this.callback);
	}

}
