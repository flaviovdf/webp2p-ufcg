package proxy;

import java.util.Set;

import server.WebServer;
import core.entity.AbstractMessage;

public class GetResponse extends AbstractMessage {

	private final Set<WebServer> servers;
	private final long requestID;

	public GetResponse(Set<WebServer> servers, long requestID) {
		this.servers = servers;
		this.requestID = requestID;
	}

	public void process() {
		((Proxy) entity).hereAreServers(requestID, servers);
	}

}
