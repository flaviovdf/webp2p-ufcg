package webp2p_sim.core;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import webp2p_sim.ds.DiscoveryService;
import webp2p_sim.proxy.Browser;
import webp2p_sim.proxy.RequestGenerator;
import webp2p_sim.server.TrafficGenerator;
import webp2p_sim.server.WebServer;
import webp2p_sim.util.RequestFileGenerator;
import edu.uah.math.distributions.ContinuousUniformDistribution;
import edu.uah.math.distributions.ExponentialDistribution;

public class SimulatorCentralized extends Simulator {

	public SimulatorCentralized(File inputFile, long maxTicks) {
		super(maxTicks);
		
		DiscoveryService ds = new DiscoveryService("FAKE Discovery Service", new ContinuousUniformDistribution(0, 0));
		WebServer server = new WebServer("192.168.254.1", new ExponentialDistribution(1f/50), ds);
		Browser browser = new Browser("Browser", new ContinuousUniformDistribution(0, 0), server);
		RequestGenerator req = new RequestGenerator(browser);
		
		Set<WebServer> servers = new HashSet<WebServer>();
		servers.add(server);
		TrafficGenerator trafficGenerator = new TrafficGenerator(new RequestFileGenerator(new ExponentialDistribution(1), getNumberOfTicks(), servers ).generateRequests());
		req.loadFile(inputFile);
		
		Clock.getInstance().addEntities(req, server, browser, trafficGenerator);
	}

}
