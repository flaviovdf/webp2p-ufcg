package webp2p_sim.server;

import java.util.List;
import java.util.Map;

import webp2p_sim.common.RequestCallBack;
import webp2p_sim.core.Clock;
import webp2p_sim.core.entity.SimpleQueuedEntity;
import webp2p_sim.core.entity.TimedEntity;
import webp2p_sim.core.network.Address;
import webp2p_sim.core.network.ApplicationMessage;
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
	private final MockCallback[] mockCallbacks;
	private final int nMocks = 10;
	private int mockToUse;

	public TrafficGenerator(Map<Long, List<UrlToWebServer>> tickToRequests, Network network, long upBand, long downBand) {
		this.tickToRequests = tickToRequests;
		
		Host host = null;
		
		this.mockCallbacks = new MockCallback[nMocks];
		for(int i = 0; i < nMocks; i++) {
			do {
				int byte1 = (int) (Math.random() * 253 + 1);
				int byte2 = (int) (Math.random() * 253 + 1);
				int byte3 = (int) (Math.random() * 253 + 1);
				int byte4 = (int) (Math.random() * 253 + 1);
				
				host = new Host(new Address(byte1, byte2, byte3, byte4), new AsymetricBandwidth(upBand, downBand));
				
			} while (network.isBound(host));
			this.mockCallbacks[i] = new MockCallback(host, network);
		}
		
		this.mockToUse = 0;
	}

	public void tickOcurred() {
		Long currentTick = Clock.getInstance().getCurrentTick();
		List<UrlToWebServer> list = this.tickToRequests.get(currentTick);
		
		if (list != null) {
			for (UrlToWebServer urlTo : list) {
				MockCallback mockCallback = mockCallbacks[mockToUse];
				GetContentRequest getContentRequest = new GetContentRequest(longGenrator.getNextID(), urlTo.getUrl(), mockCallback.getHost());
				getContentRequest.setReceiverEntity(urlTo.getServer());
				urlTo.getServer().receiveMessage(getContentRequest);
			}
		}
		
		mockToUse = (mockToUse + 1) % nMocks;
	}

	private class MockCallback extends SimpleQueuedEntity implements RequestCallBack {

		public MockCallback(Host host, Network network) {
			super(host, new ContinuousUniformDistribution(0, 0), network, true);
		}

		public void hereIsContent(long request, int result, long size) {
		}

		public void receiveMessage(ApplicationMessage applicationMessage) {
		}
	}
	
	
}
