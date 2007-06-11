package webp2p_sim.proxy;

import webp2p_sim.core.entity.AbstractApplicationMessage;
import webp2p_sim.core.entity.ApplicationMessage;

public class HereIsContentMessage extends AbstractApplicationMessage implements ApplicationMessage {

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
