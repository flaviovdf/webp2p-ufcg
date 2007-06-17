package webp2p_sim.server;

import webp2p_sim.core.entity.AbstractApplicationMessage;
import webp2p_sim.proxy.Proxy;
import webp2p_sim.proxy.Request;

public class GetContentRequest extends AbstractApplicationMessage {

	private long request;
	private String url;
	private Proxy callback;

	public GetContentRequest(long request, String url, Proxy callback) {
		this.request = request;
		this.url = url;
		this.callback = callback;
	}

	public void process() {
		((WebServer) entity).getContent(new Request(this.request, this.url, this.callback));
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
