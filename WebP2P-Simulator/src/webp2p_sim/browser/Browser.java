package webp2p_sim.browser;

import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.proxy.GeneratorInterested;
import webp2p_sim.proxy.Proxy;
import edu.uah.math.distributions.Distribution;

public class Browser extends SimpleQueuedEntity implements GeneratorInterested {

	private final Proxy proxy;

	public Browser(String name, Distribution distribution, Proxy proxy) {
		super(name, distribution);
		this.proxy = proxy;
	}

	public void generateRequest(String url) {
		this.proxy.sendRequest(url); // TODO
	}
}
