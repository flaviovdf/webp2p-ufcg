package webp2p_sim.server;

import java.util.List;
import java.util.Map;

import webp2p_sim.common.RequestCallBack;
import webp2p_sim.core.Clock;
import webp2p_sim.core.entity.ApplicationMessage;
import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.core.entity.TimedEntity;
import webp2p_sim.core.network.Address;
import webp2p_sim.core.network.AsymetricBandwidth;
import webp2p_sim.core.network.Host;
import webp2p_sim.core.network.Network;
import webp2p_sim.util.RandomLongGenerator;
import webp2p_sim.util.UrlToWebServer;
import edu.uah.math.distributions.ContinuousUniformDistribution;

// generate traffic on webservers
public class TrafficGenerator implements TimedEntity {

	
	private final Map<Long, List<UrlToWebServer>> tickToRequests;
	private final RandomLongGenerator longGenrator = new RandomLongGenerator();
	private final long upBand;
	private final long downBand;
	private final Network network;

	public TrafficGenerator(Map<Long, List<UrlToWebServer>> tickToRequests, long upBand, long downBand, Network network) {
		this.tickToRequests = tickToRequests;
		this.upBand = upBand;
		this.downBand = downBand;
		this.network = network;
	}

	public void tickOcurred() {
		Long currentTick = Clock.getInstance().getCurrentTick();
		List<UrlToWebServer> list = this.tickToRequests.get(currentTick);
		
		if (list != null) {
			for (UrlToWebServer urlTo : list) {
				
				Host host = null;
				do {
					int byte1 = (int) (Math.random() * 253 + 1);
					int byte2 = (int) (Math.random() * 253 + 1);
					int byte3 = (int) (Math.random() * 253 + 1);
					int byte4 = (int) (Math.random() * 253 + 1);
					
					host = new Host(new Address(byte1, byte2, byte3, byte4), new AsymetricBandwidth(upBand, downBand));
					
				} while (network.isBound(host));
				
				
				MockCallback mockCallback = new MockCallback(host, network);
				network.bind(host, mockCallback);
				network.sendMessage(mockCallback.getHost(), urlTo.getServer(), new GetContentRequest(longGenrator.getNextID(), urlTo.getUrl(), mockCallback.getHost()));
			}
		}
		
	}

	private class MockCallback extends SimpleQueuedEntity implements RequestCallBack {

		public MockCallback(Host host, Network network) {
			super(host, new ContinuousUniformDistribution(0, 0), network);
		}

		public void hereIsContent(long request, int result) {
			unbindSelf();
		}

		public void receiveMessage(ApplicationMessage applicationMessage) {
		}
	}
	
	
}
