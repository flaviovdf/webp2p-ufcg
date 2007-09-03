package webp2p_sim.core;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import webp2p_sim.browser.Browser;
import webp2p_sim.core.entity.TimedEntity;
import webp2p_sim.core.network.Bandwidth;
import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.proxy.Proxy;
import webp2p_sim.proxy.RequestGenerator;
import webp2p_sim.server.TrafficGenerator;
import webp2p_sim.server.WebServer;
import webp2p_sim.util.RequestFileGenerator;
import webp2p_sim.util.UrlToWebServer;

public class SimulatorDistributed extends Simulator {
	
	public SimulatorDistributed(DistributedParams params) {
		super(params);
		
		DiscoveryService ds = params.getDs();
		
		Set<WebServer> servers = params.getWebServers();
		
		Proxy proxy = params.getProxy();
		
		Browser browser = params.getBrowser();
		RequestGenerator req = new RequestGenerator(browser);
		req.loadFile(new File(params.getBrowserInputFile()));
		
		Map<Long, List<UrlToWebServer>> generateRequests = new RequestFileGenerator(params.getTrafficMean(), getNumberOfTicks(), servers ).generateRequests();
		Bandwidth bandwidth = browser.getHost().getBandwidth();
		TrafficGenerator trafficGenerator = new TrafficGenerator(generateRequests, params.getNetwork(), bandwidth.getTotalUpBand(), bandwidth.getTotalDownBand());
		
		System.out.println(generateRequests.get(12l).size());
		
		Clock.getInstance().addEntities(servers.toArray(new TimedEntity[servers.size()]));
		Clock.getInstance().addEntities(req, ds, proxy, browser, trafficGenerator);
	}

}
