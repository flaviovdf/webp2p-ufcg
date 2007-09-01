package webp2p_sim.proxy;

import webp2p_sim.common.RequestCallBack;
import webp2p_sim.core.entity.AbstractApplicationMessage;

public class HereIsContentMessage extends AbstractApplicationMessage {

	private final long request;
	private final int result;

	public HereIsContentMessage(long request, int result) {
		this.request = request;
		this.result = result;
	}

	public void realProcess() {
		((RequestCallBack) receiverEntity).hereIsContent(request, result);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (int) (request ^ (request >>> 32));
		result = PRIME * result + this.result;
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
		final HereIsContentMessage other = (HereIsContentMessage) obj;
		if (request != other.request)
			return false;
		if (result != other.result)
			return false;
		return true;
	}

}
