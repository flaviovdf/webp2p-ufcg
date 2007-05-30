package webp2p_sim.proxy;

import webp2p_sim.core.entity.AbstractMessage;
import webp2p_sim.core.entity.Message;

public class HereIsContentMessage extends AbstractMessage implements Message {

	private final long request;
	private final int result;

	public HereIsContentMessage(long request, int result) {
		this.request = request;
		this.result = result;
	}

	public void process() {
		((Proxy) entity).hereIsContent(request, result);
	}

}
