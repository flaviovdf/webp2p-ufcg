package webp2p_sim.server;

import webp2p_sim.core.entity.AbstractApplicationMessage;
import webp2p_sim.proxy.Request;
import webp2p_sim.proxy.RequestCallBack;

public class GetContentRequest extends AbstractApplicationMessage {

	private long request;
	private String url;
	private RequestCallBack callback;

	public GetContentRequest(long request, String url, RequestCallBack callback) {
		this.request = request;
		this.url = url;
		this.callback = callback;
	}

	public void process() {
		((ContentIF) entity).getContent(new Request(this.request, this.url, this.callback));
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((callback == null) ? 0 : callback.hashCode());
		result = PRIME * result + (int) (request ^ (request >>> 32));
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
