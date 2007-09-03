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
import webp2p_sim.proxy.Proxy;
import webp2p_sim.proxy.Request;
import webp2p_sim.util.RandomLongGenerator;
import edu.uah.math.distributions.ContinuousUniformDistribution;

// generate traffic on webservers
public class P2PTrafficGenerator implements TimedEntity {
	
	private final Map<Long, List<String>> tickToRequests;
	private final RandomLongGenerator longGenrator = new RandomLongGenerator();
	private final Proxy[] mockProxies;
	private final MockCallback mockCallback;
	
	private final int nMocks = 5;
	private int mockToUse;

	public P2PTrafficGenerator(Map<Long, List<String>> tickToRequests, Network network, Host discoveryService) {
		this.tickToRequests = tickToRequests;
		
		Host cbHost = null;
		do {
			int byte1 = (int) (Math.random() * 253 + 1);
			int byte2 = (int) (Math.random() * 253 + 1);
			int byte3 = (int) (Math.random() * 253 + 1);
			int byte4 = (int) (Math.random() * 253 + 1);
			
			cbHost = new Host(new Address(byte1, byte2, byte3, byte4), new AsymetricBandwidth(Long.MAX_VALUE, Long.MAX_VALUE));
			
		} while (network.isBound(cbHost));
		this.mockCallback = new MockCallback(cbHost, network);
		
		this.mockProxies = new Proxy[nMocks];
		for(int i = 0; i < nMocks; i++) {
			
			Host pHost = null;
			do {
				int byte1 = (int) (Math.random() * 253 + 1);
				int byte2 = (int) (Math.random() * 253 + 1);
				int byte3 = (int) (Math.random() * 253 + 1);
				int byte4 = (int) (Math.random() * 253 + 1);
				
				pHost = new Host(new Address(byte1, byte2, byte3, byte4), new AsymetricBandwidth(Long.MAX_VALUE, Long.MAX_VALUE));
				
			} while (network.isBound(pHost));
			
			mockProxies[i] = new Proxy(pHost, new ContinuousUniformDistribution(0, 0), network, discoveryService, new RandomLongGenerator(), true);
		}
		
		this.mockToUse = 0;
	}

	public void tickOcurred() {
		Long currentTick = Clock.getInstance().getCurrentTick();
		List<String> list = this.tickToRequests.get(currentTick);
		
		if (list != null) {
			for (String urlTo : list) {
				Proxy p = mockProxies[mockToUse];
				p.getContent(new Request(longGenrator.getNextID(), urlTo, mockCallback.getHost()));
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

