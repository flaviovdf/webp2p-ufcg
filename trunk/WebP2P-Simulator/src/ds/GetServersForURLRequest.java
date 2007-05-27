package ds;

import proxy.Proxy;
import core.entity.AbstractMessage;

public class GetServersForURLRequest extends AbstractMessage {

	private final long requestID;
	private final String url;
	private final Proxy callback;

	public GetServersForURLRequest(long requestID, String url, Proxy callback) {
		this.requestID = requestID;
		this.url = url;
		this.callback = callback;
	}

	public void process() {
		((DiscoveryService) entity).getRequest(requestID, url, callback);
	}

}
