package webp2p_sim.ds;

import webp2p_sim.core.entity.AbstractApplicationMessage;
import webp2p_sim.proxy.Request;

public class GetServersForURLRequest extends AbstractApplicationMessage {

	private Request request;

	public GetServersForURLRequest(Request request) {
		this.request = request;
	}

	public void realProcess() {
		((DiscoveryService) receiverEntity).getRequest(request);
	}

	@Override
	public int hashCode() {
		return this.request.hashCode();
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
		if (request == null) {
			if (other.request != null)
				return false;
		} else if (!request.equals(other.request))
			return false;
		return true;
	}
}
