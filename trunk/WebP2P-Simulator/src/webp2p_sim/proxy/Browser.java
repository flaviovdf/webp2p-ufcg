package webp2p_sim.proxy;

import edu.uah.math.distributions.Distribution;
import webp2p_sim.core.entity.SimpleQueuedEntity;

public class Browser extends SimpleQueuedEntity implements GeneratorInterested {

	private final Proxy proxy;

	public Browser(String name, Distribution distribution, Proxy proxy) {
		super(name, distribution);
		this.proxy = proxy;
	}

	public void generateRequest(String url) {
		this.proxy.sendMessage(null); // TODO
	}

}
