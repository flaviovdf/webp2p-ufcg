package webp2p_sim.core;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import webp2p_sim.proxy.Browser;
import webp2p_sim.proxy.RequestGenerator;
import webp2p_sim.server.TrafficGenerator;
import webp2p_sim.server.WebServer;
import webp2p_sim.util.RequestFileGenerator;

public class SimulatorCentralized extends Simulator {

	public SimulatorCentralized(CentralizedParams centralizedParams) {
		super(centralizedParams);
		
		WebServer server = centralizedParams.getWebServer();
		Browser browser = centralizedParams.getBrowser();
		RequestGenerator req = new RequestGenerator(browser);
		
		Set<WebServer> servers = new HashSet<WebServer>();
		servers.add(server);
		TrafficGenerator trafficGenerator = new TrafficGenerator(new RequestFileGenerator(centralizedParams.getTrafficDistribution(), getNumberOfTicks(), servers ).generateRequests());
		req.loadFile(new File(centralizedParams.getBrowserInputFile()));
	
		browser.setMetricCalculator(collector);
		
		Clock.getInstance().addEntities(req, server, browser, trafficGenerator);
	}


}
