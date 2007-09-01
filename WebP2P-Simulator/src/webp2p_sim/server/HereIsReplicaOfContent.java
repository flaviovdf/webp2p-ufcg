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

	public void realProcess() {
		((WebServer) receiverEntity).hereIsReplicaOfUrl(url, replicationTTL, size);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + replicationTTL;
		result = prime * result + size;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final HereIsReplicaOfContent other = (HereIsReplicaOfContent) obj;
		if (replicationTTL != other.replicationTTL)
			return false;
		if (size != other.size)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}



}
