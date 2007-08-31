package webp2p_sim.server;

import java.util.List;
import java.util.Map;

import webp2p_sim.common.RequestCallBack;
import webp2p_sim.core.Clock;
import webp2p_sim.core.entity.ApplicationMessage;
import webp2p_sim.core.entity.TimedEntity;
import webp2p_sim.util.RandomLongGenerator;
import webp2p_sim.util.UrlToWebServer;

// generate traffic on webservers
public class TrafficGenerator implements TimedEntity {

	
	private final Map<Long, List<UrlToWebServer>> tickToRequests;
	private final RandomLongGenerator longGenrator = new RandomLongGenerator();
	private final MockCallback mockCallback;

	public TrafficGenerator(Map<Long, List<UrlToWebServer>> tickToRequests) {
		this.tickToRequests = tickToRequests;
		this.mockCallback = new MockCallback();
	}

	public void tickOcurred() {
		Long currentTick = Clock.getInstance().getCurrentTick();
		List<UrlToWebServer> list = this.tickToRequests.get(currentTick);
		
		if (list != null) {
			for (UrlToWebServer urlTo : list) {
				urlTo.getServer().sendMessage(new GetContentRequest(longGenrator.getNextID(), urlTo.getUrl(), mockCallback));
			}
		}
		
	}

	private class MockCallback implements RequestCallBack {

		public void hereIsContent(long request, int result) {
			// TODO Auto-generated method stub
			
		}

		public void sendMessage(ApplicationMessage applicationMessage) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
}
