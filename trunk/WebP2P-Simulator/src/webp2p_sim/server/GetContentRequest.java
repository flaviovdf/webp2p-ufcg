package webp2p_sim.server;

import webp2p_sim.common.ContentIF;
import webp2p_sim.core.network.AbstractApplicationMessage;
import webp2p_sim.core.network.Host;
import webp2p_sim.proxy.Request;

public class GetContentRequest extends AbstractApplicationMessage {

	private long request;
	private String url;
	private Host callback;

	public GetContentRequest(long request, String url, Host callback) {
		this.request = request;
		this.url = url;
		this.callback = callback;
	}

	public void realProcess() {
		((ContentIF) receiverEntity).getContent(new Request(this.request, this.url, this.callback));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((callback == null) ? 0 : callback.hashCode());
		result = prime * result + (int) (request ^ (request >>> 32));
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
		final GetContentRequest other = (GetContentRequest) obj;
		if (callback == null) {
			if (other.callback != null)
				return false;
		} else if (!callback.equals(other.callback))
			return false;
		if (request != other.request)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
