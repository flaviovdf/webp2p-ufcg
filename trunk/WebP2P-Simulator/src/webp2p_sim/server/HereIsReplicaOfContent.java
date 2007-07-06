package webp2p_sim.server;

import webp2p_sim.core.entity.AbstractApplicationMessage;

public class HereIsReplicaOfContent extends AbstractApplicationMessage {

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

	public boolean equals(Object o) {
		if (!(o instanceof HereIsReplicaOfContent)) return false;
		
		HereIsReplicaOfContent other = (HereIsReplicaOfContent) o;
		return this.url.equals( other.url ) && this.replicationTTL == other.replicationTTL && this.size == other.size;
	}

}
