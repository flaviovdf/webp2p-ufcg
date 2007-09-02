package webp2p_sim.proxy;

import webp2p_sim.common.RequestCallBack;
import webp2p_sim.core.network.AbstractApplicationMessage;

public class HereIsContentMessage extends AbstractApplicationMessage {

	private final long request;
	private final int result;
	private final long contentSize;

	public HereIsContentMessage(long request, int result, long size) {
		super(size);
		this.request = request;
		this.result = result;
		this.contentSize = size;
	}

	public void realProcess() {
		((RequestCallBack) receiverEntity).hereIsContent(request, result, contentSize);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (contentSize ^ (contentSize >>> 32));
		result = prime * result + (int) (request ^ (request >>> 32));
		result = prime * result + this.result;
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
		if (contentSize != other.contentSize)
			return false;
		if (request != other.request)
			return false;
		if (result != other.result)
			return false;
		return true;
	}



}
