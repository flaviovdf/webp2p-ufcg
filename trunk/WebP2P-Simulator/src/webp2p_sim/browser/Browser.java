package webp2p_sim.browser;

import org.apache.log4j.Logger;

import webp2p_sim.common.RequestCallBack;
import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.core.network.Host;
import webp2p_sim.core.network.Network;
import webp2p_sim.proxy.GeneratorInterested;
import webp2p_sim.server.GetContentRequest;
import webp2p_sim.util.RandomLongGenerator;
import edu.uah.math.distributions.Distribution;

public class Browser extends SimpleQueuedEntity implements RequestCallBack, GeneratorInterested {

	private static Logger LOG = Logger.getLogger( Browser.class );
	
	private final Host proxy;
	private final RandomLongGenerator generator;

	public Browser(Host host, Distribution distribution, Network network, Host proxy) {
		super(host, distribution, network);
		this.proxy = proxy;
		this.generator = new RandomLongGenerator();
	}
	
	protected RandomLongGenerator getRandomRequestGenerator() {
		return this.generator;
	}

	public void hereIsContent(long request, int result, long size) {
		LOG.info("Request: "+request+" received " + size +" bits with result value: "+result);
	}

	public void generateRequest(String url) {
		long request = this.generator.getNextID();
		LOG.info("Request: "+request+" sent to url: "+url);
		sendMessage(proxy, new GetContentRequest(request, url, this.getHost()));
	}
	
}
