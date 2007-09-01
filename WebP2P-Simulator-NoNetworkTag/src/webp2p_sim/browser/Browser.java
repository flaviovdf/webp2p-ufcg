package webp2p_sim.browser;

import org.apache.log4j.Logger;

import webp2p_sim.common.ContentIF;
import webp2p_sim.common.RequestCallBack;
import webp2p_sim.core.entity.NetworkEntity;
import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.proxy.GeneratorInterested;
import webp2p_sim.server.GetContentRequest;
import webp2p_sim.util.RandomLongGenerator;
import edu.uah.math.distributions.Distribution;

public class Browser extends SimpleQueuedEntity implements RequestCallBack, GeneratorInterested {

	private static Logger LOG = Logger.getLogger( Browser.class );
	
	private final NetworkEntity proxy;
	private final RandomLongGenerator generator;

	public Browser(String name, Distribution distribution, NetworkEntity proxy) {
		super(name, distribution);
		
		//Integrity check.
		assert proxy instanceof ContentIF;
		
		this.proxy = proxy;
		this.generator = new RandomLongGenerator();
	}
	
	protected RandomLongGenerator getRandomRequestGenerator() {
		return this.generator;
	}

	public void hereIsContent(long request, int result) {
		LOG.info("Request: "+request+" received with result value: "+result);
	}

	public void generateRequest(String url) {
		long request = this.generator.getNextID();
		LOG.info("Request: "+request+" sent to url: "+url);
		this.proxy.sendMessage(new GetContentRequest(request,url,this));
	}
	
}
