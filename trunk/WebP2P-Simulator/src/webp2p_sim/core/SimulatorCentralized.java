package webp2p_sim.core;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import webp2p_sim.browser.Browser;
import webp2p_sim.proxy.RequestGenerator;
import webp2p_sim.server.TrafficGenerator;
import webp2p_sim.server.WebServer;
import webp2p_sim.util.RequestFileGenerator;
import webp2p_sim.util.UrlToWebServer;

public class SimulatorCentralized extends Simulator {

	public SimulatorCentralized(CentralizedParams centralizedParams) {
		super(centralizedParams);
		
		WebServer server = centralizedParams.getWebServer();
		
		Browser browser = centralizedParams.getBrowser();
		RequestGenerator req = new RequestGenerator(browser);
		req.loadFile(new File(centralizedParams.getBrowserInputFile()));
		
		Set<WebServer> servers = new HashSet<WebServer>();
		servers.add(server);
		
		Map<Long, List<UrlToWebServer>> generateRequests = new RequestFileGenerator(centralizedParams.getTrafficMean(), getNumberOfTicks(), servers ).generateRequests();
		TrafficGenerator trafficGenerator = new TrafficGenerator(generateRequests, centralizedParams.getNetwork());
		
		Clock.getInstance().addEntities(req, server, browser, trafficGenerator);
	}


}
