package webp2p_sim.ds;

import webp2p_sim.core.entity.AbstractMessage;
import webp2p_sim.proxy.Proxy;

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

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((callback == null) ? 0 : callback.hashCode());
		result = PRIME * result + (int) (requestID ^ (requestID >>> 32));
		result = PRIME * result + ((url == null) ? 0 : url.hashCode());
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
		final GetServersForURLRequest other = (GetServersForURLRequest) obj;
		if (callback == null) {
			if (other.callback != null)
				return false;
		} else if (!callback.equals(other.callback))
			return false;
		if (requestID != other.requestID)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	


}
