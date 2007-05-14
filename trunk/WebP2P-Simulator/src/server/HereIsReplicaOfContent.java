package server;

import core.entity.AbstractMessage;

public class HereIsReplicaOfContent extends AbstractMessage {

	private final String url;
	private final int replicationTTL;
	private final int size;

	public HereIsReplicaOfContent(String url, int replicationTTL, int size) {
		this.url = url;
		this.replicationTTL = replicationTTL;
		this.size = size;
	}

	public void process() {
		((WebServer) entity).hereIsReplicaOfUrl(url, replicationTTL, size);
	}

}
