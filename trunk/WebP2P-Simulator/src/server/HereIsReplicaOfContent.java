package server;

import core.entity.AbstractMessage;

public class HereIsReplicaOfContent extends AbstractMessage {

	private final String url;
	private final int replicationTTL;

	public HereIsReplicaOfContent(String url, int replicationTTL) {
		this.url = url;
		this.replicationTTL = replicationTTL;
	}

	public void process() {
		((WebServer) entity).hereIsReplicaOfUrl(url, replicationTTL);
	}

}
