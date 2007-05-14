package ds;

import proxy.ProxyImpl;
import core.entity.AbstractMessage;

public class GetServersForURLRequest extends AbstractMessage {

	private final long requestID;
	private final String url;
	private final ProxyImpl callback;

	public GetServersForURLRequest(long requestID, String url, ProxyImpl callback) {
		this.requestID = requestID;
		this.url = url;
		this.callback = callback;
	}

	public void process() {
		((DiscoveryService) entity).getRequest(requestID, url, callback);
	}

}
